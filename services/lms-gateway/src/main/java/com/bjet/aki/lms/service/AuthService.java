package com.bjet.aki.lms.service;

import com.bjet.aki.lms.domain.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

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
    public Optional<UserDetails> findByEmail(String username) {
        logger.info("Fetch request from LMS-GATEWAY: findByEmail , email={}", username);
        ResponseEntity<User> response = restTemplate.getForObject("http://lms-common-service/commons/users/email?email" + username, ResponseEntity.class);
        if(response != null && response.getStatusCode().is2xxSuccessful()){
            UserDetails userDetails = getUserDetails(response.getBody());
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    private UserDetails getUserDetails(User user) {
        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail();
            }
        };
        return userDetails;
    }

}
