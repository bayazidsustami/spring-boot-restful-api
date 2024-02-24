package com.example.restfullapi.demo.controller;

import com.example.restfullapi.demo.entity.User;
import com.example.restfullapi.demo.model.RegisterUserRequest;
import com.example.restfullapi.demo.model.UpdateUserRequest;
import com.example.restfullapi.demo.model.UserResponse;
import com.example.restfullapi.demo.model.WebResponse;
import com.example.restfullapi.demo.repository.UserRepository;
import com.example.restfullapi.demo.security.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("baybay");
        request.setPassword("rahasia");
        request.setName("bayazid");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<String> webResponse = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            Assertions.assertEquals("OK", webResponse.getData());
        });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo( result -> {
            WebResponse<String> webResponse = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            Assertions.assertNotNull(webResponse.getErrors());
        });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("Test");

        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("rahasia");
        request.setName("Test");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo( result -> {
            WebResponse<String> webResponse = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            Assertions.assertNotNull(webResponse.getErrors());
        });
    }

    @Test
    void getUserUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo( result -> {
            WebResponse<String> webResponse = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            Assertions.assertNotNull(webResponse.getErrors());
        });
    }

    @Test
    void getUserUnauthorizedEmptyToken() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo( result -> {
            WebResponse<String> webResponse = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            Assertions.assertNotNull(webResponse.getErrors());
        });
    }

    @Test
    void getUserExpiredToken() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpireAt(System.currentTimeMillis() - 1000000000L);

        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo( result -> {
            WebResponse<String> webResponse = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            Assertions.assertNotNull(webResponse.getErrors());
        });
    }

    @Test
    void getUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpireAt(System.currentTimeMillis() + 1000000000L);

        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<UserResponse> webResponse = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            Assertions.assertNull(webResponse.getErrors());
            Assertions.assertEquals("test", webResponse.getData().getUsername());
            Assertions.assertEquals("Test", webResponse.getData().getName());
        });
    }

    @Test
    void updateUserUnauthorized() throws Exception {
        UpdateUserRequest userRequest = new UpdateUserRequest();

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo( result -> {
            WebResponse<String> webResponse = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            Assertions.assertNotNull(webResponse.getErrors());
        });
    }

    @Test
    void updateUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpireAt(System.currentTimeMillis() + 1000000000L);
        userRepository.save(user);

        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setName("bay");
        userRequest.setPassword("bay12345");

        mockMvc.perform(
                patch("/api/users/current")
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<UserResponse> webResponse = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            Assertions.assertNull(webResponse.getErrors());
            Assertions.assertEquals("bay", webResponse.getData().getName());
            Assertions.assertEquals("test", webResponse.getData().getUsername());

            User userDB = userRepository.findById("test").orElse(null);
            Assertions.assertNotNull(userDB);
            Assertions.assertTrue(BCrypt.checkpw("bay12345", userDB.getPassword()));
        });
    }
}