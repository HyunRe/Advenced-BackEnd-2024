package com.lion.demo.config;

import com.lion.demo.security.JwtRequestFilter;
import com.lion.demo.security.MyOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SecurityConfig {
    @Autowired private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired private MyOAuth2UserService myOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(auth -> auth.disable())   // CSRF 방어 기능 비활성화
                .headers(x -> x.frameOptions(y -> y.disable()))     // H2 Console 사용을 위해
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/book/**", "/cart/**", // "/book/list", "/book/detail",
                                "/mall/list", "/mall/detail",
                                "/user/register", "h2-console", "/demo/**",
                                "img/**", "/js/**", "/css/**", "/error/**").permitAll()     // 누구든 허용
                        .requestMatchers(// "/book/insert", "/book/yes24",
                                "/order/listAll",
                                "/order/bookStat", "/user/list", "/user/delete").hasAuthority("ROLE_ADMIN")     // 인가된 관리자 허용
                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth
                        .loginPage("/user/login")       // login form
                        .loginProcessingUrl("/user/login")      // 스프링이 낚아 챔 UserDetailService 구현 객체에서 처리해 주어야 함
                        .usernameParameter("uid")
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/user/loginSuccess", true)      // 로그인 후 해야할 일
                        .failureHandler(authenticationFailureHandler)       // 로그인 실패 (password 오류)
                        .permitAll()
                )
                .logout(auth ->auth
                        .logoutUrl("/user/logout")
                        .invalidateHttpSession(true)        // 로그아웃 시 session 삭제
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/user/login")
                )
                .oauth2Login(auth -> auth
                        .loginPage("/user/login")
                        // 1. 코드 받기 (인증) 2. 엑세스 토큰 (권한) 3. 사용자 프로필 정보 획득
                        // 4. 3에서 받은 정보를 토대로 DB에 없으면 회원 가입
                        // 5. provider가 제공 하는 정보
                        .userInfoEndpoint(user -> user.userService(myOAuth2UserService))
                        .defaultSuccessUrl("/user/loginSuccess", true)
                );
        return httpSecurity.build();
    }

    // JWT Filter Bean 등록
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    // Authentication Manager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
