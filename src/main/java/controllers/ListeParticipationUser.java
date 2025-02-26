package controllers;

import Models.Participation;
import Models.StatutP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceParticipation;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ListeParticipationUser implements Initializable {

    @FXML
    private TableView<Participation> tableView;

    @FXML
    private TableColumn<Participation, String> evenementColumn;

    @FXML
    private TableColumn<Participation, StatutP> statutColumn;

    @FXML
    private TableColumn<Participation, Integer> placesReserveesColumn;

    @FXML
    private TableColumn<Participation, String> commentaireColumn;

    @FXML
    private ComboBox<StatutP> statutComboBox; // ComboBox pour les statuts

    private final ServiceParticipation serviceParticipation = new ServiceParticipation();
    private ObservableList<Participation> toutesLesParticipations; // Liste complète des participations

    public ListeParticipationUser() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Associer les colonnes du tableau aux propriétés de l'objet Participation
        evenementColumn.setCellValueFactory(cellData -> cellData.getValue().getEvenement().nomProperty());
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

        // Remplir la ComboBox avec les valeurs de l'enum StatutP
        statutComboBox.setItems(FXCollections.observableArrayList(StatutP.values()));

        // Charger les participations dans le tableau
        loadParticipations();
    }

    private void loadParticipations() {
        try {
            // Récupérer toutes les participations depuis la base de données
            toutesLesParticipations = FXCollections.observableArrayList(serviceParticipation.recuperer());
            tableView.setItems(toutesLesParticipations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour filtrer les participations par statut
    @FXML
    private void filtrerParStatut() {
        StatutP statutSelectionne = statutComboBox.getValue();

        if (statutSelectionne == null) {
            // Si aucun statut n'est sélectionné, afficher toutes les participations
            tableView.setItems(toutesLesParticipations);
        } else {
            // Filtrer les participations par statut
            ObservableList<Participation> participationsFiltrees = FXCollections.observableArrayList();
            for (Participation participation : toutesLesParticipations) {
                if (participation.getStatutP() == statutSelectionne) {
                    participationsFiltrees.add(participation);
                }
            }
            tableView.setItems(participationsFiltrees);
        }
    }
}