package com.example.restfullapi.demo.controller;

import com.example.restfullapi.demo.entity.User;
import com.example.restfullapi.demo.model.ContactResponse;
import com.example.restfullapi.demo.model.CreateContactRequest;
import com.example.restfullapi.demo.model.UpdateContactRequest;
import com.example.restfullapi.demo.model.WebResponse;
import com.example.restfullapi.demo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> get(User user, @PathVariable("contactId") String id) {
        ContactResponse contactResponse = contactService.get(user, id);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @PutMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> update(
            User user,
            @RequestBody UpdateContactRequest request,
            @PathVariable("contactId") String contactId
    ) {
        request.setId(contactId);

        ContactResponse contactResponse = contactService.update(user, request);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

}
