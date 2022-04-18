package openchat.authservice.repository;

import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import openchat.authservice.model.RefreshTokenModel;

public interface RefreshTokenRepository extends Neo4jRepository<RefreshTokenModel, Long> {

    RefreshTokenModel findByToken(UUID token);

    RefreshTokenModel findByEmail(String email);

    @Query("MATCH (refreshToken:RefreshTokenModel{token: $token}) DELETE refreshToken")
    void deleteByToken(String token);

    @Query("MATCH (refreshToken:RefreshTokenModel) WHERE $token IN refreshToken.oldToken DELETE refreshToken")
    void deleteByOldToken(String token);
}
