package controllers;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import models.Categorie;
import models.CategorieEnum;
import models.Produit;
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
import services.ServiceProduit;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class ListeProduitController {
    @FXML
    private TableView<Produit> tableView;
    @FXML
    private ListView<Produit> listView;
    @FXML
    private TableColumn<Produit, Integer> colId;

    @FXML
    private TableColumn<Produit, String> colNom;
    @FXML
    private TableColumn<Produit, String> colDescription;
    @FXML
    private TableColumn<Produit, String> colRef;
    @FXML
    private TableColumn<Produit, Integer> colPrix;
    @FXML
    private TableColumn<Produit, Integer> colQuantite;
    @FXML
    private TableColumn<Produit, CategorieEnum> colCategorie;


    @FXML
    private TableColumn<Produit, Void> actionColumn;

    private final ServiceProduit se = new ServiceProduit();




    @FXML
    public void initialize() throws SQLException {
        // Configure les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colRef.setCellValueFactory(new PropertyValueFactory<>("ref"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        List categorieComboBox = new List();
        categorieComboBox.getItems().equals(CategorieEnum.getToutesLesCategories().values());


        // Charger les catégories
        loadProduits();

        // Ajouter les boutons d'action
        addActionButtons();
    }




    public void loadProduits() {
        try {
            ObservableList<Produit> produits = FXCollections.observableArrayList(se.readAll1());
            // Debugging: vérifier le contenu de la liste
            produits.forEach(produit -> System.out.println("Produit: " + produit));

            tableView.setItems(produits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<Produit, Void>() {
            private final Button deleteButton = new Button("Supprimer");
            private final Button updateButton = new Button("Modifier");
            private final Button ajouterButton = new Button("Afficher");

            {
                deleteButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    try {
                        deleteProduit(produit);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                updateButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    updateProduit(produit, event);
                });
                ajouterButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    ajouterProduit(produit, event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(deleteButton, updateButton,ajouterButton));
                }
            }
        });
    }

    private void deleteProduit(Produit produit) throws SQLException {
        if (produit == null) {
            System.err.println("Produit null !");
            return;
        }

        // Création de l'alerte de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce produit ?");
        alert.setContentText("Produit : " + produit.getNom());

        // Boutons Oui/Non
        ButtonType buttonOui = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonOui, buttonNon);

        // Afficher l'alerte et attendre la réponse de l'utilisateur
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonOui) {
                try {
                    int id = produit.getId();
                    se.supprimer(id); // Suppression dans la base de données
                    loadProduits(); // Rafraîchir la liste après suppression
                    System.out.println("Produit supprimé avec succès !");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateProduit(Produit produit, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduit.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre ModifierCategorie
            ModifierProduitController controller = loader.getController();
            // Passer la catégorie sélectionnée
            controller.setProduit(produit);

            // Obtenir le stage actuel à partir du bouton ou autre élément déclencheur
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root)); // Afficher la nouvelle scène
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void ajouterProduit(Produit produit, ActionEvent event) {
        try {
            // Chargement de la vue AfficherProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherProduit.fxml"));
            Parent root = loader.load();

            // Vérification du contrôleur
            AfficherProduitController controller = loader.getController();
            if (controller != null) {
                controller.setProduit(produit);
            } else {
                System.err.println("Erreur : Impossible de récupérer le contrôleur de AfficherProduit.fxml.");
                return;
            }

            // Récupération et mise à jour de la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de AfficherProduit.fxml : " + e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.err.println("Erreur : Vérifiez que AfficherProduit.fxml utilise bien ModifierProduitController.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAjouterProduit(ActionEvent event) {
        try {
            // Charger le fichier FXML de AjouterProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
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
    private void handleAjouterCategorie(ActionEvent event) {
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
    private void handlepasserProduit(ActionEvent event) {
        try {
            // Charger le fichier FXML de AjouterProduit.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCommande.fxml"));
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
