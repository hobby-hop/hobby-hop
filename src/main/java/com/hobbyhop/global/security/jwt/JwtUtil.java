package com.hobbyhop.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

	// Header Key 값
	public static final String AUTHORIZATION_HEADER = "Authorization";

	// Token 식별자
	public static final String BEARER_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_KEY = "";

	private final long TOKEN_TIME = 240 * 60 * 1000L;
	private final long REFRESH_TOKEN_TIME = 240 * 60 * 1000L;

	@Value("${jwt.secret.key}")
	private String secretKey;

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	private Key key;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String createToken(String email) { // JWT 토큰 생성
		Date date = new Date();

		return BEARER_PREFIX +
				Jwts.builder()
						.setSubject(email)
						.setExpiration(new Date(date.getTime() + TOKEN_TIME))
						.setIssuedAt(date)
						.signWith(key, signatureAlgorithm)
						.compact();
	}

	public String createRefreshToken() {
		Date now = new Date();

		return BEARER_PREFIX
				+ Jwts.builder()
				.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
				.setIssuedAt(now) // 발급일
				.signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘 (HS256)
				.compact();
	}

	public String resolveToken(HttpServletRequest httpServletRequest) {
		String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.error("Invalid JWT signature, 유효하지 않은 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
		}
		return false;
	}

	// HttpServletRequest 에서 JWT 가져오기
	public String getJwtFromHeader(HttpServletRequest request) {

		String tokenWithBearer = request.getHeader("AUTHORIZATION");

		if(StringUtils.hasText(tokenWithBearer) && tokenWithBearer.startsWith(BEARER_PREFIX)) {

			return tokenWithBearer.substring(7);
		}
		return null;
	}

	// 토큰에서 사용자 정보 가져오기
	public Claims getUserInfo(String tokenValue) {

		return Jwts.parserBuilder().setSigningKey(key)
				.build().parseClaimsJws(tokenValue).getBody();
	}
}