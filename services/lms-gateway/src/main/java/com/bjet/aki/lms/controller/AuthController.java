package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.domain.LoginRequest;
import com.bjet.aki.lms.domain.User;
import com.bjet.aki.lms.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@Tag(name = "Authentication Controller",
        description = "Central authentication gateway. Login, Basic registration are handled by this controller")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/registration")
    public ResponseEntity<?> register(@RequestBody User user){
        authService.registerUser(user);
        return ResponseEntity.accepted().build();
    }
}
