package openchat.authservice.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import openchat.authservice.model.UserModel;

public interface AuthRepository extends Neo4jRepository<UserModel, Long> {

    UserModel findByMail(String mail);
}
