package com.boghdady.unittestingdemo.repositoryTests;


import com.boghdady.unittestingdemo.models.User;
import com.boghdady.unittestingdemo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    //Test case 1: Test save method.
    @Test
    @Transactional
    @Rollback
    public void testSaveUser() {
        // Define test data
        String name = "test1";
        String email = "test1@example.com";

        // Create a User Object with the test data
        User user = User.builder().name(name).email(email).build();

        // Save the User to the DB
        User savedUser = userRepository.save(user);

        // Assert that the retrieved user is not null
        assertNotNull(savedUser);

        // Assert that the retrieved user id is not null
        assertNotNull(savedUser.getId());

        // Assert the retrieved user's name matches the expected name
        assertEquals(name, savedUser.getName());

        // Assert the retrieved user's email matches the expected email
        assertEquals(email, savedUser.getEmail());

    }


    // Test case 2: Test findByEmail method for existing user.
    @Test
    @Transactional
    @Rollback
    public void testFindByEmailUserFound() {
        // Define the test data
        String name = "test2";
        String email = "test2@example.com";

        // Create a user object with the test data
        User user = User.builder().name(name).email(email).build();


        // Save the user to the DB
        userRepository.save(user);

        // Retrieve the user from the DB using findByEmail()
        User foundUser = userRepository.findByEmail(email);

        // Assert that the retrieved user is not null
        assertNotNull(foundUser);

        // Assert that the retrieved user's email matches the expected email
        assertEquals(email, foundUser.getEmail());

        // Assert that the retrieved user's name matches the expected name
        assertEquals(name, foundUser.getName());
    }


    // Test case 3: Test findByEmail method for non-existing user.
    @Test
    @Transactional
    @Rollback
    public void testFindByEmailUserNotFound() {
        // Find a non-existing user
        User foundUser = userRepository.findByEmail("non-existing@example.com");

        // Assert that the retrieved user is null
        assertNull(foundUser);
    }






}
