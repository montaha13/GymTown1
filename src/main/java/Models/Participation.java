package Models;

import java.util.Date;

public class Participation {
    private int id;
    private Utilisateur utilisateur;
    private Evenement evenement;
    private StatutP statutP;

    // Constructeurs
    public Participation() {}

    public Participation( Utilisateur utilisateur , Evenement evenement, Date dateParticipation, double montantPaye, StatutP statutP) {
      this.utilisateur =utilisateur ;
        this.evenement = evenement;
        this.statutP = statutP;
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

    // toString

    @Override
    public String toString() {
        return "Participation{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", evenement=" + evenement +
                ", statutP=" + statutP +
                '}';
    }
}
