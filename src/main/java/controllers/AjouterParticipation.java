package controllers;

import Models.Participation;
import Models.StatutP;
import Models.Utilisateur;
import Models.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import services.ServiceParticipation;
import services.ServiceEvenement;

import java.sql.SQLException;

public class AjouterParticipation {

    @FXML
    private TextField nombreDePlacesReserveesField;

    @FXML
    private TextArea commentaireField;

    private ServiceParticipation sp; // ServiceParticipation instance
    private ServiceEvenement serviceEvenement; // ServiceEvenement instance
    private Evenement evenement;

    // Constructeur par défaut
    public AjouterParticipation() {
        try {
            // Les services sont initialisés ici
            this.sp = new ServiceParticipation();
            this.serviceEvenement = new ServiceEvenement(); // Ajoutez également à ce niveau si nécessaire
        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données : " + e.getMessage());
        }
    }

    // Méthode pour définir l'événement
    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    @FXML
    public void participer(ActionEvent event) {
        String nombreDePlacesReserveesStr = nombreDePlacesReserveesField.getText().trim();
        String commentaire = commentaireField.getText().trim();

        if (nombreDePlacesReserveesStr.isEmpty()) {
            afficherMessage(Alert.AlertType.WARNING, "Champ vide", "Veuillez saisir le nombre de places réservées.");
            return;
        }

        try {
            int nombreDePlacesReservees = Integer.parseInt(nombreDePlacesReserveesStr);
            Utilisateur utilisateur = getCurrentUser(); // Méthode pour obtenir l'utilisateur courant

            if (evenement == null) {
                afficherMessage(Alert.AlertType.ERROR, "Erreur", "Aucun événement sélectionné.");
                return;
            }

            Participation participation = new Participation(
                    utilisateur,
                    evenement,
                    StatutP.EN_ATTENTE,
                    commentaire,
                    nombreDePlacesReservees
            );

            sp.ajouter(participation); // Appel pour ajouter la participation

            // Vérifiez et mettez à jour le statut de l'événement si nécessaire
            serviceEvenement.verifierEtMettreAJourStatut(evenement);

            afficherMessage(Alert.AlertType.INFORMATION, "Succès", "Participation ajoutée avec succès!");

        } catch (NumberFormatException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur", "Le nombre de places réservées doit être un nombre valide.");
        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout de la participation : " + e.getMessage());
        }
    }

    private Utilisateur getCurrentUser() {
        // Implémentez votre logique pour obtenir l'utilisateur actuel
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1); // Pour l'exemple, utilisez un ID quelconque
        utilisateur.setNom("Test User");
        return utilisateur;
    }

    private void afficherMessage(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}