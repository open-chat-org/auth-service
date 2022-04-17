package openchat.authservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import openchat.authservice.dto.SignInRequestDto;
import openchat.authservice.dto.SignUpRequestDto;
import openchat.authservice.model.User;
import openchat.authservice.model.UserModel;
import openchat.authservice.repository.AuthRepository;
import openchat.authservice.service.AuthService;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpRequestDto request) throws Exception {
        String username = request.getUsername();
        UserModel existUserName = authRepository.findByUsername(username);
        if (existUserName != null) {
            throw new Exception("Username exist");
        }

        String email = request.getEmail();
        UserModel existUser = authRepository.findByEmail(email);
        if (existUser != null) {
            throw new Exception("User exist");
        }

        UserModel userModel = new UserModel();
        userModel.setUsername(username);
        userModel.setEmail(email);

        String password = request.getPassword();
        String hashPassword = passwordEncoder.encode(password);
        userModel.setPassword(hashPassword);

        authRepository.save(userModel);
    }

    @Override
    public Mono<User> signIn(SignInRequestDto request) throws Exception {
        String email = request.getEmail();
        UserModel userModel = authRepository.findByEmail(email);
        if (userModel == null) {
            throw new Exception("Wrong username or password");
        }

        if (!passwordEncoder.matches(request.getPassword(), userModel.getPassword())) {
            throw new Exception("Wrong username or password");
        }

        User user = new User();
        user.setMail(email);
        user.setPassword(userModel.getPassword());

        return Mono.just(user);
    }
}
