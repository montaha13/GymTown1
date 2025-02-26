package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        String page = "";

        // Déterminer quelle page charger en fonction du bouton cliqué
        String buttonText = ((Node) event.getSource()).toString();

        if (buttonText.contains("Produit")) {
            page = "/resources/AffProduit.fxml";
        } else if (buttonText.contains("Produit")) {
            page = "/resources/ListeProduits.fxml";
        } else if (buttonText.contains("CommandesRES")) {
            page = "ListeCommandesRES.fxml";
        }

        if (!page.isEmpty()) {
            // Charger la nouvelle page
            root = FXMLLoader.load(getClass().getResource(page));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
