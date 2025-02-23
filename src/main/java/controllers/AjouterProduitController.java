package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Produit;
import models.CategorieEnum;
import services.ServiceProduit;
import javafx.scene.image.ImageView;

import javafx.scene.image.Image;


import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Button btnBrowser;
    private FileChooser fileChooser;
    private File file;
    private Stage stage;
    @FXML
    private ComboBox<String> categorieComboBox;
    @FXML
    private AnchorPane anchorPane;
    private Desktop deskTop=Desktop.getDesktop();
    @FXML
    private ImageView imageView;
    private Image image;
    private FileInputStream fis;
    @FXML

    private void initialize() {

        // Remplir la ComboBox avec les valeurs de CategorieEnum
        for (CategorieEnum categorie : CategorieEnum.values()) {
            categorieComboBox.getItems().add(categorie.name());
        }
fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All files","**"),
                new FileChooser.ExtensionFilter("Image","*.png","*.jpg","*.gif"),
                new FileChooser.ExtensionFilter("Text File","*.txt")

        );
        // Debug : vérifier les éléments ajoutés dans le ComboBox
        System.out.println("Valeurs de la ComboBox : " + categorieComboBox.getItems());
    }


    @FXML
    void ajouterproduit(ActionEvent event) {
        String nom = nomTF.getText().trim();
        String description = descriptionTF.getText().trim();
        String ref = refTF.getText().trim();
        String photo = photoTF.getText().trim();  // Récupérer l'URL du fichier image
        String prixStr = prixTF.getText().trim();
        String quantiteStr = quantiteTF.getText().trim();
        String categorieStr = categorieComboBox.getValue();

        // Vérification de la catégorie récupérée
        System.out.println("Catégorie sélectionnée: " + categorieStr); // Debug

        // Vérifier si les champs sont vides
        if (nom.isEmpty() || description.isEmpty() || ref.isEmpty() || photo.isEmpty() ||
                prixStr.isEmpty() || quantiteStr.isEmpty() || categorieStr == null || categorieStr.isEmpty()) {
            afficherMessage(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            float prix = Float.parseFloat(prixStr);
            int quantite = Integer.parseInt(quantiteStr);

            // Utilisation directe de la valeur sélectionnée dans le ComboBox
            CategorieEnum categorie = CategorieEnum.valueOf(categorieStr.toUpperCase());

            // Ajouter le produit avec l'URL de l'image
            sp.ajouter(new Produit(photo, description, nom, ref, quantite, prix, categorie));

            // Afficher un message de succès
            afficherMessage(Alert.AlertType.INFORMATION, "Succès", "Produit ajouté avec succès !");

            // Charger la liste des produits après ajout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeProduits.fxml"));
            try {
                Parent root = loader.load();
                AfficherProduitController ap = loader.getController();
                ap.loadProduits();
                // Changer la scène actuelle
                Stage stage = (Stage) nomTF.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } catch (NumberFormatException e) {
            afficherMessage(Alert.AlertType.ERROR, "Format invalide", "Veuillez entrer un prix et une quantité valides.");
        } catch (IllegalArgumentException e) {
            afficherMessage(Alert.AlertType.ERROR, "Catégorie invalide", "Veuillez entrer une catégorie valide.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    @FXML
    private void handleBrowser(ActionEvent event) throws IOException {
        stage = (Stage) anchorPane.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            // Récupérer l'URL du fichier et l'afficher dans le champ 'photoTF'
            String fileUrl = file.toURI().toString();
            photoTF.setText(fileUrl);  // Afficher l'URL dans le TextField photoTF

            // Afficher l'image dans le ImageView
            image = new Image(file.toURI().toString(), imageView.getFitHeight(), imageView.getFitHeight(), true, true);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
        }
    }

    @FXML
    private void annulerproduit(ActionEvent event) {
        try {
            // Charger le fichier FXML de AjouterProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeProduits.fxml"));
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
