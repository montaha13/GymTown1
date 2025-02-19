package com.example.gestionutilisateur.Entity;

public class User {
    private int id;
    private String role;
    private String email;
    private String password;
    private String prenom;
    private String nom;
    private String telephone;
    private String adresse;
    private String photo;
    private String specialite;
    private int note;
    // Constructeur vide
    public User() {
    }
    // Constructeur avec paramètres
    public User( int id ,String role,String email,String password,String prenom, String nom, String telephone,String adresse,String photo, String specialite, int note) {
        this.id = id;
        this.role = role;
        this.email = email;
        this.password = password;
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.photo = photo;
        this.specialite = specialite;
        this.note = note;
    }

    public User( String role,String email,String password,String prenom, String nom, String telephone,String adresse,String photo, String specialite, int note) {
        this.role = role;
        this.email = email;
        this.password = password;
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.photo = photo;
        this.specialite = specialite;
        this.note = note;

    }



    // Getters et setters
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", roles='" + role + '\'' +
                '}';
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public String getPhoto() {
        return photo;
    }

    public String getSpecialite() {
        return specialite;
    }

    public int getNote() {
        return note;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getRoles() {
        return role;
    }

    public void setRoles(String roles) {
        this.role = roles;
    }
}
