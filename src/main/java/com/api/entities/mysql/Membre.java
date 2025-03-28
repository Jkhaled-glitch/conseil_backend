package com.api.entities.mysql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "membre_seq_gen")
    @SequenceGenerator(name = "membre_seq_gen", sequenceName = "MEMBRE_SEQ", allocationSize = 1, initialValue = 1)
    private Long id;

    private String nom;

    private String prenom;

    private String role;

    @Column(name = "is_actif")
    private Boolean isActif;

}
