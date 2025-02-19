package controllers;
import controllers.AfficherEvenementTable;

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

public class AjouterEvenement {

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

    public AjouterEvenement() throws SQLException {
    }

    @FXML
    void Ajouter(ActionEvent event) {
        try {
            // Convertir frais en double
            double fraisValue = Double.parseDouble(frais.getText());

            // Convertir statut en Statut (assurez-vous que la valeur entrée dans statuttf correspond à un statut valide)
            Statut statutValue = Statut.valueOf(statuttf.getText().toUpperCase());

            // Créer l'objet Evenement avec les bonnes valeurs
            Evenement evenement = new Evenement(
                    nomtf.getText(),
                    localisationtf.getText(),
                    phototf.getText(),
                    descriptiontf.getText(),
                    dateDebuttf.getText(),
                    dateFintf.getText(),
                    fraisValue,
                    statutValue
            );

            // Appeler le service pour ajouter l'événement
            se.ajouter(evenement);

            // Charger la fenêtre d'affichage de l'événement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementTable.fxml"));
            Parent root = loader.load();
            AfficherEvenementTable controller = loader.getController(); // Important: get the controller

            // **OPTIONNEL** Pour rafraîchir la liste immédiatement (si readAll est bien implémenté)
            controller.loadEvenements();  // Utilisez la méthode loadEvenements pour rafraichir la liste

            // Changer la scène
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();  // Récupérer le stage actuel
            stage.setScene(scene);
            stage.show();




        } catch (NumberFormatException e) {
            // Gestion des erreurs en cas de conversion incorrecte pour frais
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Veuillez entrer une valeur valide pour les frais.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            // Gestion des erreurs si le statut n'est pas valide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Le statut spécifié est invalide.");
            alert.showAndWait();
        } catch (SQLException e) {
            // Gestion des erreurs SQL
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur SQL");
            alert.setContentText("Une erreur est survenue lors de l'ajout de l'événement : " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            // Gestion des erreurs lors du chargement de la fenêtre FXML
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de chargement");
            alert.setContentText("Une erreur est survenue lors du chargement de la fenêtre d'affichage.");
            alert.showAndWait();
        }
    }
}
