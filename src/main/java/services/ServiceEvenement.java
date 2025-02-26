package services;

import Models.Evenement;
import Models.Statut;
import Models.StatutP;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceEvenement implements IService<Evenement> {

    private Connection cnx;
    private ScheduledExecutorService scheduler;

    public ServiceEvenement() throws SQLException {
        cnx = MyDataBase.getInstance().getCnx();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::mettreAJourStatutEvenements, 0, 1, TimeUnit.DAYS); // Vérifier tous les jours
    }

    @Override
    public void ajouter(Evenement evenement) throws SQLException {
        String sql = "INSERT INTO evenement (nom, localisation, photo, description, dateDebut, dateFin, frais, statut, nombreDePlaces) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, evenement.getNom());
            statement.setString(2, evenement.getLocalisation());
            statement.setString(3, evenement.getPhoto());
            statement.setString(4, evenement.getDescription());
            statement.setString(5, evenement.getDateDebut());
            statement.setString(6, evenement.getDateFin());
            statement.setDouble(7, evenement.getFrais());
            statement.setString(8, evenement.getStatut().name());
            statement.setInt(9, evenement.getNombreDePlaces());
            statement.executeUpdate();
            System.out.println("Événement ajouté avec succès");
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM evenement WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
            System.out.println("Événement supprimé avec succès");
        }
    }

    @Override
    public void modifier(Evenement evenement) throws SQLException {
        if (evenement.getStatut() == null) {
            evenement.setStatut(Statut.A_VENIR); // Défaut ou autre
        }

        String sql = "UPDATE evenement SET nom = ?, localisation = ?, photo = ?, description = ?, dateDebut = ?, dateFin = ?, frais = ?, statut = ?, nombreDePlaces = ? WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, evenement.getNom());
            st.setString(2, evenement.getLocalisation());
            st.setString(3, evenement.getPhoto());
            st.setString(4, evenement.getDescription());
            st.setString(5, evenement.getDateDebut());
            st.setString(6, evenement.getDateFin());
            st.setDouble(7, evenement.getFrais());
            st.setString(8, evenement.getStatut().name());
            st.setInt(9, evenement.getNombreDePlaces());
            st.setInt(10, evenement.getId());
            st.executeUpdate();
            System.out.println("Événement modifié avec succès");
        }
    }

    @Override
    public List<Evenement> recuperer() throws SQLException {
        String sql = "SELECT * FROM evenement";
        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(sql)) {
            List<Evenement> evenements = new ArrayList<>();
            while (rs.next()) {
                Evenement e = new Evenement();
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setLocalisation(rs.getString("localisation"));
                e.setPhoto(rs.getString("photo"));
                e.setDescription(rs.getString("description"));
                e.setDateDebut(rs.getString("dateDebut"));
                e.setDateFin(rs.getString("dateFin"));
                e.setFrais(rs.getDouble("frais"));
                e.setStatut(Statut.fromString(rs.getString("statut")));
                e.setNombreDePlaces(rs.getInt("nombreDePlaces"));
                evenements.add(e);
            }
            return evenements;
        }
    }

    public List<Evenement> readAll() throws SQLException {
        List<Evenement> list = new ArrayList<>();
        String req = "SELECT * FROM Evenement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Evenement e = new Evenement();
            e.setId(rs.getInt("id"));
            e.setNom(rs.getString("nom"));
            e.setLocalisation(rs.getString("localisation"));
            e.setPhoto(rs.getString("photo"));
            e.setDescription(rs.getString("description"));
            e.setDateDebut(rs.getString("dateDebut"));
            e.setDateFin(rs.getString("dateFin"));
            e.setFrais(rs.getDouble("frais"));
            e.setStatut(Statut.fromString(rs.getString("statut")));
            e.setNombreDePlaces(rs.getInt("nombreDePlaces")); // Ajout de nombreDePlaces

            list.add(e);
        }

        return list;
    }

    public void mettreAJourStatutEvenements() {
        try {
            String sql = "SELECT * FROM evenement";
            try (Statement ste = cnx.createStatement();
                 ResultSet rs = ste.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String dateFinStr = rs.getString("dateFin");
                    String statutStr = rs.getString("statut");

                    if (dateFinStr != null && !dateFinStr.isEmpty()) {
                        java.sql.Date dateFin = java.sql.Date.valueOf(dateFinStr);
                        java.sql.Date aujourdHui = new java.sql.Date(System.currentTimeMillis());

                        if (dateFin.before(aujourdHui) && !Statut.TERMINE.name().equals(statutStr)) {
                            String updateSql = "UPDATE evenement SET statut = ? WHERE id = ?";
                            try (PreparedStatement updateStmt = cnx.prepareStatement(updateSql)) {
                                updateStmt.setString(1, Statut.TERMINE.name());
                                updateStmt.setInt(2, id);
                                updateStmt.executeUpdate();
                                System.out.println("Événement ID:" + id + " mis à jour à 'TERMINE'");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void verifierEtMettreAJourStatut(Evenement evenement) throws SQLException {
        // Récupérer le total des places réservées pour cet événement avec statut "Accepté"
        String sql = "SELECT SUM(nombreDePlacesReservees) AS totalReservees " +
                "FROM participation " +
                "WHERE evenement_id = ? AND statutP = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, evenement.getId());
            stmt.setString(2, StatutP.ACCEPTER.name()); // Filtrer les participations acceptées

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int totalReservees = rs.getInt("totalReservees");
                    int totalPlaces = evenement.getNombreDePlaces();

                    // Si le total des réservations atteint ou dépasse le nombre de places, marques l'événement comme complet
                    if (totalReservees >= totalPlaces) {
                        evenement.setStatut(Statut.COMPLET);
                        String updateSql = "UPDATE evenement SET statut = ? WHERE id = ?";
                        try (PreparedStatement updateStmt = cnx.prepareStatement(updateSql)) {
                            updateStmt.setString(1, Statut.COMPLET.name());
                            updateStmt.setInt(2, evenement.getId());
                            updateStmt.executeUpdate();
                            System.out.println("Événement ID:" + evenement.getId() + " mis à jour à 'COMPLET'");
                        }
                    }
                }
            }
        }
    }

}