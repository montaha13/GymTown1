package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardAdmin {

    @FXML
    private void handleGererEvenements() {
        // Rediriger vers la page de gestion des événements
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementTable.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gérer les Événements");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGererUtilisateurs() {
        // Rediriger vers la page de gestion des utilisateurs
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListParticipationAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gérer les Participations");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStatistiques() {
        // Rediriger vers la page des statistiques
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Statistiques.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistiques");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}