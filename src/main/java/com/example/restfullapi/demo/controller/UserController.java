package com.example.restfullapi.demo.controller;

import com.example.restfullapi.demo.entity.User;
import com.example.restfullapi.demo.model.RegisterUserRequest;
import com.example.restfullapi.demo.model.UpdateUserRequest;
import com.example.restfullapi.demo.model.UserResponse;
import com.example.restfullapi.demo.model.WebResponse;
import com.example.restfullapi.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> response(@RequestBody RegisterUserRequest request) {
        userService.register(request);

        return WebResponse.<String>builder()
                .data("OK")
                .build();
    }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(User user) {
        UserResponse userResponse = userService.get(user);
        return WebResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }

    @PatchMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(User user,@RequestBody UpdateUserRequest request) {
        UserResponse response = userService.update(user, request);
        return WebResponse.<UserResponse>builder()
                .data(response)
                .build();
    }

}
