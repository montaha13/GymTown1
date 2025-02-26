package controllers;

import Models.Evenement;
import Models.Utilisateur;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ServiceEvenement;
import services.ServiceVote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ListeEvenementsUser {
    @FXML
    private VBox container;

    @FXML
    private ListView<Evenement> listView;

    @FXML
    private TextField searchField;

    private final ServiceEvenement se = new ServiceEvenement();
    private ObservableList<Evenement> allEvenements;

    public ListeEvenementsUser() throws SQLException {
    }

    @FXML
    public void initialize() {
        loadEvenements();
    }

    @FXML
    public void loadEvenements() {
        try {
            allEvenements = FXCollections.observableArrayList(se.readAll());
            listView.setItems(allEvenements);

            listView.setCellFactory(param -> new ListCell<Evenement>() {
                @Override
                protected void updateItem(Evenement evenement, boolean empty) {
                    super.updateItem(evenement, empty);

                    if (empty || evenement == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        HBox card = new HBox(10);
                        card.getStyleClass().add("event-card");

                        // Image de l'événement
                        ImageView imageView = new ImageView();
                        if (evenement.getPhoto() != null && !evenement.getPhoto().isEmpty()) {
                            try {
                                Image image = new Image(evenement.getPhoto());
                                imageView.setImage(image);
                                imageView.setFitWidth(100);
                                imageView.setFitHeight(100);
                                imageView.setPreserveRatio(true);
                                imageView.getStyleClass().add("event-image");
                            } catch (IllegalArgumentException e) {
                                System.err.println("URL de l'image invalide : " + evenement.getPhoto());
                                imageView.setVisible(false);
                            }
                        } else {
                            imageView.setVisible(false);
                        }

                        // Détails de l'événement
                        VBox details = new VBox(5);
                        Text title = new Text(evenement.getNom());
                        title.getStyleClass().add("event-title");
                        Text location = new Text("Localisation: " + evenement.getLocalisation());
                        Text date = new Text("Date: " + evenement.getDateDebut());
                        Text places = new Text("Places disponibles: " + evenement.getNombreDePlaces());
                        location.getStyleClass().add("event-details");
                        date.getStyleClass().add("event-details");
                        places.getStyleClass().add("event-details");

                        details.getChildren().addAll(title, location, date, places);

                        // Créer une HBox pour les boutons d'action
                        HBox actions = new HBox(5);
                        Button participateButton = new Button("Participer");
                        participateButton.getStyleClass().add("action-button");
                        participateButton.setOnAction(event -> handleParticipation(evenement));
                        actions.getChildren().add(participateButton);

                        Button detailsButton = new Button("Détails");
                        detailsButton.getStyleClass().add("action-button");
                        detailsButton.setOnAction(event -> showDetails(evenement));
                        actions.getChildren().add(detailsButton);

                        // HBox pour les étoiles et icônes de partage
                        VBox starAndShareBox = new VBox(5);
                        addStarRating(starAndShareBox, evenement);
                        addShareIcons(starAndShareBox, evenement);

                        details.getChildren().add(actions);
                        details.getChildren().add(starAndShareBox); // Ajouter les étoiles et les icônes sous les boutons d'action
                        card.getChildren().addAll(imageView, details);
                        setGraphic(card);
                        setText(null);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addStarRating(VBox starAndShareBox, Evenement evenement) {
        HBox starRating = new HBox(5);
        int maxStars = 5;
        final int[] userRating = {0};

        for (int i = 1; i <= maxStars; i++) {
            Button star = new Button("★");
            star.getStyleClass().add("star-button");
            int finalI = i;

            star.setOnAction(event -> {
                userRating[0] = finalI;
                updateStars(starRating, userRating[0]);
                try {
                    new ServiceVote().saveVote(evenement.getId(), getCurrentUser().getId(), userRating[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            starRating.getChildren().add(star);
        }

        starAndShareBox.getChildren().add(starRating);
    }

    private void updateStars(HBox starRating, int userRating) {
        for (int i = 0; i < starRating.getChildren().size(); i++) {
            Button star = (Button) starRating.getChildren().get(i);
            if (i < userRating) {
                star.setStyle("-fx-text-fill: gold;");
            } else {
                star.setStyle("-fx-text-fill: gray;");
            }
        }
    }

    private String getEventDetails(Evenement evenement) {
        StringBuilder eventDetails = new StringBuilder();

        if (evenement.getNom() != null && !evenement.getNom().isEmpty()) {
            eventDetails.append("Événement : ").append(evenement.getNom()).append("\n");
        }

        if (evenement.getLocalisation() != null && !evenement.getLocalisation().isEmpty()) {
            eventDetails.append("Localisation : ").append(evenement.getLocalisation()).append("\n");
        }

        if (evenement.getDateDebut() != null) {
            eventDetails.append("Date : ").append(evenement.getDateDebut()).append("\n");
        }

        if (evenement.getDescription() != null && !evenement.getDescription().isEmpty()) {
            eventDetails.append("Description : ").append(evenement.getDescription());
        }

        return eventDetails.toString();
    }

    private String createShareUrl(String baseUrl, String params) {
        try {
            return baseUrl + "?text=" + URLEncoder.encode(params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            showErrorSharing("Erreur de création de l'URL de partage.");
            return "";
        }
    }

    private void addShareIcons(VBox starAndShareBox, Evenement evenement) {
        HBox shareIcons = new HBox(10);

        // Icône Facebook
        ImageView facebookIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/facebook.png")));
        facebookIcon.setFitWidth(30);
        facebookIcon.setFitHeight(30);
        facebookIcon.setOnMouseClicked(event -> handleShare(evenement, "facebook"));
        shareIcons.getChildren().add(facebookIcon);

        // Icône Twitter
        ImageView twitterIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/twitter.png")));
        twitterIcon.setFitWidth(30);
        twitterIcon.setFitHeight(30);
        twitterIcon.setOnMouseClicked(event -> handleShare(evenement, "twitter"));
        shareIcons.getChildren().add(twitterIcon);

        starAndShareBox.getChildren().add(shareIcons);
    }

    private void handleShare(Evenement evenement, String platform) {
        String eventDetails = getEventDetails(evenement);
        String shareUrl = "";

        switch (platform) {
            case "facebook":
                try {
                    shareUrl = "https://www.facebook.com/sharer/sharer.php?u=" + URLEncoder.encode(evenement.getPhoto(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Platform.runLater(() -> showErrorSharing("Erreur lors de l'encodage de l'URL Facebook."));
                    return;
                }
                break;
            case "twitter":
                shareUrl = createShareUrl("https://twitter.com/intent/tweet", eventDetails);
                break;
        }

        String finalShareUrl = shareUrl;
        new Thread(() -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(finalShareUrl));
                Platform.runLater(() -> showNotification("Partage réussi sur " + platform + "!"));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showErrorSharing("Erreur lors de l'ouverture des liens de partage."));
            }
        }).start();
    }

    private void showErrorSharing(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de partage");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showNotification(String message) {
        Alert notification = new Alert(Alert.AlertType.INFORMATION);
        notification.setTitle("Partage");
        notification.setHeaderText(null);
        notification.setContentText(message);
        notification.showAndWait();
    }

    @FXML
    private void handleParticipation(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterParticipation.fxml"));
            Parent root = loader.load();

            AjouterParticipation controller = loader.getController();
            controller.setEvenement(evenement);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Participer à l'événement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir le formulaire de participation.");
            alert.showAndWait();
        }
    }

    private void showDetails(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailsEvenement.fxml"));
            Parent root = loader.load();

            DetailsEvenement controller = loader.getController();
            controller.initData(evenement);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Détails de l'événement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir la page de détail.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Evenement> filteredList = allEvenements.stream()
                .filter(evenement -> evenement.getNom().toLowerCase().contains(query) ||
                        evenement.getLocalisation().toLowerCase().contains(query))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        listView.setItems(filteredList);
    }

    @FXML
    public void Inscription() {
        // Logique de votre inscription ici
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inscription");
        alert.setHeaderText(null);
        alert.setContentText("Inscription réussie !");
        alert.showAndWait();
    }

    private Utilisateur getCurrentUser() {
        // Simuler un utilisateur actuel (à remplacer par la logique réelle)
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1); // ID de l'utilisateur
        utilisateur.setNom("Test User"); // Nom de l'utilisateur
        return utilisateur;
    }

}