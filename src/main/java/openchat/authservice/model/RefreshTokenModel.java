package openchat.authservice.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.Data;

@Node
@Data
public class RefreshTokenModel {

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private UUID token;
    private Instant expiryDate;
    private List<UUID> oldToken;
}
