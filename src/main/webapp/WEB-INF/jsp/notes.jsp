<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Notes</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

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
            box-shadow: 0 8px 32px rgba(255, 255, 255, 0.1), 
                        0 4px 16px rgba(0, 0, 0, 0.3),
                        inset 0 1px 0 rgba(255, 255, 255, 0.1);
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
            overflow: hidden;
        }

        .header::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
            animation: shimmer 3s infinite;
        }

        @keyframes shimmer {
            0% { left: -100%; }
            100% { left: 100%; }
        }

        .header h1 {
            font-size: 2.5em;
            font-weight: 300;
            margin-bottom: 10px;
            text-transform: uppercase;
            letter-spacing: 3px;
            position: relative;
            z-index: 1;
        }

        .header p {
            font-size: 1.1em;
            opacity: 0.9;
            position: relative;
            z-index: 1;
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

        .filters {
            display: flex;
            gap: 20px;
            margin-bottom: 30px;
            flex-wrap: wrap;
        }

        .filter-group {
            display: flex;
            flex-direction: column;
            min-width: 200px;
        }

        .filter-group label {
            margin-bottom: 8px;
            font-weight: 500;
            color: #ffffff;
        }

        .filter-group select {
            padding: 10px 15px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.1);
            color: #ffffff;
            font-size: 1em;
            transition: all 0.3s ease;
        }

        .filter-group select:focus {
            outline: none;
            border-color: #4a9eff;
            box-shadow: 0 0 15px rgba(74, 158, 255, 0.3);
        }

        .filter-group select option {
            background: #2a2a2a;
            color: #ffffff;
        }

        .notes-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .notes-table th,
        .notes-table td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }

        .notes-table th {
            background: rgba(74, 158, 255, 0.1);
            color: #4a9eff;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
            font-size: 0.9em;
        }

        .notes-table tr:hover {
            background: rgba(255, 255, 255, 0.05);
        }

        .note-value {
            font-weight: bold;
            color: #4a9eff;
            font-size: 1.1em;
        }

        .actions {
            display: flex;
            gap: 10px;
        }

        .btn-edit, .btn-delete {
            padding: 6px 12px;
            border: none;
            border-radius: 15px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 0.9em;
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

        .add-btn {
            background: linear-gradient(135deg, #4a9eff 0%, #0066cc 100%);
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: 600;
            margin-bottom: 20px;
        }

        .add-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(74, 158, 255, 0.4);
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

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: rgba(255, 255, 255, 0.6);
        }

        .empty-state h3 {
            font-size: 1.5em;
            margin-bottom: 10px;
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
            
            .filters {
                flex-direction: column;
            }
            
            .notes-table {
                font-size: 0.9em;
            }
            
            .notes-table th,
            .notes-table td {
                padding: 10px;
            }
            
            .actions {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <a href="index.jsp" class="back-btn">← Retour</a>
            <h1>Liste des Notes</h1>
            <p>Gestion des notes des candidats</p>
        </div>

        <div class="content">
            <div class="filters">
                <div class="filter-group">
                    <label for="candidatFilter">Filtrer par candidat:</label>
                    <select id="candidatFilter">
                        <option value="">Tous les candidats</option>
                    </select>
                </div>
                
                <div class="filter-group">
                    <label for="matiereFilter">Filtrer par matière:</label>
                    <select id="matiereFilter">
                        <option value="">Toutes les matières</option>
                    </select>
                </div>
                
                <div class="filter-group">
                    <label for="correcteurFilter">Filtrer par correcteur:</label>
                    <select id="correcteurFilter">
                        <option value="">Tous les correcteurs</option>
                    </select>
                </div>
            </div>

            <button class="add-btn" onclick="showAddForm()">+ Ajouter une Note</button>

            <!-- Formulaire d'ajout/modification -->
            <div id="formContainer" class="form-container">
                <h3 id="formTitle">Ajouter une Note</h3>
                <form id="noteForm">
                    <input type="hidden" id="noteId" name="id">
                    
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="candidat">Candidat:</label>
                            <select id="candidat" name="candidat" required>
                                <option value="">Sélectionner un candidat</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="matiere">Matière:</label>
                            <select id="matiere" name="matiere" required>
                                <option value="">Sélectionner une matière</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="correcteur">Correcteur:</label>
                            <select id="correcteur" name="correcteur" required>
                                <option value="">Sélectionner un correcteur</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="note">Note:</label>
                            <input type="number" id="note" name="note" step="0.01" min="0" max="20" required>
                        </div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn-save">Enregistrer</button>
                        <button type="button" class="btn-cancel" onclick="hideForm()">Annuler</button>
                    </div>
                </form>
            </div>
            
            <div id="notesContainer">
                <div class="loading">Chargement des notes...</div>
            </div>
        </div>
    </div>

    <script>
        const API_BASE_URL = 'http://localhost:8084/api';

        document.addEventListener('DOMContentLoaded', () => {
            loadNotes();
            loadCandidats();
            loadMatieres();
            loadCorrecteurs();
        });

        // Écouteurs d'événements pour les filtres
        document.getElementById('candidatFilter').addEventListener('change', loadNotes);
        document.getElementById('matiereFilter').addEventListener('change', loadNotes);
        document.getElementById('correcteurFilter').addEventListener('change', loadNotes);

        document.getElementById('noteForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const noteId = document.getElementById('noteId').value;
            const data = {
                candidat: { id: parseInt(document.getElementById('candidat').value) },
                matiere: { id: parseInt(document.getElementById('matiere').value) },
                correcteur: { id: parseInt(document.getElementById('correcteur').value) },
                note: parseFloat(document.getElementById('note').value)
            };

            try {
                console.log('Données envoyées:', data);
                
                let response;
                if (noteId) {
                    response = await fetch(`${API_BASE_URL}/notes/${noteId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(data)
                    });
                } else {
                    response = await fetch(`${API_BASE_URL}/notes`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(data)
                    });
                }

                console.log('Response status:', response.status);
                
                if (response.ok) {
                    alert('Note sauvegardée avec succès!');
                    hideForm();
                    loadNotes();
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

        async function loadNotes() {
            try {
                const candidatId = document.getElementById('candidatFilter').value;
                const matiereId = document.getElementById('matiereFilter').value;
                const correcteurId = document.getElementById('correcteurFilter').value;
                
                let url = `${API_BASE_URL}/notes`;
                const params = new URLSearchParams();
                
                if (candidatId) params.append('candidatId', candidatId);
                if (matiereId) params.append('matiereId', matiereId);
                if (correcteurId) params.append('correcteurId', correcteurId);
                
                if (params.toString()) {
                    url += '?' + params.toString();
                }
                
                const response = await fetch(url);
                const notes = await response.json();
                
                const container = document.getElementById('notesContainer');
                
                if (notes.length === 0) {
                    container.innerHTML = `
                        <div class="empty-state">
                            <h3>Aucune note trouvée</h3>
                            <p>Aucune note ne correspond aux critères de filtrage.</p>
                        </div>
                    `;
                    return;
                }
                
                container.innerHTML = `
                    <table class="notes-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Candidat</th>
                                <th>Matière</th>
                                <th>Correcteur</th>
                                <th>Note</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${notes.map(note => `
                                <tr>
                                    <td>${note.id}</td>
                                    <td>${note.candidat.nom}</td>
                                    <td>${note.matiere.nom}</td>
                                    <td>${note.correcteur.nom}</td>
                                    <td class="note-value">${note.note}</td>
                                    <td>
                                        <div class="actions">
                                            <button class="btn-edit" onclick="editNote(${note.id})">Modifier</button>
                                            <button class="btn-delete" onclick="deleteNote(${note.id})">Supprimer</button>
                                        </div>
                                    </td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                `;
            } catch (error) {
                console.error('Erreur:', error);
                document.getElementById('notesContainer').innerHTML = '<div class="error">Erreur lors du chargement des notes</div>';
            }
        }

        async function loadCandidats() {
            try {
                const response = await fetch(`${API_BASE_URL}/candidats`);
                const candidats = await response.json();
                
                // Remplir le filtre
                const filterSelect = document.getElementById('candidatFilter');
                filterSelect.innerHTML = '<option value="">Tous les candidats</option>';
                
                // Remplir le formulaire
                const formSelect = document.getElementById('candidat');
                formSelect.innerHTML = '<option value="">Sélectionner un candidat</option>';
                
                candidats.forEach(candidat => {
                    const option1 = document.createElement('option');
                    option1.value = candidat.id;
                    option1.textContent = candidat.nom;
                    filterSelect.appendChild(option1);
                    
                    const option2 = document.createElement('option');
                    option2.value = candidat.id;
                    option2.textContent = candidat.nom;
                    formSelect.appendChild(option2);
                });
            } catch (error) {
                console.error('Erreur:', error);
            }
        }

        async function loadMatieres() {
            try {
                const response = await fetch(`${API_BASE_URL}/matieres`);
                const matieres = await response.json();
                
                // Remplir le filtre
                const filterSelect = document.getElementById('matiereFilter');
                filterSelect.innerHTML = '<option value="">Toutes les matières</option>';
                
                // Remplir le formulaire
                const formSelect = document.getElementById('matiere');
                formSelect.innerHTML = '<option value="">Sélectionner une matière</option>';
                
                matieres.forEach(matiere => {
                    const option1 = document.createElement('option');
                    option1.value = matiere.id;
                    option1.textContent = matiere.nom;
                    filterSelect.appendChild(option1);
                    
                    const option2 = document.createElement('option');
                    option2.value = matiere.id;
                    option2.textContent = matiere.nom;
                    formSelect.appendChild(option2);
                });
            } catch (error) {
                console.error('Erreur:', error);
            }
        }

        async function loadCorrecteurs() {
            try {
                const response = await fetch(`${API_BASE_URL}/correcteurs`);
                const correcteurs = await response.json();
                
                // Remplir le filtre
                const filterSelect = document.getElementById('correcteurFilter');
                filterSelect.innerHTML = '<option value="">Tous les correcteurs</option>';
                
                // Remplir le formulaire
                const formSelect = document.getElementById('correcteur');
                formSelect.innerHTML = '<option value="">Sélectionner un correcteur</option>';
                
                correcteurs.forEach(correcteur => {
                    const option1 = document.createElement('option');
                    option1.value = correcteur.id;
                    option1.textContent = correcteur.nom;
                    filterSelect.appendChild(option1);
                    
                    const option2 = document.createElement('option');
                    option2.value = correcteur.id;
                    option2.textContent = correcteur.nom;
                    formSelect.appendChild(option2);
                });
            } catch (error) {
                console.error('Erreur:', error);
            }
        }

        function showAddForm() {
            document.getElementById('formContainer').style.display = 'block';
            document.getElementById('formTitle').textContent = 'Ajouter une Note';
            document.getElementById('noteForm').reset();
            document.getElementById('noteId').value = '';
        }

        function hideForm() {
            document.getElementById('formContainer').style.display = 'none';
        }

        async function editNote(id) {
            try {
                const response = await fetch(`${API_BASE_URL}/notes/${id}`);
                const note = await response.json();
                
                document.getElementById('formContainer').style.display = 'block';
                document.getElementById('formTitle').textContent = 'Modifier une Note';
                document.getElementById('noteId').value = note.id;
                document.getElementById('candidat').value = note.candidat.id;
                document.getElementById('matiere').value = note.matiere.id;
                document.getElementById('correcteur').value = note.correcteur.id;
                document.getElementById('note').value = note.note;
            } catch (error) {
                console.error('Erreur:', error);
                alert('Erreur lors du chargement de la note');
            }
        }

        async function deleteNote(id) {
            if (confirm('Êtes-vous sûr de vouloir supprimer cette note ?')) {
                try {
                    await fetch(`${API_BASE_URL}/notes/${id}`, { method: 'DELETE' });
                    loadNotes();
                } catch (error) {
                    console.error('Erreur:', error);
                    alert('Erreur lors de la suppression');
                }
            }
        }
    </script>
</body>
</html>
