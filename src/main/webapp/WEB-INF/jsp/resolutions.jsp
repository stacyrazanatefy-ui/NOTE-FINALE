<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Résolutions - Système de Notes</title>
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
            max-width: 1000px;
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

        .resolution-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .resolution-card {
            background: rgba(255, 255, 255, 0.05);
            border: 2px solid rgba(255, 255, 255, 0.2);
            border-radius: 15px;
            padding: 20px;
            transition: all 0.3s ease;
        }

        .resolution-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(74, 158, 255, 0.2);
            border-color: #4a9eff;
        }

        .resolution-id {
            background: #4a9eff;
            color: white;
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.8em;
            display: inline-block;
            margin-bottom: 10px;
        }

        .resolution-name {
            font-size: 1.3em;
            font-weight: bold;
            color: #4a9eff;
            margin-bottom: 10px;
        }

        .resolution-description {
            color: rgba(255, 255, 255, 0.7);
            font-size: 0.9em;
            margin-bottom: 15px;
        }

        .card-actions {
            display: flex;
            gap: 10px;
            margin-top: 15px;
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

        .form-group {
            display: flex;
            flex-direction: column;
            margin-bottom: 20px;
        }

        .form-group label {
            margin-bottom: 8px;
            font-weight: 500;
            color: #ffffff;
        }

        .form-group input {
            padding: 12px 16px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.1);
            color: #ffffff;
            font-size: 1em;
            transition: all 0.3s ease;
        }

        .form-group input:focus {
            outline: none;
            border-color: #4a9eff;
            box-shadow: 0 0 15px rgba(74, 158, 255, 0.3);
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
            
            .resolution-grid {
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
            <h1>Gestion des Résolutions</h1>
            <p>Configuration des méthodes de résolution</p>
        </div>

        <div class="content">
            <div class="section-header">
                <h2>Liste des Résolutions</h2>
                <button class="add-btn" onclick="showAddForm()">+ Ajouter une Résolution</button>
            </div>

            <!-- Formulaire d'ajout/modification -->
            <div id="formContainer" class="form-container">
                <h3 id="formTitle">Ajouter une Résolution</h3>
                <form id="resolutionForm">
                    <input type="hidden" id="resolutionId" name="id">
                    
                    <div class="form-group">
                        <label for="nom">Nom de la résolution:</label>
                        <input type="text" id="nom" name="nom" placeholder="Ex: Moyenne, Max, Min, Grand" required>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn-save">Enregistrer</button>
                        <button type="button" class="btn-cancel" onclick="hideForm()">Annuler</button>
                    </div>
                </form>
            </div>
            
            <div id="resolutionsContainer">
                <div class="loading">Chargement des résolutions...</div>
            </div>
        </div>
    </div>

    <script>
        const API_BASE_URL = 'http://localhost:8084/api';

        document.addEventListener('DOMContentLoaded', () => {
            loadResolutions();
        });

        document.getElementById('resolutionForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const resolutionId = document.getElementById('resolutionId').value;
            const resolutionName = document.getElementById('nom').value.trim();
            
            const data = {
                nom: resolutionName
            };

            try {
                console.log('Données envoyées:', data);
                
                let response;
                if (resolutionId) {
                    response = await fetch(`${API_BASE_URL}/resolutions/${resolutionId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(data)
                    });
                } else {
                    response = await fetch(`${API_BASE_URL}/resolutions`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(data)
                    });
                }

                console.log('Response status:', response.status);
                
                if (response.ok) {
                    alert('Résolution sauvegardée avec succès!');
                    hideForm();
                    loadResolutions();
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

        async function loadResolutions() {
            try {
                const response = await fetch(`${API_BASE_URL}/resolutions`);
                const resolutions = await response.json();
                
                const container = document.getElementById('resolutionsContainer');
                container.innerHTML = '<div class="resolution-grid">' + 
                    resolutions.map(resolution => `
                        <div class="resolution-card">
                            <div class="resolution-id">#${resolution.id}</div>
                            <div class="resolution-name">${resolution.nom}</div>
                            <div class="resolution-description">Méthode de résolution: ${resolution.nom}</div>
                            <div class="card-actions">
                                <button class="btn-edit" onclick="editResolution(${resolution.id}, '${resolution.nom}')">Modifier</button>
                                <button class="btn-delete" onclick="deleteResolution(${resolution.id})">Supprimer</button>
                            </div>
                        </div>
                    `).join('') + '</div>';
            } catch (error) {
                console.error('Erreur:', error);
                document.getElementById('resolutionsContainer').innerHTML = '<div class="error">Erreur lors du chargement des résolutions</div>';
            }
        }

        function showAddForm() {
            document.getElementById('formContainer').style.display = 'block';
            document.getElementById('formTitle').textContent = 'Ajouter une Résolution';
            document.getElementById('resolutionForm').reset();
            document.getElementById('resolutionId').value = '';
        }

        function hideForm() {
            document.getElementById('formContainer').style.display = 'none';
        }

        function editResolution(id, nom) {
            document.getElementById('formContainer').style.display = 'block';
            document.getElementById('formTitle').textContent = 'Modifier une Résolution';
            document.getElementById('resolutionId').value = id;
            document.getElementById('nom').value = nom;
        }

        async function deleteResolution(id) {
            if (confirm('Êtes-vous sûr de vouloir supprimer cette résolution ?')) {
                try {
                    await fetch(`${API_BASE_URL}/resolutions/${id}`, { method: 'DELETE' });
                    loadResolutions();
                } catch (error) {
                    console.error('Erreur:', error);
                    alert('Erreur lors de la suppression');
                }
            }
        }
    </script>
</body>
</html>
