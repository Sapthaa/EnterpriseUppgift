package com.sapthaa.webserviceuppgift.config.Security;

import com.sapthaa.webserviceuppgift.service.CustomUserDetailsService;
import com.sapthaa.webserviceuppgift.service.LoggerService;
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

    private final LoggerService logger;

    public MovieWebSecurityConfiguration(LoggerService logger) {
        this.logger = logger;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailsService customUserDetailsService) {
        logger.info("Konfigurerar SecurityFilterChain...");

        try {
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
                    )
                    .rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
                            .rememberMeParameter("remember-me")
                            .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1000))
                            .key("SecurityKey")
                            .userDetailsService(customUserDetailsService));

            logger.info("SecurityFilterChain konfigurerad utan fel.");
            return http.build();

        } catch (Exception e) {
            logger.error("Ett fel uppstod vid konfiguration av SecurityFilterChain: {}" + e.getMessage(), e);
            throw new RuntimeException("Kunde inte konfigurera säkerhetsinställningarna", e);
        }
    }
}
