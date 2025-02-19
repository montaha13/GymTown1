package tests;

import Models.*;
import services.ServiceEvenement;
import services.ServiceParticipation;
import tools.MyDataBase;
import java.sql.Date;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) throws SQLException {
        MyDataBase md = MyDataBase.getInstance();


//creation des services
        ServiceEvenement ee = new ServiceEvenement();
        ServiceParticipation sp = new ServiceParticipation();

        Evenement e1 = new Evenement("Conférence", "Tunis", "image.jpg",
                "Une conférence sur la technologie", "2025-03-10", "2025-03-12", 100.0,Statut.A_VENIR);



        // Création des objets Utilisateur et Evenement
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1);  // Assigner l'ID de l'utilisateur (vous pouvez récupérer l'utilisateur depuis la base si nécessaire)

        Evenement evenement = new Evenement();
        evenement.setId(2);  // Assigner l'ID de l'événement (vous pouvez récupérer l'événement depuis la base si nécessaire)

        // Création de la participation avec l'utilisateur et l'événement existants
        Participation p1 = new Participation(utilisateur, evenement, new Date(System.currentTimeMillis()), 100.0, StatutP.EN_ATTENTE);

        try {
            // Supprimer l'événement avec ID 3
            ee.supprimer(26);
            System.out.println("Événement avec ID 3 supprimé avec succès");

            // Recuperer tous les événements après suppression
            System.out.println("Événements récupérés après suppression:");
            System.out.println(ee.recuperer());
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'opération: " + e.getMessage());
        }
    }
}