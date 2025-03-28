package com.api.entities.mysql;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Table(name="interfaces")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interfaces_seq_gen")
    @SequenceGenerator(name = "interfaces_seq_gen", sequenceName = "INTERFACES_SEQ", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name ;


    @Column(unique = true, nullable = false)
    private String path;

  
}