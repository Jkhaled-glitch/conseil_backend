

package com.api.dao.mysql;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.entities.mysql.Permission;

import java.util.Optional;


@Repository
public interface PermissionDao extends JpaRepository<Permission, Long> {

    Optional<Permission>  findByName(String name);


    Optional<Permission>  findByPath(String path);


    List<Permission> findAllByOrderByNameAsc();



}


