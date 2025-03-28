package com.api.service;

import com.api.entities.mysql.Historique;
import com.api.enums.HistoriqueAction;
import java.util.Date;
import java.util.List;



public interface HistoriqueService {
    Historique add(HistoriqueAction action, Class<?> entityClass, String reference);
    List<Historique> getAll();

    Historique getById(Long id);

    List<Historique>  findByCreatedByUsername(String username);

    List<Historique> findByCreatedAtBetween(Date startDate, Date endDate); 

}
