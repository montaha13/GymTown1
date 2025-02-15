package models;

public class Cours {
    private int id;
    private String dateDebut;
    private String dateFin;
    private String localisation;
    private String description;

    public Cours(String dateDebut, String dateFin, String localisation, String description) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.localisation = localisation;
        this.description = description;

    }

    public Cours() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public String toString() {
        return "Cour{" + "id=" + id + ", dateDebut='" + dateDebut + '\'' + ", dateFin='" + dateFin + '\'' +
                ", localisation='" + localisation + '\'' + ", description='" + description + '}';
    }
}
