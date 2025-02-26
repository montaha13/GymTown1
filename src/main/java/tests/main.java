package tests;

import Models.*;
import services.ServiceEvenement;
import services.ServiceParticipation;
import tools.MyDataBase;

import java.sql.SQLException;
import java.util.List;

public class main {
    public static void main(String[] args) throws SQLException {
        MyDataBase md = MyDataBase.getInstance();

        // Création des services
        ServiceEvenement ee = new ServiceEvenement();
        ServiceParticipation sp = new ServiceParticipation();

        // Création d'un événement avec le nombre de places
        Evenement e1 = new Evenement(
                "Conférence", // nom
                "Tunis", // localisation
                "image.jpg", // photo
                "Une conférence sur la technologie", // description
                "2025-03-10", // dateDebut
                "2025-03-12", // dateFin
                100.0, // frais
                Statut.A_VENIR, // statut
                200 // nombreDePlaces
        );

        // Création des objets Utilisateur et Evenement
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1);  // Assigner l'ID de l'utilisateur (vous pouvez récupérer l'utilisateur depuis la base si nécessaire)

        Evenement evenement = new Evenement();
        evenement.setId(2);  // Assigner l'ID de l'événement (vous pouvez récupérer l'événement depuis la base si nécessaire)

        // Création de la participation avec l'utilisateur et l'événement existants
        Participation participation = new Participation(
                utilisateur,
                evenement,
                StatutP.EN_ATTENTE,
                "Je souhaite une place près de la scène.", // commentaire
                2 // nombreDePlacesReservees
        );

        try {
            // Ajouter une participation
            sp.ajouter(participation);
            System.out.println("Participation ajoutée avec succès");

            // Récupérer toutes les participations
            List<Participation> participations = sp.readAll();
            System.out.println("Liste des participations :");
            for (Participation p : participations) {
                System.out.println(p);
            }

            // Modifier une participation
            participation.setCommentaire("Je souhaite annuler une place.");
            participation.setNombreDePlacesReservees(1);
            sp.modifier(participation);
            System.out.println("Participation modifiée avec succès");

            // Supprimer une participation
            sp.supprimer(participation.getId());
            System.out.println("Participation supprimée avec succès");

            // Récupérer tous les événements après suppression
            System.out.println("Événements récupérés après suppression:");
            System.out.println(ee.recuperer());
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'opération: " + e.getMessage());
        }
    }
}