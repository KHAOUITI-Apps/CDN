/**
 * Copyright © KHAOUITI Apps 2025 | https://www.khaouitiapps.com/
 *
 * Author: KHAOUITI ABDELHAKIM (Software Engineer from ENSIAS)
 *
 * Any use, distribution, or modification of this code must be explicitly allowed by the owner.
 * For permissions, contact me or visit my LinkedIn:
 * https://www.linkedin.com/in/khaouitiabdelhakim/
 */

package com.khaouitiapps.CDN;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/video/**").permitAll() // Allow video streaming
                        .anyRequest().authenticated()
                )
                .csrf().disable(); // Disable CSRF for testing

        return http.build();
    }
}
