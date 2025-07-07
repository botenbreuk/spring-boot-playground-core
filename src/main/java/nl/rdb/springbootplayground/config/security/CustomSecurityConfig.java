package nl.rdb.springbootplayground.config.security;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse;
import static org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN;
import static org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher.withDefaults;

import lombok.RequiredArgsConstructor;
import nl.rdb.springbootplayground.config.error.GenericErrorHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final GenericErrorHandler errorHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public RestAuthenticationFilter authenticationFilter() throws Exception {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/authentication", HttpMethod.POST.name());
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return new RestAuthenticationFilter(errorHandler, matcher, authenticationManager, objectMapper);
    }

    @Bean
    public RestAccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler(errorHandler);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Set custom filters in the chain between existing spring security filters.
        http.addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class);

        // Set paths that needs special or no security.
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/authentication").permitAll()
                .anyRequest().authenticated());

        // Handlers for when authentication goes wrong.
        http.exceptionHandling(config -> config
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(accessDeniedHandler()));

        // Configure logout
        http.logout(config -> config
                .logoutRequestMatcher(withDefaults().matcher(DELETE, "/authentication"))
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()));

        // Set csrf repository for CSRF/XSRF
        http.csrf(config -> config
                .csrfTokenRequestHandler(csrfRequestHandler())
                .csrfTokenRepository(csrfTokenRepository()));

        // See: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Referrer-Policy
        http.headers(config -> config.referrerPolicy(referrerPolicy -> referrerPolicy.policy(SAME_ORIGIN)));

        // Set session manager. Also configures HttpSessionSecurityContextRepository for keeping track
        // of the session between calls.
        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = withHttpOnlyFalse();
        repository.setCookiePath("/");
        return repository;
    }

    private CsrfTokenRequestAttributeHandler csrfRequestHandler() {
        CsrfTokenRequestAttributeHandler csrfTokenHandler = new CsrfTokenRequestAttributeHandler();
        // By setting the csrfRequestAttributeName to null, the CsrfToken must first be loaded to determine what attribute name to use.
        // This causes the CsrfToken to be loaded on every request.
        // This disables deferred loading CsrfToken.
        csrfTokenHandler.setCsrfRequestAttributeName(null);
        return csrfTokenHandler;
    }
}
