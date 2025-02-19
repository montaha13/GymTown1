package controllers;

import Models.Evenement;
import Models.Statut;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierEvenement {

    private final ServiceEvenement se = new ServiceEvenement();

    @FXML
    private TextField dateDebuttf;

    @FXML
    private TextField dateFintf;

    @FXML
    private TextField descriptiontf;

    @FXML
    private TextField frais;

    @FXML
    private TextField localisationtf;

    @FXML
    private TextField nomtf;

    @FXML
    private TextField phototf;

    @FXML
    private TextField statuttf;

    private Evenement evenement; // L'événement à modifier
    private int evenementId; // L'ID de l'événement à modifier

    private Stage stage;  // Pour accéder au stage depuis la classe

    public ModifierEvenement() throws SQLException {
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
        this.evenementId = evenement.getId();

        // Initialiser les champs de texte avec les valeurs de l'événement
        if (evenement != null) {  // Ajoutez cette vérification
            nomtf.setText(evenement.getNom());
            localisationtf.setText(evenement.getLocalisation());
            phototf.setText(evenement.getPhoto());
            descriptiontf.setText(evenement.getDescription());
            dateDebuttf.setText(evenement.getDateDebut());
            dateFintf.setText(evenement.getDateFin());
            frais.setText(String.valueOf(evenement.getFrais()));
            statuttf.setText(evenement.getStatut().name());
        }
    }

    @FXML
    void Modifier(ActionEvent event) {
        try {
            // Créer un nouvel objet Evenement avec les valeurs des champs de texte
            Evenement updatedEvenement = new Evenement();
            updatedEvenement.setId(evenementId);
            updatedEvenement.setNom(nomtf.getText());
            updatedEvenement.setLocalisation(localisationtf.getText());
            updatedEvenement.setPhoto(phototf.getText());
            updatedEvenement.setDescription(descriptiontf.getText());
            updatedEvenement.setDateDebut(dateDebuttf.getText());
            updatedEvenement.setDateFin(dateFintf.getText());
            updatedEvenement.setFrais(Double.parseDouble(frais.getText()));
            updatedEvenement.setStatut(Statut.valueOf(statuttf.getText()));

            // Appeler le service pour mettre à jour l'événement
            se.modifier(updatedEvenement);

            // Retourner à la liste des événements
            retournerALaListe(event); // Appel à la méthode pour changer de vue

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la mise à jour de l'événement.");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            // Gérer l'exception si frais n'est pas un nombre valide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le champ 'Frais' doit contenir un nombre valide.");
            alert.showAndWait();
        }
    }

    private void retournerALaListe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementTable.fxml")); // Assurez-vous que le chemin est correct
            Parent root = loader.load();
            AfficherEvenementTable controller = loader.getController();
            controller.loadEvenements(); // Recharger les événements dans la liste

            // Obtenir le stage courant
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException  e) {  // Catch SQLException here
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors du retour à la liste.");
            alert.showAndWait();
        }
    }
}