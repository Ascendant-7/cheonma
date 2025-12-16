package corp.sky.nano.security;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import corp.sky.nano.user.CustomerUserDetailsService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private CustomerUserDetailsService service;
    private JwtFilter filter;

    public SecurityConfig(CustomerUserDetailsService service, JwtFilter filter) {
        this.service = service;
        this.filter = filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (Exception e) {
            secureRandom = new SecureRandom();
        }
        return new BCryptPasswordEncoder(12, secureRandom);
    }

    @Bean
    public SecurityFilterChain securiyFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(crsf -> crsf.disable()).cors(Customizer.withDefaults()).userDetailsService(service)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/api/login", "/login", "/register", "/api/register").permitAll()
                                .requestMatchers("/products").authenticated()
                                .anyRequest().authenticated())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    // @Bean
    // public DaoAuthenticationProvider authProvider() {
    // DaoAuthenticationProvider authenticationProvider = new
    // DaoAuthenticationProvider(service);
    // authenticationProvider.setPasswordEncoder(passwordEncoder());
    // return authenticationProvider;
    // }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
