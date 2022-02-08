package openchat.authservice.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import openchat.authservice.util.JwtUtil;
import reactor.core.publisher.Mono;

@Component
public class AuthContextRepository implements ServerSecurityContextRepository {

    @Autowired
    private AuthManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return jwtUtil.extractToken(exchange.getRequest())
            .switchIfEmpty(Mono.empty())
            .flatMap(authToken -> {
                Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
                return authManager.authenticate(auth).map(SecurityContextImpl::new);
            });
    }
}
