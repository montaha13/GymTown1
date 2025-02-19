package com.example.gestionutilisateur.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.gestionutilisateur.Entity.User;
import com.example.gestionutilisateur.service.SystemNotification;
import com.example.gestionutilisateur.service.Usercrud;
import javafx.scene.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;



public class Registre {
    @FXML
    private Label noteErrorLabel;
    @FXML
    private Label nomErrorLabel;

    @FXML
    private Label prenomErrorLabel;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label adresseErrorLabel;

    @FXML
    private Label telephoneErrorLabel;

    @FXML
    private Label passwordErrorLabel;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField Adresse;

    @FXML
    private TextField email;

    @FXML
    private Label lblErrors;

    @FXML
    private Button loginButton;

    @FXML
    private TextField nom;

    @FXML
    private PasswordField password;

    @FXML
    private TextField prenom;

    @FXML
    private Label generalErrorLabel;

    @FXML
    private TextField telephone;
    @FXML
    private TextField specialite;
    @FXML
    private TextField photo;
    @FXML
    private TextField note;
    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    public void initialize() {

        roleComboBox.setPromptText("Choisir un rôle");
        roleComboBox.getItems().addAll("admin", "sportif", "responsableSalle", "coach");
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    @FXML
    void Registre(ActionEvent event) {

        String nom = this.nom.getText();
        String prenom = this.prenom.getText();
        String email = this.email.getText();
        String adresse = this.Adresse.getText();
        String telephone = this.telephone.getText();
        String password = this.password.getText();
        String photo = this.photo.getText();
        String specialite = this.specialite.getText();
        int note = 0;
        try {
            note = Integer.parseInt(this.note.getText()); // Essaye de convertir la note en entier
        } catch (NumberFormatException e) {
            setErrorLabel(noteErrorLabel, "La note doit être un nombre entier");
            return;
        }

        // Optionnel : vérifie que la note est dans une plage raisonnable (par exemple, entre 0 et 20)
        if (note < 0 || note > 20) {
            setErrorLabel(noteErrorLabel, "La note doit être entre 0 et 20");
            return;
        }

        if (!checkFieldsNotEmpty()) {
            return;
        }
            if (!isValidEmail(email)) {
                setErrorLabel(emailErrorLabel, " invalid email address");
                return;
            }
            if (new Usercrud().isEmailExistsInDatabase(email)) {
                // Display an error message in red in the email error label
                setErrorLabel(emailErrorLabel, "Email already exists");
                return;
            }

        String selectedRole = roleComboBox.getValue();
        if (selectedRole == null || selectedRole.isEmpty()) {
            // Display an alert if no role is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a role");
            alert.show();
            return;
        }


    String hashedPassword = hashPassword(password);
    User newUser = new User(selectedRole,email,hashedPassword, prenom,nom ,telephone, adresse,photo ,specialite,note );

    Usercrud add = new Usercrud();
    add.addUser(newUser);

    SystemNotification.showNotification("New User Registered", "A new user has been registered.");

    switchScene("/com/example/gestionutilisateur/Login.fxml", event);


}

    private void switchScene(String fxmlFile, ActionEvent event) {
        try {
            System.out.println("fxml:"+ fxmlFile);

            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleButtonAction(MouseEvent event) {

    }
    @FXML
    void Login(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionutilisateur/Login.fxml"));
            Parent root = loader.load();
            loginButton.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setErrorLabel(Label label, String message) {
        label.setTextFill(Color.RED);
        label.setText(message);
    }

    private void clearErrorLabels() {
        nomErrorLabel.setText("");
        nomErrorLabel.setTextFill(Color.BLACK);
        prenomErrorLabel.setText("");
        prenomErrorLabel.setTextFill(Color.BLACK);
        emailErrorLabel.setText("");
        emailErrorLabel.setTextFill(Color.BLACK);
        adresseErrorLabel.setText("");
        adresseErrorLabel.setTextFill(Color.BLACK);
        telephoneErrorLabel.setText("");
        telephoneErrorLabel.setTextFill(Color.BLACK);
        passwordErrorLabel.setText("");
        passwordErrorLabel.setTextFill(Color.BLACK);
    }
    private boolean checkFieldsNotEmpty () {
        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || email.getText().isEmpty() ||
                Adresse.getText().isEmpty() || telephone.getText().isEmpty() || password.getText().isEmpty() ||
                photo.getText().isEmpty() || specialite.getText().isEmpty() || note.getText().isEmpty()) {

            setErrorLabel(generalErrorLabel, "All fields must be filled out.");
            return false;
        }
        return true;
    }
    }
