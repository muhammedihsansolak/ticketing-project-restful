package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RolesAllowed({"Admin","Manager"})
    public ResponseEntity<ResponseWrapper> getUsers(){
        return ResponseEntity.ok(new ResponseWrapper(
                true,
                "Users are successfully retrieved!",
                HttpStatus.OK.value(),
                userService.listAllUsers()
        ));
    }


    @GetMapping("/{username}")
    @RolesAllowed({"Admin","Manager"})
    public ResponseEntity<ResponseWrapper> getUserByUsername(@PathVariable("username")String username){
        return ResponseEntity.ok(new ResponseWrapper(
                true,
                "User "+ username + " is successfully retrieved!",
                HttpStatus.OK.value(),
                userService.findByUserName(username)
        ));
    }

    @PostMapping
    @RolesAllowed({"Admin"})
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
    @RolesAllowed({"Admin"})
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
    @RolesAllowed({"Admin"})
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
