package com.hobbyhop.global.security.jwt;

import com.hobbyhop.global.exception.jwt.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {

    private final RedisTemplate<String, String> redisTemplate;

    // Header Key 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_KEY = "";

    private static final long ACCESS_TOKEN_TIME = 24 * 60 * 60 * 1000L; // 24 hours
    private static final long REFRESH_TOKEN_TIME = 30 * 24 * 60 * 60 * 1000L; // 30 days

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(String username) { // JWT 토큰 생성
        return createToken(username, ACCESS_TOKEN_TIME);
    }

    public String createRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_TIME);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new InvalidJwtSignatureException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException();
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtTokenException();
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException();
        }
    }

    // HttpServletRequest 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String tokenWithBearer = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(tokenWithBearer) && tokenWithBearer.startsWith(BEARER_PREFIX)) {
            return tokenWithBearer;
        }
        return null;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfo(String tokenValue) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(tokenValue).getBody();
    }

    public void saveAccessTokenByUsername(String username, String accessToken) {
        redisTemplate.opsForValue().set(username, accessToken, REFRESH_TOKEN_TIME, TimeUnit.MILLISECONDS);
    }

    public void saveRefreshTokenByAccessToken(String accessToken, String refreshToken) {
        redisTemplate.opsForValue().set(accessToken, refreshToken, REFRESH_TOKEN_TIME, TimeUnit.MILLISECONDS);
    }

    public boolean shouldAccessTokenBeRefreshed(String accessTokenValue) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessTokenValue);
            return false;
        } catch (SecurityException | MalformedJwtException e) {
            throw new InvalidJwtSignatureException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException();
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtTokenException();
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException();
        }
    }

    public String getRefreshtokenByAccessToken(String accessToken) {
        return redisTemplate.opsForValue().get(accessToken);
    }

    public String createAccessTokenByRefreshToken(String refreshTokenValue) {
        Claims info = getUserInfo(refreshTokenValue);
        String username = info.getSubject();
        return createAccessToken(username);
    }

    public void regenerateToken(String newAccessToken, String accessToken,
                                String refreshTokenValue) {
        Claims info = getUserInfo(refreshTokenValue);
        String username = info.getSubject();

        Long expirationTime = info.getExpiration().getTime();

        // 새로 만든 AccessToken을 redis에 저장
        redisTemplate.opsForValue()
                .set(username, newAccessToken,
                        expirationTime, TimeUnit.MILLISECONDS);
        // 새로 만든 AccessToken을 key로 refreshToken을 다시 DB에 저장
        redisTemplate.opsForValue().set(newAccessToken,
                BEARER_PREFIX + refreshTokenValue,
                expirationTime, TimeUnit.MILLISECONDS);
        // 만료된 token으로 저장되어있는 refreshToken은 삭제
        redisTemplate.delete(accessToken);
    }

    public boolean checkIsLoggedOut(String accessToken) {
        return !redisTemplate.hasKey(accessToken);
    }

    public String createExpiredToken(String accessToken) {
        String username = getUserInfo(accessToken.substring(7)).getSubject();
        return createToken(username, 0);
    }

    public void removeRefreshToken(String accessToken) {
        if (!redisTemplate.hasKey(accessToken)) {
            throw new InvalidRefreshTokenException();
        }
        redisTemplate.delete(accessToken);
    }

    public void removeAccessToken(String accessToken) {
        Claims claims = getUserInfo(accessToken.substring(7));
        String username = claims.getSubject();
        if (!redisTemplate.hasKey(username)) {
            throw new InvalidJwtException();
        }
        redisTemplate.delete(username);
    }

    public void rebaseToken(String newAccessToken, String oldAccessToken) {
        String newUsername = getUserInfo(newAccessToken.substring(7)).getSubject();
        String oldUsername = getUserInfo(oldAccessToken.substring(7)).getSubject();
        String oldRefreshToken = redisTemplate.opsForValue().get(oldAccessToken);
        Long expirationTime = getUserInfo(oldRefreshToken.substring(7)).getExpiration().getTime();
        String newRefreshToken = createToken(newUsername, expirationTime);

        // 이전 username으로 발급된 토큰을 DB에서 삭제
        redisTemplate.delete(oldUsername);
        redisTemplate.delete(oldAccessToken);
        // 새로 만든 username으로 된 토큰을 DB에 저장
        redisTemplate.opsForValue().set(newUsername, newAccessToken,
                expirationTime, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(newAccessToken, newRefreshToken,
                expirationTime, TimeUnit.MILLISECONDS);
    }

    /**
     * createToken 메서드
     *
     * @param username
     * @param tokenTime
     * @return
     */
    private String createToken(String username, long tokenTime) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(date.getTime() + tokenTime))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }
}