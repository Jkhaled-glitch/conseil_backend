package com.api.entities.mysql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.api.enums.HistoriqueAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Historique {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historique_seq_gen")
    @SequenceGenerator(name = "historique_seq_gen", sequenceName = "HISTORIQUE_SEQ", allocationSize = 1, initialValue = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private HistoriqueAction action;

    private String entity;

    private String reference; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    // Auto créer une date
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
