package com.munira.miniproject.security;

import com.munira.miniproject.constants.AppConstants;
import com.munira.miniproject.exception.UnauthorizedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->{
                    auth
                            .requestMatchers(HttpMethod.POST,
                                    AppConstants.SIGN_IN,
                                    AppConstants.SIGN_UP
                            ).permitAll()
                            .requestMatchers(HttpMethod.GET,"/users/{userId}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,"/users/{userId}/history").hasRole("USER")
                            .requestMatchers(HttpMethod.GET,"/users/{userId}/**").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.GET,"/books/all").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.POST,"/books/create").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT,"/books/update").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE,"/books/delete").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,"/books/{bookId}/borrow").hasRole("USER")
                            .requestMatchers(HttpMethod.GET,"/books/{bookId}/return").hasRole("USER")
                            .requestMatchers(HttpMethod.POST,"/books/{bookId}/review/create").hasRole("USER")
                            .requestMatchers(HttpMethod.GET,"/books/{bookId}/review").hasAnyRole("USER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT,"/books/{bookId}/review/{reviewId}/update").hasRole("USER")
                            .requestMatchers(HttpMethod.DELETE,"/books/{bookId}/review/{reviewId}/delete").hasRole("USER")
                            .anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            throw new UnauthorizedException("You are not authorized to access this!!");
                        })
                )
                .addFilter(new CustomAuthenticationFilter(authenticationManager))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}
