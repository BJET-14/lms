package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.model.AuthenticationRequest;
import com.bjet.aki.lms.model.AuthenticationResponse;
import com.bjet.aki.lms.model.User;
import com.bjet.aki.lms.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(path = "/registration")
    public ResponseEntity<?> register(@RequestBody User user){
        authenticationService.registerUser(user);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            AuthenticationResponse response = authenticationService.authenticate(authRequest);
            return ResponseEntity.ok(response);
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping(path = "/validate/token/{token}")
    public ResponseEntity<Boolean> validateToken(@PathVariable String token){
        Boolean validityResponse = authenticationService.validateToken(token);
        return ResponseEntity.ok(validityResponse);
    }
}
