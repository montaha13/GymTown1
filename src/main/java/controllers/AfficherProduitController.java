package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Categorie;
import models.CategorieEnum;
import models.Produit;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AfficherProduitController {

    @FXML
    private Text rnom;

    @FXML
    private Text rdescription;

    @FXML
    private Text rref;

    @FXML
    private Text rphoto;

    @FXML
    private Text rprix;

    @FXML
    private Text rquantite;

    @FXML
    private Text categorieComboBox;
    @FXML
    private AnchorPane anchorPane;
    private Desktop deskTop=Desktop.getDesktop();
    @FXML
    private ImageView imageView;
    private Image image;
    private FileInputStream fis;

    private Produit produit; // Le produit à modifier
    private int produitId;   // L'ID du produit à modifier

    private Stage stage;     // Pour accéder au stage



    // Méthode d'initialisation appelée après le chargement du FXML
    @FXML
    public void initialize() {
        // Remplir le ComboBox avec les noms des catégories de l'énumération

    }

    // Méthode permettant de recevoir le produit à modifier et de remplir les champs de l'interface
    public void setProduit(Produit produit) {
        if (produit == null) {
            System.out.println("Produit NULL !");
            return;
        }
        this.produit = produit;
        this.produitId = produit.getId();

        rnom.setText(produit.getNom());
        rdescription.setText(produit.getDescription());
        rref.setText(produit.getRef());

        rprix.setText(String.valueOf(produit.getPrix()));
        rquantite.setText(String.valueOf(produit.getQuantite()));
        // Sélectionner la catégorie correspondante dans le ComboBox
        categorieComboBox.setText(produit.getCategorie().name());
        // Charger l'image si disponible
        try {
            String photoPath = produit.getPhoto();
            if (photoPath != null && !photoPath.isEmpty()) {
                Image image = new Image(photoPath);
                imageView.setImage(image);
            } else {
                // Si l'image n'est pas trouvée, afficher une image par défaut
                imageView.setImage(new Image("file:/path/to/default/image.jpg")); // Changez le chemin par une image valide par défaut
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            imageView.setImage(new Image("file:/path/to/default/image.jpg")); // Image par défaut en cas d'erreur
        }
    }




    // Setters pour chaque champ
    public void setRnom(String rnom) {
        this.rnom.setText(rnom);
    }

    public void setRdescription(String rdescription) {
        this.rdescription.setText(rdescription);
    }

    public void setRref(String rref) {
        this.rref.setText(rref);
    }

    public void setRphoto(String rphoto) {
        this.rphoto.setText(rphoto);
    }

    public void setRprix(String rprix) {
        this.rprix.setText(rprix);
    }

    public void setRquantite(String rquantite) {
        this.rquantite.setText(rquantite);
    }



    @FXML
    private void listeproduit(ActionEvent event) {
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

    public void loadProduits() {
    }
}
