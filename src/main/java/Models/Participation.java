package Models;

import java.util.Date;

public class Participation {
    private int id;
    private Utilisateur utilisateur;
    private Evenement evenement;
    private StatutP statutP;

    // Nouveaux attributs
    private String commentaire;           // Commentaire de l'utilisateur
    private int nombreDePlacesReservees;  // Nombre de places réservées

    // Constructeurs
    public Participation() {}

    public Participation(Utilisateur utilisateur, Evenement evenement, StatutP statutP, String commentaire, int nombreDePlacesReservees) {
        this.utilisateur = utilisateur;
        this.evenement = evenement;
        this.statutP = statutP;
        this.commentaire = commentaire;
        this.nombreDePlacesReservees = nombreDePlacesReservees;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public StatutP getStatutP() {
        return statutP;
    }

    public void setStatutP(StatutP statutP) {
        this.statutP = statutP;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getNombreDePlacesReservees() {
        return nombreDePlacesReservees;
    }

    public void setNombreDePlacesReservees(int nombreDePlacesReservees) {
        this.nombreDePlacesReservees = nombreDePlacesReservees;
    }

    // toString
    @Override
    public String toString() {
        return "Participation{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", evenement=" + evenement +
                ", statutP=" + statutP +
                ", commentaire='" + commentaire + '\'' +
                ", nombreDePlacesReservees=" + nombreDePlacesReservees +
                '}';
    }
}