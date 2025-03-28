package com.api.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.entities.mysql.User;
import com.api.request.AuthenticationRequest;
import com.api.request.ChangePasswordRequest;
import com.api.request.RegisterRequest;
import com.api.response.AuthenticationResponse;
import com.api.service.AuthenticationService;
import com.api.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/users")
@Tag(name="User" , description="Handle Users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping
    public List<User> getAllUsers() { 
        return service.GetAllUsers();

    }




    @PostMapping("/register")
    public AuthenticationResponse register( @RequestBody RegisterRequest request ) {
        return authenticationService.register(request);
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable("id") String id,
            @RequestBody RegisterRequest request
    ) {
        return service.modifierUser(request,id);
    }


    @PutMapping("/{id}/change-etat")
    public String changeEtat( @PathVariable("id") String id  ) {
        return service.changerEtat(id);
    }


   

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        return service.deleteUser(id);
    }



  

   
    @PostMapping("/change-password")
    public String changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        return service.changePassword(request, connectedUser);
     
    }

    @PostMapping("/change-password-by-admin")
    public String changePassword(
          @RequestBody AuthenticationRequest request
    ) {
        return service.adminChangeUserPassword(request);
    }

    
}
