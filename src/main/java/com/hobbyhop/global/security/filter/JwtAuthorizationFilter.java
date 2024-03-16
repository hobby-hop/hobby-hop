package com.hobbyhop.global.security.filter;

import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import com.hobbyhop.global.security.userdetails.UserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getJwtFromHeader(request);

        if (Objects.nonNull(accessToken)) {
            if (jwtUtil.checkIsLoggedOut(accessToken)) {
                accessToken = jwtUtil.createExpiredToken(accessToken);
            }

            String accessTokenValue = accessToken.substring(7);

            if (jwtUtil.shouldAccessTokenBeRefreshed(accessToken.substring(7))) {
                String refreshTokenValue = jwtUtil.getRefreshtokenByAccessToken(accessToken).substring(7);
                if (jwtUtil.validateToken(refreshTokenValue)) {
                    String newAccessToken = jwtUtil.createAccessTokenByRefreshToken(refreshTokenValue);
                    response.setHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);

                    jwtUtil.regenerateToken(newAccessToken, accessToken, refreshTokenValue);

                    accessTokenValue = newAccessToken.substring(7);
                }
            }

            if (!jwtUtil.validateToken(accessTokenValue)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효한 토큰이 아닙니다.");
                return;
            }
            Claims info = jwtUtil.getUserInfo(accessTokenValue);
            String username = info.getSubject();
            setAuthentication(username);
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        UserDetailsImpl userDetails = userDetailsService.getUserDetails(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}