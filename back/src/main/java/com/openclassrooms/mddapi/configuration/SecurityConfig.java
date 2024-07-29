package com.openclassrooms.mddapi.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Configuration de la sécurité Spring pour l'application.
 * Définit les règles de sécurité et les filtres de sécurité à appliquer.
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final UserAuthProvider userAuthProvider;

/**
 * Définit le bean PasswordEncoder pour encoder les mots de passe des utilisateurs.
 *
 * @return une instance de BCryptPasswordEncoder
 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Configure les règles de sécurité pour l'application.
     *
     * @param http l'objet HttpSecurity pour personnaliser les configurations de sécurité
     * @return l'objet SecurityFilterChain construit
     * @throws Exception si une erreur survient lors de la configuration de la sécurité
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers(HttpMethod.POST, "/auth/login","/auth/register").permitAll()
                                .anyRequest().authenticated()
                );
        return  http.build();
    }


}
