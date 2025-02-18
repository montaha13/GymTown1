package services;

import models.Categorie;
import models.Commande;
import models.StatutCommande;
//import models.Utilisateur;
import models.Produit;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommande implements IService<Commande> {

    Connection cnx;

    public ServiceCommande() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Commande commande) throws SQLException {
        // Vérifier si la connexion est correctement établie
        if (cnx == null) {
            throw new SQLException("La connexion à la base de données est null.");
        }

        // Vérifier si l'objet Produit est null
        if (commande.getProduit() == null) {
            throw new SQLException("Le produit associé à la commande est null.");
        }

        // Préparer la requête SQL
        String sql = "INSERT INTO commande(produit_id, nombre, localisation, telephone, mail, date, prixUnitaire, total, statut) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            // Remplir les paramètres de la requête
            ste.setInt(1, commande.getProduit().getId());  // Produit ID
            ste.setInt(2, commande.getNombre());  // Nombre
            ste.setString(3, commande.getLocalisation());  // Localisation
            ste.setString(4, commande.getTelephone());  // Téléphone
            ste.setString(5, commande.getMail());  // Mail
            ste.setTimestamp(6, Timestamp.valueOf(commande.getDate()));  // Date
            ste.setFloat(7, commande.getPrixUnitaire());  // Prix unitaire
            ste.setFloat(8, commande.getTotal());  // Total
            ste.setString(9, commande.getStatut().name());  // Statut (en tant que String)

            // Exécution de la requête
            ste.executeUpdate();
            System.out.println("Commande ajoutée avec succès");
        } catch (SQLException e) {
            // Gestion de l'exception SQL
            System.err.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
            throw e;  // Rethrow pour pouvoir le gérer ailleurs si nécessaire
        }
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM commande WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        System.out.println("Commande supprimée");
    }

    @Override
    public void modifier(int id, String statut) throws SQLException {
        String sql = "UPDATE commande SET statut = ? WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, statut);
        st.setInt(2, id);
        st.executeUpdate();
        System.out.println("Commande modifiée");
    }

    @Override
    public List<Commande> recuperer() throws SQLException {
        String sql = "SELECT * FROM commande";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);

        List<Commande> commandes = new ArrayList<>();
        while (rs.next()) {
            Commande c = new Commande();
            c.setId(rs.getInt("id"));
            c.setLocalisation(rs.getString("localisation"));
            c.setTelephone(rs.getString("telephone"));
            c.setMail(rs.getString("mail"));
            c.setNombre(rs.getInt("nombre"));
            c.setPrixUnitaire(rs.getFloat("prixUnitaire"));
            c.setTotal(rs.getFloat("total"));
            c.setDate(rs.getTimestamp("date").toLocalDateTime());
            c.setStatut(StatutCommande.valueOf(rs.getString("statut")));

            // Charger les objets Utilisateur et Produit (ajuster selon ton modèle)
            //Utilisateur utilisateur = new Utilisateur();
            //utilisateur.setId(rs.getInt("utilisateur_id"));
            //c.setUtilisateur(utilisateur);

            Produit produit = new Produit();
            produit.setId(rs.getInt("produit_id"));
            c.setProduit(produit);

            commandes.add(c);
        }
        return commandes;
    }

    @Override
    public List<Categorie> readAll() throws SQLException {
        return List.of();
    }

    @Override
    public List<Produit> readAllProduits() throws SQLException {
        return List.of();
    }
}
