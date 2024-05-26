package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import com.cytech.collections.*;
import com.cytech.individu.*;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML pour la vue d'accueil
            // Assurez-vous que le chemin est correct et correspond � l'emplacement du fichier FXML
            Parent accueilView = FXMLLoader.load(getClass().getResource("/com/cytech/view/FXML/Accueil.fxml"));
            
            // D�finir le titre de la fen�tre principale
            primaryStage.setTitle("Orientation des �tudiants");
            
            // D�finir la sc�ne avec la vue d'accueil et sp�cifier la taille de la fen�tre
            primaryStage.setScene(new Scene(accueilView, 800, 600)); // Vous pouvez ajuster la taille si n�cessaire
            
            // Afficher la fen�tre principale
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		System.setProperty("org.apache.poi.util.POILogger", "org.apache.poi.util.NullLogger");
		launch(args);
		
		//Test de r�cup�ration du bool�en et de sa modification
		/*
		GestionAccesVoeux g = new GestionAccesVoeux();
		System.out.println(g.getBoolValue());
		//On attribut � boolValue sa valeur inverse
		g.setBoolValue(!g.getBoolValue());
		g.modifierJson();
		System.out.println(g.getBoolValue());
		*/
		
		//Test pour lancer l'operation
		/*
		lancerOperation();
		
		EtudiantCollection etudiantCollection = new EtudiantCollection();
        
        // Lire les �tudiants depuis le fichier JSON
        etudiantCollection.lireJson();
        
        // Afficher la liste tri�e
        for (Etudiant etudiant : etudiantCollection.getCollection()) {
            System.out.println(etudiant.getPrenom());
            System.out.println(etudiant.getResultat());
        }
        */
		
	}
	
	//M�thode pour lancer l'op�ration
	public static void lancerOperation() {
		EtudiantCollection etuCollection = new EtudiantCollection();
		etuCollection.lireJson();
		VoeuxCollection voeuxCollection = new VoeuxCollection();
		voeuxCollection.lireJson();
		
		//Trier etuCollection.collection par moyenne
		etuCollection.triParMoy();
		
		//On cr�e une liste de compteur qui nous indique le nombre de places restantes dans les options
		List<Integer> compteurPlace = new ArrayList<>();
		for(int i = 0; i < voeuxCollection.getCollection().size(); i++) {
			compteurPlace.add(voeuxCollection.getCollection().get(i).getNbPlace());
		}
		
		//Pour tous les �tudiants
		for (int i = 0; i < etuCollection.getCollection().size(); i++) {
			Etudiant etudiant = etuCollection.getCollection().get(i);
			
			//Pour chaque �tudiant on parcours sa liste de voeux
			for (int j = 0; j < etudiant.getListeVoeux().size(); j++) {
				
				//Il faut trouver la position du voeux dans la liste des voeux pour pouvoir ensuite regarder dans la liste des compteur combien de places reste-il
				int k = 0;
				while(!(voeuxCollection.getCollection().get(k).equals(etudiant.getListeVoeux().get(j)))) {
					k++;
				}
				
				//On regarde si il reste de la place
				if(compteurPlace.get(k) > 0) {
					etudiant.setResultat(etudiant.getListeVoeux().get(j));
					compteurPlace.set(k, (int) (compteurPlace.get(k) - 1.));
					
					//On �crit dans la json
					etuCollection.ajouterEtudiant(etudiant);
					
					//On sort de la boucle for car le j �me voeux de l'�tudiant a �t� accept�
					break;
				}
			}

		}
	}
	
}
