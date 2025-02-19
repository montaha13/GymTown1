package com.example.gestionutilisateur.service;

import com.example.gestionutilisateur.Entity.User;
import com.example.gestionutilisateur.cnx.MyConnections;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class Usercrud {

    public void addUser(User user) {
        String email = user.getEmail();
        String checkQuery = "SELECT COUNT(*) FROM utilisateur WHERE email='" + email + "'";
        try {
            Statement checkStatement = MyConnections.getInstance().getCnx().createStatement();
            ResultSet resultSet = checkStatement.executeQuery(checkQuery);
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count > 0) {
                System.out.println("User with this email already exists in the database.");
                return; // Exit the method without adding the user
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while checking for existing email: " + e.getMessage());
            return; // Exit the method if an error occurs
        }

        // If the email doesn't exist, proceed with adding the user
        String requete = "INSERT INTO utilisateur (role, email, `password`, prenom, nom, telephone, adresse, photo, specialite, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = MyConnections.getInstance().getCnx().prepareStatement(requete)) {
            st.setString(1, user.getRoles());      // role
            st.setString(2, user.getEmail());      // email
            st.setString(3, user.getPassword());   // password
            st.setString(4, user.getPrenom());     // prenom
            st.setString(5, user.getNom());        // nom
            st.setString(6, user.getTelephone());  // telephone
            st.setString(7, user.getAdresse());    // adresse
            st.setString(8, user.getPhoto());      // photo
            st.setString(9, user.getSpecialite()); // specialite
            st.setInt(10, user.getNote());
            System.out.println(st);


            st.executeUpdate();
            System.out.println("User added!!");
        } catch (SQLException e) {
            System.out.println("Error occurred while adding user: " + e.getMessage());
        }
    }



    public boolean isEmailExistsInDatabase(String email) {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE email=?";
        try {
            PreparedStatement statement = MyConnections.getInstance().getCnx().prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while checking email existence: " + e.getMessage());
        }
        return false;
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM utilisateur WHERE email=?";
        User user = null;

        try {
            PreparedStatement statement = MyConnections.getInstance().getCnx().prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String role = resultSet.getString("role");
                String userEmail = resultSet.getString("email");
                String password = resultSet.getString("password");
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                String telephone = resultSet.getString("telephone");
                String adresse = resultSet.getString("adresse");
                String photo = resultSet.getString("photo");
                String specialite = resultSet.getString("specialite");
                int note = resultSet.getInt("note");


                user = new User(id,role,userEmail,password, prenom,nom, telephone, adresse, photo, specialite,note);
                System.out.println(user.toString());// Adjusted instantiation
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by email: " + e.getMessage());
        }

        return user;
    }


    public void updateUser(User user) {
        // Vérifie si l'email existe déjà dans la base de données, en excluant l'utilisateur actuel
        String email = user.getEmail();
        int userId = user.getId();
        String checkQuery = "SELECT COUNT(*) FROM utilisateur WHERE email=? AND id<>?";

        try (PreparedStatement checkStatement = MyConnections.getInstance().getCnx().prepareStatement(checkQuery)) {
            checkStatement.setString(1, email);
            checkStatement.setInt(2, userId);

            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count > 0) {
                System.out.println("This email already exists in the database.");
                return; // Quitte la méthode sans mettre à jour l'utilisateur
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while checking for existing email: " + e.getMessage());
            return; // Quitte la méthode si une erreur survient
        }

        // Si l'email n'existe pas, continue avec la mise à jour de l'utilisateur
        String updateQuery = "UPDATE utilisateur SET " +
                "nom=?, " +
                "prenom=?, " +
                "email=?, " +
                "`password`=?, " +
                "adresse=?, " +
                "telephone=?, " +
                "role=?, " +
                "photo=?, " +
                "specialite=?, " +
                "note=? " +
                "WHERE id=?";

        try (PreparedStatement st = MyConnections.getInstance().getCnx().prepareStatement(updateQuery)) {
            // Remplissage des paramètres de la requête préparée
            st.setString(1, user.getNom());
            st.setString(2, user.getPrenom());
            st.setString(3, user.getEmail());
            st.setString(4, user.getPassword());
            st.setString(5, user.getAdresse());
            st.setString(6, user.getTelephone());
            st.setString(7, user.getRoles());
            st.setString(8, user.getPhoto());
            st.setString(9, user.getSpecialite());
            st.setInt(10, user.getNote());
            st.setInt(11, user.getId());

            // Exécution de la mise à jour
            st.executeUpdate();
            System.out.println("User updated successfully!!");
        } catch (SQLException e) {
            System.out.println("Error occurred while updating user: " + e.getMessage());
        }
    }
    public void deleteUser(User user) {
        String requete = "DELETE FROM utilisateur WHERE id=" + user.getId();
        try {
            Statement st = MyConnections.getInstance().getCnx().createStatement();
            st.executeUpdate(requete);
            System.out.println("User deleted!!");
        } catch (SQLException e) {
            System.out.println("Error occurred while deleting user: " + e.getMessage());
        }
    }

    public List<User> getAllData() {
        List<User> userList = new ArrayList<>();

        String query = "SELECT id, `role`, email, password,prenom, nom, telephone, adresse, photo, specialite, note FROM utilisateur";
        try {
            Statement statement = MyConnections.getInstance().getCnx().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String role = resultSet.getString("role");
                String userEmail = resultSet.getString("email");
                String password = resultSet.getString("password");
                String prenom = resultSet.getString("prenom");
                String nom = resultSet.getString("nom");
                String telephone = resultSet.getString("telephone");
                String adresse = resultSet.getString("adresse");
                String photo = resultSet.getString("photo");
                String specialite = resultSet.getString("specialite");
                int note = resultSet.getInt("note");

                User user = new User(id, role, userEmail, password, prenom, nom, telephone, adresse, photo, specialite, note);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching user data: " + e.getMessage());
        }

        // Debugging: Check if the list is populated
        System.out.println("User list size: " + userList.size());  // Add this line for debugging
        return userList;
    }
    public void resetPassword(String email, String newPassword) {
        String query = "UPDATE utilisateur SET `password` = ? WHERE email = ?";

        try {
            PreparedStatement statement = MyConnections.getInstance().getCnx().prepareStatement(query);
            statement.setString(1, newPassword);
            statement.setString(2, email);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password reset successfully.");
            } else {
                System.out.println("Failed to reset password. User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error resetting password: " + e.getMessage());
        }
    }
    public boolean isVerificationCodeCorrect(String email, String verificationCode) {
        String query = "SELECT verification_code FROM utilisateur WHERE email = ?";
        try {
            PreparedStatement statement = MyConnections.getInstance().getCnx().prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedVerificationCode = resultSet.getString("verification_code");
                return verificationCode.equals(storedVerificationCode);
            } else {
                System.out.println("No user found with email: " + email);
                return false; // Email not found in the database
            }
        } catch (SQLException e) {
            System.out.println("Error checking verification code: " + e.getMessage());
            return false; // Error occurred while querying the database
        }
    }


    public void updateVerificationCode(String email, String verificationCode) {
        String query = "UPDATE utilisateur SET verification_code = ? WHERE email = ?";
        try {
            PreparedStatement statement = MyConnections.getInstance().getCnx().prepareStatement(query);
            statement.setString(1, verificationCode);
            statement.setString(2, email);
            statement.executeUpdate();
            System.out.println("Verification code updated for user with email: " + email);
        } catch (SQLException e) {
            System.out.println("Error updating verification code: " + e.getMessage());
        }
    }




}