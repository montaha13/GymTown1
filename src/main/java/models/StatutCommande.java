package models;

public enum StatutCommande {
    en_attente,
    acceptée,
    refusée;

    public static StatutCommande fromString(String status) {
        if (status != null) {
            for (StatutCommande s : StatutCommande.values()) {
                if (s.name().equalsIgnoreCase(status.trim())) {
                    return s;
                }
            }
        }
        return en_attente; // Statut par défaut si la valeur est inconnue
    }




}
