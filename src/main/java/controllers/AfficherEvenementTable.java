package controllers;

import Models.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;

public class AfficherEvenementTable {
    @FXML
    private TableView<Evenement> tableView;

    @FXML
    private TableColumn<Evenement, String> nomColumn;

    @FXML
    private TableColumn<Evenement, String> localisationColumn;

    @FXML
    private TableColumn<Evenement, String> photoColumn;

    @FXML
    private TableColumn<Evenement, String> descriptionColumn;

    @FXML
    private TableColumn<Evenement, String> dateDebutColumn;

    @FXML
    private TableColumn<Evenement, String> dateFinColumn;

    @FXML
    private TableColumn<Evenement, String> fraisColumn;

    @FXML
    private TableColumn<Evenement, String> statutColumn;

    @FXML
    private TableColumn<Evenement, Void> actionColumn;

    private final ServiceEvenement se = new ServiceEvenement();

    public AfficherEvenementTable() throws SQLException {
    }

    @FXML
    public void initialize() {
        // Configure the columns
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        localisationColumn.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        fraisColumn.setCellValueFactory(new PropertyValueFactory<>("frais"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Load events
        loadEvenements();

        // Add action buttons
        addActionButtons();
    }

    @FXML
    public void loadEvenements() {
        try {
            ObservableList<Evenement> evenements = FXCollections.observableArrayList(se.readAll());
            tableView.setItems(evenements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<Evenement, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");

            {
                deleteButton.setOnAction(event -> {
                    Evenement evenement = getTableView().getItems().get(getIndex());
                    deleteEvenement(evenement);
                });

                updateButton.setOnAction(event -> {
                    Evenement evenement = getTableView().getItems().get(getIndex());
                    updateEvenement(evenement, event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(deleteButton, updateButton));
                }
            }
        });
    }

    @FXML
    private void deleteEvenement(Evenement evenement) {
        if (evenement == null) {
            System.err.println("L'événement est null !");
            return;
        }

        int id = evenement.getId();

        if (id <= 0) {
            System.err.println("ID invalide pour la suppression: " + id);
            return;
        }

        try {
            se.supprimer(id);
            loadEvenements(); // Recharger la liste après suppression
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateEvenement(Evenement evenement, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvenement.fxml"));
            Parent root = loader.load();
            ModifierEvenement controller = loader.getController();
            controller.setEvenement(evenement);

            // Récupérer le stage actuel
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}