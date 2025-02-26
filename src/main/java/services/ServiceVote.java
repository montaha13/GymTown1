package services;

import tools.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceVote {
    private Connection connection;

    public ServiceVote() {
        this.connection = MyDataBase.getInstance().getCnx(); // Utiliser getCnx() pour obtenir la connexion
    }

    public void saveVote(int evenementId, int utilisateurId, int note) throws SQLException {
        String query = "INSERT INTO votes (evenement_id, utilisateur_id, note) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, evenementId);
            ps.setInt(2, utilisateurId);
            ps.setInt(3, note);
            ps.executeUpdate();
        }
    }

    public double getAverageRating(int evenementId) throws SQLException {
        String query = "SELECT AVG(note) FROM votes WHERE evenement_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, evenementId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0; // Retourne 0.0 si aucune note n'existe
    }
}