package com.HireHub.HireHub.entity;

import com.HireHub.HireHub.entity.enums.StatutCandidature;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidatures")
@Data
@NoArgsConstructor
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_candidature", nullable = false, updatable = false)
    private LocalDateTime dateCandidature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutCandidature statut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidat_id", nullable = false)
    private User candidat;

    @JsonIgnore
    @OneToMany(mappedBy = "candidature")
    private List<Entretien> entretiens = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offre_id", nullable = false)
    private OffreEmploi offre;

    @PrePersist
    protected void onCreate() {
        if (dateCandidature == null) {
            dateCandidature = LocalDateTime.now();
        }
        if (statut == null) {
            statut = StatutCandidature.EN_ATTENTE;
        }
    }
}