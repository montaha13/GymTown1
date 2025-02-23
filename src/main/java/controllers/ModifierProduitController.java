package controllers;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import models.Produit;
import models.CategorieEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceProduit;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ModifierProduitController {

    private final ServiceProduit se = new ServiceProduit();
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
    private TextField mid;
    @FXML
    private TextField mnom;
    @FXML
    private TextField mdescription;
    @FXML
    private TextField mref;
    @FXML
    private TextField mphoto;
    @FXML
    private TextField mprix;
    @FXML
    private TextField mquantite;
  // ComboBox pour la catégorie
    // ImageView pour afficher l'image du produit
  private FileChooser fileChooser;
    private File file;
    private Stage stage;
    private Produit produit; // Le produit à modifier
    private int produitId;   // L'ID du produit à modifier
       // Pour accéder au stage

    public ModifierProduitController() throws SQLException {
    }

    // Méthode d'initialisation appelée après le chargement du FXML
    @FXML
    public void initialize() {
        // Remplir le ComboBox avec les noms des catégories de l'énumération
        categorieComboBox.setItems(FXCollections.observableArrayList(
                Arrays.stream(CategorieEnum.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        ));
    }

    // Méthode permettant de recevoir le produit à modifier et de remplir les champs de l'interface
    public void setProduit(Produit produit) {
        if (produit == null) {
            System.out.println("Produit NULL !");
            return;
        }
        this.produit = produit;
        this.produitId = produit.getId();
        mid.setText(String.valueOf(produit.getId()));
        mnom.setText(produit.getNom());
        mdescription.setText(produit.getDescription());
        mref.setText(produit.getRef());
        mphoto.setText(produit.getPhoto());
        mprix.setText(String.valueOf(produit.getPrix()));
        mquantite.setText(String.valueOf(produit.getQuantite()));
        // Sélectionner la catégorie correspondante dans le ComboBox
        categorieComboBox.getSelectionModel().select(produit.getCategorie().name());

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

    // Méthode appelée lors du clic sur le bouton "Modifier"


    // Retourner à la liste des produits après modification
    private void retournerALaListe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeProduits.fxml"));
            Parent root = loader.load();
            ListeProduitController controller = loader.getController();
            controller.loadProduits();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner à la liste des produits.");
            e.printStackTrace();
        }
    }

    // Affichage d'une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour annuler et revenir à la liste
    @FXML
    private void annulerProduit(ActionEvent event) {
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
    @FXML
    void updateProduit(ActionEvent event) throws SQLException {
        try {
            // Vérifier que la catégorie n'est pas vide
            if (categorieComboBox.getValue() == null || categorieComboBox.getValue().isEmpty()) {
                showAlert("Erreur", "Le champ 'Catégorie' ne peut pas être vide.");
                return;
            }

            // Vérifier si les autres champs sont remplis (par exemple, nom, prix, etc.)
            if (mnom.getText().isEmpty() || mprix.getText().isEmpty() || mquantite.getText().isEmpty()) {
                showAlert("Erreur", "Tous les champs obligatoires doivent être remplis.");
                return;
            }

            // Créer un objet Produit mis à jour avec les nouvelles valeurs
            Produit updatedProduit = new Produit();
            updatedProduit.setId(produitId);
            updatedProduit.setNom(mnom.getText());
            updatedProduit.setDescription(mdescription.getText());
            updatedProduit.setRef(mref.getText());
            updatedProduit.setPhoto(mphoto.getText());  // Assurez-vous que le chemin est correct
            updatedProduit.setPrix(Float.parseFloat(mprix.getText()));
            updatedProduit.setQuantite(Integer.parseInt(mquantite.getText()));
            updatedProduit.setCategorie(CategorieEnum.valueOf(categorieComboBox.getValue().toUpperCase()));

            // Appeler le service pour modifier le produit
            se.modifier1(updatedProduit);

            showAlert("Succès", "Le produit a été modifié avec succès.");
            retournerALaListe(event);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix et la quantité doivent être des nombres valides.");
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBrowser(ActionEvent event) throws IOException {
        stage = (Stage) anchorPane.getScene().getWindow();
        fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            // Récupérer l'URL du fichier et l'afficher dans le champ 'photoTF'
            String fileUrl = file.toURI().toString();
            mphoto.setText(fileUrl);  // Afficher l'URL dans le TextField photoTF

            // Afficher l'image dans le ImageView
            image = new Image(fileUrl, imageView.getFitHeight(), imageView.getFitHeight(), true, true);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
        }
    }

}
