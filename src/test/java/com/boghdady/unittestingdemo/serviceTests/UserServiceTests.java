package com.boghdady.unittestingdemo.serviceTests;

import com.boghdady.unittestingdemo.dtos.UserDto;
import com.boghdady.unittestingdemo.models.User;
import com.boghdady.unittestingdemo.repositories.UserRepository;
import com.boghdady.unittestingdemo.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   //  tells JUnit to use the MockitoExtension to initialize mocks.
public class UserServiceTests {

    // Creates mock instances of the dependencies, here it's UserRepository.
    @Mock
    private UserRepository userRepository;

    // @InjectMocks injects the mocks into the userService instance.
    @InjectMocks
    private UserServiceImpl userService;

    private static final String TEST_NAME = "test";
    private static final String TEST_EMAIL = "test@example.com";
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        // Create a UserDto Object with test data before running the tests
        userDto = UserDto.builder().name(TEST_NAME).email(TEST_EMAIL).build();
    }

    // Test case 1: Test Successful User Registration
    @Test
    public void registerUserSuccess() {
        // Mock the userRepository.findByEmail method to return null,
        // simulating that no user exists with the provided email
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(null);

        // Call the registerUser method on the userService with the userDto
        ResponseEntity<?> response = userService.registerUser(userDto);

        // Verify that the findByEmail method was called exactly once with the given email
        verify(userRepository, times(1)).findByEmail(userDto.getEmail());

        // Verify that the save method was called exactly once with any User object
        verify(userRepository, times(1)).save(any(User.class));

        // Assert that the response status code is HttpStatus.CREATED
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Success: User details successfully saved!", response.getBody());
    }


    // Test case 2: Test User Registration with Existing Email
    @Test
    public void registerUserWithAlreadyExistsEmailFail() {
        // Mock the userRepository.findByEmail() to return a new user object,
        // Simulating that a user already exists with the provided email
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(new User());

        // Call the registerUser method on the userService with the userDto
        ResponseEntity<?> response = userService.registerUser(userDto);

        // Verify that the findByEmail method was called exactly once with the given email
        verify(userRepository, times(1)).findByEmail(userDto.getEmail());

        // Verify that the save method was not called, as the email already exists
        verify(userRepository, times(0)).save(any(User.class));

        // Assert that the response status code is HttpStatus.CONFLICT
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Fail: Email already exists!", response.getBody());
    }



    // Test case 3: Test Exception Handling
    @Test
    public void registerUserWithInternalServerErrorFail() {
        // Mock the userRepository.findByEmail method to throw a RuntimeException,
        // simulating an unexpected error during the database operation
        when(userRepository.findByEmail(userDto.getEmail())).thenThrow(new RuntimeException());

        // Call the registerUser method on the userService with the userDto
        ResponseEntity<?> response = userService.registerUser(userDto);

        // Verify that the findByEmail method was called exactly once with the given email
        verify(userRepository, times(1)).findByEmail(userDto.getEmail());

        // Verify that the save method was not called due to the exception
        verify(userRepository, times(0)).save(any(User.class));

        // Assert that the response status code is HttpStatus.INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Fail: Failed to process request now. Try again later", response.getBody());
    }





}
