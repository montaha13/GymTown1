package models;

import java.time.LocalDateTime;
import java.util.Date;

public class Commande {
    private int id;

    private Produit produit;
    private LocalDateTime date;
    private String localisation;
    private String telephone;
    private String mail;
    private int nombre;
    private float prixUnitaire;
    private float total;
    private StatutCommande statut;

    // Constructeur avec paramètres
    public Commande( Produit produit, int nombre, String localisation,
                    String telephone, String mail, StatutCommande statut) {

        this.produit = produit;
        this.nombre = nombre;
        this.localisation = localisation;
        this.telephone = telephone;
        this.mail = mail;
        this.date = LocalDateTime.now();
        this.prixUnitaire = produit.getPrix();
        this.total = this.nombre * this.prixUnitaire;
        this.statut = statut;
    }

    // Constructeur par défaut
    public Commande() {}

    public Commande(Produit p, Date date, String tunis, String number, String mail, int i, float prix, StatutCommande statutCommande) {
    }

    public Commande(Produit produit, LocalDateTime now, String tunis, String number, String mail, int i, float prix, StatutCommande statutCommande) {
    }

    public Commande(int nombre, String localisation, String telephone, String mail, String date, float prix, float total, String statut) {
    }


    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        this.prixUnitaire = produit.getPrix();
        this.total = this.nombre * this.prixUnitaire;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
        this.total = this.nombre * this.prixUnitaire;
    }

    public float getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(float prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        this.total = this.nombre * this.prixUnitaire;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    // Méthode toString pour afficher les détails de la commande
    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +

                ", produit=" + (produit != null ? produit.getNom() : "Aucun produit") +
                ", date=" + date +
                ", localisation='" + localisation + '\'' +
                ", telephone='" + telephone + '\'' +
                ", mail='" + mail + '\'' +
                ", nombre=" + nombre +
                ", prixUnitaire=" + prixUnitaire +
                ", total=" + total +
                ", statut=" + statut +
                "}\n";
    }
}
