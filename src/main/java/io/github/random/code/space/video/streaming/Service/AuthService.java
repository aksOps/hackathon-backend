package io.github.random.code.space.video.streaming.Service;

import io.github.random.code.space.video.streaming.config.AuthType;
import io.github.random.code.space.video.streaming.model.User;
import io.github.random.code.space.video.streaming.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public AuthType signup(String username, String password) {
        try {
            if (userRepository.findById(username).isPresent()) {
                return AuthType.ALREADY_EXISTS;
            } else {
                userRepository.save(new User(username, encodeString(password)));
                return AuthType.SIGNUP_SUCCESS;
            }
        } catch (Exception e) {
            return AuthType.SIGNUP_FAILED;
        }

    }

    public AuthType authenticate(String username, String password) {
        var optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getPassword().equals(encodeString(password))) {
                return AuthType.LOGIN_SUCCESS;
            }
            return AuthType.LOGIN_FAILED;
        } else {
            return AuthType.USER_NOT_FOUND;
        }
    }

    private String encodeString(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}