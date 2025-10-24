package org.example.personalblog.config;

import lombok.RequiredArgsConstructor;
import org.example.personalblog.util.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${FRONTEND_URI}")
    private String frontendUri;
    private final JwtAuthFilter jwtAuthFilter;
    private static final String ROLE_ADMIN = "ADMIN";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/articles", "/api/v1/articles/**").permitAll()
                        .requestMatchers("/api/v1/auth/login", "/api/v1/subscriptions",
                                "/api/v1/subscriptions/confirm").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/articles").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/articles/*").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/articles/*").hasRole(ROLE_ADMIN)

                        .anyRequest().authenticated())
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin(frontendUri);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
