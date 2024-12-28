package io.github.random.code.space.video.streaming.controllers;

import io.github.random.code.space.video.streaming.Service.AuthService;
import io.github.random.code.space.video.streaming.config.AuthType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        var authType = authService.authenticate(username, password);
        if (authType.equals(AuthType.LOGIN_SUCCESS)) {
            return ResponseEntity.ok(username);
        } else if (authType.equals(AuthType.USER_NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthType> signup(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        var authType = authService.signup(username, password);
        if (authType == AuthType.SIGNUP_SUCCESS) {
            return ResponseEntity.status(HttpStatus.CREATED).body(AuthType.SIGNUP_SUCCESS);
        }
        if (authType == AuthType.ALREADY_EXISTS) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(AuthType.ALREADY_EXISTS);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(AuthType.SIGNUP_FAILED);
        }
    }
}