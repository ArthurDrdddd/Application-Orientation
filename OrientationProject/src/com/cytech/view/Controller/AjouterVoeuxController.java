package com.cytech.view.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.cytech.collections.VoeuxCollection;
import com.cytech.individu.Voeux;
import com.cytech.model.BoiteDeDialogue;
import com.cytech.model.ChangeScene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AjouterVoeuxController {
		
		// Cr�ez une instance de ChangeScene
		private ChangeScene sceneChanger = new ChangeScene();
		// Cr�er une instance de BoiteDeDialogue
		private BoiteDeDialogue dialogue = new BoiteDeDialogue();
		// Instance de VoeuxCollection pour g�rer les voeux
		private VoeuxCollection voeuxCollection = new VoeuxCollection();
		
		private Voeux voeu = new Voeux();
		@FXML
		private TextField nomTextField;
		@FXML
		private TextField filiereTextField;
		@FXML
		private TextField nbPlaceTextField;
		@FXML
		private TextField descriptionTextField;
		@FXML
		private TextField idTextField;
		
		@FXML
		private void handleValider(ActionEvent event) throws IOException {
			int var=0;
			voeuxCollection.lireJson();
			String nom = nomTextField.getText();
			if(!nom.matches("[a-zA-Z�-�& ]+")) {
				dialogue.Error("Seules les caract�res suivants sont accept�s pour le nom : \n- a-z \n- A-Z \n- caract�res avec accents");
				nomTextField.clear();
				return;
			}
			voeu.setNom(nomTextField.getText().trim());
			
			try {
				voeu.setNbPlace(Integer.parseInt(nbPlaceTextField.getText().trim()));
			}
			catch (NumberFormatException e) {
				dialogue.Error("Seules des chiffres sont attendus pour les champs suivants : \n- Nombre de places\n- ID");
				nbPlaceTextField.clear();
				return;
			}
			
			try {
				voeu.setIdVoeux(Integer.parseInt(idTextField.getText().trim()));
			}
			catch (NumberFormatException e) {
				dialogue.Error("Seules des chiffres sont attendus pour les champs suivants : \n- Nombre de places\n- ID");
				idTextField.clear();
				return;
			}
			
			String[] filieresArray = filiereTextField.getText().trim().split("\\s*,\\s*");
			ArrayList<String> filieresList = new ArrayList<>(Arrays.asList(filieresArray));
			voeu.setFiliereEligible(filieresList);
			voeu.setDescription(descriptionTextField.getText().trim());
			for(Voeux voeuxExistants : voeuxCollection.getCollection()) {
				if(voeuxExistants.getIdVoeux()==voeu.getIdVoeux()) {
					var=1;
					dialogue.Error("Cet ID est d�j� attribu� � une option !");
				}
				else if(voeuxExistants.getNom().equals(voeu.getNom())) {
					var=1;
					dialogue.Error("Une option existe d�j� sous ce nom !");
				}
			}
			if(var==0) {
				voeuxCollection.ajouterVoeux(voeu);
				dialogue.Confirmation("L'option a bien �t� ajout�e � la liste des options !");
				sceneChanger.changeScene("/com/cytech/view/FXML/GererVoeuxRespo.fxml", event);
			}
		}
		@FXML
		private void handleRetourMenu(ActionEvent event) throws IOException {
			sceneChanger.changeScene("/com/cytech/view/FXML/GererVoeuxRespo.fxml", event);
		}
}
