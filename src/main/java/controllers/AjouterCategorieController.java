package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Categorie;
import services.ServiceCategorie;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterCategorieController {
    private final ServiceCategorie sc = new ServiceCategorie();

    @FXML
    private TextField categorieTF;

    @FXML
    void ajoutercategorie(ActionEvent event) {
        String categorieNom = categorieTF.getText().trim();

        // Vérifier si le champ est vide
        if (categorieNom.isEmpty()) {
            afficherMessage(Alert.AlertType.WARNING, "Champ vide", "Veuillez saisir un nom de catégorie.");
            return;
        }

        try {
            // Ajouter la catégorie
            sc.ajouter(new Categorie(categorieNom));

            // Afficher un message de succès
            afficherMessage(Alert.AlertType.INFORMATION, "Succès", "Catégorie ajoutée avec succès !");

            // Charger la fenêtre AfficherCategorie.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCategorie.fxml"));
            try {
                Parent root = loader.load();
                AfficherCategorieController ac = loader.getController();
                ac.setRcategorie(categorieNom);
                ac.setRlist(sc.readAll().toString());
                categorieTF.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }


            // Changer la scène actuelle

        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", e.getMessage());
        } }

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
