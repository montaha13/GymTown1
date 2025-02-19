package Models;

public enum Statut {
    A_VENIR,   // L'événement est à venir
    EN_COURS,  // L'événement est en cours
    TERMINE,   // L'événement est terminé
    ANNULE,    // L'événement a été annulé
    EN_ATTENTE;

    // Méthode pour obtenir le statut depuis la base de données avec une normalisation
    public static Statut fromString(String status) {
        if (status != null) {
            for (Statut s : Statut.values()) {
                if (s.name().equalsIgnoreCase(status.trim())) {
                    return s;
                }
            }
        }
        return EN_ATTENTE; // ou tout autre statut par défaut
    }
}

