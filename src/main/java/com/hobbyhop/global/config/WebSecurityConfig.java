package com.hobbyhop.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.filter.JwtAuthorizationFilter;
import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()// resources 접근 허용 설
                                .requestMatchers("/api/users/signup", "/api/users/login").permitAll() // 회원가입, 로그인 페이지 접근 허용
                                .requestMatchers("/api/users/login/kakao/callback").permitAll() // 카카오 소셜 로그인 허용
                                .requestMatchers(HttpMethod.GET, "/api/clubs").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/clubs/{clubId}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/clubs/{clubId}/posts").permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-resourcees/**",
                                        "/swagger-ui/**", "/webjars/**", "/swagger/**").permitAll() // 스웨거 허용
                                .anyRequest().authenticated()
        );

        // 토큰 인증 오류 처리  --> 이거 잘 모르겠더라구요(맞지 않는 에러가 뜰 때도 있습니다 -> 개발하면서 확인하면서 고쳐가야할듯싶습니다)
        http.exceptionHandling( config -> {
            config.accessDeniedHandler(accessDeniedHandler());
            config.authenticationEntryPoint(errorPoint());
        });

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 로컬 테스트, 프론트 배포 주소
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("http://127.0.0.1:5500", "https://hobbyhop.site"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));

        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        corsConfiguration.setExposedHeaders(List.of("*"));

        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    // 이거 따로 customAccessDeniedHandler 구현 할 수 있습니다!
    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            ApiResponse apiResponse = ApiResponse.of(HttpStatus.FORBIDDEN,ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        };
    }

    // 이거 따로 customAuthenticationEntryPoint 구현 할 수 있습니다!
    private AuthenticationEntryPoint errorPoint() {
        return (request, response, authException) -> {
            authException.printStackTrace();
            ApiResponse apiResponse = ApiResponse.of(HttpStatus.BAD_REQUEST, "유효한 토큰이 아닙니다. 혹은 url을 다시 확인하세요.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        };
    }
}