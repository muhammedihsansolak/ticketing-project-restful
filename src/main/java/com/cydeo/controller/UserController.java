package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name="User Controller", description = "User API")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RolesAllowed({"Admin","Manager"})
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved users(OK)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content
            )
    })
    @ExecutionTime
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
    @Operation(summary = "Get user by username")
    @ExecutionTime
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
    @Operation(summary = "Creates user")
    public ResponseEntity<ResponseWrapper> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, Map<String, String>> response = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                if (response.containsKey(fieldError.getField())) {
                    response.get(fieldError.getField()).put(fieldError.getCode(), fieldError.getDefaultMessage());
                } else {
                    response.put(fieldError.getField(), new HashMap<>());
                    response.get(fieldError.getField()).put(fieldError.getCode(), fieldError.getDefaultMessage());
                }
            }
            return ResponseEntity.badRequest().body(
                    ResponseWrapper.builder()
                            .success(false)
                            .message("Please check the input information")
                            .code(HttpStatus.BAD_REQUEST.value())
                            .data(response).build());
        }
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
    @Operation(summary = "Updates user")
    public ResponseEntity<ResponseWrapper> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, Map<String, String>> response = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                if (response.containsKey(fieldError.getField())) {
                    response.get(fieldError.getField()).put(fieldError.getCode(), fieldError.getDefaultMessage());
                } else {
                    response.put(fieldError.getField(), new HashMap<>());
                    response.get(fieldError.getField()).put(fieldError.getCode(), fieldError.getDefaultMessage());
                }
            }
            return ResponseEntity.badRequest().body(
                    ResponseWrapper.builder()
                            .success(false)
                            .message("Please check the input information")
                            .code(HttpStatus.BAD_REQUEST.value())
                            .data(response).build());
        }
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
    @Operation(summary = "Deletes user")
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
