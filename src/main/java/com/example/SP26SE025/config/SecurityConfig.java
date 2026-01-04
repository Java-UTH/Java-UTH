package com.example.SP26SE025.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.SP26SE025.repository.UserRepository;
import com.example.SP26SE025.security.JwtFilter;
import com.example.SP26SE025.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // =========================
    // AUTHENTICATION BEANS
    // =========================

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /*
    // =========================
    // OAUTH2 – TẠM THỜI TẮT
    // =========================
    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository, new BCryptPasswordEncoder());
    }
    */

    // =========================
    // SECURITY FILTER CHAIN
    // =========================

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

            .authenticationProvider(authenticationProvider())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/home", "/login", "/register",
                    "/css/**", "/js/**", "/images/**", "/fonts/**",
                    "/authenticate"
                ).permitAll()

                // ROLE-BASED ACCESS CONTROL
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/clinic/**").hasRole("CLINIC")
                .requestMatchers("/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/staff/**").hasRole("STAFF")
                .requestMatchers(
                    "/customer/**",
                    "/profile",
                    "/test-services/**",
                    "/menstrual_cycle/**"
                ).hasRole("CUSTOMER")

                .anyRequest().authenticated()
            )

            // =========================
            // FORM LOGIN
            // =========================
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")

                .successHandler((request, response, authentication) -> {
                    var authorities = authentication.getAuthorities();
                    String redirectUrl = "/login?error=true";

                    for (var authority : authorities) {
                        String role = authority.getAuthority();

                        if (role.equals("ROLE_ADMIN")) {
                            redirectUrl = "/admin/users";
                            break;
                        } else if (role.equals("ROLE_CLINIC")) {
                            redirectUrl = "/clinic/home";
                            break;
                        } else if (role.equals("ROLE_DOCTOR")) {
                            redirectUrl = "/doctor/home";
                            break;
                        } else if (role.equals("ROLE_CUSTOMER")) {
                            redirectUrl = "/customer/home";
                            break;
                        }
                    }
                    response.sendRedirect(redirectUrl);
                })

                .failureUrl("/login?error=true")
                .permitAll()
            )

            /*
            // =========================
            // OAUTH2 LOGIN – TẠM TẮT
            // =========================
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo ->
                    userInfo.userService(customOAuth2UserService())
                )
                .successHandler((request, response, authentication) ->
                    response.sendRedirect("/customer/home")
                )
                .failureHandler((request, response, exception) ->
                    response.sendRedirect("/login?oauth2_error=true")
                )
            )
            */

            // =========================
            // LOGOUT
            // =========================
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        // JWT FILTER
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
