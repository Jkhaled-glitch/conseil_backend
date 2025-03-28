package com.api.controller;


import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.entities.mysql.Historique;
import com.api.service.HistoriqueService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/historique")
@Tag(name="Historique" , description="Handle Historique")
@RequiredArgsConstructor
public class HistoriqueController {

    private final HistoriqueService historiqueService;

    @GetMapping
    public List<Historique> getAll() {
        return historiqueService.getAll();
    }

    @GetMapping("/{id}")
    public Historique getById(@PathVariable Long id) {
           return historiqueService.getById(id);
        
    }

    @GetMapping("username/{username}")
    public List<Historique> getByCreatedByUsername(@PathVariable String username) {

            return historiqueService.findByCreatedByUsername(username);

    }


    @GetMapping("/date")
    public List<Historique> getByDateInterval(
            @RequestParam Date startDate,
            @RequestParam Date endDate
    ) {
        return historiqueService.findByCreatedAtBetween(startDate,endDate);
    }


}

