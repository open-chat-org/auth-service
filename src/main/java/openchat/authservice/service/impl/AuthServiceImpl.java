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
        String mail = request.getMail();
        UserModel existUser = authRepository.findByMail(mail);
        if (existUser != null) {
            throw new Exception("User exist");
        }

        UserModel userModel = new UserModel();
        userModel.setUsername(request.getUsername());
        userModel.setMail(request.getMail());

        String password = request.getPassword();
        String hashPassword = passwordEncoder.encode(password);
        userModel.setPassword(hashPassword);

        authRepository.save(userModel);
    }

    @Override
    public Mono<User> signIn(SignInRequestDto request) throws Exception {
        String mail = request.getMail();
        UserModel userModel = authRepository.findByMail(mail);
        if (userModel == null) {
            throw new Exception("User not exist");
        }

        if (!passwordEncoder.matches(request.getPassword(), userModel.getPassword())) {
            throw new Exception("Wrong username or password");
        }

        User user = new User();
        user.setMail(mail);
        user.setPassword(userModel.getPassword());

        return Mono.just(user);
    }
}
