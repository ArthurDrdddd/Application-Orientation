package com.cytech.view.Controller;

import java.io.IOException;
import com.cytech.model.ChangeScene;
import com.cytech.model.EtudiantSession;
import com.cytech.individu.Etudiant;
import com.cytech.individu.Voeux;
import com.cytech.model.BoiteDeDialogue;
import com.cytech.collections.GestionAccesVoeux;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AccueilEtudiantController {

    // Instances de ChangeScene et BoiteDeDialogue pour changer les sc�nes et afficher les bo�tes de dialogue
    private ChangeScene sceneChanger = new ChangeScene(); 
    private BoiteDeDialogue boiteDeDialogue = new BoiteDeDialogue();
    private GestionAccesVoeux gestionAccesVoeux = new GestionAccesVoeux(); // Instance pour g�rer l'acc�s aux voeux

    @FXML
    private Label labelBienvenue;  // Label pour afficher le message de bienvenue

    @FXML
    private Button ficheVoeuxButton;  // Bouton pour acc�der � la fiche de voeux

    // M�thode appel�e automatiquement apr�s le chargement du fichier FXML
    @FXML
    public void initialize() {
        Etudiant etudiant = EtudiantSession.getInstance().getEtudiantConnecte();
        if (etudiant != null) {
            labelBienvenue.setText("Bienvenue, " + etudiant.getPrenom() + " " + etudiant.getNom());
        } else {
            labelBienvenue.setText("Bienvenue");
        }
        ficheVoeuxButton.setDisable(!gestionAccesVoeux.getBoolValue()); // D�sactiver le bouton si l'acc�s est d�sactiv�
    }

    // M�thode pour g�rer la d�connexion de l'�tudiant
    @FXML
    private void handleSeDeconnecter(ActionEvent event) throws IOException {
        EtudiantSession.getInstance().clearSession(); // Nettoyer la session
        sceneChanger.changeScene("/com/cytech/view/FXML/Accueil.fxml", event);
    }

    // M�thode pour acc�der aux donn�es de l'�tudiant
    @FXML
    private void handleDonnees(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/DonneesEtudiant.fxml", event);
    }

    // M�thode pour acc�der aux voeux disponibles
    @FXML
    private void handleVoeuxDispo(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/VoeuxDispoEtudiant.fxml", event);
    }

    // M�thode pour acc�der � la fiche de voeux de l'�tudiant
    @FXML
    private void handleFicheVoeux(ActionEvent event) throws IOException {
        Etudiant etudiant = EtudiantSession.getInstance().getEtudiantConnecte();
        if (etudiant != null) {
            // Initialiser la bo�te de dialogue avec la sc�ne principale
            Stage primaryStage = (Stage) ficheVoeuxButton.getScene().getWindow();
            boiteDeDialogue.setDialogStage(primaryStage);

            if (etudiant.getListeVoeux() != null && !etudiant.getListeVoeux().isEmpty()) {
                // Afficher une alerte si l'�tudiant a d�j� confirm� ses voeux
                boiteDeDialogue.Information("Vous avez d�j� envoy� votre liste de voeux : \n" + getVoeuxListAsString(etudiant));
            } else {
                // Changer la sc�ne vers la fiche de voeux si l'�tudiant n'a pas encore confirm� ses voeux
                sceneChanger.changeScene("/com/cytech/view/FXML/FicheVoeuxEtudiant.fxml", event);
            }
        }
    }

    // M�thode pour obtenir la liste des voeux de l'�tudiant sous forme de cha�ne de caract�res
    private String getVoeuxListAsString(Etudiant etudiant) {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Voeux voeux : etudiant.getListeVoeux()) {
            sb.append(index).append(". ").append(voeux.getNom()).append("\n");
            index++;
        }
        return sb.toString();
    }

    // M�thode pour acc�der aux r�sultats des voeux de l'�tudiant
    @FXML
    private void handleResultat(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/ResultatEtudiant.fxml", event);
    }
}
