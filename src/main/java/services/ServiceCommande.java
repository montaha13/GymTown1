package services;

import models.*;
import tools.MyDataBase;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ServiceCommande implements IService<Commande> {

    private static final Logger logger = Logger.getLogger(ServiceCommande.class.getName());
    private Connection cnx;
    private ObservableList<Commande> commandesList;

    public ServiceCommande() {
        cnx = MyDataBase.getInstance().getCnx();
        commandesList = FXCollections.observableArrayList(); // Liste observable pour l'interface
    }

    @Override
    public void ajouter(Commande commande) throws SQLException, IOException {
        String sql = "INSERT INTO commande (prix, localisation, telephone, mail, nombre, total, statut, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setFloat(1, commande.getPrix());
            stmt.setString(2, commande.getLocalisation());
            stmt.setString(3, commande.getTelephone());
            stmt.setString(4, commande.getMail());
            stmt.setInt(5, commande.getNombre());
            stmt.setFloat(6, commande.getTotal());
            stmt.setString(7, commande.getStatut().name());
            stmt.setTimestamp(8, Timestamp.valueOf(commande.getDate()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Commande ajoutée avec succès !");
                commandesList.add(commande); // Ajoute la commande à la liste observable
            } else {
                System.out.println("Erreur lors de l'ajout de la commande.");
            }
        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout de la commande : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Commande> readAll2() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commande";
        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setId(rs.getInt("id"));
                commande.setPrix(rs.getFloat("prix"));
                commande.setLocalisation(rs.getString("localisation"));
                commande.setTelephone(rs.getString("telephone"));
                commande.setMail(rs.getString("mail"));
                commande.setNombre(rs.getInt("nombre"));
                commande.setTotal(rs.getFloat("total"));
                commande.setDate(rs.getTimestamp("date").toLocalDateTime());

                // Correction de l'affectation du statut
                String statutStr = rs.getString("statut");
                if (statutStr != null) {
                    try {
                        commande.setStatut(StatutCommande.valueOf(statutStr)); // Convertir la chaîne en énumération
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valeur invalide pour statut: " + statutStr);
                        commande.setStatut(StatutCommande.en_attente); // Valeur par défaut en cas d'erreur
                    }
                } else {
                    commande.setStatut(StatutCommande.en_attente); // Valeur par défaut si le statut est null
                }

                commandes.add(commande); // Ajouter la commande à la liste
            }
        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la lecture des commandes : " + e.getMessage());
            throw e;
        }
        return commandes;
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM commande WHERE id = ?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, id);
            int rowsAffected = ste.executeUpdate();
            if (rowsAffected == 0) {
                afficherMessage(Alert.AlertType.ERROR, "Erreur", "Aucune commande trouvée avec l'ID : " + id);
            } else {
                afficherMessage(Alert.AlertType.INFORMATION, "Succès", "Commande supprimée avec succès.");
                // Supprimer la commande de la liste observable
                commandesList.removeIf(c -> c.getId() == id);
            }
        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la suppression de la commande : " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void modifier(int id, String nom) throws SQLException {

    }

    @Override
    public void modifier1(int id, String nom, String description, String ref, String photo, double prix, int quantite, String categorie) throws SQLException {

    }


    @Override
    public List<Categorie> readAll() throws SQLException {
        return List.of();
    }

    @Override
    public List<Produit> readAll1() throws SQLException {
        return List.of();
    }

    @Override
    public void modifier1(Commande updatedCommande) {

    }

    @Override
    public void modifier1(Produit updatedProduit) {

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
    public void modifierStatut(int id, StatutCommande statut) throws SQLException {
        String sql = "UPDATE commande SET statut = ? WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, statut.name());  // Utilisation du nom de l'énumération pour le statut
            st.setInt(2, id);
            int rows = st.executeUpdate();
            if (rows > 0) {
                cnx.commit(); // Commit explicitement les changements
                System.out.println("Commande modifiée avec succès !");

                // Mise à jour du statut de la commande dans la liste observable
                for (int i = 0; i < commandesList.size(); i++) {
                    Commande c = commandesList.get(i);
                    if (c.getId() == id) {
                        // Mettre à jour le statut de la commande dans la liste
                        c.setStatut(statut);
                        commandesList.set(i, c);  // Remplacer l'élément modifié dans la liste
                        break;
                    }
                }
            } else {
                System.out.println("Aucune commande trouvée avec l'ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du statut de la commande : " + e.getMessage());
        }
    }



    public void modifier3(Commande updatedCommande) {
        String sql = "UPDATE commande SET prix = ?, localisation = ?, telephone = ?, mail = ?, nombre = ?, total = ?, statut = ? WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setFloat(1, updatedCommande.getPrix());
            st.setString(2, updatedCommande.getLocalisation());
            st.setString(3, updatedCommande.getTelephone());
            st.setString(4, updatedCommande.getMail());
            st.setInt(5, updatedCommande.getNombre());
            st.setFloat(6, updatedCommande.getTotal());
            st.setString(7, updatedCommande.getStatut().name());
            st.setInt(8, updatedCommande.getId());

            int rows = st.executeUpdate();
            if (rows > 0) {
                cnx.commit(); // Commit explicitement les changements
                System.out.println("Commande modifiée avec succès !");

                // Mise à jour de la commande dans la liste observable
                for (int i = 0; i < commandesList.size(); i++) {
                    Commande c = commandesList.get(i);
                    if (c.getId() == updatedCommande.getId()) {
                        // Mettre à jour les valeurs de la commande dans la liste
                        commandesList.set(i, updatedCommande);
                        break;
                    }
                }
            } else {
                System.out.println("Aucune commande trouvée avec l'ID " + updatedCommande.getId());
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la commande : " + e.getMessage());
        }
    }

    // Méthode d'affichage d'alertes
    private void afficherMessage(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<Commande> recuperer() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commande";

        try (Connection cnx = MyDataBase.getConnection();
             Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                float prix = rs.getFloat("prix");
                String localisation = rs.getString("localisation");
                String telephone = rs.getString("telephone");
                String mail = rs.getString("mail");
                int nombre = rs.getInt("nombre");
                float total = rs.getFloat("total");
                // Récupération de la date et conversion en LocalDateTime
                LocalDateTime date = rs.getTimestamp("date") != null ? rs.getTimestamp("date").toLocalDateTime() : null;

// Récupération du statut et conversion en énumération
                String statutStr = rs.getString("statut");
                StatutCommande statut = statutStr != null ? StatutCommande.valueOf(statutStr) : null;


                // Création de l'objet Commande
                Commande commande = new Commande(id, prix, localisation, telephone, mail, nombre, total, date, statut);
                System.out.println("Commande récupérée : " + commande);

                commandes.add(commande);  // Ajout de la commande à la liste
            }
        }

        return commandes;
    }
}
