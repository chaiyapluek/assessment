package com.kbtg.bootcamp.posttest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(req -> req   
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/users/**", "/lotteries/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults())
            .build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder.encode("password"))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
