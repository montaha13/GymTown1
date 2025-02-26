package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Evenement {
    private int id;
    private String nom;
    private String localisation;
    private String photo;
    private String description;
    private String dateDebut;
    private String dateFin;
    private double frais;
    private Statut statut;
    private int nombreDePlaces; // Nouvel attribut

    public Evenement() {
    }

    // Constructeur avec ID
    public Evenement(int id, String nom, String localisation, String photo, String description, String dateDebut, String dateFin, double frais, Statut statut, int nombreDePlaces) {
        this.id = id;
        this.nom = nom;
        this.localisation = localisation;
        this.photo = photo;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.frais = frais;
        this.statut = statut;
        this.nombreDePlaces = nombreDePlaces;
    }

    // Constructeur sans ID (pour l'ajout)
    public Evenement(String nom, String localisation, String photo, String description, String dateDebut, String dateFin, double frais, Statut statut, int nombreDePlaces) {
        this.nom = nom;
        this.localisation = localisation;
        this.photo = photo;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.frais = frais;
        this.statut = statut;
        this.nombreDePlaces = nombreDePlaces;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public double getFrais() {
        return frais;
    }

    public Statut getStatut() {
        return statut;
    }

    public int getNombreDePlaces() {
        return nombreDePlaces;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setFrais(double frais) {
        this.frais = frais;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public void setNombreDePlaces(int nombreDePlaces) {
        this.nombreDePlaces = nombreDePlaces;
    }
    // Méthode pour la liaison avec JavaFX
    public StringProperty nomProperty() {
        return new SimpleStringProperty(nom);
    }
    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", localisation='" + localisation + '\'' +
                ", photo='" + photo + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", frais=" + frais +
                ", statut=" + statut +
                ", nombreDePlaces=" + nombreDePlaces +
                '}';
    }
}