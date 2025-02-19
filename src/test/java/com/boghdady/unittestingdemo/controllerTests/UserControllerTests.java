package com.boghdady.unittestingdemo.controllerTests;

import com.boghdady.unittestingdemo.dtos.UserDto;
import com.boghdady.unittestingdemo.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserControllerTests.class)
public class UserControllerTests {


    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    // Test case 1: Test Successful User Registration
    @Test
    public void registerUserSuccess() throws Exception {
        // Prepare a valid UserDto request body
        String userDtoJson = "{\"name\": \"Test\", \"email\": \"test@gmail.com\"}";

        // Mock userService.registerUser to return a successful response
        when(userService.registerUser(any())).thenReturn(ResponseEntity.status(HttpStatus.CREATED)
                                                                        .body("Success: User details successfully saved!"));

        // Perform POST request to /user/new with valid UserDto
        mockMvc.perform(MockMvcRequestBuilders.post("/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                // Verify that the response status code is 201 create.
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Success: User details successfully saved!"));
    }




    // Test case 2: Test User Registration with Existing Email
    @Test
    public void registerUserWithAlreadyExistsEmailFail() throws Exception {
        // Prepare a valid UserDto request body
        String userDtoJson = "{\"name\": \"Test\", \"email\": \"test@gmail.com\"}";

        // Mock userService.registerUser to return a conflict response
        when(userService.registerUser(any())).thenReturn(ResponseEntity.status(HttpStatus.CONFLICT).body("Fail: Email already exists!"));

        // Perform POST request to /user/new with valid UserDto
        mockMvc.perform(MockMvcRequestBuilders.post("/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                // Verify that the response status code is conflict
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("Fail: Email already exists!"));
    }



    // Test case 3: Test User Registration with Invalid Input
    @Test
    public void registerUserWithInvalidInputFail() throws Exception {
        // Prepare an invalid UserDto request body with an no name and invalid email
        String userDtoJson = "{\"email\": \"testgmail.com\"}";

        // Perform a POST request to /user/new with the invalid UserDto
        mockMvc.perform(MockMvcRequestBuilders.post("/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                // Verify that the response status code is 400 Bad Request
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                // Verify that the response body is has correctly defined errors
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name is required!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Email is not in valid format!"));

        // Verify that the UserService's registerUser method is not called
        verify(userService, times(0)).registerUser(any(UserDto.class));
    }


    // Test case 4: Test Exception Handling
    @Test
    public void registerUserWithInternalServerErrorFail() throws Exception {
        // Prepare a valid UserDto request body
        String userDtoJson = "{\"name\": \"Test\", \"email\": \"test@gmail.com\"}";

        // Mock userService.registerUser to return a exception response
        when(userService.registerUser(any())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Fail: Failed to process request now. Try again later"));

        // Perform POST request to /user/new with valid UserDto
        mockMvc.perform(MockMvcRequestBuilders.post("/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                // Verify that the response status code is 500 Internal server error
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Fail: Failed to process request now. Try again later"));
    }



}