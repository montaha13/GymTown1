package controllers;

import models.Categorie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Produit;
import services.ServiceCategorie;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierCategorieController {

    private final ServiceCategorie se = new ServiceCategorie();

    @FXML
    private TextField mid; // Champ pour l'ID de la catégorie

    @FXML
    private TextField mcategorie; // Champ pour le nom de la catégorie

    private Categorie categorie; // L'objet catégorie à modifier
    private int categorieId; // L'ID de la catégorie à modifier

    private Stage stage;  // Pour accéder au stage

    public ModifierCategorieController() throws SQLException {
    }

    /**
     * Initialise la catégorie à modifier
     */
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        this.categorieId = categorie.getId();

        if (categorie != null) {
            mid.setText(String.valueOf(categorie.getId())); // Afficher l'ID dans le champ
            mcategorie.setText(categorie.getNom()); // Afficher le nom de la catégorie
        }
    }

    @FXML
    void Modifier(ActionEvent event) throws SQLException {
        try {
            // Vérifier si les champs sont remplis
            if (mcategorie.getText().trim().isEmpty()) {
                showAlert("Erreur", "Le champ 'Nom' ne peut pas être vide.");
                return;
            }

            // Mise à jour de l'objet catégorie
            Categorie updatedCategorie = new Categorie();
            updatedCategorie.setId(categorieId);
            updatedCategorie.setNom(mcategorie.getText());

            // Appeler le service pour modifier la catégorie
            se.modifier(updatedCategorie);

            // Afficher un message de succès
            showAlert("Succès", "La catégorie a été modifiée avec succès.");

            // Retourner à la liste
            retournerALaListe(event);

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification.");
            e.printStackTrace();
        }
    }

    /**
     * Retourner à la liste des catégories après modification
     */
    private void retournerALaListe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeCategorie.fxml")); // Vérifier le chemin correct du fichier FXML
            Parent root = loader.load();
            ListeCategorieController controller = loader.getController();
            controller.loadCategories(); // Méthode pour rafraîchir la liste

            // Obtenir le stage actuel et changer de scène
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner à la liste des catégories.");
            e.printStackTrace();
        }
    }

    /**
     * Afficher une boîte de dialogue d'alerte
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void updateCategorie(ActionEvent actionEvent) throws SQLException {
        Modifier(actionEvent);
    }
    @FXML
    private void annulercategorie(ActionEvent event) {
        try {
            // Charger le fichier FXML de AjouterProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeCategorie.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le FXML chargé
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène actuelle avec la nouvelle scène
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}
