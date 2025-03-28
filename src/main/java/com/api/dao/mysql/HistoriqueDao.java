package com.api.dao.mysql;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.entities.mysql.Historique;

import java.util.Date;
import java.util.List;


@Repository
public interface HistoriqueDao extends JpaRepository<Historique, Long> {

    List<Historique> findAllByOrderByCreatedAtDesc();

    List<Historique> findByCreatedByUsername(String username);

    List<Historique> findByCreatedAtBetween(Date startDate, Date endDate);

}
