package Models;

public class Gym  {
    private int id;
    private String nom;
    private String localisation;
    private String photo;
    private String horaires;
    private String contact;

    // Constructeurs
    public Gym() {}

    public Gym(String nom) {
        this.nom = nom;
    }

    public Gym(String nom, String localisation, String photo, String horaires, String contact) {
        this.nom = nom;
        this.localisation = localisation;
        this.photo = photo;
        this.horaires = horaires;
        this.contact = contact;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    // Méthode toString pour afficher les informations de la salle
    @Override
    public String toString() {
        return "Salle [id=" + id + ", nom=" + nom + ", localisation=" + localisation +
                ", photo=" + photo + ", horaires=" + horaires + ", contact=" + contact + "]";
    }
}
