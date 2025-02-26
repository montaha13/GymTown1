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
import java.time.LocalDate;

public class ModifierEvenement {

    private final ServiceEvenement se = new ServiceEvenement();

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextArea descriptiontf;

    @FXML
    private TextField frais;

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

    private Evenement evenement;
    private int evenementId;

    private FileChooser fileChooser;
    private File file;

    public ModifierEvenement() throws SQLException {
    }

    @FXML
    void initialize() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
        this.evenementId = evenement.getId();

        if (evenement != null) {
            nomtf.setText(evenement.getNom());
            localisationtf.setText(evenement.getLocalisation());
            phototf.setText(evenement.getPhoto());
            descriptiontf.setText(evenement.getDescription());
            dateDebutPicker.setValue(LocalDate.parse(evenement.getDateDebut()));
            dateFinPicker.setValue(LocalDate.parse(evenement.getDateFin()));
            frais.setText(String.valueOf(evenement.getFrais()));
            nombreDePlacesField.setText(String.valueOf(evenement.getNombreDePlaces()));

            // Afficher l'image si l'URL est valide
            if (evenement.getPhoto() != null && !evenement.getPhoto().isEmpty()) {
                Image image = new Image(evenement.getPhoto());
                imageView.setImage(image);
                imageView.setPreserveRatio(true);
            }
        }
    }

    @FXML
    void handleBrowser(ActionEvent event) {
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
    void Modifier(ActionEvent event) {
        try {
            if (!validerSaisie()) {
                return; // Si la validation échoue, on ne procède pas à la modification
            }

            int nombreDePlaces = Integer.parseInt(nombreDePlacesField.getText());
            if (nombreDePlaces < 0) {
                showAlert("Erreur de saisie", "Le nombre de places ne peut pas être négatif.");
                return;
            }

            Evenement updatedEvenement = new Evenement();
            updatedEvenement.setId(evenementId);
            updatedEvenement.setNom(nomtf.getText());
            updatedEvenement.setLocalisation(localisationtf.getText());
            updatedEvenement.setPhoto(phototf.getText());
            updatedEvenement.setDescription(descriptiontf.getText());
            updatedEvenement.setDateDebut(dateDebutPicker.getValue().toString());
            updatedEvenement.setDateFin(dateFinPicker.getValue().toString());
            updatedEvenement.setFrais(Double.parseDouble(frais.getText()));
            updatedEvenement.setNombreDePlaces(nombreDePlaces);

            // Déterminer le statut automatiquement
            updatedEvenement.setStatut(determinerStatut());

            se.modifier(updatedEvenement);
            retournerALaListe(event);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour de l'événement.");
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Le champ 'Frais' ou 'Nombre de places' doit contenir un nombre valide.");
        }
    }

    private Statut determinerStatut() {
        if (dateFinPicker.getValue().isBefore(LocalDate.now())) {
            return Statut.TERMINE; // Si la date de fin est passée
        } else {
            return Statut.A_VENIR; // Sinon, l'événement est à venir
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
        if (dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null) {
            messageErreur.append("Veuillez sélectionner une date de début et une date de fin.\n");
        } else if (dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())) {
            messageErreur.append("La date de début doit être antérieure à la date de fin.\n");
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

    @FXML
    private void annuler(ActionEvent event) {
        retournerALaListe(event);
    }

    private void retournerALaListe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementTable.fxml"));
            Parent root = loader.load();
            AfficherEvenementTable controller = loader.getController();
            controller.loadEvenements();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du retour à la liste.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}