package com.api.entities.mysql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Conseil {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conseil_seq_gen")
    @SequenceGenerator(name = "conseil_seq_gen", sequenceName = "CONSEIL_SEQ", allocationSize = 1, initialValue = 1)
    private Long id;

    private String nom ;
    @Column(name="d_reunion")
    private String dReunion ;
    @Column(name="d_accord")
    private String dAccord ;
    

    @OneToOne
    private Document pv ;
    

    

    @ManyToMany
    @JoinTable(
            name = "conseil_documents",
            joinColumns = @JoinColumn(name = "conseil_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private List<Document> documents;


    @ManyToMany
    @JoinTable(
            name = "conseil_point",
            joinColumns = @JoinColumn(name = "conseil_id"),
            inverseJoinColumns = @JoinColumn(name = "point_id"))
    private List<Point> points;




    @ManyToMany
    @JoinTable(
            name = "conseil_presence",
            joinColumns = @JoinColumn(name = "conseil_id"),
            inverseJoinColumns = @JoinColumn(name = "presence_id"))
    private List<Presence> presences;

}
    


