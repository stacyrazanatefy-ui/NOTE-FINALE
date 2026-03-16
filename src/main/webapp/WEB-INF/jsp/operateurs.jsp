<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Opérateurs - Système de Notes</title>
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

        .operateur-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .operateur-card {
            background: rgba(255, 255, 255, 0.05);
            border: 2px solid rgba(255, 255, 255, 0.2);
            border-radius: 15px;
            padding: 20px;
            transition: all 0.3s ease;
        }

        .operateur-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(74, 158, 255, 0.2);
            border-color: #4a9eff;
        }

        .operateur-id {
            background: #4a9eff;
            color: white;
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.8em;
            display: inline-block;
            margin-bottom: 10px;
        }

        .operateur-symbol {
            font-size: 2em;
            font-weight: bold;
            color: #4a9eff;
            margin-bottom: 10px;
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
            
            .operateur-grid {
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
            <h1>Gestion des Opérateurs</h1>
            <p>Configuration des opérateurs de comparaison</p>
        </div>

        <div class="content">
            <div class="section-header">
                <h2>Liste des Opérateurs</h2>
                <button class="add-btn" onclick="showAddForm()">+ Ajouter un Opérateur</button>
            </div>

            <!-- Formulaire d'ajout/modification -->
            <div id="formContainer" class="form-container">
                <h3 id="formTitle">Ajouter un Opérateur</h3>
                <form id="operateurForm">
                    <input type="hidden" id="operateurId" name="id">
                    
                    <div class="form-group">
                        <label for="operateur">Symbole de l'opérateur:</label>
                        <input type="text" id="operateur" name="operateur" placeholder="Ex: >, <, >=, <=" required>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn-save">Enregistrer</button>
                        <button type="button" class="btn-cancel" onclick="hideForm()">Annuler</button>
                    </div>
                </form>
            </div>
            
            <div id="operateursContainer">
                <div class="loading">Chargement des opérateurs...</div>
            </div>
        </div>
    </div>

    <script>
        const API_BASE_URL = 'http://localhost:8084/api';

        document.addEventListener('DOMContentLoaded', () => {
            loadOperateurs();
        });

        document.getElementById('operateurForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const operateurId = document.getElementById('operateurId').value;
            const operateurSymbol = document.getElementById('operateur').value.trim();
            
            const data = {
                operateur: operateurSymbol
            };

            try {
                console.log('Données envoyées:', data);
                
                let response;
                if (operateurId) {
                    response = await fetch(`${API_BASE_URL}/operateurs/${operateurId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(data)
                    });
                } else {
                    response = await fetch(`${API_BASE_URL}/operateurs`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(data)
                    });
                }

                console.log('Response status:', response.status);
                
                if (response.ok) {
                    alert('Opérateur sauvegardé avec succès!');
                    hideForm();
                    loadOperateurs();
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

        async function loadOperateurs() {
            try {
                const response = await fetch(`${API_BASE_URL}/operateurs`);
                const operateurs = await response.json();
                
                const container = document.getElementById('operateursContainer');
                container.innerHTML = '<div class="operateur-grid">' + 
                    operateurs.map(operateur => `
                        <div class="operateur-card">
                            <div class="operateur-id">#${operateur.id}</div>
                            <div class="operateur-symbol">${operateur.operateur}</div>
                            <div class="card-actions">
                                <button class="btn-edit" onclick="editOperateur(${operateur.id}, '${operateur.operateur}')">Modifier</button>
                                <button class="btn-delete" onclick="deleteOperateur(${operateur.id})">Supprimer</button>
                            </div>
                        </div>
                    `).join('') + '</div>';
            } catch (error) {
                console.error('Erreur:', error);
                document.getElementById('operateursContainer').innerHTML = '<div class="error">Erreur lors du chargement des opérateurs</div>';
            }
        }

        function showAddForm() {
            document.getElementById('formContainer').style.display = 'block';
            document.getElementById('formTitle').textContent = 'Ajouter un Opérateur';
            document.getElementById('operateurForm').reset();
            document.getElementById('operateurId').value = '';
        }

        function hideForm() {
            document.getElementById('formContainer').style.display = 'none';
        }

        function editOperateur(id, operateur) {
            document.getElementById('formContainer').style.display = 'block';
            document.getElementById('formTitle').textContent = 'Modifier un Opérateur';
            document.getElementById('operateurId').value = id;
            document.getElementById('operateur').value = operateur;
        }

        async function deleteOperateur(id) {
            if (confirm('Êtes-vous sûr de vouloir supprimer cet opérateur ?')) {
                try {
                    await fetch(`${API_BASE_URL}/operateurs/${id}`, { method: 'DELETE' });
                    loadOperateurs();
                } catch (error) {
                    console.error('Erreur:', error);
                    alert('Erreur lors de la suppression');
                }
            }
        }
    </script>
</body>
</html>
