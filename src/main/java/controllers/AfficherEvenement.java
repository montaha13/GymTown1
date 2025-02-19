package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import Models.Evenement; // Assurez-vous d'importer la classe Evenement
import services.ServiceEvenement;

public class AfficherEvenement {

    @FXML
    private TextField rdateDebut;

    @FXML
    private TextField rdateFin;

    @FXML
    private TextField rdescription;

    @FXML
    private TextField rfrais;

    @FXML
    private TextField rlist;

    @FXML
    private TextField rlocalisation;

    @FXML
    private TextField rnom;

    @FXML
    private TextField rphoto;

    @FXML
    private TextField rstatut;




    public void setRdateDebut(String rdateDebut) {
        this.rdateDebut.setText(rdateDebut);
    }

    public void setRdateFin(String rdateFin) {
        this.rdateFin.setText(rdateFin);
    }

    public void setRdescription(String rdescription) {
        this.rdescription.setText(rdescription);
    }

    public void setRfrais(String rfrais) {
        this.rfrais.setText(rfrais);
    }

    public void setRlist(String rlist) {
        this.rlist.setText(rlist);
    }

    public void setRlocalisation(String rlocalisation) {
        this.rlocalisation.setText(rlocalisation);
    }

    public void setRnom(String rnom) {
        this.rnom.setText(rnom);
    }

    public void setRphoto(String rphoto) {
        this.rphoto.setText(rphoto);
    }

    public void setRstatut(String rstatut) {
        this.rstatut.setText(rstatut);
    }

}
