package controllers;

import Models.Participation;
import Models.StatutP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;
import services.ServiceParticipation;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListParticipationAdmin implements Initializable {

    @FXML
    private TableView<Participation> tableView;

    @FXML
    private TableColumn<Participation, String> evenementColumn;

    @FXML
    private TableColumn<Participation, String> utilisateurColumn;

    @FXML
    private TableColumn<Participation, StatutP> statutColumn;

    @FXML
    private TableColumn<Participation, Integer> placesReserveesColumn;

    @FXML
    private TableColumn<Participation, String> commentaireColumn;

    @FXML
    private TableColumn<Participation, Void> actionColumn;

    @FXML
    private ComboBox<StatutP> statusComboBox;

    private final ServiceParticipation serviceParticipation = new ServiceParticipation();

    public ListParticipationAdmin() throws SQLException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusComboBox.getItems().addAll(StatutP.values());

        evenementColumn.setCellValueFactory(cellData -> cellData.getValue().getEvenement().nomProperty());
        utilisateurColumn.setCellValueFactory(cellData -> cellData.getValue().getUtilisateur().nomProperty());
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statutP"));
        placesReserveesColumn.setCellValueFactory(new PropertyValueFactory<>("nombreDePlacesReservees"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));

        // Appliquer une cellule personnalisée pour la colonne statut
        statutColumn.setCellFactory(column -> new TableCell<Participation, StatutP>() {
            @Override
            protected void updateItem(StatutP statut, boolean empty) {
                super.updateItem(statut, empty);

                if (empty || statut == null) {
                    setText(null);
                    setStyle(""); // Réinitialiser le style si la cellule est vide
                } else {
                    setText(statut.toString()); // Afficher le statut

                    // Appliquer une couleur au texte en fonction du statut
                    switch (statut) {
                        case EN_ATTENTE:
                            setStyle("-fx-text-fill: #FFA500;"); // Orange
                            break;
                        case ACCEPTER:
                            setStyle("-fx-text-fill: #00FF00;"); // Vert
                            break;
                        case REFUSER:
                            setStyle("-fx-text-fill: #FF0000;"); // Rouge
                            break;
                        default:
                            setStyle(""); // Style par défaut
                            break;
                    }
                }
            }
        });

        addActionButtons();
        loadParticipations();
    }

    private void loadParticipations() {
        try {
            ObservableList<Participation> participations = FXCollections.observableArrayList(serviceParticipation.recuperer());
            tableView.setItems(participations);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Une erreur est survenue lors de la récupération des participations.");
        }
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<Participation, Void>() {
            private final Button modifyButton = new Button();

            {
                // Charger l'image de l'icône
                Image icon = new Image(getClass().getResourceAsStream("/icons/edit.png")); // Remplacez par le chemin réel de votre icône
                ImageView imageView = new ImageView(icon);
                imageView.setFitWidth(20); // Réglez la largeur souhaitée
                imageView.setFitHeight(20); // Réglez la hauteur souhaitée
                modifyButton.setGraphic(imageView); // Définir l'image comme graphique du bouton

                // Action de ce bouton
                modifyButton.setOnAction(event -> {
                    Participation participation = getTableView().getItems().get(getIndex());
                    modifyStatut(participation);
                });

                modifyButton.setStyle("-fx-background-color: transparent;"); // Rendre le fond du bouton transparent
                modifyButton.setTooltip(new Tooltip("Modifier le statut")); // Ajouter un tooltip pour l'accessibilité
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                }
            }
        });
    }

    private void modifyStatut(Participation participation) {
        ChoiceDialog<StatutP> dialog = new ChoiceDialog<>(participation.getStatutP(), StatutP.values());
        dialog.setTitle("Modifier le statut");
        dialog.setHeaderText("Choisir un nouveau statut pour la participation");
        dialog.setContentText("Statut :");

        Optional<StatutP> result = dialog.showAndWait();
        result.ifPresent(newStatut -> {
            try {
                participation.setStatutP(newStatut);
                serviceParticipation.modifier(participation);
                loadParticipations();
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlertaError("Une erreur est survenue lors de la modification du statut.");
            }
        });
    }

    @FXML
    private void handleStatusChange() {
        StatutP selectedStatus = statusComboBox.getValue();
        if (selectedStatus != null) {
            loadParticipationsByStatut(selectedStatus);
        }
    }

    private void loadParticipationsByStatut(StatutP selectedStatus) {
        try {
            ObservableList<Participation> participations = FXCollections.observableArrayList(serviceParticipation.recuperer());
            ObservableList<Participation> filteredParticipations = FXCollections.observableArrayList();

            for (Participation participation : participations) {
                if (participation.getStatutP() == selectedStatus) {
                    filteredParticipations.add(participation);
                }
            }

            tableView.setItems(filteredParticipations);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Une erreur est survenue lors de la récupération des participations.");
        }
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur");
        alert.setContentText(mensaje);
        alert.initStyle(StageStyle.UTILITY);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #FFFFFF;");

        ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
        buttonBar.getButtons().forEach(b -> b.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;"));

        alert.showAndWait();
    }
}