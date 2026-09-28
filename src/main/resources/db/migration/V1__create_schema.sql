-- 001_create_schema.sql
-- Final schema (no ALTER): users, offres_emploi, cvs, candidatures, entretiens

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                     nom VARCHAR(20) NOT NULL,
    prenom VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM ('ADMIN', 'CANDIDAT', 'RECRUTEUR') NOT NULL,
    active BOOLEAN NOT NULL DEFAULT 1
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS offres_emploi (
                                             id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                             titre VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    localisation VARCHAR(150) NOT NULL,
    type_contrat ENUM ('ALTERNANCE', 'CDD', 'CDI', 'FREELANCE', 'STAGE') NOT NULL,
    date_publication DATE NOT NULL,
    date_limite DATE NOT NULL,
    recruteur_id BIGINT NOT NULL,

    KEY idx_offres_recruteur_id (recruteur_id),
    CONSTRAINT fk_offre_recruteur
    FOREIGN KEY (recruteur_id) REFERENCES users (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cvs (
                                   id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                   candidat_id BIGINT NOT NULL,
                                   nom_fichier VARCHAR(255) NOT NULL,
    chemin_fichier VARCHAR(255) NOT NULL,
    contenu LONGBLOB,
    date_upload DATETIME NOT NULL,

    KEY idx_cvs_candidat_id (candidat_id),
    CONSTRAINT fk_cv_candidat
    FOREIGN KEY (candidat_id) REFERENCES users (id)
    ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS candidatures (
                                            id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                            date_candidature DATETIME NOT NULL,
                                            statut ENUM ('ACCEPTEE', 'EN_ATTENTE', 'REFUSEE') NOT NULL,
    candidat_id BIGINT NOT NULL,
    offre_id BIGINT NOT NULL,
    cv_id BIGINT NULL,

    -- one candidate cannot apply twice to the same offer
    CONSTRAINT uq_candidature_candidat_offre UNIQUE (candidat_id, offre_id),

    KEY idx_candidatures_candidat_id (candidat_id),
    KEY idx_candidatures_offre_id (offre_id),
    KEY idx_candidatures_cv_id (cv_id),

    CONSTRAINT fk_candidature_candidat
    FOREIGN KEY (candidat_id) REFERENCES users (id)
    ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_candidature_offre
    FOREIGN KEY (offre_id) REFERENCES offres_emploi (id)
    ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_candidature_cv
    FOREIGN KEY (cv_id) REFERENCES cvs (id)
    ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS entretiens (
                                          id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                          `date` DATE NOT NULL,
                                          heure TIME NOT NULL,
                                          lieu VARCHAR(150) NOT NULL,
    statut ENUM ('PLANIFIE', 'REUSSI', 'ECHEC', 'ANNULE') NOT NULL DEFAULT 'PLANIFIE',
    candidature_id BIGINT NOT NULL,
    recruteur_id BIGINT NOT NULL,

    KEY idx_entretiens_candidature_id (candidature_id),
    KEY idx_entretiens_recruteur_id (recruteur_id),

    CONSTRAINT fk_entretien_candidature
    FOREIGN KEY (candidature_id) REFERENCES candidatures (id)
    ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_entretien_recruteur
    FOREIGN KEY (recruteur_id) REFERENCES users (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB;