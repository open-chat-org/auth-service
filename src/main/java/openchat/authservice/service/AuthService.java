package openchat.authservice.service;

import java.util.HashMap;
import java.util.UUID;

import openchat.authservice.dto.SignInRequestDto;
import openchat.authservice.dto.SignUpRequestDto;
import reactor.core.publisher.Mono;

public interface AuthService {

    void signUp(SignUpRequestDto request) throws Exception;

    Mono<HashMap<String, Object>> signIn(SignInRequestDto request) throws Exception;

    Mono<HashMap<String, Object>> createRefreshToken(UUID uuid) throws Exception;
}
