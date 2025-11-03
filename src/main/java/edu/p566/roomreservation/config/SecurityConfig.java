package edu.p566.roomreservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Simple in-memory user so login works immediately in Render.
     * Username: user
     * Password: password
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("user")
                .password("{noop}password")
                .roles("USER")
                .build()
        );
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public pages & assets
                .requestMatchers("/", "/login", "/error", "/styles.css").permitAll()
                .requestMatchers("/search/**", "/floors/**", "/rooms/**", "/availability/**").permitAll()
                .requestMatchers("/bookings/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(login -> login
                .loginPage("/login").permitAll()
                
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout").permitAll()
            )
            // Keep CSRF enabled; your forms include the token
            .csrf(Customizer.withDefaults());

        return http.build();
    }
}
