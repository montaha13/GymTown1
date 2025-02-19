package com.example.gestionutilisateur.controllers;


import com.example.gestionutilisateur.Entity.User;
import com.example.gestionutilisateur.service.Usercrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EditProfileController {
    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField adresseField;

    @FXML
    private TextField telephoneField;

    private User currentUser;
    @FXML
    private TextField photoField;

    @FXML
    private TextField noteField;


    @FXML
    private TextField specialiteField;

    public void initialize() {
        // Fetch current user from session
        currentUser = SessionManager.getCurrentUser();

        // Populate fields with current user's information
        nomField.setText(currentUser.getNom());
        prenomField.setText(currentUser.getPrenom());
        emailField.setText(currentUser.getEmail());
        adresseField.setText(currentUser.getAdresse());
        telephoneField.setText(currentUser.getTelephone());
        photoField.setText(currentUser.getPhoto());
        noteField.setText(String.valueOf(currentUser.getNote()));
        specialiteField.setText(currentUser.getSpecialite());
    }

    private void switchScene(String fxmlFile, ActionEvent event) {
        try {
            System.out.println("fxml:" + fxmlFile);

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
    void saveChanges(ActionEvent event) {
        // Get updated information from input fields
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String adresse = adresseField.getText();
        String telephone = telephoneField.getText();
        String photo = photoField.getText();
        int note = Integer.parseInt(noteField.getText());
        String specialite = specialiteField.getText();
        String password = passwordField.getText(); // You may choose to hash this if you want to update the password

        // Update user object with new information
        currentUser.setNom(nom);
        currentUser.setPrenom(prenom);
        currentUser.setEmail(email);
        currentUser.setAdresse(adresse);
        currentUser.setTelephone(telephone);
        currentUser.setPhoto(photo);
        currentUser.setNote(note);
        currentUser.setSpecialite(specialite);

        // Update user in the database
        Usercrud usercrud = new Usercrud();
        usercrud.updateUser(currentUser);

        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Profile Updated", "Your profile has been updated successfully.");
        String userRole = currentUser.getRoles();
        if (userRole.equals("Admin")) {
            switchScene("/com/example/gestionutilisateur/AdminPage.fxml", event);
        } else {
            switchScene("/com/example/gestionutilisateur/Home.fxml", event);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void AdminPage(ActionEvent event) {
        switchScene("/com/example/gestionutilisateur/AdminPage.fxml", event);
    }


}