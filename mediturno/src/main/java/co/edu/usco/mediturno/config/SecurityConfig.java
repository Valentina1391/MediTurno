package co.edu.usco.mediturno.config;

import co.edu.usco.mediturno.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Configuración de seguridad
 */
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration c = new CorsConfiguration();
                c.setAllowedOrigins(List.of("http://localhost:4200"));
                c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                c.setAllowedHeaders(List.of("*"));
                return c;
            }))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                // Especialidades y médicos: todos pueden consultar, solo ADMIN modifica
                .requestMatchers(HttpMethod.GET, "/api/especialidades/**", "/api/medicos/**").authenticated()
                .requestMatchers("/api/especialidades/**", "/api/medicos/**").hasRole("ADMIN")
                // Citas
                .requestMatchers(HttpMethod.POST, "/api/citas").hasAnyRole("PACIENTE", "ADMIN")
                .requestMatchers("/api/citas/*/estado").hasAnyRole("MEDICO", "ADMIN")
                .requestMatchers("/api/citas/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}