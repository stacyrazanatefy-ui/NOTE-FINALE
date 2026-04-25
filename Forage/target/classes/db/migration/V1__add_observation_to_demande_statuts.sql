-- Ajouter la colonne observation à la table demande_statuts
ALTER TABLE demande_statuts 
ADD COLUMN observation VARCHAR(1000);

-- Ajouter un commentaire pour décrire la colonne
COMMENT ON COLUMN demande_statuts.observation IS 'Observation ou justification pour le changement de statut de la demande';
