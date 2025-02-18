package services;

import models.Categorie;
import models.CategorieEnum;
import models.Produit;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduit implements IService<Produit> {

    Connection cnx;

    public ServiceProduit() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Produit produit) throws SQLException {
//        String sql="insert into produit(nom,prix,photo,quantite, ref,description,status)" +"values ('"+produit.getNom()+"',"+produit.getPrix()+",'"+produit.getPhoto()+"',"+produit.getQuantite()+", '"+produit.getRef()+"','"+produit.getDescription()+"','"+produit.getStatus()+"')";
//
//        Statement st = cnx.createStatement();
//        st.executeUpdate(sql);

        String sql = "insert into produit(nom,prix,photo,quantite, ref,description,categorie)" + "values (?,?,?,?,?,?,?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, produit.getNom());
        ste.setFloat(2, produit.getPrix());
        ste.setString(3, produit.getPhoto());
        ste.setInt(4, produit.getQuantite());
        ste.setString(5, produit.getRef());
        ste.setString(6, produit.getDescription());
        ste.setString(7, produit.getCategorie().getNom());
        ste.executeUpdate();
        System.out.println("Produit ajoutée");

    }


    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "delete from produit where id=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        System.out.println("Produit supprimée");
    }

    @Override
    public void modifier(int id, String nom) throws SQLException {
        String sql = "Update produit set nom=? where id=?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, nom);
        st.setInt(2, id);
        st.executeUpdate();
        System.out.println("Produit Modifiée");
    }

    @Override
    public List<Produit> recuperer() throws SQLException {
        String sql = "select * from produit ";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Produit> produits = new ArrayList<>();
        while (rs.next()) {
            Produit p = new Produit();
            p.setId(rs.getInt("id"));
            p.setPhoto(rs.getString("photo"));
            p.setDescription(rs.getString("description"));
            p.setNom(rs.getString("nom"));
            p.setRef(rs.getString("ref"));
            p.setQuantite(rs.getInt("quantite"));
            p.setPrix(rs.getFloat("prix"));

            String nomCategorie = rs.getString("categorie"); // Assurez-vous que cette colonne existe bien dans votre table

            try {
                CategorieEnum categorie = CategorieEnum.fromNom(nomCategorie);
                p.setCategorie(categorie);
            } catch (IllegalArgumentException e) {
                System.err.println("Erreur : Catégorie inconnue - " + nomCategorie);
                e.printStackTrace(); // Pour voir l'erreur dans la console
            }
        }
        return produits;
    }

    @Override
    public List<Categorie> readAll() throws SQLException {
        return List.of();
    }


    @Override
    public List<Produit> readAllProduits() {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produit";

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String photo = rs.getString("photo");
                String description = rs.getString("description");
                String nom = rs.getString("nom");
                String ref = rs.getString("ref");
                Integer quantite = rs.getInt("quantite");
                Integer prix = rs.getInt("prix");
                String categorie = rs.getString("categorie");

                // Création du produit à partir des données de la base
                Produit produit = new Produit(id, photo, description, nom, ref, quantite, prix, categorie);

                // Ajout du produit à la liste
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits: " + e.getMessage());
        }
        return produits;
    }
}