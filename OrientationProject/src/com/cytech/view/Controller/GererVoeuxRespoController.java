package com.cytech.view.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cytech.collections.VoeuxCollection;
import com.cytech.individu.Voeux;
import com.cytech.model.ChangeScene;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GererVoeuxRespoController {

	// Cr�ez une instance de ChangeScene
	private ChangeScene sceneChanger = new ChangeScene();
	// Instance de VoeuxCollection pour g�rer les voeux
	private VoeuxCollection voeuxCollection = new VoeuxCollection();

	@FXML
	private TableView<Voeux> tableVoeux;
	@FXML
	private TableColumn<Voeux, String> colNom;
	@FXML
	private TableColumn<Voeux, String> colFiliere;
	@FXML
	private TableColumn<Voeux, Double> colPlace;

	@FXML
	private Button retirerVoeuButton;
	@FXML
	private Button modifierVoeuButton;

	// Propri�t� observable pour garder une trace de la s�lection dans le tableau
	private final SimpleBooleanProperty isVoeuSelected = new SimpleBooleanProperty(false);

	private ObservableList<Voeux> loadVoeux() throws IOException {
		// Charger les voeux � partir du fichier JSON en utilisant VoeuxCollection
		voeuxCollection.lireJson();
		// R�cup�rer la liste de voeux depuis VoeuxCollection et la convertir en
		// ObservableList
		List<Voeux> voeuxList = voeuxCollection.getCollection();
		return FXCollections.observableArrayList(voeuxList);
	}

	@FXML
	private void initialize() {
		colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
		colFiliere.setCellValueFactory(cellData -> {
			// Utilisation d'une propri�t� observable pour afficher la liste des fili�res
			// sous forme de cha�ne
			ArrayList<String> filieres = cellData.getValue().getFiliereEligible();
			String filieresString = String.join(", ", filieres);
			return new SimpleStringProperty(filieresString);
		});
		colPlace.setCellValueFactory(new PropertyValueFactory<>("nbPlace"));

		// Charger les voeux depuis le fichier JSON
		try {
			//tableVoeux.setItems(loadVoeux());
			tableVoeux.getItems().clear();
			tableVoeux.getItems().addAll(loadVoeux());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Lier la visibilit� des boutons � la propri�t� isVoeuSelected
		retirerVoeuButton.disableProperty().bind(isVoeuSelected.not());
		modifierVoeuButton.disableProperty().bind(isVoeuSelected.not());

		// �couteur pour d�tecter les changements de s�lection dans le tableau
		tableVoeux.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			isVoeuSelected.set(newValue != null); // Met � jour la propri�t� isVoeuSelected
		});

	}

	@FXML
	private void handleRetirerVoeu(ActionEvent event) throws IOException {
		Voeux selectedVoeux = tableVoeux.getSelectionModel().getSelectedItem();
		if (selectedVoeux != null) {
			voeuxCollection.getCollection().remove(selectedVoeux);
			// Supprime visuellement dans le TableView
			tableVoeux.getItems().remove(selectedVoeux);
			// On sauvegarde la collection pour mettre � jour le fichier JSON.
			voeuxCollection.sauvegarderJson();
		}
	}

	@FXML
	private void handleModifierVoeu(ActionEvent event) throws IOException {
		Voeux selectedVoeux = tableVoeux.getSelectionModel().getSelectedItem();
		if (selectedVoeux != null) {
			// Charger la page de modification
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cytech/view/FXML/ModifierVoeux.fxml"));
			Parent root = loader.load();

			// Obtenir le contr�leur de la vue de modification
			ModifierVoeuxController modifierController = loader.getController();

			// Passer le voeu s�lectionn� au contr�leur de modification
			modifierController.initialisation(selectedVoeux);

			// Configurer une nouvelle sc�ne pour la page de modification
			Stage modifierStage = new Stage();
			modifierStage.initModality(Modality.WINDOW_MODAL);
			modifierStage.initOwner(((Node) event.getSource()).getScene().getWindow());
			modifierStage.setScene(new Scene(root));
			modifierStage.showAndWait();
			// Mettre � jour le tab de la page principale apr�s la modification (recharger les voeux)
			tableVoeux.getItems().clear();
			tableVoeux.getItems().addAll(loadVoeux());
		}
	}
	
	@FXML
	private void handleAjouterVoeu(ActionEvent event) throws IOException {
		sceneChanger.changeScene("/com/cytech/view/FXML/AjouterVoeux.fxml", event);
	}

	@FXML
	private void handleRetourMenu(ActionEvent event) throws IOException {
		sceneChanger.changeScene("/com/cytech/view/FXML/AccueilRespo.fxml", event);
	}
}
