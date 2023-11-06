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

    private UserDetailsService userDetailsService; // load user details from a database

    public SecurityConfig(UserDetailsService userSvc) {  // constructor class
        this.userDetailsService = userSvc;
    }

    /**
     * @Bean annotation is used to expose a DaoAuthenticationProvider, type of AuthenticationProvider, 
     *       bean in the Spring application context.
     * Any calls to authenticationProvider() will then be intercepted to 
     *       @return the bean instance with attached user details and password encoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    /**
     * @Bean annotation is used to define a custom MvcRequestMatcher.Builder 
     *       bean in the Spring application context.
     * Any calls to mvc() will then be intercepted to @return the bean instance.
     */
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    /** 
     * @Bean annotation is used to define a Cross-Origin Resource Sharing (CORS) settings configurer
     *        bean in the Spring application context.
     * Any calls to corsConfigurer() will then be intercepted to 
     *        Allow cross-origin requests from "http://localhost:3000" to any endpoint (/**)
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("https://203-web-dev.vercel.app");
            }
        };
    }
    
    /** 
     * @Bean annotation is used to define a securityFilterChain settings configurer
     *        bean in the Spring application context.
     * Any calls to securityFilterChain() will then be intercepted to 
     *        Disable Cross-Origin Resource Sharing (CORS) 
     *               to allow or restrict web browsers from making requests
     *        Enable Cross-Site Request Forgery (CSRF) protection
     *               as it's needed for browser-based attacks  
     *        Disable form-based login
     *        Specify the authentication provider for HttpSecurity
     *        Specify the authorization rules for HttpSecurity
     *        Enable Http Basic Authentication
     *        @return the http built bean instance.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .cors(cors -> cors.disable()) // allow or restrict web browsers from making requests
                .csrf(csrf -> csrf.disable()) // CSRF protection is needed only for browser-based attacks
                .formLogin(fg -> fg.disable())
                .authenticationProvider(authenticationProvider()) // specifies HttpSecurity authentication provider

                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/people/**"))
                                                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/people/**"))
                                                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/v1/people/**"))
                                                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/v1/people/**"))
                                                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/getallQ"))
                                                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/patients/{id}/getQ"))
                                                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_PATIENT")
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/patients/{id}/getnewQ"))
                                                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_PATIENT")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/rooms"))
                                                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/rooms"))
                                                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/datetimeslot"))
                                                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/appointment/queryAvailableTimeSlot/{date}"))
                                                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_PATIENT")
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/appointment/bookNewAppointment"))
                                                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_PATIENT")

                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/login/{username}"))
                                                        .hasAnyAuthority("ROLE_PATIENT", "ROLE_PATIENT_UNVERIFIED")
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/admin/login/{username}"))
                                                        .hasAuthority("ROLE_ADMIN")

                        .anyRequest().permitAll())
                .httpBasic(withDefaults()) // allow Http Basic Authentication
        ;
        return http.build();
    }

    /**
     * @Bean annotation is used to declare a PasswordEncoder
     *       bean in the Spring application context.
     * Any calls to encoder() will then be intercepted to @return the bean instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
