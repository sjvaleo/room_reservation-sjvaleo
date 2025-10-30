package edu.p566.roomreservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(auth -> auth
              .requestMatchers(
                  "/", "/search", "/availability", "/availability/**",
                  "/rooms/**", "/floors/**", "/login",
                  // make sure our static stylesheet is always public
                  "/styles.css", "/css/**", "/js/**", "/images/**"
              ).permitAll()
              .anyRequest().authenticated()
          )
          .formLogin(form -> form
              .loginPage("/login")
              // after login, we'll check for a pending booking
              .defaultSuccessUrl("/post-login", false)
              .permitAll()
          )
          .logout(logout -> logout
              .logoutUrl("/logout")
              .logoutSuccessUrl("/search")
              .deleteCookies("JSESSIONID")
              .permitAll()
          )
          .csrf(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user  = User.withUsername("user").password("{noop}password").roles("USER").build();
        var admin = User.withUsername("admin").password("{noop}password").roles("USER","ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
