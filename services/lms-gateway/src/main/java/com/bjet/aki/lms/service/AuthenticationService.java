package com.bjet.aki.lms.service;

import com.bjet.aki.lms.config.UserDetailsConfig;
import com.bjet.aki.lms.domain.AuthenticationRequest;
import com.bjet.aki.lms.domain.AuthenticationResponse;
import com.bjet.aki.lms.domain.User;
import com.bjet.aki.lms.security.JwtService;
import com.bjet.aki.lms.token.Token;
import com.bjet.aki.lms.token.TokenRepository;
import com.bjet.aki.lms.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public void registerUser(User user){
        logger.info("Save request from LMS-GATEWAY: registerUser , email={}", user.getEmail());
        // checking user already existed or not
        Boolean existResponse = restTemplate.getForObject("http://lms-common-service/commons/users/exist?email=" + user.getEmail(), Boolean.class);
        if(existResponse != null ){
            if(existResponse){
                throw new RuntimeException("User already exist");
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        restTemplate.postForObject("http://lms-common-service/commons/users", user, Void.class);
        logger.info("User saved successfully. email={}", user.getEmail());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        logger.info("Initiating authentication: email={}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User response = restTemplate.getForObject("http://lms-common-service/commons/users/email?email=" + request.getEmail(), User.class);
        if(response != null){
            UserDetails userDetails = UserDetailsConfig.getUserDetails(response);
            var jwtToken = jwtService.generateToken(userDetails);
            var refreshToken = jwtService.generateRefreshToken(userDetails);
            revokeAllUserTokens(response);
            saveUserToken(response, jwtToken);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } else{
            logger.error("Failed authentication from lms-gateway. Exception throws from lms-common-service. email={}", request.getEmail());
            throw new RuntimeException("Authentication failed. Please contact administrator");
        }
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .userId(user.getId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            ResponseEntity<User> userResponse = restTemplate.getForObject("http://lms-common-service/commons/users/email?email" + userEmail, ResponseEntity.class);
            if(userResponse != null && userResponse.getStatusCode().is2xxSuccessful()){
                UserDetails userDetails = UserDetailsConfig.getUserDetails(userResponse.getBody());
                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    var accessToken = jwtService.generateToken(userDetails);
                    revokeAllUserTokens(userResponse.getBody());
                    saveUserToken(userResponse.getBody(), accessToken);
                    var authResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                }
            }
        }
    }
}
