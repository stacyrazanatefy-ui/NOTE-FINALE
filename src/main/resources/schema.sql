-- ============================================
-- Création des tables pour PostgreSQL
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
