package com.HireHub.HireHub.entity;

import com.HireHub.HireHub.entity.enums.TypeContrat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offres_emploi")
@Data
@NoArgsConstructor
public class OffreEmploi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 150)
    private String localisation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_contrat", nullable = false, length = 20)
    private TypeContrat typeContrat;

    @Column(name = "date_limite", nullable = false)
    private LocalDate dateLimite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recruteur_id", nullable = false)
    private User recruteur;

    @JsonIgnore
    @OneToMany(mappedBy = "offre")
    private List<Candidature> candidatures = new ArrayList<>();
}
