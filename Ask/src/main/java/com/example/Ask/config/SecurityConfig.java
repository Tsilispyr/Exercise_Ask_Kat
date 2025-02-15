package com.example.Ask.config;

import com.example.Ask.Entities.Role;
import com.example.Ask.Entities.User;
import com.example.Ask.Repositories.RoleRepository;
import com.example.Ask.Repositories.UserRepository;
import com.example.Ask.Service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private RoleRepository roleRepository;
    private UserService userService;

    private UserDetailsService userDetailsService;

    private BCryptPasswordEncoder passwordEncoder;
    public SecurityConfig(UserService userService, UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/register", "/saveUser", "/images/**", "/js/**", "/css/**").permitAll()
                        .requestMatchers("/Requests").hasRole("ADMIN")
                        .requestMatchers("/Requests").hasRole("DOCTOR")
                        .requestMatchers("/Requests").hasRole("SHELTER")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/Animal", true)
                        .permitAll())
                .logout((logout) -> logout.permitAll());
        return http.build();
    }


}
