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
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 24 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_TIME = 30 * 24 * 60 * 60 * 1000L;
    @Value("${jwt.secret.key}")
    private String secretKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(String username) {
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

    public String getJwtFromHeader(HttpServletRequest request) {
        String tokenWithBearer = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(tokenWithBearer) && tokenWithBearer.startsWith(BEARER_PREFIX)) {
            return tokenWithBearer;
        }
        return null;
    }

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

        redisTemplate.opsForValue()
                .set(username, newAccessToken,
                        expirationTime, TimeUnit.MILLISECONDS);

        redisTemplate.opsForValue().set(newAccessToken,
                BEARER_PREFIX + refreshTokenValue,
                expirationTime, TimeUnit.MILLISECONDS);

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

        redisTemplate.delete(oldUsername);
        redisTemplate.delete(oldAccessToken);

        redisTemplate.opsForValue().set(newUsername, newAccessToken,
                expirationTime, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(newAccessToken, newRefreshToken,
                expirationTime, TimeUnit.MILLISECONDS);
    }

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