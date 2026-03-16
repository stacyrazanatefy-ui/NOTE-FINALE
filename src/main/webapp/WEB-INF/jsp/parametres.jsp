<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Paramètres - Système de Notes</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 50%, #0f0f0f 100%);
            min-height: 100vh;
            padding: 20px;
            margin: 0;
            color: #ffffff;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: rgba(20, 20, 20, 0.95);
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 20px;
            box-shadow: 0 8px 32px rgba(255, 255, 255, 0.1);
            overflow: hidden;
            backdrop-filter: blur(10px);
        }

        .header {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0.05) 100%);
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
            color: #ffffff;
            padding: 40px 30px;
            text-align: center;
            position: relative;
        }

        .header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
            text-shadow: 0 0 20px rgba(255, 255, 255, 0.2);
        }

        .header p {
            opacity: 0.9;
            color: rgba(255, 255, 255, 0.8);
        }

        .back-btn {
            position: absolute;
            top: 20px;
            left: 20px;
            background: rgba(255, 255, 255, 0.1);
            border: 2px solid rgba(255, 255, 255, 0.3);
            color: white;
            padding: 10px 20px;
            border-radius: 25px;
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .back-btn:hover {
            background: rgba(255, 255, 255, 0.2);
            transform: translateY(-2px);
        }

        .content {
            padding: 40px 30px;
        }

        .section-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .section-header h2 {
            font-size: 1.8em;
            color: #4a9eff;
        }

        .add-btn {
            background: linear-gradient(135deg, #4a9eff 0%, #0066cc 100%);
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: 600;
        }

        .add-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(74, 158, 255, 0.4);
        }

        .parametre-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .parametre-card {
            background: rgba(255, 255, 255, 0.05);
            border: 2px solid rgba(255, 255, 255, 0.2);
            border-radius: 15px;
            padding: 20px;
            transition: all 0.3s ease;
        }

        .parametre-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(74, 158, 255, 0.2);
            border-color: #4a9eff;
        }

        .parametre-id {
            background: #4a9eff;
            color: white;
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.8em;
            display: inline-block;
            margin-bottom: 10px;
        }

        .parametre-details {
            margin-bottom: 15px;
        }

        .parametre-details strong {
            color: #4a9eff;
        }

        .card-actions {
            display: flex;
            gap: 10px;
        }

        .btn-edit, .btn-delete {
            padding: 8px 15px;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: 500;
        }

        .btn-edit {
            background: #28a745;
            color: white;
        }

        .btn-edit:hover {
            background: #218838;
        }

        .btn-delete {
            background: #dc3545;
            color: white;
        }

        .btn-delete:hover {
            background: #c82333;
        }

        .form-container {
            background: rgba(255, 255, 255, 0.05);
            border: 2px solid rgba(255, 255, 255, 0.2);
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
            display: none;
        }

        .form-container h3 {
            margin-bottom: 20px;
            color: #4a9eff;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 20px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-group label {
            margin-bottom: 8px;
            font-weight: 500;
            color: #ffffff;
        }

        .form-group select,
        .form-group input {
            padding: 12px 16px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.1);
            color: #ffffff;
            font-size: 1em;
            transition: all 0.3s ease;
        }

        .form-group select:focus,
        .form-group input:focus {
            outline: none;
            border-color: #4a9eff;
            box-shadow: 0 0 15px rgba(74, 158, 255, 0.3);
        }

        .form-group select option {
            background: #2a2a2a;
            color: #ffffff;
        }

        .form-actions {
            display: flex;
            gap: 15px;
        }

        .btn-save, .btn-cancel {
            padding: 12px 25px;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: 600;
        }

        .btn-save {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white;
        }

        .btn-save:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(40, 167, 69, 0.4);
        }

        .btn-cancel {
            background: rgba(255, 255, 255, 0.1);
            color: white;
            border: 2px solid rgba(255, 255, 255, 0.3);
        }

        .btn-cancel:hover {
            background: rgba(255, 255, 255, 0.2);
        }

        .loading {
            text-align: center;
            padding: 40px;
            color: #4a9eff;
            font-size: 1.2em;
        }

        .error {
            background: rgba(255, 0, 0, 0.1);
            border: 2px solid rgba(255, 0, 0, 0.3);
            color: #ff6b6b;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .success {
            background: rgba(0, 255, 0, 0.1);
            border: 2px solid rgba(0, 255, 0, 0.3);
            color: #51cf66;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        @media (max-width: 768px) {
            .container {
                margin: 10px;
                border-radius: 15px;
            }
            
            .header {
                padding: 30px 20px;
            }
            
            .header h1 {
                font-size: 2em;
            }
            
            .content {
                padding: 30px 20px;
            }
            
            .parametre-grid {
                grid-template-columns: 1fr;
            }
            
            .section-header {
                flex-direction: column;
                gap: 20px;
                text-align: center;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <a href="index.jsp" class="back-btn">← Retour</a>
            <h1>Gestion des Paramètres</h1>
            <p>Configuration des règles de calcul</p>
        </div>

        <div class="content">
            <div class="section-header">
                <h2>Liste des Paramètres</h2>
                <button class="add-btn" onclick="showAddForm()">+ Ajouter un Paramètre</button>
            </div>

            <!-- Formulaire d'ajout/modification -->
            <div id="formContainer" class="form-container">
                <h3 id="formTitle">Ajouter un Paramètre</h3>
                <form id="parametreForm">
                    <input type="hidden" id="parametreId" name="id">
                    
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="matiere">Matière:</label>
                            <select id="matiere" name="matiere" required>
                                <option value="">Sélectionner une matière</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="diff">Différence:</label>
                            <input type="number" id="diff" name="diff" step="0.01" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="operateur">Opérateur:</label>
                            <select id="operateur" name="operateur" required>
                                <option value="">Sélectionner un opérateur</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="resolution">Résolution:</label>
                            <select id="resolution" name="resolution" required>
                                <option value="">Sélectionner une résolution</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn-save">Enregistrer</button>
                        <button type="button" class="btn-cancel" onclick="hideForm()">Annuler</button>
                    </div>
                </form>
            </div>
            
            <div id="parametresContainer">
                <div class="loading">Chargement des paramètres...</div>
            </div>
        </div>
    </div>

    <script>
        const API_BASE_URL = 'http://localhost:8084/api';

        document.addEventListener('DOMContentLoaded', () => {
            loadParametres();
            loadMatieres();
            loadOperateurs();
            loadResolutions();
        });

        document.getElementById('parametreForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const parametreId = document.getElementById('parametreId').value;
                console.log('ID du paramètre à modifier:', parametreId);
            const data = {
                matiere: { id: parseInt(document.getElementById('matiere').value) },
                diff: parseFloat(document.getElementById('diff').value),
                operateur: { id: parseInt(document.getElementById('operateur').value) },
                resolution: { id: parseInt(document.getElementById('resolution').value) }
            };

            try {
                console.log('Données envoyées:', data);
                
                let response;
                if (parametreId) {
                    response = await fetch(`${API_BASE_URL}/parametres/${parametreId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(data)
                    });
                } else {
                    response = await fetch(`${API_BASE_URL}/parametres`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(data)
                    });
                }

                console.log('Response status:', response.status);
                
                if (response.ok) {
                    alert('Paramètre sauvegardé avec succès!');
                    hideForm();
                    loadParametres();
                } else {
                    const errorText = await response.text();
                    console.error('Erreur response:', errorText);
                    alert('Erreur lors de la sauvegarde: ' + errorText);
                }
            } catch (error) {
                console.error('Erreur:', error);
                alert('Erreur lors de la sauvegarde: ' + error.message);
            }
        });

        async function loadParametres() {
            try {
                const response = await fetch(`${API_BASE_URL}/parametres`);
                const parametres = await response.json();
                
                const container = document.getElementById('parametresContainer');
                container.innerHTML = '<div class="parametre-grid">' + 
                    parametres.map(parametre => `
                        <div class="parametre-card">
                            <div class="parametre-id">#${parametre.id}</div>
                            <div class="parametre-details">
                                <strong>Matière:</strong> ${parametre.matiereNom}<br>
                                <strong>Différence:</strong> ${parametre.diff}<br>
                                <strong>Opérateur:</strong> ${parametre.operateurSymbole}<br>
                                <strong>Résolution:</strong> ${parametre.resolutionNom}
                            </div>
                            <div class="card-actions">
                                <button class="btn-edit" onclick="editParametre(${parametre.id})">Modifier</button>
                                <button class="btn-delete" onclick="deleteParametre(${parametre.id})">Supprimer</button>
                            </div>
                        </div>
                    `).join('') + '</div>';
            } catch (error) {
                console.error('Erreur:', error);
                document.getElementById('parametresContainer').innerHTML = '<div class="error">Erreur lors du chargement des paramètres</div>';
            }
        }

        async function loadMatieres() {
            try {
                const response = await fetch(`${API_BASE_URL}/matieres`);
                const matieres = await response.json();
                
                const select = document.getElementById('matiere');
                select.innerHTML = '<option value="">Sélectionner une matière</option>';
                
                matieres.forEach(matiere => {
                    const option = document.createElement('option');
                    option.value = matiere.id;
                    option.textContent = matiere.nom;
                    select.appendChild(option);
                });
            } catch (error) {
                console.error('Erreur:', error);
            }
        }

        async function loadOperateurs() {
            try {
                const response = await fetch(`${API_BASE_URL}/operateurs`);
                const operateurs = await response.json();
                
                const select = document.getElementById('operateur');
                select.innerHTML = '<option value="">Sélectionner un opérateur</option>';
                
                operateurs.forEach(operateur => {
                    const option = document.createElement('option');
                    option.value = operateur.id;
                    option.textContent = operateur.operateur;
                    select.appendChild(option);
                });
            } catch (error) {
                console.error('Erreur:', error);
            }
        }

        async function loadResolutions() {
            try {
                const response = await fetch(`${API_BASE_URL}/resolutions`);
                const resolutions = await response.json();
                
                const select = document.getElementById('resolution');
                select.innerHTML = '<option value="">Sélectionner une résolution</option>';
                
                resolutions.forEach(resolution => {
                    const option = document.createElement('option');
                    option.value = resolution.id;
                    option.textContent = resolution.nom;
                    select.appendChild(option);
                });
            } catch (error) {
                console.error('Erreur:', error);
            }
        }

        function showAddForm() {
            document.getElementById('formContainer').style.display = 'block';
            document.getElementById('formTitle').textContent = 'Ajouter un Paramètre';
            document.getElementById('parametreForm').reset();
            document.getElementById('parametreId').value = '';
        }

        function hideForm() {
            document.getElementById('formContainer').style.display = 'none';
        }

        async function editParametre(id) {
            try {
                console.log('=== DÉBUT ÉDITION ===');
                console.log('ID reçu:', id);
                console.log('Type de ID:', typeof id);
                console.log('ID est undefined?', id === undefined);
                console.log('ID est null?', id === null);
                
                if (!id || id === undefined || id === null) {
                    console.error('ID invalide:', id);
                    alert('Erreur: ID du paramètre invalide');
                    return;
                }
                
                console.log('URL appelée:', `${API_BASE_URL}/parametres/${id}/edit`);
                const response = await fetch(`${API_BASE_URL}/parametres/${id}/edit`);
                
                console.log('Response status:', response.status);
                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('Erreur API:', errorText);
                    throw new Error('Erreur API: ' + errorText);
                }
                
                const parametre = await response.json();
                console.log('Paramètre récupéré pour édition:', parametre);
                
                // Pour l'édition, on doit récupérer les IDs depuis les listes déroulantes
                const matiereSelect = document.getElementById('matiere');
                const operateurSelect = document.getElementById('operateur');
                const resolutionSelect = document.getElementById('resolution');
                
                // Trouver l'ID correspondant au nom
                const matiereId = Array.from(matiereSelect.options).find(option => option.textContent === parametre.matiereNom)?.value;
                const operateurId = Array.from(operateurSelect.options).find(option => option.textContent === parametre.operateurSymbole)?.value;
                const resolutionId = Array.from(resolutionSelect.options).find(option => option.textContent === parametre.resolutionNom)?.value;
                
                console.log('IDs trouvés:', {matiereId, operateurId, resolutionId});
                
                document.getElementById('formContainer').style.display = 'block';
                document.getElementById('formTitle').textContent = 'Modifier un Paramètre';
                document.getElementById('parametreId').value = parametre.id;
                document.getElementById('diff').value = parametre.diff;
                
                if (matiereId) document.getElementById('matiere').value = matiereId;
                if (operateurId) document.getElementById('operateur').value = operateurId;
                if (resolutionId) document.getElementById('resolution').value = resolutionId;
                
                console.log('Formulaire rempli avec succès');
                console.log('=== FIN ÉDITION ===');
            } catch (error) {
                console.error('Erreur lors de l\'édition:', error);
                alert('Erreur lors de l\'édition: ' + error.message);
            }
        }

        async function deleteParametre(id) {
            if (confirm('Êtes-vous sûr de vouloir supprimer ce paramètre ?')) {
                try {
                    await fetch(`${API_BASE_URL}/parametres/${id}`, { method: 'DELETE' });
                    loadParametres();
                } catch (error) {
                    console.error('Erreur:', error);
                    alert('Erreur lors de la suppression');
                }
            }
        }
    </script>
</body>
</html>
