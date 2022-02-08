package openchat.authservice.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import openchat.authservice.model.User;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private String EXPIRATION;

    private static final String BEARER_PREFIX = "Bearer ";

    public Mono<String> extractToken(@NonNull ServerHttpRequest request) {
        try {
            HttpHeaders headers = request.getHeaders();
            String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
                String token = authorization.substring(BEARER_PREFIX.length()).trim();
                if (!token.isBlank()) {
                    return Mono.just(token);
                }
            }

            return Mono.empty();
        } catch (Exception error) {
            log.error("Can not extract token", error);
            return Mono.empty();
        }
    }

    public DecodedJWT getAllClaimsFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier jwtVerifier = JWT
            .require(algorithm)
            .build();
        DecodedJWT jwt = jwtVerifier.verify(token);

        return jwt;
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiresAt();
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        return doGenerateToken(claims, user.getMail());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        Long expirationTimeLong = Long.parseLong(EXPIRATION);
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);

        return JWT.create()
            .withExpiresAt(expirationDate)
            .sign(algorithm);
    }
}
