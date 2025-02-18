package tests;

import models.*;
import services.ServiceCategorie;
import services.ServiceCommande;
import services.ServiceProduit;


import java.sql.Date;
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
                CategorieEnum. VETEMENTS_ACCESSOIRES_SPORT

        );

        Categorie c = new Categorie("TAPIS");

        Produit produit = new Produit();
        produit.setId(13);  // Assurez-vous de bien initialiser l'ID du produit

        Commande co = new Commande(produit , LocalDateTime.now(), "Tunis", "98765432", "test@mail.com", 2, 100, StatutCommande.en_attente );

        Commande coo = new Commande(
                p,
                LocalDateTime.now(),
                "Tunis",
                "98765432",
                "test@mail.com",
                2,
                p.getPrix() * 2,
                StatutCommande.en_attente // Vérifiez la syntaxe exacte pour l'enum
        );









        ServiceProduit sv = new ServiceProduit();
        ServiceCategorie sc = new ServiceCategorie();
        ServiceCommande svc = new ServiceCommande();


        try {
            sv.ajouter(p);
            //sv.supprimer(7);
             //sv.modifier(10, "sac");
            //sc.ajouter(c);
            //sc.supprimer(6);
            //sc.modifier(10, "sac");
            //svc.ajouter(co);
            //svc.ajouter(coo);
            //svc.supprimer(7);

            //svc.modifier(10, "sac");
           System.out.println(sv.recuperer());
            //System.out.println(sc.recuperer());
            //System.out.println(svc.recuperer());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
