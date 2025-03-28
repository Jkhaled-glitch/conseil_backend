package com.api.service;

import com.api.entities.mysql.User;
import com.api.request.AuthenticationRequest;
import com.api.request.RegisterRequest;
import com.api.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request) ;


     AuthenticationResponse authenticate(AuthenticationRequest request) ;

    void saveUserToken(User user, String jwtToken) ;

    void revokeAllUserTokens(User user) ;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException ;
}
