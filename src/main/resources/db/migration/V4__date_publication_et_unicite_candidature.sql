-- Ajoute la date de publication des offres (nécessaire au tri "Plus récentes").
ALTER TABLE offres_emploi ADD COLUMN date_publication DATE NULL;

UPDATE offres_emploi SET date_publication = '2026-09-01' WHERE date_publication IS NULL;

ALTER TABLE offres_emploi MODIFY COLUMN date_publication DATE NOT NULL;

-- Garantit qu'un candidat ne peut pas postuler deux fois à la même offre.
ALTER TABLE candidatures ADD CONSTRAINT uq_candidature_candidat_offre UNIQUE (candidat_id, offre_id);