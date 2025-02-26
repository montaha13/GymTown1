package tests;

import models.*;
import services.ServiceCategorie;
import services.ServiceCommande;
import services.ServiceProduit;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        // Création des produits
        Produit p = new Produit(
                "https://i.pinimg.com/originals/59/b8/a8/59b8a8de048399f5a390c185698bc23d.jpg",
                "sport taekwondo1122",
                "ceinture5",
                "CE24",
                4084,
                75504,
                CategorieEnum.VETEMENTS_ACCESSOIRES_SPORT
        );

        // Création d'une catégorie
        Categorie c = new Categorie("TAPIS");

        // Création d'un produit avec ID défini manuellement (assurez-vous que cela correspond à la logique de votre service)
        Produit produit = new Produit();
        produit.setId(13);  // Assurez-vous de bien initialiser l'ID du produit

        // Création d'une commande



        // Services pour gérer les entités
        ServiceProduit sv = new ServiceProduit();
        ServiceCategorie sc = new ServiceCategorie();
        ServiceCommande svc = new ServiceCommande();

        try {
            // Vous pouvez décommenter ces lignes en fonction de ce que vous souhaitez tester
            sv.ajouter(p); // Ajouter le produit
            //sv.supprimer(7); // Supprimer un produit (exemple avec l'ID 7)
            //sv.modifier(10, "sac"); // Modifier un produit (exemple avec l'ID 10)

            //sc.ajouter(c); // Ajouter une catégorie
            //sc.supprimer(6); // Supprimer une catégorie
            //sc.modifier(10, "sac"); // Modifier une catégorie (exemple avec l'ID 10)

            //svc.ajouter(co); // Ajouter une commande
            //svc.ajouter(coo); // Ajouter une autre commande
            //svc.supprimer(7); // Supprimer une commande

            // Récupérer et afficher les produits, catégories, ou commandes
            System.out.println(sv.recuperer()); // Récupérer les produits
            //System.out.println(sc.recuperer()); // Récupérer les catégories
            //System.out.println(svc.recuperer()); // Récupérer les commandes

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur IO : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
