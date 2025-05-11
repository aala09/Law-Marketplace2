package tn.esprit.cloud_in_mypocket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // WebSocket
                        .requestMatchers("/ws/**").permitAll()

                        // Auth & Registration
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()

                        // Uploads
                        .requestMatchers(HttpMethod.POST, "/api/uploads/user-photo").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/uploads/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/uploads/**").permitAll()

                        // Packs
                        .requestMatchers(HttpMethod.GET, "/api/packs/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/packs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/packs/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/packs/**").permitAll()

                        //
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()

                        // Paiements
                        .requestMatchers(HttpMethod.POST, "/api/paiements/create-intent").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/paiements/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/paiements/**").permitAll()

                        // Subscription Notifications
                        .requestMatchers(HttpMethod.POST, "/api/subscription-notifications").permitAll()

                        // Recu
                        .requestMatchers(HttpMethod.GET, "/api/recu/**").permitAll()

                        // Users
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").permitAll()

                        // Revenue prediction
                        .requestMatchers(HttpMethod.GET, "/api/revenue/predict").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/revenue/predict/**").permitAll()

                        // Test endpoints
                        .requestMatchers("/api/test/**").permitAll()

                        // Everything else needs authentication
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Angular frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("adminpass"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("userpass"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
