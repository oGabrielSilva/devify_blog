package cyou.devify.blog.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cyou.devify.blog.enums.Role;
import cyou.devify.blog.filters.SecurityFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig implements WebMvcConfigurer {
  @Autowired
  SecurityFilter securityFilter;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            request -> request.requestMatchers("/internal/profile", "/internal/profile/**")
                .hasRole(Role.COMMON.asString())
                .requestMatchers("/internal/mod", "/internal/mod/**").hasRole(Role.MODERATOR.asString())
                .requestMatchers("/internal", "/internal/**").hasRole(Role.EDITOR.asString())
                .requestMatchers("/internal/admin", "/internal/admin/**").hasRole(Role.ADMIN.asString())
                .anyRequest().permitAll())
        .exceptionHandling(ex -> ex.accessDeniedPage("/403")
            .authenticationEntryPoint((req, res, exMet) -> res.sendRedirect("/session?next=" + req.getServletPath())))
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
