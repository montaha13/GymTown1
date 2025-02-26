package Models;
public enum StatutP {
    EN_ATTENTE,  // Statut en attente
    ACCEPTER,    // Statut accepté (changé de ACCEPTEE à ACCEPTER)
    REFUSER;     // Statut refusé (changé de REFUSEE à REFUSER)

    // Méthode pour obtenir le statut depuis la base de données avec une normalisation
    public static StatutP fromString(String status) {
        if (status != null) {
            for (StatutP s : StatutP.values()) {
                if (s.name().equalsIgnoreCase(status.trim())) {
                    return s;
                }
            }
        }
        return EN_ATTENTE; // ou tout autre statut par défaut
    }

    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}