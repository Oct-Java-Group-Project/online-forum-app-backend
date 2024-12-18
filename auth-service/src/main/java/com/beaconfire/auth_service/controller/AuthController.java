package com.beaconfire.auth_service.controller;

import com.beaconfire.auth_service.dto.RegisterRequest;
import com.beaconfire.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.addNewUser(registerRequest);
    }

    @PatchMapping("/validate")
    public ResponseEntity<String> activateUserByToken(@RequestParam("token") String token) {
        return authService.activateUserByToken(token);
    }
}