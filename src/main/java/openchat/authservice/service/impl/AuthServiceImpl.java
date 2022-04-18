package openchat.authservice.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import openchat.authservice.dto.SignInRequestDto;
import openchat.authservice.dto.SignUpRequestDto;
import openchat.authservice.model.RefreshTokenModel;
import openchat.authservice.model.User;
import openchat.authservice.model.UserModel;
import openchat.authservice.repository.AuthRepository;
import openchat.authservice.repository.RefreshTokenRepository;
import openchat.authservice.service.AuthService;
import openchat.authservice.util.JwtUtil;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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
    public Mono<HashMap<String, Object>> signIn(SignInRequestDto request) throws Exception {
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

        UUID refreshTokenUuid = UUID.randomUUID();

        RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
        refreshTokenModel.setEmail(email);
        refreshTokenModel.setToken(refreshTokenUuid);
        refreshTokenModel.setExpiryDate(Instant.now().plusSeconds(3600));

        refreshTokenRepository.save(refreshTokenModel);

        HashMap<String, Object> response = new HashMap<>();
        response.put("accessToken", jwtUtil.generateToken(user));
        response.put("refreshToken", refreshTokenUuid);

        return Mono.just(response);
    }

    private Boolean verifyExpiration(RefreshTokenModel refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            return false;
        }

        return true;
    }

    @Override
    public Mono<HashMap<String, Object>> createRefreshToken(UUID refreshToken) throws Exception {
        if (refreshToken == null) {
            throw new Exception("Empty refresh token");
        }

        RefreshTokenModel refreshTokenModel = refreshTokenRepository.findByToken(refreshToken);
        if (refreshTokenModel == null) {
            refreshTokenRepository.deleteByOldToken(refreshToken.toString());
            throw new Exception("Refresh token not found");
        }

        if (!verifyExpiration(refreshTokenModel)) {
            refreshTokenRepository.deleteByToken(refreshToken.toString());
            throw new Exception("Refresh token is expired");
        }

        String email = refreshTokenModel.getEmail();
        if (email == null) {
            throw new Exception("User not found");
        }

        User user = new User();
        user.setMail(email);

        List<UUID> oldRefreshToken = refreshTokenModel.getOldToken();
        if (oldRefreshToken == null) {
            oldRefreshToken = new ArrayList<>();
        }

        oldRefreshToken.add(refreshToken);

        UUID newRefreshToken = UUID.randomUUID();
        refreshTokenModel.setToken(newRefreshToken);
        refreshTokenModel.setExpiryDate(Instant.now().plusSeconds(3600));
        refreshTokenModel.setOldToken(oldRefreshToken);
        refreshTokenRepository.save(refreshTokenModel);

        HashMap<String, Object> response = new HashMap<>();
        response.put("accessToken", jwtUtil.generateToken(user));
        response.put("refreshToken", newRefreshToken);

        return Mono.just(response);
    }
}
