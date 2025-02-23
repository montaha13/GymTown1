package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Produit;
import services.ServiceProduit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AffProduitController {

    @FXML
    private FlowPane productContainer;

    @FXML
    private Button btnListeCategories, btnAjouterProduit;

    private final ServiceProduit produitService = new ServiceProduit(); // Service pour récupérer les produits

    @FXML
    public void initialize() throws SQLException {
        afficherProduits();
    }


    private void afficherProduits() throws SQLException {
        // Récupérer la liste des produits
        List<Produit> produits = produitService.readAll1();

        // Ajouter chaque produit dans le FlowPane
        for (Produit produit : produits) {
            VBox productBox = creerCarteProduit(produit);
            productContainer.getChildren().add(productBox);
        }
    }

    private VBox creerCarteProduit(Produit produit) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 10; "
                + "-fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0.5, 0, 2);");

        // Vérifier si l'image existe avant de la charger
        Image image = null;
        try {
            String photoPath = produit.getPhoto();
            if (photoPath != null && !photoPath.isEmpty()) {
                image = new Image(photoPath); // Utiliser "file:" pour les chemins locaux
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }

        // Si l'image est valide, l'afficher dans l'ImageView, sinon afficher une image par défaut
        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        if (image != null) {
            imageView.setImage(image);
        } else {
            // Si l'image n'est pas trouvée, afficher une image par défaut
            imageView.setImage(new Image("file:\\C:\\Users\\MSI\\Pictures\\Saved Pictures\\OIP.jfif"));
        }

        Text nomText = new Text("Nom : " + produit.getNom());
        nomText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Text prixText = new Text("Prix : " + produit.getPrix() + " €");
        prixText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Button btnDetails = new Button("Voir Détails");
        btnDetails.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-background-radius: 5;");

        // Passer le produit à la méthode Detail
        btnDetails.setOnAction(event -> Detail(event, produit));

        box.getChildren().addAll(imageView, nomText, prixText, btnDetails);
        return box;
    }


    @FXML
    private void listeCategories() {
        System.out.println("Affichage de la liste des catégories...");
        // Redirection vers l'interface des catégories
    }

    @FXML
    private void ajouterProduit() {
        System.out.println("Ajout d'un nouveau produit...");
        // Redirection vers l'ajout d'un produit
    }

    @FXML
    private void Detail(ActionEvent event, Produit produit) {
        try {
            // Chargement de la vue AfficherProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherProduit.fxml"));
            Parent root = loader.load();

            // Vérification du contrôleur
            AfficherProduitController controller = loader.getController();
            if (controller != null) {
                controller.setProduit(produit);
            } else {
                System.err.println("Erreur : Impossible de récupérer le contrôleur de AfficherProduit.fxml.");
                return;
            }

            // Récupération et mise à jour de la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de AfficherProduit.fxml : " + e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.err.println("Erreur : Vérifiez que AfficherProduit.fxml utilise bien AfficherProduitController.");
            e.printStackTrace();
        }
    }
}
