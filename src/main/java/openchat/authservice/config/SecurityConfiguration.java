package openchat.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import openchat.authservice.auth.AuthContextRepository;
import openchat.authservice.auth.AuthManager;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Autowired
    private AuthManager authManager;

    @Autowired
    private AuthContextRepository authContextRepository;

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
            // Stateless
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .logout().disable()

            // .requestCache()
            // .requestCache(NoOpServerRequestCache.getInstance())
            // .and()

            .authenticationManager(authManager)
            .securityContextRepository(authContextRepository)

            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers("/api/v0.1/auth/sign-in").permitAll()
            .pathMatchers("/api/v0.1/auth/sign-up").permitAll()
            .anyExchange().authenticated()
            .and().build();
    }
}
