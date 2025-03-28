
package com.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.entities.mysql.Document;
import com.api.dao.mysql.DocumentDao;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.api.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/documents")
@Tag(name = "Document", description = "Handle Document")
@RequiredArgsConstructor
public class DocumentController {

    @Autowired
    DocumentDao dao;

   
    @GetMapping("/{id}")
    public Document getById(@PathVariable("id") Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document inexiste "));
    }

    

}

