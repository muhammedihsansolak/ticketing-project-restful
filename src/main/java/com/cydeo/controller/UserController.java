package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getUsers(){
        return ResponseEntity.ok(new ResponseWrapper(
                true,
                "Users are successfully retrieved!",
                HttpStatus.OK.value(),
                userService.listAllUsers()
        ));
    }


    @GetMapping("/{username}")
    public ResponseEntity<ResponseWrapper> getUserByUsername(@PathVariable("username")String username){
        return ResponseEntity.ok(new ResponseWrapper(
                true,
                "User "+ username + " is successfully retrieved!",
                HttpStatus.OK.value(),
                userService.findByUserName(username)
        ));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO userDTO){
        userService.save(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .message("User "+ userDTO.getUserName() +" is successfully created!")
                .code(HttpStatus.CREATED.value())
                .build()
        );
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO userDTO){
        userService.update(userDTO);

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("User "+ userDTO.getUserName() +" is successfully updated!")
                .code(HttpStatus.OK.value())
                .build()
        );
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username")String username){
        userService.delete(username);

        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("User "+ username +" is successfully deleted!")
                .code(HttpStatus.OK.value())
                .build()
        );
    }



}
