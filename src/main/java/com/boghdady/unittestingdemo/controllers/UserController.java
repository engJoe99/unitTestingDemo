package com.boghdady.unittestingdemo.controllers;


import com.boghdady.unittestingdemo.dtos.UserDto;
import com.boghdady.unittestingdemo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    // Autowired annotation to inject the UserService dependency
    @Autowired
    private UserService userService;

    // Endpoint to handle POST requests for registering a new user
    @PostMapping("/new")
    // Annotation to validate the request body before calling register user method
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        // Delegate the registration logic to the UserService
        return userService.registerUser(userDto);
    }



}
