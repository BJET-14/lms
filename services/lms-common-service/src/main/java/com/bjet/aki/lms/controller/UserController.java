package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.model.Role;
import com.bjet.aki.lms.model.User;
import com.bjet.aki.lms.model.UserSaveRequest;
import com.bjet.aki.lms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commons/users")
@Tag(name = "User Controller",
        description = "User related basic api requests. Important properties will be handed by specific role wise controller")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(name = "asPage", required = false, defaultValue = "false") Boolean asPage,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size,
                                      @RequestParam(name = "firstName", required = false) String firstName,
                                      @RequestParam(name = "lastName", required = false) String lastName,
                                      @RequestParam(name = "email", required = false) String email,
                                      @RequestParam(name = "role", required = false) Role role){
        if(asPage){
            Pageable pageable = PageRequest.of(page, size);
            PagedResult<User> productsAsPage = userService.findAllUsers(pageable, firstName, lastName, email, role);
            return ResponseEntity.ok(productsAsPage);
        }
        List<User> productsAsList = userService.findAllUsers(firstName, lastName, email, role);
        return ResponseEntity.ok(productsAsList);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getUsers(@PathVariable("id") Long id){
        User user = userService.findbyId(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserSaveRequest request){
        userService.saveUser(request);
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
