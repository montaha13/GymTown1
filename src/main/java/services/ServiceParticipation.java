package services;

import Models.Evenement;
import Models.Participation;
import Models.StatutP;
import Models.Utilisateur;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceParticipation implements IService<Participation> {
    private static final Logger logger = Logger.getLogger(ServiceParticipation.class.getName());
    private Connection cnx;

    public ServiceParticipation() throws SQLException {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Participation participation) throws SQLException {
        if (participation == null) {
            logger.log(Level.WARNING, "Tentative d'ajout d'une participation nulle.");
            throw new IllegalArgumentException("La participation ne peut pas être nulle.");
        }

        if (participation.getUtilisateur() == null || participation.getEvenement() == null) {
            throw new IllegalArgumentException("L'utilisateur et l'événement ne peuvent pas être nuls.");
        }

        String sql = "INSERT INTO participation (utilisateur_id, evenement_id, statutP, commentaire, nombreDePlacesReservees) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setInt(1, participation.getUtilisateur().getId());
            statement.setInt(2, participation.getEvenement().getId());
            statement.setString(3, participation.getStatutP().name());
            statement.setString(4, participation.getCommentaire());
            statement.setInt(5, participation.getNombreDePlacesReservees());
            statement.executeUpdate();
            logger.log(Level.INFO, "Participation ajoutée avec succès.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'ajout de la participation.", e);
            throw e;
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM participation WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
            logger.log(Level.INFO, "Participation supprimée avec succès.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la suppression de la participation.", e);
            throw e;
        }
    }

    @Override
    public void modifier(Participation participation) throws SQLException {
        if (participation == null) {
            logger.log(Level.WARNING, "Tentative de modification d'une participation nulle.");
            throw new IllegalArgumentException("La participation ne peut pas être nulle.");
        }

        if (participation.getId() <= 0) {
            throw new IllegalArgumentException("L'ID de la participation doit être valide.");
        }

        String sql = "UPDATE participation SET statutP = ?, commentaire = ?, nombreDePlacesReservees = ? WHERE id = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, participation.getStatutP().name());
            st.setString(2, participation.getCommentaire());
            st.setInt(3, participation.getNombreDePlacesReservees());
            st.setInt(4, participation.getId());
            st.executeUpdate();
            logger.log(Level.INFO, "Participation modifiée avec succès.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la modification de la participation.", e);
            throw e;
        }
    }

    @Override
    public List<Participation> recuperer() throws SQLException {
        return readAll();
    }

    public List<Participation> readAll() throws SQLException {
        List<Participation> list = new ArrayList<>();
        String req = "SELECT p.*, e.nom AS nom_evenement " +
                "FROM participation p " +
                "JOIN evenement e ON p.evenement_id = e.id";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Participation p = new Participation();

                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("utilisateur_id"));
                p.setUtilisateur(utilisateur);

                Evenement evenement = new Evenement();
                evenement.setId(rs.getInt("evenement_id"));
                evenement.setNom(rs.getString("nom_evenement"));
                p.setEvenement(evenement);

                p.setId(rs.getInt("id"));
                p.setStatutP(StatutP.fromString(rs.getString("statutP")));
                p.setCommentaire(rs.getString("commentaire"));
                p.setNombreDePlacesReservees(rs.getInt("nombreDePlacesReservees"));

                list.add(p);
            }
        }

        return list;
    }

    public boolean existeParticipation(int utilisateurId, int evenementId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM participation WHERE utilisateur_id = ? AND evenement_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            stmt.setInt(2, evenementId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Participation> getParticipationsByEvent(int evenementId) throws SQLException {
        List<Participation> list = new ArrayList<>();
        String sql = "SELECT p.*, e.nom AS nom_evenement " +
                "FROM participation p " +
                "JOIN evenement e ON p.evenement_id = e.id " +
                "WHERE p.evenement_id = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, evenementId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Participation p = new Participation();

                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("utilisateur_id"));
                    p.setUtilisateur(utilisateur);

                    Evenement evenement = new Evenement();
                    evenement.setId(rs.getInt("evenement_id"));
                    evenement.setNom(rs.getString("nom_evenement"));
                    p.setEvenement(evenement);

                    p.setId(rs.getInt("id"));
                    p.setStatutP(StatutP.fromString(rs.getString("statutP")));
                    p.setCommentaire(rs.getString("commentaire"));
                    p.setNombreDePlacesReservees(rs.getInt("nombreDePlacesReservees"));

                    list.add(p);
                }
            }
        }

        return list;
    }
}