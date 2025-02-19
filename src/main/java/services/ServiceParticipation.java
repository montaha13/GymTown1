package services;

import Models.Evenement;
import Models.Participation;
import Models.StatutP;
import Models.Utilisateur;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceParticipation implements IService<Participation> {

    private Connection cnx;

    public ServiceParticipation() throws SQLException {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Participation participation) throws SQLException {
        String sql = "INSERT INTO participation (utilisateur_id, evenement_id, dateParticipation, montantPaye, statutP) VALUES (?,?, ?, ?, ?)";
        PreparedStatement statement = cnx.prepareStatement(sql);
        statement.setInt(1, participation.getUtilisateur().getId());
        statement.setInt(2, participation.getEvenement().getId());

        statement.setString(5, participation.getStatutP().name());
        statement.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM participation WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        System.out.println("Participation supprimée avec succès");
    }

    @Override
    public void modifier(Participation participation) throws SQLException {
        String sql = "UPDATE participation SET statutP = ? WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, participation.getStatutP().name());
        st.setInt(2, participation.getId());  // Assurez-vous que la classe Participation a un attribut `id`
        st.executeUpdate();
        System.out.println("Participation modifiée avec succès");
    }

    @Override
    public List<Participation> recuperer() throws SQLException {
        String sql = "SELECT * FROM participation";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<Participation> participations = new ArrayList<>();

        while (rs.next()) {
            Participation p = new Participation();
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(rs.getInt("utilisateur_id"));
            p.setUtilisateur(utilisateur);

            Evenement evenement = new Evenement();
            evenement.setId(rs.getInt("evenement_id"));
            p.setEvenement(evenement);
            p.setStatutP(StatutP.valueOf(rs.getString("statutP"))); // Utilisation de valueOf pour convertir le String en StatutP

            participations.add(p);
        }

        return participations;
    }
}
