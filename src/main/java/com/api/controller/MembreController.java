


package com.api.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.entities.mysql.Membre;
import com.api.service.MembreService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/membres")
@Tag(name="Membre" , description="Handle Membres")
@RequiredArgsConstructor
public class MembreController {
    @Autowired
    MembreService service;

    @GetMapping
    public List<Membre> getAllMembres() { 
        return service.getAll();

    }



    @GetMapping("/actif")
    public List<Membre> getActifMembres() { 
        return service.getActif();

    }


    @PutMapping("/{id}/switch-actif")
    public Membre switchActifMembres(@PathVariable("id") Long id) { 
        return service.switchActif(id);

    }

    
    @GetMapping("/{id}")
    public Membre getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }
    
}

