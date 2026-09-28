ALTER TABLE entretiens
    ADD COLUMN statut ENUM ('PLANIFIE', 'REUSSI', 'ECHEC', 'ANNULE') NOT NULL DEFAULT 'PLANIFIE';

UPDATE entretiens e
    JOIN candidatures c ON c.id = e.candidature_id
SET e.statut = 'REUSSI'
WHERE c.statut = 'ACCEPTEE';
