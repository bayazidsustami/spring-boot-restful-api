package com.example.restfullapi.demo.controller;


import com.example.restfullapi.demo.entity.Contact;
import com.example.restfullapi.demo.entity.User;
import com.example.restfullapi.demo.model.AddressResponse;
import com.example.restfullapi.demo.model.CreateAddressRequest;
import com.example.restfullapi.demo.model.WebResponse;
import com.example.restfullapi.demo.repository.AddressRepository;
import com.example.restfullapi.demo.repository.ContactRepository;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpireAt(System.currentTimeMillis() + 1000000000L);

        userRepository.save(user);

        Contact contact = new Contact();
        contact.setId("test");
        contact.setUser(user);
        contact.setFirstName("bay");
        contact.setLastName("bayazid");
        contact.setEmail("bayazid@mail.com");
        contact.setPhone("0987695032");
        contactRepository.save(contact);
    }

    @Test
    void createAddressBadRequest() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
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
    void createAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("jalan");
        request.setCity("Makassar");
        request.setProvince("Sulawesi Selatan");
        request.setCountry("Indonesia");
        request.setPostalCode("90233");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNull(response.getErrors());
            Assertions.assertNotNull(response.getData());
            Assertions.assertEquals(request.getStreet(), response.getData().getStreet());
            Assertions.assertEquals(request.getCity(), response.getData().getCity());
            Assertions.assertEquals(request.getProvince(), response.getData().getProvince());
            Assertions.assertEquals(request.getCountry(), response.getData().getCountry());
            Assertions.assertEquals(request.getPostalCode(), response.getData().getPostalCode());

            Assertions.assertTrue(addressRepository.existsById(response.getData().getId()));
        });
    }
}