package com.example.gestionutilisateur.controllers;
import com.example.gestionutilisateur.Entity.User;
import com.example.gestionutilisateur.service.Usercrud;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

import javafx.scene.control.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import java.util.Comparator;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
public class AdminPage {



    @FXML
    private Label NameUser;


    @FXML
    private TableView<User> UserTable;

    @FXML
    private TableColumn<User, String> Nom;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<User, String> Prenom;

    @FXML
    private TableColumn<User, String> Email;

    @FXML
    private TableColumn<User, String> Adresse;

    @FXML
    private TableColumn<User, String> Telephone;

    @FXML
    private TableColumn<User, String> Roles;

    @FXML
    private TableColumn<User, String> specialite;

    @FXML
    void initialize() {

        User currentUser = SessionManager.getCurrentUser();
        NameUser.setText( currentUser.getNom() + " " + currentUser.getPrenom());

        Nom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        Prenom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
        Email.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        Adresse.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdresse()));
        Telephone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));
        Roles.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoles()));
        specialite.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSpecialite()));

        Usercrud userCrud = new Usercrud();
        ObservableList<User> userList = FXCollections.observableArrayList(userCrud.getAllData());
        //userList.removeIf(user -> user.getRoles().equalsIgnoreCase("admin"));
        UserTable.setItems(userList);


        //loadUsers();


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
    void EditProfile(ActionEvent event) {
        switchScene("/com/example/gestionutilisateur/EditProfile.fxml", event);

    }


    @FXML
    void delete(ActionEvent event) {
        // Get the selected user from the table
        User selectedUser = UserTable.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Confirm deletion with a dialog box
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete User");
            alert.setContentText("Are you sure you want to delete the selected user?");

            // Handle user's choice
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User confirmed deletion
                    Usercrud usercrud = new Usercrud();
                    usercrud.deleteUser(selectedUser);
                    // Remove the user from the table
                    UserTable.getItems().remove(selectedUser);

                }
            });
        } else {
            // No user selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("No User Selected");
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
        }
    }
    @FXML
    void logout(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText("Logout");
        alert.setContentText("Are you sure you want to log out?");

        // Handle user's choice
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed logout
                SessionManager.clearSession(); // Clear the session
                // Close the current window (assuming this is the only stage)
                Stage stage = (Stage) NameUser.getScene().getWindow();
                stage.close();
                // You might want to open a login window here
                // Example:
                 LoginController.showLoginWindow();
            }
        });
    }






    @FXML
    void sortAsc() {
        ObservableList<User> userList = UserTable.getItems();
        userList.sort(Comparator.comparing(User::getNom));
    }

    @FXML
    void sortDesc() {
        ObservableList<User> userList = UserTable.getItems();
        userList.sort(Comparator.comparing(User::getNom).reversed());
    }


    @FXML
    void Materiels(ActionEvent event) {
        switchScene("/com/example/gestionutilisateur/Materiels.fxml", event);

    }
    // Method to calculate role count
    @FXML
    void generatePDF(ActionEvent event) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700); // Starting position
            PDType1Font font = PDType1Font.HELVETICA;

            String text = "List of Users.";
            String[] lines = text.split("\n");

            for (String line : lines) {
                contentStream.newLineAtOffset(15, -15);
                contentStream.setFont(font, 12);
                contentStream.showText(line);
                contentStream.newLine();
            }

            ObservableList<User> userList = UserTable.getItems();
            float startY = 650; // Initial vertical position
            float lineHeight = 15; // Height of each line
            float currentY = startY;

            for (User user : userList) {
                float spaceLeft = currentY - 50; // Calculate remaining space on the page
                float requiredSpace = 6 * lineHeight + 40; // Space required for one user entry

                if (spaceLeft < requiredSpace) { // If not enough space, move to the next page
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);

                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 700); // Starting position
                    currentY = startY;
                }

                contentStream.newLineAtOffset(0, -20); // Add some space between each user

                contentStream.showText("Nom: " + user.getNom());
                contentStream.newLineAtOffset(0, -lineHeight); // Move to the next line
                contentStream.showText("Prénom: " + user.getPrenom());
                contentStream.newLineAtOffset(0, -lineHeight);
                contentStream.showText("Email: " + user.getEmail());
                contentStream.newLineAtOffset(0, -lineHeight);
                contentStream.showText("Adresse: " + user.getAdresse());
                contentStream.newLineAtOffset(0, -lineHeight);
                contentStream.showText("Téléphone: " + user.getTelephone());
                contentStream.newLineAtOffset(0, -lineHeight);
                contentStream.showText("Rôles: " + user.getRoles());
                contentStream.newLineAtOffset(0, -lineHeight);

                contentStream.newLineAtOffset(0, -20); // Add some space between each user
                currentY -= requiredSpace;
            }

            contentStream.endText();
            contentStream.close();

            document.save("user_list.pdf");
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Généré");
            alert.setHeaderText(null);
            alert.setContentText("Le document PDF contenant la liste des utilisateurs a été généré avec succès !");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la génération du PDF");
            alert.setContentText("Une erreur est survenue lors de la génération du PDF. Veuillez réessayer.");
            alert.showAndWait();
        }
    }

}
