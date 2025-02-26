package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import models.Commande;
import models.Produit;
import models.StatutCommande;
import services.ServiceCommande;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListeCommandeRESController {

    @FXML
    private TableView<Commande> tableCommandes;

    @FXML
    private TableColumn<Commande, Integer> colId;
    @FXML
    private TableColumn<Commande, String> colMail;
    @FXML
    private TableColumn<Commande, String> colLocalisation;
    @FXML
    private TableColumn<Commande, String> colTelephone;
    @FXML
    private TableColumn<Commande, Double> colPrix;
    @FXML
    private TableColumn<Commande, Integer> colNombre;
    @FXML
    private TableColumn<Commande, Double> colTotal;
    @FXML
    private TableColumn<Commande, String> colDate;
    @FXML
    private TableColumn<Commande, String> colStatut;
    @FXML
    private TableColumn<Commande, Void> actionColumn;
    @FXML
    private FlowPane productContainer;
    @FXML
    private TextField searchField;
    private final ServiceCommande sc = new ServiceCommande();
    @FXML
    private ComboBox<String> statusComboBox;

    // Déclaration de la variable allCommandes comme ObservableList
    private ObservableList<Commande> allCommandes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colMail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        colLocalisation.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Charger les commandes
        loadCommandes();

        // Ajouter des boutons d'action dans la colonne
        addActionButtons();

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

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<Commande, Void>() {
            private final Button deleteButton = new Button();
            private final Button updateButton = new Button();

            {
                // Ajouter une image pour le bouton supprimer
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/23890710-facile-supprimer-icone-le-icone-pouvez-etre-utilise-pour-sites-internet-impression-modeles-presentation-modeles-illustrations-etc-gratuit-vectoriel-removebg-preview.png")));
                deleteIcon.setFitWidth(20); // Ajuster la taille
                deleteIcon.setFitHeight(20);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;"); // Rendre le fond transparent

                deleteButton.setOnAction(event -> {
                    Commande commande = getTableView().getItems().get(getIndex());
                    try {
                        deleteCommande(commande);
                    } catch (SQLException e) {
                        showErrorAlert("Erreur de suppression", "Impossible de supprimer la commande.");
                    }
                });

                // Ajouter une image pour le bouton modifier
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/OIP-removebg-preview (1).png")));
                updateIcon.setFitWidth(20); // Ajuster la taille
                updateIcon.setFitHeight(20);
                updateButton.setGraphic(updateIcon);
                updateButton.setStyle("-fx-background-color: transparent;");

                updateButton.setOnAction(event -> {
                    Commande commande = getTableView().getItems().get(getIndex());
                    updateCommande(commande, event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonContainer = new HBox(10, updateButton, deleteButton);
                    setGraphic(buttonContainer);
                }
            }
        });
    }

    // Méthode pour supprimer une commande
    private void deleteCommande(Commande commande) throws SQLException {
        if (commande != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer cette commande ?");
            alert.setContentText("Commande ID : " + commande.getId());

            ButtonType buttonOui = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonOui, buttonNon);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonOui) {
                    try {
                        sc.supprimer(commande.getId());
                        loadCommandes();
                    } catch (SQLException e) {
                        showErrorAlert("Erreur de suppression", "Impossible de supprimer la commande.");
                    }
                }
            });
        }
    }

    // Méthode pour modifier une commande
    private void updateCommande(Commande commande, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCommande.fxml"));
            Parent root = loader.load();

            ModifierCommandeController controller = loader.getController();
            controller.setCommande(commande);

            // Créer un Stage modal
            Stage modifierStage = new Stage();
            modifierStage.initModality(Modality.APPLICATION_MODAL); // Bloque l'interface parente
            modifierStage.setScene(new Scene(root));
            modifierStage.showAndWait(); // Attend la fermeture

            // Recharger les données après modification
            loadCommandes();

        } catch (IOException e) {
            showErrorAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }


    // Méthode pour afficher les détails d'une commande
    private void showCommandeDetails(Commande commande, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
            Parent root = loader.load();

            AfficherCommandeController controller = loader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Erreur d'affichage", "Impossible d'ouvrir la fenêtre de détails.");
        }
    }

    // Afficher une alerte d'erreur
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour ajouter un produit (en utilisant un autre formulaire)
    @FXML
    private void handleAjouterProduit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout de produit.");
        }
    }

    // Méthode pour ajouter une catégorie (en utilisant un autre formulaire)
    @FXML
    private void handleAjouterCategorie(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout de catégorie.");
        }
    }
}
