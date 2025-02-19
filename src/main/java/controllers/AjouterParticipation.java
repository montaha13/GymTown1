package controllers;

import Models.Participation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceParticipation;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterParticipation {

    @FXML
    private TextField dateParticipationt;

    @FXML
    private TextField montantPayet;

    private ServiceParticipation sp = new ServiceParticipation();

    public AjouterParticipation() throws SQLException {
    }

    @FXML
    void participer(ActionEvent event) {

        String dateParticipation = dateParticipationt.getText().trim();
        String montantPayeStr = montantPayet.getText().trim();

        // Vérifier si les champs sont vides
        if (dateParticipation.isEmpty() || montantPayeStr.isEmpty()) {
            afficherMessage(Alert.AlertType.WARNING, "Champ vide", "Veuillez saisir tous les champs.");
            return;
        }

        try {
            double montantPaye = Double.parseDouble(montantPayeStr);
            // Créer une nouvelle participation (vous devez récupérer l'utilisateur et l'événement de manière appropriée)
            // Exemple : Utilisateur utilisateur = new Utilisateur();
            // Exemple : Evenement evenement = new Evenement();

            Participation participation = new Participation();

            // Ajouter ici l'utilisateur et l'événement à l'objet participation.

            sp.ajouter(participation);

            // Afficher un message de succès
            afficherMessage(Alert.AlertType.INFORMATION, "Succès", "Participation ajoutée avec succès!");

            // Charger la fenêtre AfficherParticipation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichrParticipation.fxml"));
            try {
                Parent root = loader.load();
                Stage stage = (Stage) dateParticipationt.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } catch (NumberFormatException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur", "Le montant payé doit être un nombre.");
        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", e.getMessage());
        }
    }

    /**
     * Méthode utilitaire pour afficher des alertes.
     *
     * @param type    Type d'alerte (INFORMATION, WARNING, ERROR)
     * @param titre   Titre de l'alerte
     * @param message Message à afficher
     */
    private void afficherMessage(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
