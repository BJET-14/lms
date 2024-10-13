package com.bjet.aki.lms.service;

import com.bjet.aki.lms.exception.CommonException;
import com.bjet.aki.lms.model.AuthenticationRequest;
import com.bjet.aki.lms.model.AuthenticationResponse;
import com.bjet.aki.lms.model.User;
import com.bjet.aki.lms.model.UserSaveRequest;
import com.bjet.aki.lms.token.Token;
import com.bjet.aki.lms.token.TokenRepository;
import com.bjet.aki.lms.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    public void registerUser(User user) {
        logger.info("Save request from lms-auth-service: registerUser , email={}", user.getEmail());
        // checking user already existed or not
        Boolean existResponse = restTemplate.getForObject("http://lms-common-service/commons/users/exist?email=" + user.getEmail(), Boolean.class);
        if(existResponse != null ){
            if(existResponse){
                throw new CommonException("15", "User already exist");
            }
        }
        UserSaveRequest request = UserSaveRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole())
                .encryptedPassword(passwordEncoder.encode(user.getPassword()))
                .password(user.getPassword())
                .build();
        restTemplate.postForObject("http://lms-common-service/commons/users", request, Void.class);
        logger.info("User saved successfully. email={}", request.getEmail());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = restTemplate.getForObject("http://lms-common-service/commons/users/email?email=" + request.getEmail(), User.class);
        if(user != null){
            var jwtToken = jwtService.generateToken(user.getEmail());
            var refreshToken = jwtService.generateRefreshToken(user.getEmail());
            revokeAllUserTokens(user.getId());
            saveUserToken(user.getId(), jwtToken);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .role(user.getRole())
                    .build();
        } else{
            logger.error("Failed authentication from lms-gateway. Exception throws from lms-common-service. email={}", request.getEmail());
            throw new CommonException("14", "Authentication failed. Please contact administrator");
        }
    }

    private void revokeAllUserTokens(Long userId) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(userId);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(Long userId, String jwtToken) {
        var token = Token.builder()
                .userId(userId)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new CommonException("11", "missing authorization header");
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User userResponse = restTemplate.getForObject("http://lms-common-service/commons/users/email?email" + userEmail, User.class);
            if(userResponse != null){
                if (jwtService.isTokenValid(refreshToken, userResponse.getEmail())) {
                    var accessToken = jwtService.generateToken(userResponse.getEmail());
                    revokeAllUserTokens(userResponse.getId());
                    saveUserToken(userResponse.getId(), accessToken);
                    var authResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    return authResponse;
                }
            }
        } else {
            throw new CommonException("12", "un authorized access to application");
        }
        return null;
    }

    public Boolean validateToken(String token) {
        String email = jwtService.extractUsername(token);
        return jwtService.isTokenValid(token, email);
    }
}
