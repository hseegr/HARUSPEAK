package com.haruspeak.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/login/**", "/oauth2/**").permitAll()
                    .anyRequest().permitAll()
//                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/") // 로그인 실패 시 이동할 URL
            );

        return http.build();
    }
}