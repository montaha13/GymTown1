package models;

public class Utilisateur {
    private int id;  // Ajout de l'attribut id
    private String nom;
    private String prenom;
    private String email;

    // Constructeur
    public Utilisateur(int id, String nom, String prenom, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    // Getter pour id
    public int getId() {
        return id;
    }

    // Setter pour id
    public void setId(int id) {
        this.id = id;
    }

    // Getters et setters pour nom, prenom, et email
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Méthode toString pour afficher les détails de l'utilisateur
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
