package com.HireHub.HireHub.entity;

import com.HireHub.HireHub.entity.enums.StatutCandidature;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidatures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_candidature", nullable = false, updatable = false)
    private LocalDateTime dateCandidature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutCandidature statut;

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