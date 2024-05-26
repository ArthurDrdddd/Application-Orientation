package com.cytech.view.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cytech.collections.EtudiantCollection;
import com.cytech.collections.VoeuxCollection;
import com.cytech.individu.Etudiant;
import com.cytech.individu.Voeux;
import com.cytech.model.BoiteDeDialogue;
import com.cytech.model.ChangeScene;
import com.cytech.model.EnvoieEmail;
import com.cytech.model.EtudiantSession;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

import javax.mail.MessagingException;

public class FicheVoeuxEtudiantController {

    // Instance pour changer de sc�ne
    private ChangeScene sceneChanger = new ChangeScene();
    
    // Instance pour afficher des bo�tes de dialogue
    private BoiteDeDialogue dialogue = new BoiteDeDialogue();

    // ListView pour afficher les voeux disponibles
    @FXML
    private ListView<Voeux> voeuxDisponiblesListView;

    // ListView pour afficher les voeux class�s
    @FXML
    private ListView<Voeux> classementVoeuxListView;

    // Bouton pour confirmer les voeux
    @FXML
    private Button handleConfirmer;

    // Listes observables pour les voeux disponibles et class�s
    private ObservableList<Voeux> voeuxDisponibles;
    private ObservableList<Voeux> classementVoeux;
    
    // Propri�t� pour v�rifier si tous les voeux sont class�s
    private BooleanProperty allVoeuxClassified;
    
    // M�thode appel�e automatiquement apr�s le chargement du fichier FXML
    @FXML
    private void initialize() {
        // Lire les voeux depuis le fichier JSON
        VoeuxCollection voeuxCollection = new VoeuxCollection();
        voeuxCollection.lireJson();

        // R�cup�rer l'�tudiant connect�
        Etudiant etudiantConnecte = EtudiantSession.getInstance().getEtudiantConnecte();
        
        // Filtrer les voeux en fonction de la fili�re de l'�tudiant connect�
        List<Voeux> voeuxFiltres = voeuxCollection.getCollection().stream()
            .filter(voeux -> voeux.getFiliereEligible().contains(etudiantConnecte.getFiliere()))
            .collect(Collectors.toList());

        // Initialiser les listes observables
        voeuxDisponibles = FXCollections.observableArrayList(voeuxFiltres);
        classementVoeux = FXCollections.observableArrayList();
        allVoeuxClassified = new SimpleBooleanProperty(false);

        // Associer les listes aux ListView
        voeuxDisponiblesListView.setItems(voeuxDisponibles);
        classementVoeuxListView.setItems(classementVoeux);

        // Configurer les ListCell personnalis�es
        voeuxDisponiblesListView.setCellFactory(new Callback<ListView<Voeux>, ListCell<Voeux>>() {
            @Override
            public ListCell<Voeux> call(ListView<Voeux> listView) {
                return new VoeuxCell();
            }
        });

        classementVoeuxListView.setCellFactory(new Callback<ListView<Voeux>, ListCell<Voeux>>() {
            @Override
            public ListCell<Voeux> call(ListView<Voeux> listView) {
                return new VoeuxClassementCell();
            }
        });

        // Configurer le drag and drop
        configureDragAndDrop();
        
        // Listener pour v�rifier si tous les voeux sont class�s
        classementVoeux.addListener((ListChangeListener<Voeux>) c -> {
            allVoeuxClassified.set(classementVoeux.size() == voeuxFiltres.size());
        });

        // D�sactiver le bouton "Confirmer" si tous les voeux ne sont pas class�s
        handleConfirmer.disableProperty().bind(allVoeuxClassified.not());
    }

    // Configure les op�rations de glisser-d�poser
    private void configureDragAndDrop() {
        voeuxDisponiblesListView.setOnDragDetected(event -> {
            Dragboard db = voeuxDisponiblesListView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            Voeux selectedVoeux = voeuxDisponiblesListView.getSelectionModel().getSelectedItem();
            content.putString(selectedVoeux != null ? selectedVoeux.getNom() : "");
            db.setContent(content);
            event.consume();
        });

        classementVoeuxListView.setOnDragOver(event -> {
            if (event.getGestureSource() != classementVoeuxListView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        classementVoeuxListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                Voeux selectedVoeux = voeuxDisponiblesListView.getSelectionModel().getSelectedItem();
                if (selectedVoeux != null) {
                    classementVoeux.add(selectedVoeux);
                    voeuxDisponibles.remove(selectedVoeux);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        voeuxDisponiblesListView.setOnDragOver(event -> {
            if (event.getGestureSource() != voeuxDisponiblesListView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        voeuxDisponiblesListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                Voeux selectedVoeux = classementVoeuxListView.getSelectionModel().getSelectedItem();
                if (selectedVoeux != null) {
                    voeuxDisponibles.add(selectedVoeux);
                    classementVoeux.remove(selectedVoeux);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    // M�thode pour g�rer l'action de retour � l'�cran d'accueil de l'�tudiant
    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/AccueilEtudiant.fxml", event);
    }

    // M�thode pour g�rer l'action de confirmation des voeux
    @FXML
    private void handleConfirmer(ActionEvent event) throws IOException {
        // R�cup�rer l'�tudiant connect�
        Etudiant etudiantConnecte = EtudiantSession.getInstance().getEtudiantConnecte();
        
        // Mettre � jour la liste des voeux de l'�tudiant
        etudiantConnecte.setListVoeux(new ArrayList<>(classementVoeux));

        // Lire et mettre � jour les informations de l'�tudiant dans la collection
        EtudiantCollection etudiantCollection = new EtudiantCollection();
        etudiantCollection.lireJson();
        etudiantCollection.mettreAJourEtudiant(etudiantConnecte);
        etudiantCollection.sauvegarderJson();

        // Envoi de l'email de confirmation
        String email = etudiantConnecte.getMail();
        String sujet = "Confirmation de vos voeux";
        StringBuilder message = new StringBuilder("Votre classement de voeux a �t� confirm�. Voici votre classement:\n");
        for (int i = 0; i < classementVoeux.size(); i++) {
            message.append(i + 1).append(". ").append(classementVoeux.get(i).getNom()).append("\n");
        }

        try {
            EnvoieEmail.sendEmail(email, sujet, message.toString());
            dialogue.Information("Email de confirmation envoy� avec succ�s.");
        } catch (MessagingException e) {
            dialogue.Error("�chec de l'envoi de l'email de confirmation.");
            e.printStackTrace();
        }

        // Afficher une bo�te de dialogue de confirmation
        dialogue.Confirmation("Le classement de vos voeux a bien �t� enregistr� !");
        
        // Retour � l'�cran d'accueil de l'�tudiant
        sceneChanger.changeScene("/com/cytech/view/FXML/AccueilEtudiant.fxml", event);
    }
    
    // M�thode pour r�initialiser le classement des voeux
    @FXML
    private void handleReset (ActionEvent event) throws IOException {
    	classementVoeuxListView.getItems().clear();
    	initialize();
    }

    // Classe interne pour d�finir la repr�sentation d'un voeu dans la ListView des voeux disponibles
    private class VoeuxCell extends ListCell<Voeux> {
        @Override
        protected void updateItem(Voeux item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.getNom());
            }
        }
    }

    // Classe interne pour d�finir la repr�sentation d'un voeu dans la ListView des voeux class�s
    private class VoeuxClassementCell extends ListCell<Voeux> {
        @Override
        protected void updateItem(Voeux item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText((classementVoeux.indexOf(item) + 1) + ". " + item.getNom());
            }
        }
    }
}
