package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.Produit; // Modèle de produit
import javafx.stage.Stage;

public class AfficherProduit12Controller {

    @FXML
    private ImageView productImage;
    @FXML
    private Text productName;
    @FXML
    private Text productDescription;
    @FXML
    private Text productRef;
    @FXML
    private Text productPrix;
    @FXML
    private Text productQuantite;
    @FXML
    private Text productCategorie;
    @FXML
    private Button btnRetour;

    private Produit produitSelectionne;

    // Méthode pour afficher les détails du produit sélectionné
    public void setProduitDetails(Produit produit) {
        this.produitSelectionne = produit;
        productName.setText(produit.getNom());
        productDescription.setText(produit.getDescription());
        productRef.setText("Réf: " + produit.getRef());
        productPrix.setText("Prix: " + produit.getPrix() + " €");
        productQuantite.setText("Quantité: " + produit.getQuantite());
        productCategorie.setText("Catégorie: " + produit.getCategorie());


    }

    // Bouton de retour à la liste des produits
    @FXML
    private void handleRetour() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close(); // Ferme la fenêtre des détails
    }
}
