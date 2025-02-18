package services;

import models.Categorie;
import models.CategorieEnum;
import models.Produit;
import tools.MyDataBase;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategorie implements IService<Categorie> {

    private Connection cnx;


    public ServiceCategorie() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public void ajouter(Categorie categorie) throws SQLException {
        String sql = "INSERT INTO categorie(nom) VALUES (?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, categorie.getNom());
        ste.executeUpdate();

        // Ajouter dynamiquement la catégorie à l'énumération
        CategorieEnum.ajouterCategorieDynamique(categorie.getNom());

        System.out.println("Catégorie ajoutée et enregistrée dans l'énumération.");
    }




    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "delete from categorie where id=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        System.out.println("Catégorie supprimée");
    }

    @Override
    public void modifier(int id, String nom) throws SQLException {
        String sql = "Update categorie set nom=? where id=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, nom);
        st.setInt(2, id);
        st.executeUpdate();
        System.out.println("Catégorie modifiée");
    }

    public List<Categorie> recuperer() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";

        try (Connection cnx = MyDataBase.getConnection();
             Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int categorieId = rs.getInt("id");
                String categorieNom = rs.getString("nom");

                Categorie categorie = new Categorie(categorieId, categorieNom);

                // Charger les produits associés à la catégorie
                loadProduitsPourCategorie(cnx, categorie);
                categories.add(categorie);

                // Ajouter dynamiquement à l'énumération
                CategorieEnum.ajouterCategorieDynamique(categorieNom);
            }
        }
        return categories;
    }

    private void loadProduitsPourCategorie(Connection cnx, Categorie categorie) throws SQLException {
        String query = "SELECT nom FROM produit WHERE categorie = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, categorie.getNom());

            try (ResultSet rsProduits = stmt.executeQuery()) {
                List<Produit> produits = new ArrayList<>();
                while (rsProduits.next()) {
                    Produit produit = new Produit();
                    produit.setNom(rsProduits.getString("nom"));
                    produits.add(produit);
                }
                categorie.setProduits(produits);
            }
        }
    }
    @Override
    public List<Categorie> readAll() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                Categorie categorie = new Categorie(id, nom);

                // Charger les produits associés à la catégorie
                loadProduitsPourCategorie(cnx, categorie);
                categories.add(categorie);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des catégories: " + e.getMessage());
            throw e; // Relever l'exception pour qu'elle soit gérée ailleurs
        }
        return categories;
    }

    @Override
    public List<Produit> readAllProduits() throws SQLException {
        return List.of();
    }

}
