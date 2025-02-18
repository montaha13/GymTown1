package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AfficherProduitController {

    @FXML
    private TextField rnom;

    @FXML
    private TextField rdescription;

    @FXML
    private TextField rref;

    @FXML
    private TextField rphoto;

    @FXML
    private TextField rprix;

    @FXML
    private TextField rquantite;

    @FXML
    private TextField rcategorie;

    @FXML
    private TextField rlist;

    // Setters pour chaque champ
    public void setRnom(String rnom) {
        this.rnom.setText(rnom);
    }

    public void setRdescription(String rdescription) {
        this.rdescription.setText(rdescription);
    }

    public void setRref(String rref) {
        this.rref.setText(rref);
    }

    public void setRphoto(String rphoto) {
        this.rphoto.setText(rphoto);
    }

    public void setRprix(String rprix) {
        this.rprix.setText(rprix);
    }

    public void setRquantite(String rquantite) {
        this.rquantite.setText(rquantite);
    }

    public void setRcategorie(String rcategorie) {
        this.rcategorie.setText(rcategorie);
    }

    public void setRlist(String rlist) {
        this.rlist.setText(rlist);
    }
}
