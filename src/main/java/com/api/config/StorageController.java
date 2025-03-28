package com.api.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/storage")
public class StorageController {

    private final String uploadDir = "uploads"; // Dossier de stockage des fichiers

    // Utilisation de RequestMapping pour capturer tous les chemins après /storage/**
 
    @GetMapping("/**")
    public ResponseEntity<Resource> getDocument(HttpServletRequest request) {
        try {
            // Extraire le chemin de la requête en enlevant "/storage/"
            String uri = request.getRequestURI();
            String path = uri.substring(uri.indexOf("/storage/") + "/storage/".length());

            System.out.println("path : "+ path);

        
        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8.name());

        System.out.println("decodedPath : "+ decodedPath);

        // Le reste de votre code reste inchangé...
        if (!decodedPath.matches("[a-zA-Z0-9/_\\-\\.\\p{InArabic}]+")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }



            // Diviser le chemin pour construire le chemin absolu
            String[] pathComponents = decodedPath.split("/");

            // Construire le chemin absolu vers le fichier
            Path filePath = Paths.get(uploadDir);
            for (String component : pathComponents) {
                filePath = filePath.resolve(component);  // Ajoute chaque sous-dossier ou fichier
            }

            // Normalisation du chemin pour éviter les attaques de traversée de répertoires
            filePath = filePath.normalize();

            // Sécurisation pour empêcher les attaques de traversée de répertoires
            if (!filePath.toString().startsWith(uploadDir)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            File file = filePath.toFile();

            // Vérifier si le fichier existe
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Charger le fichier en tant que ressource
            Resource resource = new UrlResource(filePath.toUri());
            String mimeType = Files.probeContentType(filePath);

            // Retourner le fichier avec son type MIME
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType != null ? mimeType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
