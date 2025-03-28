package com.api.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.service.JobService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Jobs", description = "Execute jobs Manually")
@RequiredArgsConstructor
public class JobsController {

    private final JobService service;

    @PostMapping("/synchronize")
    public String synchroniseDecompte() {
      
        service.scheduled(); 
            return "Job executed successfully.";
        
    }



    
}
