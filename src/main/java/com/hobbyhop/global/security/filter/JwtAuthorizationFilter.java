package com.hobbyhop.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import com.hobbyhop.global.security.jwt.JwtUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getJwtFromHeader(request);

        if (StringUtils.hasText(tokenValue)) {
            if (!jwtUtil.validateToken(tokenValue)) {
                ObjectMapper objectMapper = new ObjectMapper();

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효한 토큰이 아닙니다.");

                return;
            }

            Claims info = jwtUtil.getUserInfo(tokenValue);

            String userName = info.getSubject();
            setAuthentication(userName);
        }

        filterChain.doFilter(request, response);
    }
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String tokenValue = jwtUtil.getJwtFromHeader(request);
//
//        if(StringUtils.hasText(tokenValue)) {
//            if(!jwtUtil.validateToken(tokenValue)) {
//                ObjectMapper objectMapper = new ObjectMapper();
//
//                response.setStatus(400);
//                response.setContentType("application/json");
//                response.setCharacterEncoding("UTF-8");
//                response.sendError(400, "유효한 토큰이 아닙니다.");
//
//                return;
//            }
//
//            Claims info = jwtUtil.getUserInfo(tokenValue);
//
//            String userName = info.getSubject();
//            setAuthentication(userName);
//        }
//
//        filterChain.doFilter(request, response);
//    }

    public void setAuthentication(String userName) {

        UserDetailsImpl userDetails = userDetailsService.getUserDetails(userName);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}