package com.api.entities.mysql;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Point {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_seq_gen")
    @SequenceGenerator(name = "point_seq_gen", sequenceName = "POINT_SEQ", allocationSize = 1, initialValue = 1)
    private Long id;

    private String nom; 
     
    private Integer  numero ; 

    @ManyToMany
    @JoinTable(
            name = "point_document",
            joinColumns = @JoinColumn(name = "point_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
   

    private List<Document> documents ;
    
}
