CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(20) NOT NULL,
    prenom VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM ('ADMIN', 'CANDIDAT', 'RECRUTEUR') NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS offres_emploi (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    localisation VARCHAR(150) NOT NULL,
    type_contrat ENUM ('ALTERNANCE', 'CDD', 'CDI', 'FREELANCE', 'STAGE') NOT NULL,
    date_limite DATE NOT NULL,
    recruteur_id BIGINT NOT NULL,
    CONSTRAINT fk_offre_recruteur FOREIGN KEY (recruteur_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS candidatures (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date_candidature DATETIME NOT NULL,
    statut ENUM ('ACCEPTEE', 'EN_ATTENTE', 'REFUSEE') NOT NULL,
    candidat_id BIGINT NOT NULL,
    offre_id BIGINT NOT NULL,
    CONSTRAINT fk_candidature_candidat FOREIGN KEY (candidat_id) REFERENCES users (id),
    CONSTRAINT fk_candidature_offre FOREIGN KEY (offre_id) REFERENCES offres_emploi (id)
);

CREATE TABLE IF NOT EXISTS entretiens (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    heure TIME NOT NULL,
    lieu VARCHAR(150) NOT NULL,
    candidature_id BIGINT NOT NULL,
    recruteur_id BIGINT NOT NULL,
    CONSTRAINT fk_entretien_candidature FOREIGN KEY (candidature_id) REFERENCES candidatures (id),
    CONSTRAINT fk_entretien_recruteur FOREIGN KEY (recruteur_id) REFERENCES users (id)
);

INSERT IGNORE INTO users (id, nom, prenom, email, password, role, active) VALUES
    (1, 'Admin', 'System', 'admin@hirehub.com', 'password', 'ADMIN', 1),
    (2, 'Martin', 'Claire', 'claire.martin@hirehub.com', 'password', 'RECRUTEUR', 1),
    (3, 'Bernard', 'Paul', 'paul.bernard@hirehub.com', 'password', 'RECRUTEUR', 1),
    (4, 'Durand', 'Sophie', 'sophie.durand@hirehub.com', 'password', 'CANDIDAT', 1),
    (5, 'Leroy', 'Thomas', 'thomas.leroy@hirehub.com', 'password', 'CANDIDAT', 1);

INSERT IGNORE INTO offres_emploi (id, titre, description, localisation, type_contrat, date_limite, recruteur_id) VALUES
    (1, 'Développeur Backend Java', 'Développement d''APIs REST Spring Boot et intégration continue.', 'Paris', 'CDI', '2026-12-31', 2),
    (2, 'Développeur Frontend React', 'Développement d''interfaces web modernes en React et TypeScript.', 'Lyon', 'CDI', '2026-12-15', 2),
    (3, 'Data Analyst', 'Analyse des données et création de rapports décisionnels.', 'Marseille', 'CDD', '2026-11-30', 3),
    (4, 'Stage Développeur Web', 'Stage de fin d''études : développement fullstack Java / React.', 'Bordeaux', 'STAGE', '2026-10-31', 3);

INSERT IGNORE INTO candidatures (id, date_candidature, statut, candidat_id, offre_id) VALUES
    (1, '2026-09-01 10:30:00', 'EN_ATTENTE', 4, 1),
    (2, '2026-09-02 14:15:00', 'ACCEPTEE', 5, 1),
    (3, '2026-09-03 09:00:00', 'REFUSEE', 4, 2),
    (4, '2026-09-04 16:45:00', 'EN_ATTENTE', 5, 3);

INSERT IGNORE INTO entretiens (id, date, heure, lieu, candidature_id, recruteur_id) VALUES
    (1, '2026-09-20', '10:00:00', 'En ligne / Google Meet', 2, 2),
    (2, '2026-09-22', '14:00:00', 'Bureau de Lille', 1, 2),
    (3, '2026-09-25', '09:30:00', 'Agence de Marseille', 4, 3);