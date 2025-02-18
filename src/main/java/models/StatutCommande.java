package models;

public enum StatutCommande {
    en_attente,
    acceptée,
    refusée;

    // Méthode pour obtenir le statut depuis la base de données avec une normalisation
    public static StatutCommande fromString(String status) {
        if (status != null) {
            for (StatutCommande s : StatutCommande.values()) {
                if (s.name().equalsIgnoreCase(status.trim())) {
                    return s;
                }
            }
        }
        return en_attente; // ou tout autre statut par défaut
    }
}
