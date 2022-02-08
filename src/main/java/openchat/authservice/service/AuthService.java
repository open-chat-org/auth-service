package openchat.authservice.service;

import openchat.authservice.dto.SignInRequestDto;
import openchat.authservice.dto.SignUpRequestDto;
import openchat.authservice.model.User;
import reactor.core.publisher.Mono;

public interface AuthService {

    void signUp(SignUpRequestDto request) throws Exception;

    Mono<User> signIn(SignInRequestDto request) throws Exception;
}
