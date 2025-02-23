package models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum CategorieEnum {
    ACCESSOIRES_BIEN_ETRE("Accessoires de bien-être"),
    VETEMENTS_ACCESSOIRES_SPORT("Vêtements et accessoires de sport"),
    NUTRITION_SUPPLEMENTS("Nutrition et suppléments");

    private static final Map<String, String> DYNAMIC_CATEGORIES = Collections.synchronizedMap(new HashMap<>());

    public static CategorieEnum fromNom(String nom) {
        for (CategorieEnum categorie : CategorieEnum.values()) {
            if (categorie.getNom().equalsIgnoreCase(nom)) {
                return categorie;
            }
        }
        throw new IllegalArgumentException("Aucune catégorie trouvée pour : " + nom);
    }



    private final String nom;

    CategorieEnum(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    // Ajouter une catégorie dynamique (évite les doublons)
    public static void ajouterCategorieDynamique(String nom) {
        String key = nom.toUpperCase().replaceAll("\\s+", "_");
        DYNAMIC_CATEGORIES.putIfAbsent(key, nom);
    }

    // Récupérer toutes les catégories (statique + dynamique)
    public static Map<String, String> getToutesLesCategories() {
        Map<String, String> allCategories = new HashMap<>();

        // Ajouter les catégories statiques
        for (CategorieEnum categorie : CategorieEnum.values()) {
            allCategories.put(categorie.name(), categorie.getNom());
        }

        // Ajouter les catégories dynamiques
        allCategories.putAll(DYNAMIC_CATEGORIES);

        return Collections.unmodifiableMap(allCategories);
    }

    // Vérifier si une catégorie existe (statique ou dynamique)
    public static boolean existeCategorie(String nom) {
        for (CategorieEnum categorie : CategorieEnum.values()) {
            if (categorie.getNom().equalsIgnoreCase(nom)) {
                return true;
            }
        }
        return DYNAMIC_CATEGORIES.containsValue(nom);
    }

    public String getText() {
        return "";
    }
}
