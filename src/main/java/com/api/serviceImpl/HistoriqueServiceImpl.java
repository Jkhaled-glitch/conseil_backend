package com.api.serviceImpl;

import com.api.dao.mysql.HistoriqueDao;
import com.api.entities.mysql.Historique;
import com.api.entities.mysql.User;
import com.api.enums.HistoriqueAction;
import com.api.exception.ResourceNotFoundException;
import com.api.service.HistoriqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueServiceImpl implements HistoriqueService {

    @Autowired
    HistoriqueDao historiqueDao;

    @Override
    public Historique add(HistoriqueAction action, Class<?> entityClass, String reference) {
    
        // Récupérer l'utilisateur connecté
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) userDetails;
    
        // Extraire le nom de la classe pour le champ entity
        String entityName = entityClass.getSimpleName();
    
        // Créer l'objet Historique
        Historique historique = Historique.builder()
                .action(action)
                .entity(entityName)
                .reference(reference)
                .createdBy(user) 
                .build();
    
        // Enregistrer l'historique dans la base de données
        return historiqueDao.save(historique);
    }
    

    @Override
    public List<Historique> getAll() {
        return historiqueDao.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Historique getById(Long id) {
        return historiqueDao.findById(id)
        .orElseThrow(()->new ResourceNotFoundException("historique inexiste "));
    }

    @Override
    public List<Historique> findByCreatedByUsername(String username) {
        return historiqueDao.findByCreatedByUsername(username);
    }

    @Override
    public List<Historique> findByCreatedAtBetween(Date startDate, Date endDate) {
        return historiqueDao.findByCreatedAtBetween(startDate, endDate);
    }

}
