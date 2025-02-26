package controllers;

import Models.Evenement;
import Models.Statut;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceEvenement;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class AjouterEvenement {

    private final ServiceEvenement se = new ServiceEvenement();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextArea descriptiontf;

    @FXML
    private TextField frais;

    @FXML
    private Label fraisApercu;

    @FXML
    private TextField localisationtf;

    @FXML
    private TextField nomtf;

    @FXML
    private TextField phototf;

    @FXML
    private TextField nombreDePlacesField;

    @FXML
    private ImageView imageView;

    private FileChooser fileChooser;
    private File file;

    public AjouterEvenement() throws SQLException {
    }

    @FXML
    private void initialize() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Aperçu dynamique des frais
        frais.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double montant = Double.parseDouble(newValue);
                fraisApercu.setText(String.format("%.2f DT", montant));
            } catch (NumberFormatException e) {
                fraisApercu.setText("0 DT"); // Réinitialise en cas d'erreur
            }
        });
    }

    @FXML
    private void handleBrowser(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String fileUrl = file.toURI().toString();
            phototf.setText(fileUrl); // Met à jour le champ de texte avec l'URL de l'image

            // Affiche l'image dans l'ImageView
            Image image = new Image(fileUrl);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
        }
    }

    @FXML
    void Ajouter(ActionEvent event) {
        try {
            if (!validerSaisie()) {
                return; // Si la validation échoue, on ne procède pas à l'ajout
            }

            double fraisValue = Double.parseDouble(frais.getText());
            int nombreDePlaces = Integer.parseInt(nombreDePlacesField.getText());

            // Déterminer le statut automatiquement
            Statut statutValue = determinerStatut();

            // Créer l'objet Evenement avec les bonnes valeurs
            Evenement evenement = new Evenement(
                    nomtf.getText(),
                    localisationtf.getText(),
                    phototf.getText(),
                    descriptiontf.getText(),
                    dateDebutPicker.getValue().format(formatter),
                    dateFinPicker.getValue().format(formatter),
                    fraisValue,
                    statutValue, // Statut déterminé automatiquement
                    nombreDePlaces
            );

            se.ajouter(evenement);

            // Rediriger vers la fenêtre d'affichage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementTable.fxml"));
            Parent root = loader.load();
            AfficherEvenementTable controller = loader.getController();
            controller.loadEvenements();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez entrer une valeur valide pour les frais ou le nombre de places.");
        } catch (SQLException | IOException e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    private boolean validerSaisie() {
        StringBuilder messageErreur = new StringBuilder();

        // Validation du nom
        if (nomtf.getText().isEmpty()) {
            messageErreur.append("Le nom de l'événement est requis.\n");
        }

        // Validation de la localisation
        if (localisationtf.getText().isEmpty()) {
            messageErreur.append("La localisation est requise.\n");
        }

        // Validation de la photo
        if (phototf.getText().isEmpty()) {
            messageErreur.append("Veuillez entrer un lien valide pour la photo.\n");
        }

        // Validation de la description
        if (descriptiontf.getText().isEmpty()) {
            messageErreur.append("La description est requise.\n");
        }

        // Validation des dates
        if (!validerDates()) {
            return false;
        }

        // Validation des frais
        if (frais.getText().isEmpty()) {
            messageErreur.append("Veuillez entrer un montant pour les frais.\n");
        } else {
            try {
                double fraisValue = Double.parseDouble(frais.getText());
                if (fraisValue < 0) {
                    messageErreur.append("Les frais ne peuvent pas être négatifs.\n");
                }
            } catch (NumberFormatException e) {
                messageErreur.append("Les frais doivent être un nombre valide.\n");
            }
        }

        // Validation du nombre de places
        if (nombreDePlacesField.getText().isEmpty()) {
            messageErreur.append("Veuillez entrer un nombre de places.\n");
        } else {
            try {
                int nombreDePlaces = Integer.parseInt(nombreDePlacesField.getText());
                if (nombreDePlaces < 0) {
                    messageErreur.append("Le nombre de places ne peut pas être négatif.\n");
                }
            } catch (NumberFormatException e) {
                messageErreur.append("Le nombre de places doit être un nombre valide.\n");
            }
        }

        // Si des erreurs sont détectées, afficher une alerte et retourner false
        if (messageErreur.length() > 0) {
            showAlert("Erreur de saisie", messageErreur.toString());
            return false;
        }

        return true;
    }

    private boolean validerDates() {
        if (dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null) {
            showAlert("Erreur de saisie", "Veuillez sélectionner une date de début et une date de fin.");
            return false;
        }

        if (dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())) {
            showAlert("Erreur de saisie", "La date de début doit être antérieure à la date de fin.");
            return false;
        }

        return true;
    }

    private Statut determinerStatut() {
        if (dateFinPicker.getValue().isBefore(java.time.LocalDate.now())) {
            return Statut.TERMINE; // Si la date de fin est passée
        } else {
            return Statut.A_VENIR; // Sinon, l'événement est à venir
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void annuler(ActionEvent event) {
        retournerALaListe(event);
    }

    private void retournerALaListe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementTable.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du retour à la liste.");
        }
    }
}