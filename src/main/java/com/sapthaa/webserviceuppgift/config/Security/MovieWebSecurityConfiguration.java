package com.sapthaa.webserviceuppgift.config.Security;

import com.sapthaa.webserviceuppgift.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class MovieWebSecurityConfiguration {

    private CustomUserDetailsService customUserDetailsService;

    public MovieWebSecurityConfiguration(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/", "/all-movies", "/search-movie", "/login", "/logout", "/register", "/movie-detail/**", "/styles.css", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Ändra omdirigering efter inloggning
                        .failureUrl("/login?error=true")
                        .permitAll() // Tillåt alla att nå login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        ).rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(1000))
                        .key("SecurityKey")
                        .userDetailsService(customUserDetailsService));

        return http.build();
    }


}
