package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;
import javax.annotation.PostConstruct;


import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
public class UserController {


    @Autowired
    private UserRepository userRepository;
    // @PostConstruct
    // public void initialize() {
    //     if (userRepository.count() == 0) {
    //         User dummyUser = new User();
    //         dummyUser.setId(0); // Set the ID to 0 for the dummy user
    //         dummyUser.setName("Dummy User");
    //         dummyUser.setEmail("dummy@example.com");
    //         userRepository.save(dummyUser);
    //     }
    // }


    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // Creating a user entity from the payload
        User user = new User();
        user.setName(payload.getName());
        user.setEmail(payload.getEmail());
        // Storing the user in the database
        userRepository.save(user);
        // Returning the ID of the newly created user
        return ResponseEntity.ok(user.getId());
    }

    

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // Checking if the user exists in the database
        return userRepository.findById(userId)
            .map(user -> {
                userRepository.delete(user);
                return new ResponseEntity<String>("User deleted successfully", HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<String>("User not found", HttpStatus.BAD_REQUEST));

        
    }



    
}
