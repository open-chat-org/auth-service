package openchat.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
            // Stateless
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .logout().disable()
            .build();
    }
}
