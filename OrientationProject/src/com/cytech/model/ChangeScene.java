package com.cytech.model;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChangeScene {

    /**
     * Change la sc�ne affich�e dans la fen�tre actuelle.
     *
     * @param fxmlFile le chemin du fichier FXML � charger
     * @param event l'�v�nement d�clench� par l'utilisateur (g�n�ralement un clic)
     * @throws IOException si le fichier FXML ne peut pas �tre charg�
     */
    public void changeScene(String fxmlFile, ActionEvent event) throws IOException {
        // Charger le fichier FXML sp�cifi�
        Parent parent = FXMLLoader.load(getClass().getResource(fxmlFile));
        
        // Cr�er une nouvelle sc�ne avec le contenu charg�
        Scene scene = new Scene(parent);
        
        // Obtenir la fen�tre (Stage) � partir de l'�v�nement
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // D�finir la nouvelle sc�ne dans la fen�tre
        window.setScene(scene);
        
        // Afficher la nouvelle sc�ne
        window.show();
    }
}
