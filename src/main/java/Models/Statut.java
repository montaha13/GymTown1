package Models;

public enum Statut {
    A_VENIR,   // L'événement est à venir
    TERMINE,   // L'événement est terminé
    COMPLET;   // L'événement est complet (plus de places disponibles)

    // Méthode pour obtenir le statut depuis la base de données avec une normalisation
    public static Statut fromString(String status) {
        if (status != null) {
            for (Statut s : Statut.values()) {
                if (s.name().equalsIgnoreCase(status.trim())) {
                    return s;
                }
            }
        }
        return A_VENIR; // Valeur par défaut si le statut n'est pas reconnu
    }
}