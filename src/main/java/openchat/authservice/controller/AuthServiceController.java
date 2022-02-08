package openchat.authservice.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
import openchat.authservice.util.JwtUtil;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthServiceController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/v0.1/auth/sign-in")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<ResponseEntity<ResponseModel>> signIn(@RequestBody SignInRequestDto request) {
        try {
            return authService.signIn(request)
                .map(user -> {
                    String token = jwtUtil.generateToken(user);

                    HashMap<String, Object> response = new HashMap<>();
                    response.put("token", token);

                    return ResponseEntity.ok(new ResponseModel(200, ResponseMessage.SUCCESSFUL, response));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
        } catch (Exception error) {
            log.error("Can not sign in", error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not auth user", error);
        }
    }

    @PostMapping("/v0.1/auth/sign-up")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<ResponseEntity<ResponseModel>> signUp(@RequestBody SignUpRequestDto request) {
        try {
            authService.signUp(request);

            ResponseModel responseModel = new ResponseModel(200, ResponseMessage.SUCCESSFUL, null);
            return Mono.just(ResponseEntity.ok(responseModel));
        } catch (Exception error) {
            log.error("Can not sign in", error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not auth user", error);
        }
    }

    @GetMapping("/v0.1/auth/ping")
    public Mono<ResponseEntity<ResponseModel>> ping() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "pong");

        return Mono.just(ResponseEntity.ok(new ResponseModel(200, ResponseMessage.SUCCESSFUL, response)));
    }
}
