package com.api.dao.mysql;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.entities.mysql.Membre;

@Repository
public interface MembreDao extends JpaRepository<Membre, Long> {

    List<Membre> findByIsActifTrue();


}


