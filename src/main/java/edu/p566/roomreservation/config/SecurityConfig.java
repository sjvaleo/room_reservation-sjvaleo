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

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login", "/styles.css", "/css/**", "/js/**", "/images/**").permitAll()
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/login")
        .defaultSuccessUrl("/search", true)   // land on /search after login
        .permitAll()
      )
      .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll()
      )
      .csrf(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(
      User.withUsername("user").password("{noop}password").roles("USER").build()
    );
  }
}
