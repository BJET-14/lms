package com.bjet.aki.lms.config;

import com.bjet.aki.lms.domain.User;
import lombok.AllArgsConstructor;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Configuration
@AllArgsConstructor
public class UserDetailsConfig {

    private final Logger logger = LoggerFactory.getLogger(UserDetailsConfig.class);
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    public Optional<UserDetails> findByEmail(String username) {
        logger.info("Fetch request from lms-gateway: findByEmail , email={}", username);
        try{
            User response = restTemplate.getForObject("http://lms-common-service/commons/users/email?email=" + username, User.class);
            if(response != null){
                UserDetails userDetails = getUserDetails(response);
                return Optional.of(userDetails);
            }
        } catch (Exception e){
            logger.error("Exception occurred in lms-common-service, email={}", username);
        }

        return Optional.empty();
    }
    public static UserDetails getUserDetails(User user) {
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
