package com.cytech.collections;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cytech.individu.Responsable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ResponsableCollection {
	private List<Responsable> collection;
    private static final String FILENAME = "BDD/responsable.json"; // 
    private static final Gson gson = new Gson();
    
 // Constructeur pour initialiser le stock de boissons
    public ResponsableCollection() {
      this.collection = new ArrayList<>();
    }

    public List<Responsable> getCollection() {
      return collection;
    }

    public void setCollection(List<Responsable> collection) {
      this.collection = collection;
    }
    
  //Cette fonction lit le fichier json et met tous les responsables dans collection (c'est un deuxi�me setter)
    public void lireJson() {
        try (FileReader reader = new FileReader(FILENAME)) {
            // Utilisation de TypeToken pour r�cup�rer la liste des �tudiants
            List<Responsable> responsables = gson.fromJson(reader, new TypeToken<List<Responsable>>(){}.getType());
            setCollection(responsables);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
 // M�thode pour ajouter un responsable au fichier JSON 
    // ATTENTION (pour utiliser cette fonction il faut que la collection soit constitu�e de ce qu'il y a initialement dans le json)
    // Je ne sais pas si cette fonction sera utile (hormis si on fait un syst�me mdp oubli�)
       public void ajouterResponsable(Responsable responsable) {
       	
       	// Rechercher et remplacer l'�tudiant existant ou ajouter le nouveau
           boolean responsableExiste = false;
           for (int i = 0; i < this.collection.size(); i++) {
               if (this.collection.get(i).getMail() == responsable.getMail()) {
                   this.collection.set(i, responsable);
                   responsableExiste = true;
                   break;
               }
           }
           
           if (!responsableExiste) {
               this.collection.add(responsable);
           }

           // �crire la liste mise � jour dans le fichier JSON
           try (FileWriter writer = new FileWriter(FILENAME)) {
               gson.toJson(this.collection, writer);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    
}
