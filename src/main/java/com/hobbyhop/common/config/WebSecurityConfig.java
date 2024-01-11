package com.hobbyhop.common.config;

//import com.hobbyhop.domain.user.service.KakaoService;
import com.hobbyhop.global.security.filter.JwtAuthenticationFilter;
import com.hobbyhop.global.security.filter.JwtAuthorizationFilter;
import com.hobbyhop.global.security.jwt.JwtUtil;
import com.hobbyhop.global.security.userdetails.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
//	private final OAuth2Service oAuth2Service;
//	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
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
		http.csrf(AbstractHttpConfigurer::disable);
		http.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.exceptionHandling(
				(exception) ->
						exception
								.authenticationEntryPoint(authenticationEntryPoint()) // 토큰인증 실패시
								.accessDeniedPage("/error/403") // 권한없는 페이지 접근 시 (admin)
		);

		http.authorizeHttpRequests((authorizeHttpRequests) ->
				authorizeHttpRequests
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()// resources 접근 허용 설
						.requestMatchers("/**","/error/**").permitAll() // 메인페이지, error 페이지 접근 허용
						.requestMatchers("/api/users/signup", "/api/users/login").permitAll() // 회원가입, 로그인 페이지 접근 허용
						.requestMatchers("/login/kakao/callback").permitAll() // 카카오 소셜 로그인 허용
						.requestMatchers("/api/users/**").permitAll()
						.requestMatchers("/api/**").permitAll()
						.requestMatchers("/api/clubs/emailcheck/**").permitAll() // 그룹 이메일 초대 허용
						.requestMatchers("/v2/api-docs", "/swagger-resourcees/**",
										 "/swagger-ui.html", "/webjars/**", "/swagger/**").permitAll() // 스웨거 허용
						.anyRequest().authenticated()
		);

		http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint(){
		return new CustomAuthenticationEntryPoint();
	}
}

//	@Bean
//	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userRepository, refreshTokenService);
//		filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
//		return filter;
//	}

//		http.authorizeHttpRequests((authorizeHttpRequest) ->
//				authorizeHttpRequest
//						.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
//						.permitAll()
//						.requestMatchers("/api/users/**")
//						.permitAll()
//						.dispatcherTypeMatchers(HttpMethod.valueOf("/login/kakao/callback")) // Kakao 인증 후 콜백 URL
//						.permitAll()
//						.anyRequest()
//						.authenticated()
//		);

//		http.oauth2Login((oauth2) ->
//				oauth2
//						.loginPage("/login") // 필요에 따라 로그인 페이지 URL 사용자 정의
//						.userInfoEndpoint(
//								userInfoEndpointConfig -> userInfoEndpointConfig.userService())
//						.clientRegistrationRepository(clientRegistrationRepository ->
//								clientRegistrationRepository
//										.addRegistration("kakao", // 등록 ID, oAuth2Service와 일치해야 함
//												registration -> registration
//														.clientId("your-kakao-client-id")
//														.clientSecret("your-kakao-client-secret")
//														.redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
//														.authorizationUri("https://kauth.kakao.com/oauth/authorize")
//														.tokenUri("https://kauth.kakao.com/oauth/token")
//														.userInfoUri("https://kapi.kakao.com/v2/user/me")
//														.userNameAttributeName("id")
//														.clientName("kakao")
//										)
//						)
//		);
//		http.oauth2Login(
//				(oauth2) ->
//						oauth2
//								.successHandler(oAuth2LoginSuccessHandler)
//								.failureHandler(oAuth2LoginFailureHandler)
//								.userInfoEndpoint(
//										userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2Service)));


//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(AbstractHttpConfigurer::disable);
//		http.sessionManagement((sessionManagement) ->
//				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//		http.authorizeHttpRequests((authorizeHttpRequest)
//				->
//				authorizeHttpRequest
//						.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
//						.permitAll()
//						.requestMatchers("/login/kakao/callback","/api/users/**")
//						.permitAll()
//						.anyRequest()
//						.authenticated() // 그 외 모든 요청 인증처리
//				);
////				.formLogin(login -> login	// form 방식 로그인 사용
////				.defaultSuccessUrl("/view/dashboard", true)	// 성공 시 dashboard로
////				.permitAll()	// 대시보드 이동이 막히면 안되므로 얘는 허용
////		)
////				.logout(withDefaults());	// 로그아웃은 기본설정으로 (/logout으로 인증해제)
////
////		return http.build();
//
//		http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
//		return http.build();
//
//	}
//}