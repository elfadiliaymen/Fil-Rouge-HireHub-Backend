CREATE TABLE IF NOT EXISTS cvs (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    candidat_id BIGINT NOT NULL,
    nom_fichier VARCHAR(255) NOT NULL,
    chemin_fichier VARCHAR(255) NOT NULL,
    contenu LONGBLOB,
    date_upload DATETIME NOT NULL,
    CONSTRAINT fk_cv_candidat FOREIGN KEY (candidat_id) REFERENCES users (id)
);

ALTER TABLE candidatures
    ADD COLUMN cv_id BIGINT NULL,
    ADD CONSTRAINT fk_candidature_cv FOREIGN KEY (cv_id) REFERENCES cvs (id);