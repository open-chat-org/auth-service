package openchat.authservice.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.Data;

@Node
@Data
public class UserModel {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String mail;
    private String password;

    @Relationship(type = "FRIEND")
    public Set<UserModel> friends;

    public void friendWith(UserModel friend) {
        if (friends == null) {
            friends = new HashSet<>();
        }

        friends.add(friend);
    }
}
