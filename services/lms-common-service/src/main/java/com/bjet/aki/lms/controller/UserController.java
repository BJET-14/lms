package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.domain.User;
import com.bjet.aki.lms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commons/users")
@Tag(name = "User Controller",
        description = "User related basic api requests. Important properties will be handed by specific role wise controller")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user){
        userService.saveUser(user);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(path = "/email")
    @Operation(summary = "This API is for auth purpose", description = "This API will be requested from LMS-GATEWAY service to get the user details by username. This api require password to check user validation")
    public ResponseEntity<?> fetchUserByEmail(@RequestParam String email){
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/exist")
    @Operation(summary = "Check user exist or not by email")
    public ResponseEntity<Boolean> existByEmail(@RequestParam String email){
        return ResponseEntity.ok(userService.exist(email));
    }
}
