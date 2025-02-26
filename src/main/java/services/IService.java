package services;

import models.Categorie;
import models.Commande;
import models.Produit;
import models.StatutCommande;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter (T t ) throws SQLException, IOException;

    List<Commande> readAll2() throws SQLException;

    void supprimer (int id) throws SQLException;
    void modifier(int id , String nom ) throws  SQLException;
    void modifier1(int id, String nom, String description, String ref, String photo, double prix, int quantite, String categorie) throws  SQLException;
    List<T> recuperer() throws SQLException;

    List<Categorie> readAll() throws SQLException;
    List<Produit> readAll1() throws SQLException;

    void modifier1(Commande updatedCommande);

    void modifier1(Produit updatedProduit);

    // Méthode pour modifier une commande
    void modifier(int id, Commande updatedCommande) throws SQLException;

    // Méthode pour modifier une commande
    void modifier(int id, StatutCommande statut) throws SQLException;

    void modifier(Commande updatedCommmande);

    // Méthode pour modifier une commande
    void modifier3(int id, String statut);

    void modifierStatut(int id, StatutCommande statut) throws SQLException;

    void modifier3(Commande updatedCommande);
}
