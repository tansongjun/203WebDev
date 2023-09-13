package net.csd.website.security;

import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
// import org.springframework.security.web.util.matcher.RequestMatcher;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    
    // UserDetailsService: to load user details from a database
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc){
        this.userDetailsService = userSvc;
    }
    
    /**
     * Exposes a bean of DaoAuthenticationProvider, a type of AuthenticationProvider
     * Attaches the user details and the password encoder   
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .cors(cors -> cors.disable())
        .csrf(csrf -> csrf.disable()) // CSRF protection is needed only for browser-based attacks
        .formLogin(fg -> fg.disable())
        .authenticationProvider(authenticationProvider()) //specifies the authentication provider for HttpSecurity
        .securityMatchers((securityMatchers) -> securityMatchers
            .requestMatchers(new AntPathRequestMatcher("/api/v1/people/")) // broken security
            // .requestMatchers(new AntPathRequestMatcher("/api/v1/people", "POST"))
        )
        .authorizeHttpRequests((authz) -> authz
            .requestMatchers(new AntPathRequestMatcher("/api/v1/people/", "GET")).hasRole("ADMIN")
            .requestMatchers(new AntPathRequestMatcher("/api/v1/people", "POST")).hasRole("ADMIN")
            .requestMatchers(new AntPathRequestMatcher("/api/v1/people/*", "PUT")).hasRole("ADMIN")
            .requestMatchers(new AntPathRequestMatcher("/api/v1/people/*", "DELETE")).hasRole("ADMIN")

            
            // .requestMatchers("/api/v1/people/**").hasRole("ADMIN")
            // .MVCMatchers("/api/v1/people/**").servletPath("/path").hasRole("ADMIN")
            
            // .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/people")).hasRole("ADMIN")
            // .requestMatchers(new AntPathRequestMatcher("/api/v1/people/", "GET")).authenticated()
            // .requestMatchers(new AntPathRequestMatcher("/api/v1/people/**", "GET")).authenticated()
            // .requestMatchers(new AntPathRequestMatcher("/api/v1/people", "POST")).authenticated()
            
            // .requestMatchers(new AntPathRequestMatcher("/api/v1/people", "POST")).hasRole("ADMIN")
            // // .requestMatchers(new AntPathRequestMatcher("/api/v1/people/**", "GET")).authenticated()
            // .requestMatchers(new AntPathRequestMatcher("/api/v1/people/*", "PUT")).hasRole("ADMIN")
            // .requestMatchers(new AntPathRequestMatcher("/api/v1/people/*", "DELETE")).hasRole("ADMIN")
        )
        .httpBasic(withDefaults()); // allow Http Basic Authentication
        return http.build();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //     .cors(cors -> cors.disable())
    //     .csrf(csrf -> csrf.disable()) // CSRF protection is needed only for browser based attacks
    //     .authorizeHttpRequests((requests) -> requests
	// 			.requestMatchers("/api/v1/people/**").permitAll()
	// 			.anyRequest().authenticated()
    //     );
    //     // .authorizeHttpRequests(authorizeRequests -> 
    //     //      authorizeRequests
    //     //      .requestMatchers("/api/v1/people/**").permitAll()
    //     // );
    //     // .authenticationProvider(authenticationProvider()); //specifies the authentication provider for HttpSecurity

    //     // .formLogin(logp -> logp.disable());  // Disable login form
    //     // .formLogin(formLogin ->  // Enable login form
    //     //         formLogin
    //     //             .loginPage("/login")
    //     //             .permitAll()
    //     // );
    //     // .rememberMe(withDefaults());  // Remember me function
    //     return http.build();
    // }

    /**
     * @Bean annotation is used to declare a PasswordEncoder bean in the Spring application context. 
     * Any calls to encoder() will then be intercepted to return the bean instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
 