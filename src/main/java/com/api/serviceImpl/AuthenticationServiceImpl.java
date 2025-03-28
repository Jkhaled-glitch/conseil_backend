package com.api.serviceImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.config.JwtService;
import com.api.dao.mysql.RoleDao;
import com.api.dao.mysql.TokenDao;
import com.api.dao.mysql.UserDao;
import com.api.entities.mysql.Role;
import com.api.entities.mysql.Token;
import com.api.entities.mysql.User;
import com.api.enums.HistoriqueAction;
import com.api.enums.TokenType;
import com.api.exception.DuplicateResourceException;
import com.api.exception.ResourceNotFoundException;
import com.api.exception.UnauthorizedException;
import com.api.request.AuthenticationRequest;
import com.api.request.RegisterRequest;
import com.api.response.AuthenticationResponse;
import com.api.service.AuthenticationService;
import com.api.service.HistoriqueService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDao repository;
    private final TokenDao tokenDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    HistoriqueService historiqueService;

    @Autowired

    RoleDao roleDao;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        String username = request.getUsername().trim().toUpperCase();

        if (repository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Le nom d'utilisateur "+username+" est déjà existe");
        }

        Role role = roleDao.findById(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role " + request.getRole() + " n'existe pas"));

        var user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword().trim()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .role(role)
                .isDeleted(false)
                .build();

        var savedUser = repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        historiqueService.add(HistoriqueAction.CREATE, User.class, savedUser.getUsername());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            User user = repository.findByUsernameAndIsDeletedFalse(request.getUsername()).orElseThrow();

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .role(user.getRole().getName())
                    .username(user.getUsername())
                    .build();
        } catch (DisabledException e) {
            throw new UnauthorizedException("Votre compte est désactivé.");
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Nom d'utilisateur et/ou mot de passe incorrect");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenDao.save(token);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenDao.findAllValidTokenByUser(user.getUsername());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenDao.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.repository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
