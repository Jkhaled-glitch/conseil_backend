package com.api.entities.mysql;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_seq_gen")
    @SequenceGenerator(name = "document_seq_gen", sequenceName = "DOCUMENT_SEQ", allocationSize = 1, initialValue = 1)
    private Long id;

    private String name; 


    @Column(name="doc_name")
    private String docName ;


    @JsonIgnore
    private String path;

    @Transient // Cet attribut n'est pas persisté en base de données
    @JsonGetter("url")
    public String getUrl() {
        return getBaseUrl() +"/storage/"+ this.getPath();
    }

    private String getBaseUrl() {
        // Récupérer la requête HTTP actuelle
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return ""; 
        }
        
        HttpServletRequest request = attributes.getRequest();
        
        // Construire l'URL de base
        String scheme = request.getScheme(); // http ou https
        String host = request.getServerName(); // nom de domaine
        int port = request.getServerPort(); // port du serveur
        
        // Si le port est standard (80 pour http, 443 pour https), on ne l'ajoute pas
        String portStr = ((port == 80 && "http".equals(scheme)) || (port == 443 && "https".equals(scheme))) 
            ? "" 
            : ":" + port;
        
        return scheme + "://" + host + portStr;
    }
}