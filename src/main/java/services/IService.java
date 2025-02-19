<<<<<<< Updated upstream
package services;

import models.Categorie;
import models.Produit;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter (T t ) throws SQLException;
    void supprimer ( int id) throws SQLException;
    void modifier(int id , String nom ) throws  SQLException;
    List<T> recuperer() throws SQLException;

    List<Categorie> readAll() throws SQLException;
    List<Produit> readAllProduits() throws SQLException;

}
=======
package services;

import Models.Statut;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter(T t) throws SQLException;
    void supprimer ( int id ) throws SQLException ;
    void modifier(T t) throws SQLException;
    List<T> recuperer() throws SQLException;
}
>>>>>>> Stashed changes
