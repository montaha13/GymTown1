package models;

import tools.MyDataBase;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Categorie {


    private int id;
    private String nom;
    private List<Produit> produits;

    // Constructeur avec paramètres pour initialiser les attributs
    public Categorie( String nom) {

        this.nom = nom;
        this.produits = new ArrayList<>();
    }
    public Categorie() {
    }

    public Categorie(CategorieEnum categorieEnum) {
    }

    // Méthode statique pour récupérer une instance de Categorie par nom
    public static Categorie valueOf(String categorieNom) {
        // Logique pour récupérer la catégorie depuis la base de données
        Categorie categorie = null;
        try (Connection connection = MyDataBase.getConnection()) {
            String query = "SELECT * FROM categorie WHERE nom = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, categorieNom);
                ResultSet resultSet = statement.executeQuery();

                // Si la catégorie existe, on la récupère
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nom = resultSet.getString("nom");
                    categorie = new Categorie(id, nom);

                } else {
                    System.out.println("Catégorie non trouvée pour le nom : " + categorieNom);
                    categorie = new Categorie(0, "Inconnu"); // Si la catégorie n'existe pas, retourne "Inconnu"
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la catégorie : " + e.getMessage());
            categorie = new Categorie(0, "Inconnu"); // Gérer l'erreur et retourner une catégorie inconnue
        }

        return categorie;
    }




    // Getter pour l'ID
    public int getId() {
        return id;
    }

    // Setter pour l'ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter pour le nom
    public String getNom() {
        return nom;
    }

    // Setter pour le nom
    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter pour la liste des produits
    public List<Produit> getProduits() {
        return produits;
    }

    // Setter pour la liste des produits
    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }


    // Constructeur avec paramètres pour initialiser les attributs
    public Categorie(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.produits = new ArrayList<>();
    }
    private Connection cnx;
    // Méthode pour charger les produits associés à une catégorie spécifique
    private void loadProduitsPourCategorie(Categorie categorie) throws SQLException {
        String query = "SELECT nom FROM produit WHERE categorie = ?";  // Filtrer avec le nom de la catégorie


        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, categorie.getNom());  // Utiliser le nom de la catégorie pour filtrer

            try (ResultSet rsProduits = stmt.executeQuery()) {
                List<Produit> produits = new ArrayList<>();
                while (rsProduits.next()) {
                    Produit produit = new Produit();
                    produit.setNom(rsProduits.getString("nom"));  // Récupérer uniquement le nom du produit
                    produits.add(produit);
                }
                categorie.setProduits(produits);  // Ajouter les produits filtrés à la catégorie
            }
        }
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", produits=" + (produits != null && !produits.isEmpty() ? produitsToString() : "Aucun produit") +
                "}\n";
    }

    // Méthode privée pour formater la liste des produits
    private String produitsToString() {
        return produits.stream()
                .map(Produit::getNom)  // Récupérer le nom de chaque produit
                .reduce((p1, p2) -> p1 + ", " + p2) // Concaténer les noms avec ", "
                .orElse("Aucun produit"); // Si la liste est vide
    }



}
