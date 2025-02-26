package controllers;

import Models.Evenement;
import Models.Participation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import services.ServiceEvenement;
import services.ServiceParticipation;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardUtilisateur implements Initializable {

    @FXML
    private VBox dashboardContainer;

    @FXML
    private ListView<Evenement> upcomingEventsListView;

    @FXML
    private ListView<Participation> myParticipationsListView;

    @FXML
    private TextField searchField;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final ServiceParticipation serviceParticipation = new ServiceParticipation();

    public DashboardUtilisateur() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUpcomingEvents();
        loadMyParticipations();
    }

    private void loadUpcomingEvents() {
        try {
            ObservableList<Evenement> upcomingEvents = FXCollections.observableArrayList(serviceEvenement.recuperer());
            upcomingEventsListView.setItems(upcomingEvents);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMyParticipations() {
        try {
            ObservableList<Participation> participations = FXCollections.observableArrayList(serviceParticipation.recuperer());
            myParticipationsListView.setItems(participations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Evenement> filteredList = upcomingEventsListView.getItems().stream()
                .filter(evenement -> evenement.getNom().toLowerCase().contains(query))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        upcomingEventsListView.setItems(filteredList);
    }
}