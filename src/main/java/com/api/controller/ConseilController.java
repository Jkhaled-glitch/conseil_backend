package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.entities.mysql.Conseil;
import com.api.entities.mysql.Point;
import com.api.request.ConseilRequest;
import com.api.request.DocumentRequest;
import com.api.request.PointRequest;
import com.api.request.PresenceRequest;
import com.api.service.ConseilService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;

import com.api.entities.mysql.Presence;

@RestController
@RequestMapping("/api/v1/conseils")
@Tag(name = "Conseil", description = "Handle Conseils")
@RequiredArgsConstructor
public class ConseilController {

    @Autowired
    ConseilService service;

    @GetMapping()
    public Page<Conseil> getAllConseils(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping("/add")
    public Conseil addConseil(@RequestBody ConseilRequest request) {
        Conseil conseil = Conseil.builder()
        .dAccord(request.getDAccord())
        .dReunion(request.getDReunion())
        .nom(request.getNom())
        .build();
        return service.addConseil(conseil);
    }

    @PostMapping("/{id}/presences/add")
    public Presence addPresence(@PathVariable Long id , @RequestBody PresenceRequest request) {
        return service.addPresence(id, request);
    }

    @PostMapping(value ="/{id}/presences/{presenceId}/document" ,
     consumes = {MediaType.MULTIPART_FORM_DATA_VALUE },
      produces = {MediaType.APPLICATION_JSON_VALUE}  )
    public Presence addDocumentToPresence(
        @PathVariable Long id ,
        @PathVariable Long presenceId,
        @RequestParam("file") MultipartFile file
       
        ) {
        return service.AddDelegation(id, presenceId, file);
    }


    @PostMapping("/{id}/points/add")
    public Point addPoint(@PathVariable Long id , @RequestBody PointRequest request) {
        return service.addPoint(id, request.getNom()) ;
    }




    @PostMapping(value ="/{id}/points/{pointId}/document" ,
     consumes = {MediaType.MULTIPART_FORM_DATA_VALUE },
      produces = {MediaType.APPLICATION_JSON_VALUE}  )
    public Point addDocumentToPoint(@PathVariable Long id ,@PathVariable Long pointId, @RequestParam("file") MultipartFile file, @RequestParam("nom") String nom) {
        return service.addDocumentToPoint(id,pointId , file, nom);
    }



















  
    @PostMapping("/{id}/add-pv")
    public Conseil addPv(
            @PathVariable("id") Long id,
            @RequestParam("file") MultipartFile file
    ) {
        DocumentRequest request = new DocumentRequest();
        request.setDocument(file);

        return service.AddPV(id, request);
    }

    @PostMapping("/{id}/add-document")
    public Conseil addDocument(
            @PathVariable("id") Long id,
            @PathVariable("point") String point,
            @RequestParam("nom") String nom,
            @RequestParam("document") MultipartFile file
    ) {
        DocumentRequest request = new DocumentRequest();
        request.setNom(nom);
        request.setDocument(file);

        return service.AssignDocument(id, request);
    }

   

    @GetMapping("/{id}")
    public Conseil getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }

}
