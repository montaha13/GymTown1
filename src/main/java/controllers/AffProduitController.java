package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Produit;
import services.ServiceProduit;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AffProduitController {

    @FXML
    private FlowPane productContainer;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnListeCategories, btnAjouterProduit;

    private final ServiceProduit produitService = new ServiceProduit(); // Service pour récupérer les produits

    @FXML
    public void initialize() {
        try {
            afficherProduits();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors du chargement des produits.");
        }

        // Ajouter un listener pour le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue));
    }

    private void afficherProduits() throws SQLException {
        List<Produit> produits = produitService.readAll1();
        updateProductDisplay(produits);
    }

    private VBox creerCarteProduit(Produit produit) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 10; "
                + "-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0.5, 0, 2);");

        Image image = null;
        try {
            String photoPath = produit.getPhoto();
            if (photoPath != null && !photoPath.isEmpty()) {
                image = new Image(photoPath);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        if (image != null) {
            imageView.setImage(image);
        } else {
            imageView.setImage(new Image("file:default_image_path.png")); // Remplacez par le chemin de votre image par défaut
        }

        Text nomText = new Text("Nom : " + produit.getNom());
        nomText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Text prixText = new Text("Prix : " + produit.getPrix() + " €");
        prixText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Button btnDetails = new Button("Voir Détails");
        btnDetails.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-background-radius: 5;");
        btnDetails.setOnAction(event -> Details(event, produit));

        box.getChildren().addAll(imageView, nomText, prixText, btnDetails);
        return box;
    }
    private void Details(ActionEvent event, Produit produit) {
        try {
            // Charger le fichier FXML pour la vue "AfficherProduit"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherProduit.fxml"));
            Parent root = loader.load();

            // Passer le produit à la vue "AfficherProduit"
            AfficherProduitController afficherProduitController = loader.getController();
            afficherProduitController.setProduit(produit);

            // Ouvrir la nouvelle scène
            Stage stage = new Stage();
            stage.setTitle("Détails du Produit");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ouverture des détails du produit.");
        }
    }
    private void handleSearch(String searchQuery) {
        List<Produit> allProduits;
        try {
            allProduits = produitService.readAll1();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de la récupération des données.");
            return;
        }

        List<Produit> filteredProduits = allProduits.stream()
                .filter(produit -> produit.getNom().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());

        updateProductDisplay(filteredProduits);
    }

    private void updateProductDisplay(List<Produit> produits) {
        productContainer.getChildren().clear();
        for (Produit produit : produits) {
            VBox productBox = creerCarteProduit(produit);
            productContainer.getChildren().add(productBox);
        }
    }

    private void showAlert(String message) {
        // Implémentez cette méthode pour afficher une alerte à l'utilisateur avec le message fourni
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        // Implémentation de la méthode de recherche
    }

}
