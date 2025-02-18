package models;



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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrix() {
        return this.prix;
    }

    public void setPrix (float prix) {
        this.prix = prix;
    }

    public int getQuantite() {
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



    public Produit(int id, float prix, int quantite, String ref, String nom, String description, String photo  , CategorieEnum categorie) {
        this.id = id;
        this.prix = prix;
        this.quantite = quantite;
        this.ref = ref;
        this.nom = nom;
        this.description = description;
        this.photo = photo;

        this.categorie = categorie;
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

