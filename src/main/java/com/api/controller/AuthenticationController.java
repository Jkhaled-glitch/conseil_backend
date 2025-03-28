package com.api.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.request.AuthenticationRequest;
import com.api.request.RegisterRequest;
import com.api.response.AuthenticationResponse;
import com.api.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")

@Tag(name = "Authenticate", description = "Authentification user")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    AuthenticationService service;

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate", description = "Login user")
    public AuthenticationResponse authenticate( @RequestBody AuthenticationRequest request ) {
        return service.authenticate(request);
    }

    // to delete
    @PostMapping("/register")
    public AuthenticationResponse register(
            @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

}
