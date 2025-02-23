package services;

import models.Categorie;
import models.Produit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter (T t ) throws SQLException, IOException;
    void supprimer ( int id) throws SQLException;
    void modifier(int id , String nom ) throws  SQLException;
    void modifier1(int id, String nom, String description, String ref, String photo, double prix, int quantite, String categorie) throws  SQLException;
    List<T> recuperer() throws SQLException;

    List<Categorie> readAll() throws SQLException;
    List<Produit> readAll1() throws SQLException;

    void modifier1(Produit updatedProduit);
}
