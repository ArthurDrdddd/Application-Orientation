package com.cytech.view.Controller;

import java.io.IOException;

import com.cytech.model.ChangeScene;
import com.cytech.model.EtudiantSession;
import com.cytech.individu.Etudiant;
import com.cytech.individu.Voeux;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ResultatEtudiantController {

    private ChangeScene sceneChanger = new ChangeScene(); // Instance de ChangeScene pour changer de sc�ne

    @FXML
    private Label labelResultat; // Label pour afficher le r�sultat

    // M�thode appel�e automatiquement apr�s le chargement du fichier FXML
    @FXML
    public void initialize() {
        Etudiant etudiant = EtudiantSession.getInstance().getEtudiantConnecte();
        if (etudiant != null) {
            Voeux resultatVoeux = etudiant.getResultat();
            if (resultatVoeux != null) {
                labelResultat.setText(resultatVoeux.getNom());
            } else {
                labelResultat.setText("Vous n'avez pas encore �t� affect� � un v�u.");
            }
        } else {
            labelResultat.setText("Aucun �tudiant connect�.");
        }
    }

    // M�thode pour g�rer l'action du bouton retour
    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/AccueilEtudiant.fxml", event);
    }
}
