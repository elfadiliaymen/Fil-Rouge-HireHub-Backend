-- Hash les mots de passe des comptes de démonstration (BCrypt).
-- Les colonnes insérées en clair par V1 sont remplacées par leur hash.
-- Mot de passe commun : "password"
UPDATE users
SET password = '$2a$10$wMRnjIn1mQXS6mQVnjaQwu2C2h3wL7ou9TwjNQIZmcmAGVAB9WuZ2'
WHERE email IN (
    'admin@hirehub.com',
    'claire.martin@hirehub.com',
    'paul.bernard@hirehub.com',
    'sophie.durand@hirehub.com',
    'thomas.leroy@hirehub.com'
);