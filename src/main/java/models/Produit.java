package models;


import java.io.File;

public class Produit {

    private int id,quantite;
    private String ref,nom,description,photo;
    private float prix;
    private CategorieEnum categorie;

    public Produit(String photo, String description, String nom, String ref, int quantite, float prix , CategorieEnum categorie ) {
        this.photo = photo;
        this.description = description;
        this.nom = nom;
        this.ref = ref;
        this.quantite = quantite;
        this.prix = prix;

        this.categorie = categorie;
    }


    public Produit() {
    }

    public Produit(String produitNom) {
    }

    public Produit(int id, String photo, String description, String nom, String ref, Integer quantite, Integer prix, String categorie) {
    }

    public Produit(int id, String photo, String nom, String description, String ref, float prix, int quantite, CategorieEnum categorie) {
    }

    public Produit(int id, String photo, String description, String nom, String ref, String quantite, String prix, String categorie) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getPrix() {
        return this.prix;
    }

    public void setPrix (float prix) {
        this.prix = prix;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getRef() {
        return this.ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public CategorieEnum getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieEnum categorie) {
        this.categorie = categorie;
    }

    public Produit(int id, String photo, String description, String nom, String ref, int quantite, int prix, String categorie) {
        this.id = id;
        this.photo = photo;
        this.description = description;
        this.nom = nom;
        this.ref = ref;
        this.quantite = quantite;
        this.prix = prix;
        this.categorie = CategorieEnum.valueOf(categorie.toUpperCase());
    }



    @Override
    public String toString() {
        return "Produit{" +
                " id=" + id +
                " prix=" + prix +
                " quantite=" + quantite +
                " ref='" + ref + '\'' +
                " nom='" + nom + '\'' +
                " description='" + description + '\'' +
                " photo='" + photo + '\'' +

                " categorie='" + categorie  + '\'' +
                "}\n";
    }




}

