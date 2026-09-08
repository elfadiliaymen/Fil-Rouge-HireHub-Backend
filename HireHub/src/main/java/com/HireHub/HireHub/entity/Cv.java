package com.HireHub.HireHub.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cvs")
@NoArgsConstructor
@Data
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidat_id", nullable = false)
    private User candidat;

    @Column(name = "nom_fichier", nullable = false, length = 255)
    private String nomFichier;

    @Column(name = "chemin_fichier", nullable = false, length = 255)
    private String cheminFichier;

    @Lob
    @Column(name = "contenu")
    private byte[] contenu;

    @Column(name = "date_upload", nullable = false)
    private LocalDateTime dateUpload;

    @PrePersist
    void onCreate() {
        if (dateUpload == null) {
            dateUpload = LocalDateTime.now();
        }
    }

}
