package com.cytech.model;

import com.cytech.individu.Etudiant;

public class EtudiantSession {
    // Instance unique de la classe EtudiantSession (singleton)
    private static EtudiantSession instance;
    
    // L'�tudiant actuellement connect�
    private Etudiant etudiantConnecte;

    // Constructeur priv� pour emp�cher l'instanciation directe
    private EtudiantSession() {
    }

    /**
     * Retourne l'instance unique de EtudiantSession.
     * Cr�e une nouvelle instance si elle n'existe pas encore.
     * 
     * @return l'instance unique de EtudiantSession
     */
    public static EtudiantSession getInstance() {
        if (instance == null) {
            instance = new EtudiantSession();
        }
        return instance;
    }

    /**
     * D�finit l'�tudiant actuellement connect�.
     * 
     * @param etudiant l'�tudiant � d�finir comme �tant connect�
     */
    public void setEtudiantConnecte(Etudiant etudiant) {
        this.etudiantConnecte = etudiant;
    }

    /**
     * Retourne l'�tudiant actuellement connect�.
     * 
     * @return l'�tudiant actuellement connect�
     */
    public Etudiant getEtudiantConnecte() {
        return etudiantConnecte;
    }

    /**
     * Efface la session de l'�tudiant actuellement connect�.
     */
    public void clearSession() {
        etudiantConnecte = null;
    }
}
