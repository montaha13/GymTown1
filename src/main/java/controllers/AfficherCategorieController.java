package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AfficherCategorieController {

    @FXML
    private TextField rcategorie;

    @FXML
    private TextField rlist;



    public void setRlist(String rlist) {
        this.rlist.setText(rlist);
    }

    public void setRcategorie(String rcategorie) {
        this.rcategorie.setText(rcategorie);
    }
}
