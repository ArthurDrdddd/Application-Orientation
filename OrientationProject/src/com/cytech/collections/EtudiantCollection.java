package com.cytech.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import com.cytech.individu.Etudiant;
import com.google.gson.reflect.TypeToken;
import java.io.FileWriter;


public class EtudiantCollection {
	private List<Etudiant> collection;
    private static final String FILENAME = "BDD/etudiant.json"; // 
    private static final Gson gson = new Gson();
    
 // Constructeur pour initialiser le stock de boissons
    public EtudiantCollection() {
      this.collection = new ArrayList<>();
    }

    public List<Etudiant> getCollection() {
      return collection;
    }

    public void setCollection(List<Etudiant> collection) {
      this.collection = collection;
    }
    
    //Cette fonction lit le fichier json et met tous les etudiants dans collection (c'est un deuxi�me setter)
    public void lireJson() {
        try (FileReader reader = new FileReader(FILENAME)) {
            // Utilisation de TypeToken pour r�cup�rer la liste des �tudiants
            List<Etudiant> etudiants = gson.fromJson(reader, new TypeToken<List<Etudiant>>(){}.getType());
            setCollection(etudiants);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 // M�thode pour ajouter un �tudiant � la collection et mettre � jour le fichier JSON
    // ATTENTION: Pour utiliser cette fonction, la collection doit d'abord �tre initialis�e avec le contenu du fichier JSON
    // Pour modifier la liste des voeux d'un �tudiant, il faut mettre � jour sa listeVoeux puis appeler ajouterEtudiant avec la nouvelle version de l'�tudiant
    public void ajouterEtudiant(Etudiant etudiant) {
        // Rechercher et remplacer l'�tudiant existant ou ajouter le nouveau
        boolean etudiantExiste = false;
        for (int i = 0; i < this.collection.size(); i++) {
            if (this.collection.get(i).getNumEtu() == etudiant.getNumEtu()) {
                this.collection.set(i, etudiant);
                etudiantExiste = true;
                break;
            }
        }
        
        // Si l'�tudiant n'existe pas encore, l'ajouter � la collection
        if (!etudiantExiste) {
            this.collection.add(etudiant);
        }

        // �crire la collection mise � jour dans le fichier JSON
        sauvegarderJson();
    }
    
    // M�thode pour mettre � jour un �tudiant existant dans la collection et le fichier JSON
    public void mettreAJourEtudiant(Etudiant etudiant) {
        for (int i = 0; i < this.collection.size(); i++) {
            if (this.collection.get(i).getNumEtu() == etudiant.getNumEtu()) {
                this.collection.set(i, etudiant);
                break;
            }
        }
        // �crire la collection mise � jour dans le fichier JSON
        sauvegarderJson();
    }

    // M�thode pour sauvegarder la collection actuelle des �tudiants dans le fichier JSON
    public void sauvegarderJson() {
        try (FileWriter writer = new FileWriter(FILENAME)) {
            gson.toJson(this.collection, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // M�thode pour trier la collection par moyennes g�n�rales
    public void triParMoy() {
        Collections.sort(this.collection, new Comparator<Etudiant>() {
            @Override
            public int compare(Etudiant e1, Etudiant e2) {
                return Double.compare(e2.getMoyGen(), e1.getMoyGen());
            }
        });
    }
}
