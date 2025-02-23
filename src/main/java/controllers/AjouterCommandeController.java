package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Commande;
import models.Produit;
import models.StatutCommande;
import services.ServiceCommande;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AjouterCommandeController {
    private final ServiceCommande sc = new ServiceCommande();

    @FXML
    private TextField nombreTF;
    @FXML
    private TextField localisationTF;
    @FXML
    private TextField mailTF;
    @FXML
    private TextField dateTF;
    @FXML
    private TextField prixTF;
    @FXML
    private TextField totalTF;


    @FXML
    void initialize() {
        // Initialiser les composants si nécessaire
    }

    @FXML
    void ajouterCommande(ActionEvent event) {
        String nombreStr = nombreTF.getText().trim();
        String localisation = localisationTF.getText().trim();
        String mail = mailTF.getText().trim();
        String date = dateTF.getText().trim();
        String prixStr = prixTF.getText().trim();
        String totalStr = totalTF.getText().trim();


        // Vérifier si les champs sont vides
        if (nombreStr.isEmpty() || localisation.isEmpty() || mail.isEmpty() || date.isEmpty() ||
                prixStr.isEmpty() || totalStr.isEmpty() ) {
            afficherMessage(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int nombre = Integer.parseInt(nombreStr);
            float prix = Float.parseFloat(prixStr);
            float total = Float.parseFloat(totalStr);

            // Créer et ajouter la commande
            Produit produit = null /* votre objet Produit */;
             /* le nombre de produits */;
             // Localisation, à ajuster si nécessaire
            String telephone = "";  // Numéro de téléphone, à ajuster si nécessaire
              // Adresse email, à ajuster si nécessaire
            StatutCommande statutCommande = StatutCommande.en_attente; // Remplacer par un statut valide

            Commande commande = new Commande(produit, nombre, localisation, telephone, mail, statutCommande);

// Ajouter la commande
            sc.ajouter(commande);

            sc.ajouter(commande);

            // Afficher un message de succès
            afficherMessage(Alert.AlertType.INFORMATION, "Succès", "Commande ajoutée avec succès !");

            // Charger le fichier FXML de la liste des commandes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeCommandes.fxml"));
            try {
                Parent root = loader.load();
                Stage stage = (Stage) nombreTF.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            afficherMessage(Alert.AlertType.ERROR, "Format invalide", "Veuillez entrer des valeurs numériques valides.");
        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue avec la base de données.");
        }
    }

    private void afficherMessage(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void annulerCommande(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeProduits.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
