package com.example.restfullapi.demo.service;

import com.example.restfullapi.demo.model.RegisterUserRequest;
import com.example.restfullapi.demo.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    public void register(RegisterUserRequest request) {
        Set<ConstraintViolation<RegisterUserRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        if (userRepository.existsById(request.getUsername())) {

        }

    }

}
