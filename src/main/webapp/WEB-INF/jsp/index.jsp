<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Note</title>
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
            max-width: 800px;
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

        .content {
            padding: 40px 30px;
        }

        .form-section {
            margin-bottom: 40px;
        }

        .form-section h2 {
            font-size: 1.5em;
            margin-bottom: 20px;
            color: #ffffff;
            border-bottom: 2px solid rgba(255, 255, 255, 0.3);
            padding-bottom: 10px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
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

        .calculate-btn {
            background: linear-gradient(135deg, #4a9eff 0%, #0066cc 100%);
            color: white;
            border: none;
            padding: 15px 40px;
            font-size: 1.1em;
            border-radius: 50px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-transform: uppercase;
            letter-spacing: 2px;
            font-weight: 600;
            box-shadow: 0 4px 15px rgba(74, 158, 255, 0.3);
        }

        .calculate-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(74, 158, 255, 0.4);
        }

        .calculate-btn:active {
            transform: translateY(0);
        }

        .result {
            background: rgba(255, 255, 255, 0.1);
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 15px;
            padding: 30px;
            margin-top: 20px;
            backdrop-filter: blur(10px);
        }

        .result h3 {
            margin-bottom: 15px;
            color: #4a9eff;
        }

        .result p {
            margin-bottom: 10px;
            font-size: 1.1em;
        }

        .result .final-note {
            font-size: 2em;
            font-weight: bold;
            color: #4a9eff;
            margin-top: 20px;
        }

        .crud-section {
            margin-top: 40px;
            padding-top: 30px;
            border-top: 2px solid rgba(255, 255, 255, 0.3);
        }

        .crud-buttons {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            margin-bottom: 30px;
        }

        .crud-btn {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0.05) 100%);
            color: white;
            border: 2px solid rgba(255, 255, 255, 0.3);
            padding: 12px 25px;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            font-weight: 500;
        }

        .crud-btn:hover {
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0.1) 100%);
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(255, 255, 255, 0.2);
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
            .form-grid {
                grid-template-columns: 1fr;
            }
            
            .crud-buttons {
                flex-direction: column;
            }
            
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
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Note</h1>
            <p>Système de calcul de notes finales</p>
        </div>

        <div class="content">
            <!-- Section de calcul de note -->
            <div class="form-section">
                <h2>Calculer la note finale</h2>
                
                <form id="noteForm">
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
                    </div>
                    
                    <button type="submit" class="calculate-btn">Calculer</button>
                </form>
            </div>

            <!-- Section d'affichage des résultats -->
            <div id="resultSection" style="display: none;">
                <div class="result">
                    <h3>Résultat du calcul</h3>
                    <div id="resultContent"></div>
                </div>
            </div>

            <!-- Section CRUD -->
            <div class="crud-section">
                <h2>Gestion des données</h2>
                
                <div class="crud-buttons">
                    <a href="parametres.jsp" class="crud-btn">Gérer les Paramètres</a>
                    <a href="operateurs.jsp" class="crud-btn">Gérer les Opérateurs</a>
                    <a href="resolutions.jsp" class="crud-btn">Gérer les Résolutions</a>
                </div>

                <div id="crudResult" class="result" style="display: none;">
                    <!-- Données CRUD seront affichées ici -->
                </div>
            </div>
        </div>
    </div>

    <script>
        const API_BASE_URL = 'http://localhost:8084/api';

        // Charger les données au démarrage
        document.addEventListener('DOMContentLoaded', async () => {
            await loadCandidatsOptions();
            await loadMatieresOptions();
        });

        async function loadCandidatsOptions() {
            try {
                const response = await fetch(`${API_BASE_URL}/candidats`);
                const candidats = await response.json();
                
                const select = document.getElementById('candidat');
                select.innerHTML = '<option value="">Sélectionner un candidat</option>';
                
                candidats.forEach(candidat => {
                    const option = document.createElement('option');
                    option.value = candidat.id;
                    option.textContent = candidat.nom;
                    select.appendChild(option);
                });
            } catch (error) {
                console.error('Erreur lors du chargement des candidats:', error);
            }
        }

        async function loadMatieresOptions() {
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
                console.error('Erreur lors du chargement des matières:', error);
            }
        }

        // Gérer la soumission du formulaire
        document.getElementById('noteForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const candidatId = document.getElementById('candidat').value;
            const matiereId = document.getElementById('matiere').value;
            
            if (!candidatId || !matiereId) {
                alert('Veuillez sélectionner un candidat et une matière');
                return;
            }
            
            try {
                const response = await fetch(`${API_BASE_URL}/notes/calculer?candidatId=${candidatId}&matiereId=${matiereId}`);
                
                if (!response.ok) {
                    throw new Error('Erreur lors du calcul de la note');
                }
                
                const result = await response.json();
                displayResult(result);
                
            } catch (error) {
                console.error('Erreur:', error);
                alert('Erreur lors du calcul de la note: ' + error.message);
            }
        });

        function displayResult(result) {
            const resultSection = document.getElementById('resultSection');
            const resultContent = document.getElementById('resultContent');
            
            resultContent.innerHTML = `
                <p><strong>Candidat:</strong> ${result.candidatNom}</p>
                <p><strong>Matière:</strong> ${result.matiereNom}</p>
                <p><strong>Notes:</strong> ${result.notes.join(', ')}</p>
                <p><strong>Méthode de calcul:</strong> ${result.methodeCalcul}</p>
                <p><strong>Condition appliquée:</strong> ${result.conditionAppliquee}</p>
                <p><strong>Résolution:</strong> ${result.resolution}</p>
                <div class="final-note">Note finale: ${result.noteFinale}</div>
            `;
            
            resultSection.style.display = 'block';
            resultSection.scrollIntoView({ behavior: 'smooth' });
        }
    </script>
</body>
</html>
