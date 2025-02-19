package com.boghdady.unittestingdemo.services;

import com.boghdady.unittestingdemo.dtos.UserDto;
import com.boghdady.unittestingdemo.models.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Object> registerUser(UserDto userDto);
}
