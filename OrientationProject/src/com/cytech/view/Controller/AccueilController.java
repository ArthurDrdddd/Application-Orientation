package com.cytech.view.Controller;

import java.io.IOException;
import com.cytech.model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AccueilController {

    // Instance de ChangeScene pour g�rer le changement de sc�nes
    private ChangeScene sceneChanger = new ChangeScene();

    /**
     * M�thode appel�e lorsque l'utilisateur clique sur le bouton pour les �tudiants.
     * Change la sc�ne pour afficher la page de connexion des �tudiants.
     * 
     * @param event l'�v�nement d�clench� par le clic sur le bouton
     * @throws IOException si le fichier FXML ne peut pas �tre charg�
     */
    @FXML
    private void handleEtudiant(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/ConnexionEtudiant.fxml", event);
    }

    /**
     * M�thode appel�e lorsque l'utilisateur clique sur le bouton pour les responsables.
     * Change la sc�ne pour afficher la page de connexion des responsables.
     * 
     * @param event l'�v�nement d�clench� par le clic sur le bouton
     * @throws IOException si le fichier FXML ne peut pas �tre charg�
     */
    @FXML
    private void handleRespo(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/ConnexionRespo.fxml", event);
    }
}
