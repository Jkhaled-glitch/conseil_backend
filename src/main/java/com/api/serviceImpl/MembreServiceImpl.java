


package com.api.serviceImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.dao.mysql.MembreDao;
import com.api.entities.mysql.Membre;
import com.api.exception.ResourceNotFoundException;
import com.api.service.MembreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MembreServiceImpl implements MembreService {


    @Autowired
    MembreDao dao ;
    
    @Override
    public List<Membre> getAll() {
       
       return dao.findAll();
    }




    @Override
    public List<Membre> getActif() {
       return dao.findByIsActifTrue();
    }


    @Override
    public Membre getById(Long id) {
       return dao.findById(id)
        .orElseThrow(()->new ResourceNotFoundException("Membre inexiste "));
    }

    @Override
    public String delete(Long id) {
        
        dao.delete(getById(id)) ;

        return "Membre Supprimé avec succées";
    }




    @Override
    public Membre switchActif(Long id) {

        Membre membre = getById(id);
      
        membre.setIsActif(!Boolean.TRUE.equals(membre.getIsActif()));

        return dao.save(membre);
      
    }

}

