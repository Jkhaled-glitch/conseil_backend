package com.api.serviceImpl;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.dao.mysql.TokenDao;
import com.api.dao.mysql.UserDao;
import com.api.entities.mysql.Role;
import com.api.entities.mysql.Token;
import com.api.entities.mysql.User;
import com.api.enums.HistoriqueAction;
import com.api.exception.BadRequestException;
import com.api.exception.ConflictException;
import com.api.exception.ForbiddenException;
import com.api.exception.ResourceNotFoundException;
import com.api.request.AuthenticationRequest;
import com.api.request.ChangePasswordRequest;
import com.api.request.RegisterRequest;
import com.api.service.AuthenticationService;
import com.api.service.UserService;

import com.api.service.HistoriqueService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    private final PasswordEncoder passwordEncoder;
    private final UserDao repository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TokenDao tokenDao ;

    @Autowired
    HistoriqueService  historiqueService ;

    @Override
    public String changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

   
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Nom d'utilisateur ou mot de passe incorrecte");
        }
        
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new BadRequestException("mot de passe inconfirmés");
        }
        

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);

        historiqueService.add(HistoriqueAction.CHANGE_PASSWORD, User.class, user.getUsername());

        

        return "Mot de passe changer avec succées !";
    }

    @Override
    public String adminChangeUserPassword(AuthenticationRequest request) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User admin = (User) userDetails;

        if(!"ADMIN".equals(admin.getRole().getName())){
            throw new  ForbiddenException("Vous n'avez pas le droit de faire cette action !");
        }

        User existingUser = userDao.findByUsernameAndIsDeletedFalse(request.getUsername()).orElseThrow(() -> new ResourceNotFoundException("Utilisateur n'existe pas"));
        if (existingUser.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            throw new Error("Mot de passe ne peut etre vide ou null ");
        }
        userDao.save(existingUser);
        historiqueService.add(HistoriqueAction.CHANGE_PASSWORD, User.class, existingUser.getUsername());
        return "Mot de passe se change avec succées !";

    }

   

    @Override
    public User modifierUser(RegisterRequest user, String id) {
        User existingUser = userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Utilisateur n'existe pas"));

        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }

        historiqueService.add(HistoriqueAction.UPDATE, User.class, existingUser.getUsername());
        return userDao.save(existingUser);
    }

    @Override
    public User findUserByRole(Role role) throws ResourceNotFoundException {
        User user = userDao.findUserByRoleAndIsDeletedFalse(role);
        return user;
    }

    @Override
    public void DeleteById(String id) {
        User existingUser = userDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Utilisateur n'existe pas"));
        existingUser.setIsDeleted(true); // suppression logique 
        historiqueService.add(HistoriqueAction.DELETE, User.class, existingUser.getUsername());
        userDao.save(existingUser);
    }

    @Override
    public List<User> GetAllUsers() {
        return (List<User>) userDao.findByIsDeletedFalse();
    }

    @Override
    public Optional<User> findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAllByRole(Role role) throws ResourceNotFoundException {
        return  userDao.findAllByRoleAndIsDeletedFalse(role);
    }

    @Override
    
    public String changerEtat(String username) {

        User existingUser = userDao.findById(username).orElseThrow(() -> new ResourceNotFoundException("Utilisateur n'existe pas"));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User connectedUser = (User) userDetails;

        if (connectedUser.getUsername().equals(username)) {
            throw new BadRequestException("Vous navez pas les droit de faire cette action");
        }

        Boolean nextActifStatus = !existingUser.getIsActif();

        if (nextActifStatus == false) {
            authenticationService.revokeAllUserTokens(existingUser);
        }



        existingUser.setIsActif(nextActifStatus);

        historiqueService.add(nextActifStatus ? HistoriqueAction.ENABLE_USER   : HistoriqueAction.DISABLE_USER  , User.class, existingUser.getUsername());
        userDao.save(existingUser);
        return "Utilisateur modifié avec succées";
    }



@Override
   public String deleteUser(String username) {
    try {
        // Supprimer les jetons d'utilisateur
        tokenDao.deleteAllTokensByUserId(username);

        // Supprimer l'utilisateur
        DeleteById(username);

        
        

        return "Utilisateur supprimé avec succès !";
    } catch (DataIntegrityViolationException e) {
        throw new ConflictException("Impossible de supprimer l'utilisateur à cause d'un conflit de données.");
    } 
}
}
