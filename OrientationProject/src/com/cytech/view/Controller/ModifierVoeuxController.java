package com.cytech.view.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.cytech.collections.VoeuxCollection;
import com.cytech.individu.Voeux;
import com.cytech.model.BoiteDeDialogue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierVoeuxController {

	// Cr�er une instance de BoiteDeDialogue
	private BoiteDeDialogue dialogue = new BoiteDeDialogue();
	// Instance de VoeuxCollection pour g�rer les voeux
	private VoeuxCollection voeuxCollection = new VoeuxCollection();

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

	private Voeux voeu;
	private int id;

	public void initialisation(Voeux voeu) {
		this.voeu = voeu;
		// Remplir les champs avec les donn�es du voeu s�lectionn�
		nomTextField.setText(voeu.getNom());
		String filieres = String.join(", ", voeu.getFiliereEligible());
		filiereTextField.setText(filieres);
		nbPlaceTextField.setText(String.valueOf(voeu.getNbPlace()));
		descriptionTextField.setText(voeu.getDescription());
		idTextField.setText(String.valueOf(voeu.getIdVoeux()));
		id=voeu.getIdVoeux();
	}

	@FXML
	private void handleEnregistrerModification(ActionEvent event) throws IOException {
		int var=1;
		int var2=1;
		voeuxCollection.lireJson();
		String nom = nomTextField.getText();
		if(!nom.matches("[a-zA-Z�-�& ]+")) {
			dialogue.Error("Seules les caract�res suivants sont accept�s pour le nom : \n- a-z \n- A-Z \n- caract�res avec accents");
			nomTextField.clear();
			return;
		}
		
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
		// Mettre � jour les donn�es du voeu avec les nouvelles valeurs des champs
		String[] filieresArray = filiereTextField.getText().trim().split("\\s*,\\s*");
		ArrayList<String> filieresList = new ArrayList<>(Arrays.asList(filieresArray));
		voeu.setFiliereEligible(filieresList);
		voeu.setDescription(descriptionTextField.getText().trim());
		if(voeu.getIdVoeux()==id) {
			var2=2;
		}
		for(Voeux voeuxExistants : voeuxCollection.getCollection()) {
			if(voeuxExistants.getIdVoeux()==voeu.getIdVoeux()&&var2!=2) {
				var=0;
				dialogue.Error("Cet ID est d�j� attribu� � une option !");
			}
		}
		if(var==1) {
			voeuxCollection.mettreAJour(voeu);
			voeu.setNom(nomTextField.getText().trim());
			voeuxCollection.mettreAJour(voeu);
			dialogue.Confirmation("Les modifications ont bien �t� prises en compte !");
			// Fermer la fen�tre de modification
			((Stage) nomTextField.getScene().getWindow()).close();
		}
	}

	@FXML
	private void handleRetourMenu(ActionEvent event) throws IOException {
		// Fermer la fen�tre de modification
		((Stage) nomTextField.getScene().getWindow()).close();
	}
}
