package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Produit;
import models.CategorieEnum;
import services.ServiceProduit;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterProduitController {
    private final ServiceProduit sp = new ServiceProduit();

    @FXML
    private TextField nomTF;
    @FXML
    private TextField descriptionTF;
    @FXML
    private TextField refTF;
    @FXML
    private TextField photoTF;
    @FXML
    private TextField prixTF;
    @FXML
    private TextField quantiteTF;
    @FXML
    private TextField categorieTF;

    @FXML
    void ajouterproduit(ActionEvent event) {
        String nom = nomTF.getText().trim();
        String description = descriptionTF.getText().trim();
        String ref = refTF.getText().trim();
        String photo = photoTF.getText().trim();
        String prixStr = prixTF.getText().trim();
        String quantiteStr = quantiteTF.getText().trim();
        String categorieStr = categorieTF.getText().trim();

        // Vérifier si les champs sont vides
        if (nom.isEmpty() || description.isEmpty() || ref.isEmpty() || photo.isEmpty() ||
                prixStr.isEmpty() || quantiteStr.isEmpty() || categorieStr.isEmpty()) {
            afficherMessage(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            float prix = Float.parseFloat(prixStr);
            int quantite = Integer.parseInt(quantiteStr);
            CategorieEnum categorie = CategorieEnum.valueOf(categorieStr.toUpperCase());

            // Ajouter le produit
            sp.ajouter(new Produit(photo, description, nom, ref, quantite, prix, categorie));

            // Afficher un message de succès
            afficherMessage(Alert.AlertType.INFORMATION, "Succès", "Produit ajouté avec succès !");

            // Charger la fenêtre AfficherCategorie.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherProduit.fxml"));
            try {
                Parent root = loader.load();
                AfficherProduitController ap = loader.getController();
                ap.setRnom(nom);
                ap.setRdescription(description);
                ap.setRref(ref);
                ap.setRphoto(photo);
                ap.setRprix(prixStr);
                ap.setRquantite(quantiteStr);
                ap.setRcategorie(categorieStr);
                ap.setRlist(sp.readAllProduits().toString());
                nomTF.getScene().setRoot(root);
                descriptionTF.getScene().setRoot(root);
               refTF.getScene().setRoot(root);
                photoTF.getScene().setRoot(root);
                prixTF.getScene().setRoot(root);
                quantiteTF.getScene().setRoot(root);
               categorieTF.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }





        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", e.getMessage());
        } catch (NumberFormatException e) {
            afficherMessage(Alert.AlertType.ERROR, "Format invalide", "Veuillez entrer un prix et une quantité valides.");
        } catch (IllegalArgumentException e) {
            afficherMessage(Alert.AlertType.ERROR, "Catégorie invalide", "Veuillez entrer une catégorie valide.");
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
