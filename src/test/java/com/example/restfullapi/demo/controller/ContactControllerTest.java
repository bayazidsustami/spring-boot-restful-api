package com.example.restfullapi.demo.controller;

import com.example.restfullapi.demo.entity.User;
import com.example.restfullapi.demo.model.ContactResponse;
import com.example.restfullapi.demo.model.CreateContactRequest;
import com.example.restfullapi.demo.model.WebResponse;
import com.example.restfullapi.demo.repository.ContactRepository;
import com.example.restfullapi.demo.repository.UserRepository;
import com.example.restfullapi.demo.security.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpireAt(System.currentTimeMillis() + 1000000000L);

        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("email");

        mockMvc.perform(
                post("/api/contacts")
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo( result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNotNull(response.getErrors());
        });
    }

    @Test
    void createContactBadSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("bay");
        request.setLastName("bayazid");
        request.setEmail("bayazid@mail.com");
        request.setPhone("0987695032");

        mockMvc.perform(
                post("/api/contacts")
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNull(response.getErrors());
            Assertions.assertNotNull(response.getData());
            Assertions.assertEquals("bay", response.getData().getFirstName());
            Assertions.assertEquals("bayazid", response.getData().getLastName());
            Assertions.assertEquals("bayazid@mail.com", response.getData().getEmail());
            Assertions.assertEquals("0987695032", response.getData().getPhone());

            Assertions.assertTrue(contactRepository.existsById(response.getData().getId()));
        });
    }
}