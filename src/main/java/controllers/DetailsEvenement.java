package controllers;

import Models.Evenement;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;

public class DetailsEvenement {

    @FXML
    private Label titreLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Label localisationLabel;

    @FXML
    private Label datesLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label fraisLabel;

    @FXML
    private Label statutLabel;

    @FXML
    private Label nombreDePlacesLabel;

    @FXML
    private WebView mapView;

    /**
     * Initialise les données de l'événement dans l'interface.
     *
     * @param evenement L'événement à afficher.
     */
    public void initData(Evenement evenement) {
        if (evenement == null) {
            System.err.println("L'événement est null.");
            return;
        }

        titreLabel.setText(evenement.getNom());

        // Gestion de l'image
        if (evenement.getPhoto() != null && !evenement.getPhoto().isEmpty()) {
            try {
                Image image = new Image(evenement.getPhoto());
                imageView.setImage(image);
            } catch (IllegalArgumentException e) {
                System.err.println("URL de l'image invalide : " + evenement.getPhoto());
                imageView.setVisible(false);
            }
        } else {
            imageView.setVisible(false);
        }

        localisationLabel.setText("Localisation : " + evenement.getLocalisation());
        datesLabel.setText("Du " + evenement.getDateDebut() + " au " + evenement.getDateFin());
        descriptionLabel.setText("Description : " + evenement.getDescription());
        fraisLabel.setText("Frais : " + evenement.getFrais() + " DT");
        statutLabel.setText("Statut : " + evenement.getStatut().toString());
        nombreDePlacesLabel.setText("Nombre de places : " + evenement.getNombreDePlaces());

        // Charger la carte
        loadMap(evenement.getLocalisation());
    }

    /**
     * Charge une carte OpenStreetMap dans le WebView.
     *
     * @param location La localisation à afficher sur la carte.
     */
    private void loadMap(String location) {
        if (location == null || location.isEmpty()) {
            System.err.println("La localisation est vide ou null.");
            return;
        }

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Carte OpenStreetMap</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.css\" />\n" +
                "    <script src=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.js\"></script>\n" +
                "    <style>\n" +
                "        #map { height: 600px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"map\"></div>\n" +
                "    <script>\n" +
                "        var map = L.map('map').setView([0, 0], 13);\n" +
                "        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                "            attribution: '© OpenStreetMap contributors'\n" +
                "        }).addTo(map);\n" +
                "        // Géocodage de la localisation\n" +
                "        fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent('" + location + "')}`)\n" +
                "            .then(response => response.json())\n" +
                "            .then(data => {\n" +
                "                if (data.length > 0) {\n" +
                "                    var lat = parseFloat(data[0].lat);\n" +
                "                    var lon = parseFloat(data[0].lon);\n" +
                "                    map.setView([lat, lon], 13);\n" +
                "                    L.marker([lat, lon]).addTo(map);\n" +
                "                }\n" +
                "            });\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";

        mapView.getEngine().loadContent(htmlContent);
    }
}