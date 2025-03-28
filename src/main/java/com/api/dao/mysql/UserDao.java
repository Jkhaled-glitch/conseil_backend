package com.api.dao.mysql;

import java.util.List;
import java.util.Optional;

import com.api.entities.mysql.User;
import com.api.entities.mysql.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {

    Optional<User> findByUsernameAndIsDeletedFalse(String username);

    Optional<User> findByUsername(String username);

    List<User> findAllByRoleAndIsDeletedFalse(Role role);

    User findUserByRoleAndIsDeletedFalse(Role role);

    List<User> findByIsDeletedFalse();

    List<User> findByIsDeletedTrue();

    //pour supprimer un tel role 
    boolean existsByRoleAndIsDeletedFalse(Role role);

    @Modifying
    @Query("UPDATE User u SET u.role = null WHERE u.role = :role ")
    void updateRoleToNullForUsers(Role role);

}
