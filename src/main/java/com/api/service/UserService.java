package com.api.service;

import com.api.request.RegisterRequest;
import com.api.request.AuthenticationRequest;
import com.api.request.ChangePasswordRequest;
import com.api.entities.mysql.User;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.api.entities.mysql.Role;

public interface UserService {
    

    String changePassword(ChangePasswordRequest request, Principal connectedUser);
    String adminChangeUserPassword(AuthenticationRequest request);
    void DeleteById(String id);
    List<User> GetAllUsers();
    Optional<User> findById(String id);
    List<User> findAllByRole(Role role);
    User modifierUser(RegisterRequest user, String id);
    User findUserByRole(Role role);

    String changerEtat(String username) ;

    String deleteUser(String username);

}
