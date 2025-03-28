package com.api.entities.mysql;

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
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "presence_seq_gen")
    @SequenceGenerator(name = "presence_seq_gen", sequenceName = "PRESENCE_SEQ", allocationSize = 1, initialValue = 1)
    private Long id;


    @ManyToOne
    private Membre membre ;

    private String statut;

    @ManyToOne
    @JoinColumn(name = "delegue_a")
    private Membre delegueA ;


    @OneToOne
    private Document document ;

}
    


