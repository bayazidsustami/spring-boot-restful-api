package com.example.restfullapi.demo.controller;

import com.example.restfullapi.demo.model.LoginUserRequest;
import com.example.restfullapi.demo.model.TokenResponse;
import com.example.restfullapi.demo.model.WebResponse;
import com.example.restfullapi.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse response = authService.login(request);
        return WebResponse.<TokenResponse>builder()
                .data(response)
                .build();
    }

}
