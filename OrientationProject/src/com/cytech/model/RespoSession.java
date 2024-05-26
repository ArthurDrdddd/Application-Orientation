package com.cytech.model;


import com.cytech.individu.Responsable;

public class RespoSession {
    // Instance unique de la classe ResponsableSession (singleton)
    private static RespoSession instance;
    
    // Le responsable actuellement connect�
    private Responsable respoConnecte;

    // Constructeur priv� pour emp�cher l'instanciation directe
    private RespoSession() {
    }

    /**
     * Retourne l'instance unique de RespoSession.
     * Cr�e une nouvelle instance si elle n'existe pas encore.
     * 
     * @return l'instance unique de RespoSession
     */
    public static RespoSession getInstance() {
        if (instance == null) {
            instance = new RespoSession();
        }
        return instance;
    }

    /**
     * D�finit le responsable actuellement connect�.
     * 
     * @param respo le responsable � d�finir comme �tant connect�
     */
    public void setRespoConnecte(Responsable respo) {
        this.respoConnecte = respo;
    }

    /**
     * Retourne le responsable actuellement connect�.
     * 
     * @return le responsable actuellement connect�
     */
    public Responsable getRespoConnecte() {
        return respoConnecte;
    }

    /**
     * Efface la session du responsable actuellement connect�.
     */
    public void clearSession() {
        respoConnecte = null;
    }
}
