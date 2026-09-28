-- 002_seed_data_ma.sql
-- Demo seed (Moroccan names/places)
-- Demo users omitted from seed data for security reasons.
-- Remove any hardcoded password hashes from migrations. Create initial users via the application registration
-- flow or provide hashed passwords at deploy time using secure secrets management.

-- Example: create users via backend endpoints or run a secure script that reads
-- passwords from environment variables and hashes them with BCrypt before inserting.

-- INSERT statements for users were intentionally removed to avoid embedding bcrypt
-- hashes or credentials in the repository.



INSERT IGNORE INTO offres_emploi
    (id, titre, description, localisation, type_contrat, date_publication, date_limite, recruteur_id)
VALUES
    (1, 'Développeur Backend Java (Spring Boot)',
        'Développement d''APIs REST Spring Boot, tests, CI/CD et bonnes pratiques.',
        'Casablanca', 'CDI', '2026-09-01', '2026-12-31', 2),

    (2, 'Développeur Frontend React / TypeScript',
        'Développement d''interfaces web modernes en React, TypeScript, consommation d''APIs.',
        'Rabat', 'CDI', '2026-09-05', '2026-12-15', 2),

    (3, 'Data Analyst (Power BI / SQL)',
        'Analyse des données, dashboards Power BI, SQL et recommandations métier.',
        'Marrakech', 'CDD', '2026-09-08', '2026-11-30', 3),

    (4, 'Stage Développeur Web Fullstack (Java/React)',
        'Stage PFE : développement fullstack (Java / React) + bonnes pratiques de code.',
        'Tanger', 'STAGE', '2026-09-10', '2026-10-31', 3);

-- CVs first (because candidatures.cv_id references cvs.id)
INSERT IGNORE INTO cvs (id, candidat_id, nom_fichier, chemin_fichier, contenu, date_upload) VALUES
    (1, 4, 'CV_Imane_Alaoui.pdf',  '/uploads/cv/imane_alaoui.pdf',  NULL, '2026-09-01 09:10:00'),
    (2, 5, 'CV_Hamza_ElIdrissi.pdf','/uploads/cv/hamza_elidrissi.pdf', NULL, '2026-09-02 11:25:00');

INSERT IGNORE INTO candidatures
    (id, date_candidature, statut, candidat_id, offre_id, cv_id)
VALUES
    (1, '2026-09-03 10:30:00', 'EN_ATTENTE', 4, 1, 1),
    (2, '2026-09-04 14:15:00', 'ACCEPTEE',  5, 1, 2),
    (3, '2026-09-06 09:00:00', 'REFUSEE',   4, 2, 1),
    (4, '2026-09-07 16:45:00', 'EN_ATTENTE',5, 3, 2);

INSERT IGNORE INTO entretiens
    (id, `date`, heure, lieu, statut, candidature_id, recruteur_id)
VALUES
    (1, '2026-09-20', '10:00:00', 'En ligne / Google Meet',          'REUSSI',   2, 2),
    (2, '2026-09-22', '14:00:00', 'Technopark Casablanca - Salaire', 'PLANIFIE', 1, 2),
    (3, '2026-09-25', '09:30:00', 'Gueliz, Marrakech (Bureau)',     'PLANIFIE', 4, 3);