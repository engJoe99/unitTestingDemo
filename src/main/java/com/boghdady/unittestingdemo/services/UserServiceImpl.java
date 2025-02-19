package com.boghdady.unittestingdemo.services;


import com.boghdady.unittestingdemo.dtos.UserDto;
import com.boghdady.unittestingdemo.models.User;
import com.boghdady.unittestingdemo.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j // Lombok annotation to automatically generate a logger instance named "log"
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public ResponseEntity<Object> registerUser(UserDto userDto) {
        try {
            if (userRepository.findByEmail(userDto.getEmail()) == null) {
                User user = User.builder().name(userDto.getName())
                                            .email(userDto.getEmail())
                                            .build();
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.CREATED).body("Success: User details successfully saved!");
            }

        } catch (Exception e) {
            log.error("User registration failed" + e.getMessage());
            // Return an internal server error response
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fail: Failed to process request now. Try again later");
        }


        return ResponseEntity.status(HttpStatus.CONFLICT).body("Fail: Email already exists!");
    }
}
