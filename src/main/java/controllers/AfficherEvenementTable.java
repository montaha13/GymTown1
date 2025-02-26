package controllers;

import Models.Evenement;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Contrôleur pour afficher la table des événements.
 */
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

    @FXML
    private TableColumn<Evenement, Integer> nombreDePlacesColumn;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    public AfficherEvenementTable() throws SQLException {
    }

    /**
     * Initialisation du contrôleur.
     */
    @FXML
    public void initialize() {
        configurerColonnes();
        loadEvenements();
        addActionButtons();
    }

    /**
     * Configure les colonnes de la table.
     */
    private void configurerColonnes() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        localisationColumn.setCellValueFactory(new PropertyValueFactory<>("localisation"));

        // Utilisez une cellule personnalisée pour les images
        photoColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPhoto()));
        photoColumn.setCellFactory(column -> new TableCell<Evenement, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Image image = new Image(item, 50, 50, true, true); // Spécifiez la taille de l'image
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        fraisColumn.setCellValueFactory(new PropertyValueFactory<>("frais"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        nombreDePlacesColumn.setCellValueFactory(new PropertyValueFactory<>("nombreDePlaces"));
    }

    /**
     * Charge les événements dans la table.
     */
    @FXML
    public void loadEvenements() {
        try {
            ObservableList<Evenement> evenements = FXCollections.observableArrayList(serviceEvenement.readAll());
            tableView.setItems(evenements);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors du chargement des événements", e.getMessage());
        }
    }

    /**
     * Ajoute les boutons d'action dans la colonne d'action.
     */
    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<Evenement, Void>() {
            private final Button deleteButton = new Button();
            private final Button updateButton = new Button();

            {
                Image deleteIcon = new Image(getClass().getResourceAsStream("/icons/delete.png"));
                ImageView deleteImageView = new ImageView(deleteIcon);
                deleteImageView.setFitWidth(20);
                deleteImageView.setFitHeight(20);
                deleteButton.setGraphic(deleteImageView);

                deleteButton.setOnAction(event -> {
                    Evenement evenement = getTableView().getItems().get(getIndex());
                    supprimerEvenement(evenement);
                });

                Image updateIcon = new Image(getClass().getResourceAsStream("/icons/edit.png"));
                ImageView updateImageView = new ImageView(updateIcon);
                updateImageView.setFitWidth(20);
                updateImageView.setFitHeight(20);
                updateButton.setGraphic(updateImageView);

                updateButton.setOnAction(event -> {
                    Evenement evenement = getTableView().getItems().get(getIndex());
                    modifierEvenement(evenement, event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(updateButton, deleteButton));
                }
            }
        });
    }

    /**
     * Supprime un événement.
     *
     * @param evenement l'événement à supprimer
     */
    private void supprimerEvenement(Evenement evenement) {
        if (evenement == null) {
            System.err.println("L'événement est null !");
            return;
        }

        int id = evenement.getId();

        if (id <= 0) {
            System.err.println("ID invalide pour la suppression: " + id);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet événement ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    serviceEvenement.supprimer(id);
                    loadEvenements();
                } catch (SQLException e) {
                    e.printStackTrace();
                    afficherErreur("Erreur lors de la suppression de l'événement", e.getMessage());
                }
            }
        });
    }

    /**
     * Modifie un événement.
     *
     * @param evenement l'événement à modifier
     * @param event     l'événement ActionEvent
     */
    private void modifierEvenement(Evenement evenement, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvenement.fxml"));
            Parent root = loader.load();

            ModifierEvenement controller = loader.getController();
            controller.setEvenement(evenement);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de la modification de l'événement", e.getMessage());
        }
    }

    /**
     * Affiche une erreur avec un message et une exception.
     *
     * @param message l'erreur à afficher
     * @param erreur  l'exception à afficher
     */
    private void afficherErreur(String message, String erreur) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message + ": " + erreur);
        alert.showAndWait();
    }

    @FXML
    private void ajouterEvenement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvenement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de l'ajout de l'événement", e.getMessage());
        }
    }
}