package services;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.*;
import tools.MyDataBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduit implements IService<Produit> {

    private static Connection cnx;
    private ImageView imageView;
    private Image image;
    private FileInputStream fis;
    private FileChooser fileChooser;
    private File file;

    public ServiceProduit() {
        cnx = MyDataBase.getInstance().getCnx();
    }
    public static Produit getProduitById(int produiId) throws SQLException {
        String query = "SELECT * FROM produit WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, produiId); // Assurez-vous que l'ID est bien passé à la requête
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Retourne un produit en utilisant les données de la base
                return new Produit(
                        rs.getInt("id"), // ID du produit
                        rs.getString("photo"),
                        rs.getString("description"),
                        rs.getString("nom"),
                        rs.getString("ref"),
                        rs.getFloat("prix"), // Prix récupéré en tant que float
                        rs.getInt("quantite"), // Quantité du produit
                        CategorieEnum.valueOf(rs.getString("categorie")) // Catégorie récupérée
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du produit par ID : " + e.getMessage());
        }
        return null; // Retourne null si aucun produit n'est trouvé
    }

    @Override
    public void ajouter(Produit produit) throws SQLException, IOException {
        String sql = "INSERT INTO produit(nom, prix, photo, quantite, ref, description, categorie) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);

        // Remplir les autres paramètres de la requête SQL
        ste.setString(1, produit.getNom());
        ste.setFloat(2, produit.getPrix());

        String photoPath = produit.getPhoto(); // Récupérer le chemin de l'image
        if (photoPath != null && !photoPath.isEmpty()) {
            // Si le chemin de l'image est valide, on l'ajoute
            ste.setString(3, photoPath); // Insertion du chemin de l'image dans la base de données
        } else {
            // Si aucun fichier photo n'est sélectionné, insérer une valeur par défaut ou NULL
            ste.setNull(3, java.sql.Types.VARCHAR); // Définit la colonne "photo" comme NULL
        }

        // Remplir les autres paramètres de la requête
        ste.setInt(4, produit.getQuantite());
        ste.setString(5, produit.getRef());
        ste.setString(6, produit.getDescription());
        ste.setString(7, produit.getCategorie().name());

        // Exécution de la mise à jour dans la base de données
        ste.executeUpdate();
        System.out.println("Produit ajouté");
    }

    @Override
    public List<Commande> readAll2() throws SQLException {
        return List.of();
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM produit WHERE id=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        System.out.println("Produit supprimé");
    }

    @Override
    public void modifier(int id, String nom) throws SQLException {
        // Implémentation éventuelle
    }

    @Override
    public void modifier1(int id, String nom, String description, String ref, String photo, double prix, int quantite, String categorie) throws SQLException {
        String sql = "UPDATE produit SET nom = ?, description = ?, ref = ?, photo = ?, prix = ?, quantite = ?, categorie = ? WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, nom);
            st.setString(2, description);
            st.setString(3, ref);

            // Si photo est un chemin de fichier, vous pouvez l'enregistrer directement
            if (photo != null && !photo.isEmpty()) {
                st.setString(4, photo); // Mise à jour avec le chemin du fichier
            } else {
                st.setNull(4, java.sql.Types.VARCHAR); // Sinon, on met NULL dans la colonne
            }

            st.setDouble(5, prix);
            st.setInt(6, quantite);
            st.setString(7, categorie);
            st.setInt(8, id);

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Produit modifié avec succès !");
            } else {
                System.out.println("Aucun produit trouvé avec l'ID donné.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du produit : " + e.getMessage());
            throw e; // Re-throw SQLException to handle it higher if needed
        }
    }

    @Override
    public void modifier1(Commande updatedCommande) {

    }

    // Implémentation de modifier1 en utilisant l'objet Produit
    @Override
    public void modifier1(Produit updatedProduit) {
        try {
            modifier1(updatedProduit.getId(),
                    updatedProduit.getNom(),
                    updatedProduit.getDescription(),
                    updatedProduit.getRef(),
                    updatedProduit.getPhoto(),
                    updatedProduit.getPrix(),
                    updatedProduit.getQuantite(),
                    updatedProduit.getCategorie().name());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du produit : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(int id, Commande updatedCommande) throws SQLException {

    }

    @Override
    public void modifier(int id, StatutCommande statut) throws SQLException {

    }

    @Override
    public void modifier(Commande updatedCommmande) {

    }

    @Override
    public void modifier3(int id, String statut) {

    }

    @Override
    public void modifierStatut(int id, StatutCommande statut) throws SQLException {

    }

    @Override
    public void modifier3(Commande updatedCommande) {

    }

    public List<Produit> recuperer() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produit";
        try (Connection cnx = MyDataBase.getConnection();
             Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String photo = rs.getString("photo");
                String description = rs.getString("description");
                String nom = rs.getString("nom");
                String ref = rs.getString("ref");
                int quantite = rs.getInt("quantite");
                double prix = rs.getDouble("prix"); // Modifier pour correspondre à la base de données (prix devrait être double)
                CategorieEnum categorie = null;
                try {
                    categorie = CategorieEnum.valueOf(rs.getString("categorie"));
                } catch (IllegalArgumentException e) {
                    System.err.println("Valeur de catégorie invalide : " + rs.getString("categorie"));
                }
                Produit produit = new Produit(id,  description, ref, nom, photo, (float) prix, quantite,  categorie);
                System.out.println("Produit récupéré : " + produit);
                produits.add(produit);
            }
        }
        return produits;
    }


    @Override
    public List<Categorie> readAll() throws SQLException {
        return List.of();
    }

    public List<Produit> readAll1() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produit";
        Statement stmt = cnx.createStatement();
        ResultSet rss = stmt.executeQuery(query);

        while (rss.next()) {
            int id = rss.getInt("id");
            String photo = rss.getString("photo");
            String description = rss.getString("description");
            String nom = rss.getString("nom");
            String ref = rss.getString("ref");
            int quantite = rss.getInt("quantite");
            float prix = rss.getFloat("prix");
            String categorie = rss.getString("categorie");



            if (description == null) description = "Pas de description";
            if (nom == null) nom = "Nom inconnu";
            if (ref == null) ref = "Référence inconnue";
            if (categorie == null) categorie = "Non catégorisé";

            // Créer l'objet Produit avec les données récupérées
            Produit produit = new Produit(id, photo, description, nom, ref, quantite, (int) prix, categorie);
            produits.add(produit);
        }

        return produits;
    }




}
