package com.cytech.view.Controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cytech.collections.EtudiantCollection;
import com.cytech.collections.VoeuxCollection;
import com.cytech.individu.Etudiant;
import com.cytech.model.ChangeScene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LancerOperationController {

    private ChangeScene sceneChanger = new ChangeScene(); // Instance de ChangeScene pour changer de sc�ne

    @FXML
    private Label labelResultat;

    @FXML
    private ListView<String> listViewResultats; // Liste d�roulante pour afficher les r�sultats

    // M�thode pour g�rer l'action de lancer l'op�ration
    @FXML
    private void handleLancerOperation(ActionEvent event) {
        try {
            // Ex�cuter l'algorithme d'orientation et obtenir les r�sultats
        	long startTime = System.nanoTime();
        	
            List<String> resultats = lancerOperation();
            
            long endTime = System.nanoTime();
            
            double tempsExe= (endTime - startTime) / 1_000_000_000.0; // Division pour convertir en seconde 
            
         // Arrondir le r�sultat � deux d�cimales
            BigDecimal tempsExe2 = new BigDecimal(tempsExe).setScale(3, RoundingMode.HALF_UP);
            System.out.println("Temps d'ex�cution de l'op�ration d'orientation : " + tempsExe  + " secondes.");
            
            ObservableList<String> items = FXCollections.observableArrayList(resultats);
            listViewResultats.setItems(items);
            labelResultat.setText("Op�ration d'orientation termin�e avec succ�s en " + tempsExe2 +"secondes !");
        } catch (Exception e) {
            e.printStackTrace();
            labelResultat.setText("Erreur lors de l'ex�cution de l'op�ration d'orientation.");
        }
    }

    // M�thode pour g�rer l'action du bouton retour
    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/AccueilRespo.fxml", event);
    }

    // Algorithme pour lancer l'op�ration et obtenir les r�sultats
    public List<String> lancerOperation() {
        EtudiantCollection etuCollection = new EtudiantCollection();
        etuCollection.lireJson();
        VoeuxCollection voeuxCollection = new VoeuxCollection();
        voeuxCollection.lireJson();

        // Trier etuCollection.collection par moyenne
        etuCollection.triParMoy();

        // Cr�er une liste de compteurs pour les places restantes dans les options
        List<Integer> compteurPlace = new ArrayList<>();
        for (int i = 0; i < voeuxCollection.getCollection().size(); i++) {
            compteurPlace.add(voeuxCollection.getCollection().get(i).getNbPlace());
        }

        // Liste pour stocker les r�sultats des �tudiants
        List<String> resultats = new ArrayList<>();

        // Pour tous les �tudiants
        for (int i = 0; i < etuCollection.getCollection().size(); i++) {
            Etudiant etudiant = etuCollection.getCollection().get(i);
            boolean placeTrouvee = false;

            // Parcourir la liste des voeux de chaque �tudiant
            for (int j = 0; j < etudiant.getListeVoeux().size(); j++) {

                // Trouver la position du voeu dans la liste des voeux
                int k = 0;
                while (!(voeuxCollection.getCollection().get(k).equals(etudiant.getListeVoeux().get(j)))) {
                    k++;
                }

                // V�rifier s'il reste des places
                if (compteurPlace.get(k) > 0) {
                    etudiant.setResultat(etudiant.getListeVoeux().get(j));
                   compteurPlace.set(k, (int) (compteurPlace.get(k) - 1.));

                    // Ajouter le r�sultat � la liste des r�sultats
                    resultats.add(etudiant.getPrenom() + " " + etudiant.getNom() + " - " + etudiant.getFiliere() + " - " + etudiant.getResultat().getNom());

                    // �crire dans le fichier JSON
                    etuCollection.ajouterEtudiant(etudiant);

                    // Sortir de la boucle car le voeu de l'�tudiant a �t� accept�
                    placeTrouvee = true;
                    break;
                }
            }

            // Si aucune place trouv�e pour les voeux de l'�tudiant
            if (!placeTrouvee) {
                resultats.add(etudiant.getPrenom() + " " + etudiant.getNom() + " - " + etudiant.getFiliere() + " - Aucune place disponible pour les voeux");
            }
        }
        return resultats;
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////VERSION AVEC ENVOIE D'EMAIL///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//package com.cytech.view.Controller;
/*
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cytech.collections.EtudiantCollection;
import com.cytech.collections.VoeuxCollection;
import com.cytech.individu.Etudiant;
import com.cytech.individu.Voeux;
import com.cytech.model.ChangeScene;
import com.cytech.model.EnvoieEmail;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.mail.MessagingException;

public class LancerOperationController {

    private ChangeScene sceneChanger = new ChangeScene(); // Instance de ChangeScene pour changer de sc�ne

    @FXML
    private Label labelResultat;

    @FXML
    private ListView<String> listViewResultats; // Liste d�roulante pour afficher les r�sultats

    // M�thode pour g�rer l'action de lancer l'op�ration
    @FXML
    private void handleLancerOperation(ActionEvent event) {
        try {
            // Ex�cuter l'algorithme d'orientation et obtenir les r�sultats
            List<String> resultats = lancerOperation();
            ObservableList<String> items = FXCollections.observableArrayList(resultats);
            listViewResultats.setItems(items);
            labelResultat.setText("Op�ration d'orientation termin�e avec succ�s !");
        } catch (Exception e) {
            e.printStackTrace();
            labelResultat.setText("Erreur lors de l'ex�cution de l'op�ration d'orientation.");
        }
    }

    // M�thode pour g�rer l'action du bouton retour
    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/com/cytech/view/FXML/AccueilRespo.fxml", event);
    }

    // Algorithme pour lancer l'op�ration et obtenir les r�sultats
    public List<String> lancerOperation() {
        EtudiantCollection etuCollection = new EtudiantCollection();
        etuCollection.lireJson();
        VoeuxCollection voeuxCollection = new VoeuxCollection();
        voeuxCollection.lireJson();

        // Trier etuCollection.collection par moyenne
        etuCollection.triParMoy();

        // Cr�er une liste de compteurs pour les places restantes dans les options
        List<Double> compteurPlace = new ArrayList<>();
        for (int i = 0; i < voeuxCollection.getCollection().size(); i++) {
            compteurPlace.add(voeuxCollection.getCollection().get(i).getNbPlace());
        }

        // Liste pour stocker les r�sultats des �tudiants
        List<String> resultats = new ArrayList<>();

        // Pour tous les �tudiants
        for (int i = 0; i < etuCollection.getCollection().size(); i++) {
            Etudiant etudiant = etuCollection.getCollection().get(i);
            boolean placeTrouvee = false;

            // Parcourir la liste des voeux de chaque �tudiant
            for (int j = 0; j < etudiant.getListeVoeux().size(); j++) {

                // Trouver la position du voeu dans la liste des voeux
                int k = 0;
                while (!(voeuxCollection.getCollection().get(k).equals(etudiant.getListeVoeux().get(j)))) {
                    k++;
                }

                // V�rifier s'il reste des places
                if (compteurPlace.get(k) > 0) {
                    etudiant.setResultat(etudiant.getListeVoeux().get(j));
                    compteurPlace.set(k, compteurPlace.get(k) - 1.);

                    // Ajouter le r�sultat � la liste des r�sultats
                    resultats.add(etudiant.getPrenom() + " " + etudiant.getNom() + " - " + etudiant.getFiliere() + " - " + etudiant.getResultat().getNom());

                    // �crire dans le fichier JSON
                    etuCollection.ajouterEtudiant(etudiant);

                    // Envoyer un email � l'�tudiant
                    String email = etudiant.getMail();
                    String sujet = "R�sultat de votre orientation";
                    String message = "Bonjour " + etudiant.getPrenom() + ",\n\nVous avez �t� s�lectionn� pour le voeu suivant : " + etudiant.getResultat().getNom() + ".\n\nCordialement,\nL'�quipe d'orientation";

                    try {
                        EnvoieEmail.sendEmail(email, sujet, message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        resultats.add("Erreur lors de l'envoi de l'email � " + etudiant.getPrenom() + " " + etudiant.getNom());
                    }

                    // Sortir de la boucle car le voeu de l'�tudiant a �t� accept�
                    placeTrouvee = true;
                    break;
                }
            }

            // Si aucune place trouv�e pour les voeux de l'�tudiant
            if (!placeTrouvee) {
                resultats.add(etudiant.getPrenom() + " " + etudiant.getNom() + " - " + etudiant.getFiliere() + " - Aucune place disponible pour les voeux");

                // Envoyer un email � l'�tudiant pour l'informer qu'aucune place n'a �t� trouv�e
                String email = etudiant.getMail();
                String sujet = "R�sultat de votre orientation";
                String message = "Bonjour " + etudiant.getPrenom() + ",\n\nMalheureusement, aucune place n'�tait disponible pour vos voeux.\n\nCordialement,\nL'�quipe d'orientation";

                try {
                    EnvoieEmail.sendEmail(email, sujet, message);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    resultats.add("Erreur lors de l'envoi de l'email � " + etudiant.getPrenom() + " " + etudiant.getNom());
                }
            }
        }

        return resultats;
    }
}
*/
