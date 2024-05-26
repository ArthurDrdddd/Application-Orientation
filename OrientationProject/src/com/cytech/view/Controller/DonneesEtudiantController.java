package com.cytech.view.Controller;

import java.io.IOException;
import com.cytech.model.ChangeScene;
import com.cytech.model.EtudiantSession;
import com.cytech.individu.Etudiant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DonneesEtudiantController {

    // Instance de ChangeScene pour changer de sc�ne
    private ChangeScene sceneChanger = new ChangeScene();

    @FXML
    private Label labelNom;    // Label pour afficher le nom de l'�tudiant

    @FXML
    private Label labelPrenom; // Label pour afficher le pr�nom de l'�tudiant

    @FXML
    private Label labelEmail;  // Label pour afficher l'email de l'�tudiant

    @FXML
    private Label labelNumEtu; // Label pour afficher le num�ro �tudiant

    @FXML
    private Label labelFiliere;// Label pour afficher la fili�re de l'�tudiant

    @FXML
    private Label labelMoyGen; // Label pour afficher la moyenne g�n�rale de l'�tudiant

    // M�thode appel�e automatiquement apr�s le chargement du fichier FXML
    @FXML
    public void initialize() {
        // R�cup�rer l'�tudiant actuellement connect�
        Etudiant etudiant = EtudiantSession.getInstance().getEtudiantConnecte();
        
        if (etudiant != null) {
            // Mettre � jour les labels avec les informations de l'�tudiant
            labelNom.setText(etudiant.getNom());
            labelPrenom.setText(etudiant.getPrenom());
            labelEmail.setText(etudiant.getMail());
            labelNumEtu.setText(String.valueOf(etudiant.getNumEtu()));
            labelFiliere.setText(etudiant.getFiliere());
            
            BigDecimal MoyGen = new BigDecimal(etudiant.getMoyGen()).setScale(2, RoundingMode.HALF_UP);
            
            labelMoyGen.setText(String.valueOf(MoyGen));
        }
    }

    // M�thode pour g�rer l'action de retour � l'�cran d'accueil de l'�tudiant
    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/AccueilEtudiant.fxml", event);
    }
}
