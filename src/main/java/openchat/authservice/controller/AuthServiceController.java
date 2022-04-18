package openchat.authservice.controller;

import java.util.HashMap;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import openchat.authservice.constant.ResponseMessage;
import openchat.authservice.dto.ResponseModel;
import openchat.authservice.dto.SignInRequestDto;
import openchat.authservice.dto.SignUpRequestDto;
import openchat.authservice.service.AuthService;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthServiceController {

    @Autowired
    private AuthService authService;

    @PostMapping("/v1.0/auth/sign-in")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<ResponseEntity<ResponseModel>> signIn(@Valid @RequestBody SignInRequestDto request) {
        try {
            return authService.signIn(request)
                .map(response -> {
                    return ResponseEntity.ok(new ResponseModel(200, ResponseMessage.SUCCESSFUL, response));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
        } catch (Exception error) {
            log.error("Can not sign in", error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not auth user", error);
        }
    }

    @PostMapping("/v1.0/auth/sign-up")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<ResponseEntity<ResponseModel>> signUp(@Valid @RequestBody SignUpRequestDto request) {
        try {
            authService.signUp(request);

            ResponseModel responseModel = new ResponseModel(200, ResponseMessage.SUCCESSFUL, null);
            return Mono.just(ResponseEntity.ok(responseModel));
        } catch (Exception error) {
            log.error("Can not sign in", error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not auth user", error);
        }
    }

    @PostMapping("/v1.0/auth/refresh-token")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<ResponseEntity<ResponseModel>> refreshToken(@RequestBody HashMap<String, Object> request) {
        try {
            return authService.createRefreshToken(UUID.fromString((String) request.get("refreshToken")))
                .map(response -> {
                    return ResponseEntity.ok(new ResponseModel(200, ResponseMessage.SUCCESSFUL, response));
                })
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
        } catch (Exception error) {
            log.error("Can not sign in", error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not auth user", error);
        }
    }
}
