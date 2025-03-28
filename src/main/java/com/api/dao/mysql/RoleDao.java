package com.api.dao.mysql;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.entities.mysql.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, String> {


}

