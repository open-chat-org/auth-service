package openchat.authservice.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.Data;

@Node
@Data
public class UserModel {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String email;
    private String password;
}
