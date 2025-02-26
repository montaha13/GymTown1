package controllers;

import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.StatutCommande;
import services.ServiceCommande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Commande;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class ListeCommandesController {
    @FXML
    private TableView<Commande> tableCommandes;  // Doit correspondre à l'ID du TableView dans le fichier FXML


    @FXML
    private TableColumn<Commande, Integer> colId;
    @FXML
    private TableColumn<Commande, Double> colPrix;
    @FXML
    private TableColumn<Commande, Integer> colNombre;
    @FXML
    private TableColumn<Commande, String> colLocalisation;
    @FXML
    private TableColumn<Commande, String> colTelephone;
    @FXML
    private TableColumn<Commande, String> colMail;
    @FXML
    private TableColumn<Commande, String> colStatut;
    @FXML
    private TableColumn<Commande, String> colDate;
    @FXML
    private TableColumn<Commande, Double> colTotal;
    @FXML
    private ComboBox<String> statusComboBox;
    private final ServiceCommande serviceCommande = new ServiceCommande();
    // Déclaration de la variable allCommandes comme ObservableList
    private ObservableList<Commande> allCommandes = FXCollections.observableArrayList();
    private final ServiceCommande sc = new ServiceCommande();
    @FXML
    public void initialize() throws SQLException {
        // Configure les colonnes


        colLocalisation.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Charger les commandes
        loadCommandes();

        // Initialiser la ComboBox avec les statuts
        ObservableList<String> statuts = FXCollections.observableArrayList("Tous");
        for (StatutCommande statut : StatutCommande.values()) {
            statuts.add(statut.name());
        }
        statusComboBox.setItems(statuts);
        statusComboBox.setValue("Tous"); // Valeur par défaut

        // Ajout d'un écouteur sur la ComboBox pour filtrer par statut
        statusComboBox.setOnAction(event -> {
            try {
                filterByStatus();
            } catch (SQLException e) {
                showErrorAlert("Erreur", "Impossible de filtrer les commandes.");
            }
        });
    }

    // Afficher une alerte d'erreur
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Filtrage des commandes en fonction du statut sélectionné
    private void filterByStatus() throws SQLException {
        String selectedStatus = statusComboBox.getValue();

        if (selectedStatus.equals("Tous")) {
            tableCommandes.setItems(allCommandes);
        } else {
            ObservableList<Commande> filteredCommandes = allCommandes.stream()
                    .filter(commande -> commande.getStatut().name().equalsIgnoreCase(selectedStatus))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tableCommandes.setItems(filteredCommandes);
        }
    }
    public void loadCommandes() {
        try {
            allCommandes.clear();
            allCommandes.addAll(sc.readAll2()); // Recharge depuis la BDD
            tableCommandes.setItems(allCommandes);
            tableCommandes.refresh(); // Force le rafraîchissement visuel
        } catch (SQLException e) {
            showErrorAlert("Erreur", "Échec du chargement des commandes.");
        }
    }


    @FXML
    private void handleAjouterProduit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
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
    private void handlePasserCommande(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PasserCommande.fxml"));
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
    private void handleAjouterCategorie(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategorie.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
