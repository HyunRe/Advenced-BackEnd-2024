package com.lion.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(auth -> auth.disable())   // CSRF 방어 기능 비활성화
                .headers(x -> x.frameOptions(y -> y.disable()))     // H2 Console 사용을 위해
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/book/list", "/book/detail", "/mall/list",
                                "/mall/detail", "/user/register", "h2-console", "/demo/**",
                                "img/**", "/js/**", "/css/**", "/error/**").permitAll()     // 누구든 허용
                        .requestMatchers("/book/insert", "/book/yes24", "/order/listAll",
                                "/order/bookStat", "/user/list", "/user/delete").hasAuthority("ROLE_ADMIN")     // 인가된 관리자 허용
                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth
                        .loginPage("/user/login")       // login form
                        .loginProcessingUrl("/user/login")      // 스프링이 낚아 챔 UserDetailService 구현 객체에서 처리해 주어야 함
                        .usernameParameter("uid")
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/user/loginSuccess", true)      // 로그인 후 해야할 일
                        .permitAll()
                )
                .logout(auth ->auth
                        .logoutUrl("/user/logout")
                        .invalidateHttpSession(true)        // 로그아웃 시 session 삭제
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/user/login")
                );
        return httpSecurity.build();
    }
}
