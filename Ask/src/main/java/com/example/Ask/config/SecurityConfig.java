// package com.example.Ask.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
// import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//             .authorizeHttpRequests(authz -> authz
//                 .requestMatchers("/public/**", "/error", "/").permitAll()
//                 .anyRequest().authenticated()
//             )
//            .oauth2ResourceServer(oauth2 -> oauth2
//                 .jwt(jwt -> jwt
//                   .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                 )
//             );
//        return http.build();
//     }


//   @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//     http
//         .authorizeHttpRequests(auth -> auth
//             .requestMatchers("/", "/index", "/css/**", "/js/**", "/images/**",
//                              "/Animal/**", "/Request/**", "/register", "/saveUser", "/login", "/error/**").permitAll()
//             .anyRequest().authenticated()
//         )
//         .formLogin(form -> form
//             .loginPage("/login").permitAll()
//         )
//         .logout(logout -> logout.permitAll());

//     // Απενεργοποιούμε προσωρινά το Keycloak/JWT για να δούμε τα views
//     http.oauth2ResourceServer().disable();
//     return http.build();
// }

//     private JwtAuthenticationConverter jwtAuthenticationConverter() {
//         JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//         grantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
//         grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

//         JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//         converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//         return converter;
//     }
// }






// package com.example.Ask.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
// import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import java.util.List;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .cors() // ενεργοποίηση CORS
//             .and()
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/", "/index", "/css/**", "/js/**", "/images/**").permitAll()
//                 .anyRequest().authenticated()
//             )
//             .oauth2ResourceServer(oauth2 -> oauth2
//                 .jwt(jwt -> jwt
//                     .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                 )
//             );

//         return http.build();
//     }

//     private JwtAuthenticationConverter jwtAuthenticationConverter() {
//         JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//         grantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
//         grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

//         JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//         converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//         return converter;
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration config = new CorsConfiguration();
//         config.setAllowedOrigins(List.of("http://localhost:5173"));
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         config.setAllowedHeaders(List.of("*"));
//         config.setAllowCredentials(true); // χρήσιμο αν έχεις cookie ή Authorization

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);
//         return source;
//     }
// }





// package com.example.Ask.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers(
//                     "/", "/index", "/css/**", "/js/**", "/images/**",
//                     "/Animal/**", "/Request/**", "/register", "/saveUser", "/login", "/error/**"
//                 ).permitAll()
//                 .anyRequest().authenticated()
//             )
//             .formLogin(form -> form
//                 .loginPage("/login").permitAll()
//             )
//             .logout(logout -> logout.permitAll());

//         // Απενεργοποιούμε προσωρινά την Keycloak ασφάλεια
//         http.oauth2ResourceServer().disable();

//         return http.build();
//     }
// }



package com.example.Ask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/index", "/css/**", "/js/**", "/images/**",
                    "/Animal/**", "/Request/**", "/register", "/saveUser", "/login", "/error/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login").permitAll()
            )
            .logout(logout -> logout.permitAll());

        // Απενεργοποιούμε πλήρως την ασφάλεια με Keycloak
        http.oauth2ResourceServer().disable();

        return http.build();
    }
}






// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/", "/index", "/css/**", "/js/**", "/images/**",
//                                  "/Animal/**", "/Request/**", "/register", "/saveUser", "/login", "/error/**").permitAll()
//                 .anyRequest().authenticated()
//             )
//             .formLogin(form -> form
//                 .loginPage("/login").permitAll()
//             )
//             .logout(logout -> logout.permitAll());

//         // Απενεργοποιούμε προσωρινά την Keycloak ασφάλεια
//         http.oauth2ResourceServer().disable();

//         return http.build();
//     }
// }



// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {
// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//     http
//         .csrf().disable()
//         .authorizeHttpRequests(auth -> auth
//             .anyRequest().permitAll()
//         );

//     return http.build();
// }
// }






// package com.example.Ask.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
// import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import java.util.List;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .cors().and()
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/", "/index", "/login", "/register", "/saveUser", "/css/**", "/js/**", "/images/**").permitAll()
//                 .anyRequest().authenticated()
//             );
//             //.oauth2ResourceServer(oauth2 -> oauth2
//                 //.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
//             //);
//         return http.build();
//     }

//     private JwtAuthenticationConverter jwtAuthenticationConverter() {
//         JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
//         converter.setAuthorityPrefix("ROLE_");
//         converter.setAuthoritiesClaimName("realm_access.roles");

//         JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
//         jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
//         return jwtConverter;
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration config = new CorsConfiguration();
//         config.setAllowedOrigins(List.of("http://localhost:5173")); // Vue frontend (προαιρετικό)
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         config.setAllowedHeaders(List.of("*"));
//         config.setAllowCredentials(true);
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);
//         return source;
//     }
// }
