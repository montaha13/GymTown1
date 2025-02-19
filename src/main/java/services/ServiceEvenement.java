package services;

import Models.Evenement;
import Models.Statut;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements IService<Evenement> {

    private Connection cnx;

    public ServiceEvenement() throws SQLException {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Evenement evenement) throws SQLException {
        String sql = "INSERT INTO evenement (nom, localisation, photo, description, dateDebut, dateFin,frais, statut) VALUES (?, ?, ?, ?, ?, ?,?, ?)";
        PreparedStatement statement = cnx.prepareStatement(sql);
        statement.setString(1, evenement.getNom());
        statement.setString(2, evenement.getLocalisation());
        statement.setString(3, evenement.getPhoto());
        statement.setString(4, evenement.getDescription());
        statement.setString(5, evenement.getDateDebut());
        statement.setString(6, evenement.getDateFin());
        statement.setString(7, String.valueOf(evenement.getFrais()));
        statement.setString(8, evenement.getStatut().name());
        statement.executeUpdate();
        System.out.println("Événement ajouté avec succès");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM evenement WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();
        System.out.println("evenement supprimée avec succès");
    }




    @Override
    public void modifier( Evenement evenement) throws SQLException {
        String sql = "UPDATE evenement SET nom = ?, localisation = ?, photo = ?, description = ?, dateDebut = ?, dateFin = ?, frais = ?, statut = ? WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(sql);
        st.setString(1, evenement.getNom());
        st.setString(2, evenement.getLocalisation());
        st.setString(3, evenement.getPhoto());
        st.setString(4, evenement.getDescription());
        st.setString(5, evenement.getDateDebut());
        st.setString(6, evenement.getDateFin());
        st.setDouble(7, evenement.getFrais());
        st.setString(8, evenement.getStatut().name());
        st.setInt(9, evenement.getId());
        st.executeUpdate();
        System.out.println("Événement modifié avec succès");
    }


    @Override
    public List<Evenement> recuperer() throws SQLException {
        String sql = "SELECT * FROM evenement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
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
            e.setDateFin(rs.getString("frais"));
            e.setStatut(Statut.fromString(rs.getString("statut"))); // Correction ici


            evenements.add(e); // Correction ici
        }

        return evenements;
    }
    public List<Evenement> readAll() throws SQLException {
        List<Evenement> list = new ArrayList<>();
        String req = "SELECT * FROM Evenement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Evenement e = new Evenement();
            e.setId(rs.getInt("id"));  // Assurez-vous que cette ligne existe !
            e.setNom(rs.getString("nom"));
            e.setLocalisation(rs.getString("localisation"));
            e.setPhoto(rs.getString("photo"));
            e.setDescription(rs.getString("description"));
            e.setDateDebut(rs.getString("dateDebut"));
            e.setDateFin(rs.getString("dateFin"));
            e.setFrais(rs.getDouble("frais"));
            e.setStatut(Statut.valueOf(rs.getString("statut"))); // Utilisation de l'énumération

            list.add(e);
        }

        return list;
    }
    }


