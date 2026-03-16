-- ============================================
-- Création de la base de données et insertion des données
-- ============================================

-- Table candidat
CREATE TABLE candidat (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

-- Table matiere
CREATE TABLE matiere (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

-- Table correcteur
CREATE TABLE correcteur (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

-- Table operateur
CREATE TABLE operateur (
    id BIGSERIAL PRIMARY KEY,
    operateur VARCHAR(10) NOT NULL
);

-- Table resolution
CREATE TABLE resolution (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL
);

-- Table parametre
CREATE TABLE parametre (
    id BIGSERIAL PRIMARY KEY,
    idmatiere BIGINT NOT NULL,
    diff NUMERIC(10,2),
    idoperateur BIGINT NOT NULL,
    idresolution BIGINT NOT NULL,
    CONSTRAINT fk_parametre_matiere FOREIGN KEY (idmatiere) REFERENCES matiere(id) ON DELETE CASCADE,
    CONSTRAINT fk_parametre_operateur FOREIGN KEY (idoperateur) REFERENCES operateur(id) ON DELETE CASCADE,
    CONSTRAINT fk_parametre_resolution FOREIGN KEY (idresolution) REFERENCES resolution(id) ON DELETE CASCADE
);

-- Table note
CREATE TABLE note (
    id BIGSERIAL PRIMARY KEY,
    note NUMERIC(10,2) NOT NULL,
    idcandidat BIGINT NOT NULL,
    idmatiere BIGINT NOT NULL,
    idcorrecteur BIGINT NOT NULL,
    CONSTRAINT fk_note_candidat FOREIGN KEY (idcandidat) REFERENCES candidat(id) ON DELETE CASCADE,
    CONSTRAINT fk_note_matiere FOREIGN KEY (idmatiere) REFERENCES matiere(id) ON DELETE CASCADE,
    CONSTRAINT fk_note_correcteur FOREIGN KEY (idcorrecteur) REFERENCES correcteur(id) ON DELETE CASCADE
);

-- ============================================
-- Insertion des données de test
-- ============================================

-- Insertion des candidats
INSERT INTO candidat (id, nom) VALUES (1, 'Candidat1');
INSERT INTO candidat (id, nom) VALUES (2, 'Candidat2');

-- Insertion des matières
INSERT INTO matiere (id, nom) VALUES (1, 'JAVA');
INSERT INTO matiere (id, nom) VALUES (2, 'PHP');

-- Insertion des correcteurs
INSERT INTO correcteur (id, nom) VALUES (1, 'Correcteur1');
INSERT INTO correcteur (id, nom) VALUES (2, 'Correcteur2');

-- Insertion des résolutions
INSERT INTO resolution (id, nom) VALUES (1, 'Petit');
INSERT INTO resolution (id, nom) VALUES (2, 'Grand');
INSERT INTO resolution (id, nom) VALUES (3, 'Moyenne');

-- Insertion des opérateurs
INSERT INTO operateur (id, operateur) VALUES (1, '<');
INSERT INTO operateur (id, operateur) VALUES (2, '<=');
INSERT INTO operateur (id, operateur) VALUES (3, '>');
INSERT INTO operateur (id, operateur) VALUES (4, '>=');

-- Insertion des paramètres
INSERT INTO parametre (id, idmatiere, diff, idoperateur, idresolution) VALUES (7, 1, 2.00, 1, 2);
INSERT INTO parametre (id, idmatiere, diff, idoperateur, idresolution) VALUES (8, 1, 2.00, 4, 3);
INSERT INTO parametre (id, idmatiere, diff, idoperateur, idresolution) VALUES (9, 2, 3.00, 2, 1);
INSERT INTO parametre (id, idmatiere, diff, idoperateur, idresolution) VALUES (10, 2, 3.00, 3, 2);

-- Insertion des notes
INSERT INTO note (id, note, idcandidat, idmatiere, idcorrecteur) VALUES (1, 12.00, 1, 1, 1);
INSERT INTO note (id, note, idcandidat, idmatiere, idcorrecteur) VALUES (2, 11.00, 1, 1, 2);
INSERT INTO note (id, note, idcandidat, idmatiere, idcorrecteur) VALUES (4, 13.00, 1, 2, 1);
INSERT INTO note (id, note, idcandidat, idmatiere, idcorrecteur) VALUES (5, 15.00, 1, 2, 2);
INSERT INTO note (id, note, idcandidat, idmatiere, idcorrecteur) VALUES (7, 12.00, 2, 1, 1);
INSERT INTO note (id, note, idcandidat, idmatiere, idcorrecteur) VALUES (8, 15.00, 2, 1, 2);
INSERT INTO note (id, note, idcandidat, idmatiere, idcorrecteur) VALUES (9, 14.00, 2, 2, 1);
INSERT INTO note (id, note, idcandidat, idmatiere, idcorrecteur) VALUES (11, 16.00, 2, 2, 2);
