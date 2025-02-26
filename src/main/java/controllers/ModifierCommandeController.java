package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Commande;
import models.StatutCommande;
import services.ServiceCommande;
import javafx.event.ActionEvent;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ModifierCommandeController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField midCommande;
    @FXML
    private TextField mEmail;
    @FXML
    private TextField mLocalisation;
    @FXML
    private TextField mTelephone;
    @FXML
    private TextField mPrix;
    @FXML
    private TextField mQuantite;
    @FXML
    private TextField mTotal;
    @FXML
    private TextField mDate;
    @FXML
    private ComboBox<String> statutComboBox;
    @FXML
    private Button btnModifierCommande;
    @FXML
    private Button btnAnnulerCommande;


    private Stage stage;
    private Commande commande; // La commande à modifier
    private int commandeId;

    private final ServiceCommande sc = new ServiceCommande();

    @FXML
    public void initialize() {
        // Remplir le ComboBox avec les noms des statuts disponibles
        statutComboBox.setItems(FXCollections.observableArrayList(
                Arrays.stream(StatutCommande.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        ));
    }

    // Méthode permettant de recevoir la commande à modifier et de remplir les champs de l'interface
    public void setCommande(Commande commande) {
        if (commande == null) {
            System.out.println("Commande NULL !");
            return;
        }
        this.commande = commande;
        this.commandeId = commande.getId();

        // Remplir les champs avec les données de la commande
        //midCommande.setText(String.valueOf(commande.getId()));
        mEmail.setText(commande.getMail());
        mLocalisation.setText(commande.getLocalisation());
        mTelephone.setText(commande.getTelephone());
        mPrix.setText(String.valueOf(commande.getPrix()));
        mQuantite.setText(String.valueOf(commande.getNombre()));
        mTotal.setText(String.valueOf(commande.getTotal()));
        mDate.setText(commande.getDate().toString());
        statutComboBox.setValue(commande.getStatut().name());
    }


    // Méthode pour modifier uniquement le statut d'une commande
    @FXML
    private void modifierCommande(ActionEvent event) {
        try {
            // Récupérer le nouveau statut à partir du ComboBox
            StatutCommande nouveauStatut = StatutCommande.valueOf(statutComboBox.getValue());

            // Modifier uniquement le statut
            sc.modifierStatut(commandeId, nouveauStatut);

            // Afficher un message de succès
            afficherMessage(AlertType.INFORMATION, "Succès", "Statut de la commande modifié avec succès.");
        } catch (IllegalArgumentException e) {
            afficherMessage(AlertType.ERROR, "Erreur", "Statut invalide.");
        } catch (Exception e) {
            afficherMessage(AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la modification.");
        }
    }

    // Méthode d'annulation de la modification
    @FXML
    private void annulerCommande(ActionEvent event) {
        stage.close(); // Ferme la fenêtre actuelle
    }

    private void afficherMessage(AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
