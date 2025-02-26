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
import models.*;
import services.ServiceCommande;
import services.ServiceProduit;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AjouterCommandeController {

    @FXML
    private TextField nombreTF, localisationTF, mailTF, dateTF, prixTF, totalTF, statutTF, telephoneTF, produitIdTF;

    private final ServiceProduit sp = new ServiceProduit();
    private final ServiceCommande sc = new ServiceCommande();

    private int produitId;

    public void setProduitId(int produitId) {
        System.out.println("🔍 ID produit reçu : " + produitId);
        if (produitId <= 0) {
            afficherMessage(Alert.AlertType.ERROR, "ID Produit Invalide", "L'ID du produit doit être supérieur à zéro.");
            return;
        }
        this.produitId = produitId;
        chargerProduit();
    }
    @FXML
    private void initialize() {
        LocalDate today = LocalDate.now();
        dateTF.setText(today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dateTF.setEditable(false);

        // Initialiser statutTF à "EN_ATTENTE" en utilisant l'énumération
        statutTF.setText(StatutCommande.en_attente.name());

        prixTF.textProperty().addListener((observable, oldValue, newValue) -> updateTotal());
        nombreTF.textProperty().addListener((observable, oldValue, newValue) -> updateTotal());

        if (produitId > 0) {
            chargerProduit();
        }
    }


    private void chargerProduit() {
        try {
            if (produitId <= 0) {
                afficherMessage(Alert.AlertType.ERROR, "ID Invalide", "L'ID du produit est invalide.");
                return;
            }

            Optional<Produit> produitOpt = Optional.ofNullable(sp.getProduitById(produitId));
            if (produitOpt.isPresent()) {
                Produit produit = produitOpt.get();
               // produitIdTF.setText(String.valueOf(produitId));
                prixTF.setText(String.valueOf(produit.getPrix()));
                updateTotal();
            } else {
                afficherMessage(Alert.AlertType.ERROR, "Produit introuvable", "Le produit avec cet ID n'existe pas.");
            }
        } catch (SQLException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de récupérer le produit : " + e.getMessage());
        }
    }

    @FXML
    private void incrementerNombrePlus() {
        try {
            int nombre = Integer.parseInt(nombreTF.getText().trim());
            nombreTF.setText(String.valueOf(nombre + 1));
        } catch (NumberFormatException e) {
            nombreTF.setText("1");
        }
        updateTotal();
    }

    @FXML
    private void incrementerNombre() {
        try {
            int nombre = Integer.parseInt(nombreTF.getText().trim());
            if (nombre > 1) {
                nombreTF.setText(String.valueOf(nombre - 1));
            }
        } catch (NumberFormatException e) {
            nombreTF.setText("1");
        }
        updateTotal();
    }


    @FXML
    private void updateTotal() {
        try {
            float prix = prixTF.getText().trim().isEmpty() ? 0 : Float.parseFloat(prixTF.getText().trim());
            int nombre = nombreTF.getText().trim().isEmpty() ? 1 : Integer.parseInt(nombreTF.getText().trim());
            totalTF.setText(String.valueOf(prix * nombre));
        } catch (NumberFormatException e) {
            totalTF.setText("0.0");
        }
    }

    @FXML
    void ajouterCommande(ActionEvent event) {
        // Récupération des valeurs des champs
      //  String produitId1 = produitIdTF.getText().trim();
        String nombreStr = nombreTF.getText().trim();
        String localisation = localisationTF.getText().trim();
        String mail = mailTF.getText().trim();
        String date = dateTF.getText().trim();
        String prixStr = prixTF.getText().trim();
        String totalStr = totalTF.getText().trim();
        String telephone = telephoneTF.getText().trim();
        String statutStr = statutTF.getText().trim();

        // Vérification que tous les champs sont remplis
        if (nombreStr.isEmpty() || localisation.isEmpty() || mail.isEmpty() ||
                prixStr.isEmpty() || totalStr.isEmpty() || telephone.isEmpty() || statutStr.isEmpty()) {
            afficherMessage(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int nombre = Integer.parseInt(nombreStr);
            float prix = Float.parseFloat(prixStr);
            float total = Float.parseFloat(totalStr);

            // Vérification de la quantité
            if (nombre <= 0) {
                afficherMessage(Alert.AlertType.ERROR, "Quantité invalide", "La quantité doit être supérieure à zéro.");
                return;
            }
            // 🔍 **Validation du téléphone**
            if (!validerTelephone(telephone)) {
                afficherMessage(Alert.AlertType.ERROR, "Numéro de téléphone invalide", "Le téléphone doit contenir uniquement des chiffres et des caractères spéciaux (+, -, espace).");
                return;
            }

            // 🔍 **Validation de l’e-mail**
            if (!validerEmail(mail)) {
                afficherMessage(Alert.AlertType.ERROR, "Adresse e-mail invalide", "Veuillez entrer une adresse e-mail valide.");
                return;
            }

            // Vérifier si l'ID produit est inférieur ou égal à zéro
            if (produitId <= 0) {
                afficherMessage(Alert.AlertType.ERROR, "ID Produit invalide", "L'ID du produit doit être supérieur à zéro.");
                return;
            }

            // Récupérer le produit à partir de l'ID
            Optional<Produit> produitOpt = Optional.ofNullable(sp.getProduitById(produitId));
            if (produitOpt.isEmpty()) {
                afficherMessage(Alert.AlertType.ERROR, "Produit introuvable", "Le produit avec cet ID n'existe pas.");
                return;
            }

            Produit produit = produitOpt.get();

            // Vérification du statut
            StatutCommande statutCommande;
            try {
                statutCommande = StatutCommande.fromString(statutStr);
            } catch (IllegalArgumentException e) {
                afficherMessage(Alert.AlertType.ERROR, "Statut invalide", "Le statut de la commande est invalide.");
                return;
            }

            // Créer la commande avec les valeurs obtenues
            Commande commande = new Commande(produit, nombre, localisation, telephone, mail, statutCommande);
            commande.setDate(LocalDate.parse(date).atStartOfDay());
            commande.setPrix(prix);
            commande.setTotal(total);  // Assurez-vous que le total est bien défini

            // Ajouter la commande à la base de données
            sc.ajouter(commande);

            // Afficher un message de succès
            afficherMessage(Alert.AlertType.INFORMATION, "Succès", "Commande ajoutée avec succès !");
        } catch (NumberFormatException e) {
            // Gérer les erreurs de format de nombre
            afficherMessage(Alert.AlertType.ERROR, "Format invalide", "Veuillez entrer des valeurs numériques valides.");
        } catch (SQLException e) {
            // Gérer les erreurs liées à la base de données
            afficherMessage(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout de la commande : " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean validerTelephone(String telephone) {
        return telephone.matches("[0-9+\\-\\s]+");
    }

    /**
     * Vérifie si une adresse e-mail est valide.
     */
    private boolean validerEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffProduit.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherMessage(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'interface.");
        }
    }

    public void setPrix(float prix) {
        prixTF.setText(String.valueOf(prix));
    }}