package openchat.authservice.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import openchat.authservice.model.User;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private String EXPIRATION;

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
