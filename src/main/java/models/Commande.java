package models;

import java.time.LocalDateTime;

public class Commande {
    public int produitId;
    public int produitTF;

    private int id;
   // private Utilisateur utilisateur;  // Attribut Utilisateur
    private Produit produit;
    private LocalDateTime date;
    private String localisation;
    private String telephone;
    private String mail;
    private int nombre;
    private float prix;
    private float total;
    private StatutCommande statut;

    public Commande(int commandeId, float v, String text, String text1, String text2, int i, float v1, LocalDateTime parse, StatutCommande statutCommande) {
    }

    public void setProduitId(int produitId) {
        System.out.println("ID produit: " + produitId);
        this.produitId = produitId;

    }
    // Constructeur avec paramètres (sans utilisateur pour ce cas)
    public Commande(Produit produit, int nombre, String localisation,
                    String telephone, String mail, StatutCommande statut) {
        this.produit = produit;
        this.nombre = nombre;
        this.localisation = localisation;
        this.telephone = telephone;
        this.mail = mail;
        this.date = LocalDateTime.now();  // Date actuelle
        this.prix = produit.getPrix();  // Prix du produit
        this.total = this.nombre * this.prix;  // Total calculé
        this.statut = statut;
    }

    // Constructeur par défaut
    public Commande() {
    }

    // Getter et setter pour Utilisateur


    // Getters et setters pour les autres attributs
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
        this.prix = produit.getPrix();
        calculerTotal();  // Recalcul du total lorsque le produit change
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
        calculerTotal();  // Recalcul du total lorsque le nombre change
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
        calculerTotal();  // Recalcul du total lorsque le prix unitaire change
    }

    public float getTotal() {
        return total;
    }

    // Méthode privée pour calculer le total
    private void calculerTotal() {
        this.total = this.nombre * this.prix;
    }

    public StatutCommande getStatut() {
        return (statut != null) ? statut : StatutCommande.en_attente;
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
                ", prixUnitaire=" + prix +
                ", total=" + total +
                ", statut=" + statut +
                "}\n";
    }

    public void setTotal(float total) {
    }
}
