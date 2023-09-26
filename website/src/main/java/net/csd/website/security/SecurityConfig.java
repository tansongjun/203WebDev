package net.csd.website.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    // UserDetailsService: to load user details from a database
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc) {
        this.userDetailsService = userSvc;
    }

    /**
     * Exposes a bean of DaoAuthenticationProvider, a type of AuthenticationProvider
     * Attaches the user details and the password encoder
     * 
     * @return
     */

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    // to return http error messages
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable()) // CSRF protection is needed only for browser-based attacks
                .formLogin(fg -> fg.disable())
                .authenticationProvider(authenticationProvider()) // specifies the authentication provider for
                                                                  // HttpSecurity
                // security
                // // .requestMatchers(new AntPathRequestMatcher("/api/v1/people", "POST"))
                // );
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/people/**")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/people/**")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/v1/people/**")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/v1/people/**")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/getallQ")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/patients/{id}/getQ"))
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_PATIENT")
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/patients/{id}/getnewQ"))
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_PATIENT")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/login/{username}")).hasAuthority("ROLE_PATIENT")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/admin/login/{username}")).hasAuthority("ROLE_ADMIN")

                        .anyRequest().permitAll())
                .httpBasic(withDefaults()) // allow Http Basic Authentication
        ;
        return http.build();
    }

    /**
     * @Bean annotation is used to declare a PasswordEncoder bean in the Spring
     *       application context.
     *       Any calls to encoder() will then be intercepted to return the bean
     *       instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
