package controllers;

import javafx.scene.control.*;
import models.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceCategorie;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ListeCategorieController {
    @FXML
    private TableView<Categorie> tableView;

    @FXML
    private TableColumn<Categorie, Integer> colId;

    @FXML


    private TableColumn<Categorie, String> colCategorie;

    @FXML
    private TableColumn<Categorie, Void> actionColumn;

    private final ServiceCategorie se = new ServiceCategorie();

    @FXML
    public void initialize() {
        // Configure les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("nom"));

        // Charger les catégories
        loadCategories();

        // Ajouter les boutons d'action
        addActionButtons();
    }

    public void loadCategories() {
        try {
            ObservableList<Categorie> categories = FXCollections.observableArrayList(se.readAll());
            tableView.setItems(categories);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<Categorie, Void>() {
            private final Button deleteButton = new Button("Supprimer");
            private final Button updateButton = new Button("Modifier");
            private final Button afficherButton = new Button("Afficher");

            {
                afficherButton.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    afficherCategorie(categorie);
                });
                deleteButton.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    deleteCategorie(categorie);
                });

                updateButton.setOnAction(event -> {
                    Categorie categorie = getTableView().getItems().get(getIndex());
                    updateCategorie(categorie, event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(deleteButton, updateButton ));
                }
            }
        });
    }


    private void deleteCategorie(Categorie categorie) {
        if (categorie == null) {
            System.err.println("Catégorie null !");
            return;
        }

        // Créer une alerte de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la catégorie");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette catégorie ?");

        // Afficher l'alerte et attendre la réponse de l'utilisateur
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            int id = categorie.getId();
            try {
                se.supprimer(id);
                loadCategories(); // Recharger la liste après suppression
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Suppression annulée.");
        }
    }


    private void updateCategorie(Categorie categorie, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCategorie.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre ModifierCategorie
            ModifierCategorieController controller = loader.getController();
            // Passer la catégorie sélectionnée
            controller.setCategorie(categorie);

            // Obtenir le stage actuel à partir du bouton ou autre élément déclencheur
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root)); // Afficher la nouvelle scène
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherCategorie(Categorie event) {
        try {
            // Charger le fichier FXML de AjouterProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCategorie.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le FXML chargé
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène actuelle avec la nouvelle scène
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }}

    @FXML
    private void     ajoutercategorie(ActionEvent event) {
        try {
            // Charger le fichier FXML de AjouterProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategorie.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le FXML chargé
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène actuelle avec la nouvelle scène
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    @FXML
    private void listeproduit(ActionEvent event) {
        try {
            // Charger le fichier FXML de AjouterProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeProduits.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le FXML chargé
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène actuelle avec la nouvelle scène
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
