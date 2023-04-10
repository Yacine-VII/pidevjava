package com.pdev.gui.front.abonnement;

import com.pdev.entities.Abonnement;
import com.pdev.services.AbonnementService;
import com.pdev.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAllController implements Initializable {

    public static Abonnement currentAbonnement;

    @FXML
    public Text topText;

    public VBox mainVBox;

    List<Abonnement> listAbonnement;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listAbonnement = AbonnementService.getInstance().getAll();
        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listAbonnement);

        if (!listAbonnement.isEmpty()) {
            for (Abonnement abonnement : listAbonnement) {

                mainVBox.getChildren().add(makeAbonnementModel(abonnement));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeAbonnementModel(
            Abonnement abonnement
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_ABONNEMENT)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#typeText")).setText("Type : " + abonnement.getType());
            ((Text) innerContainer.lookup("#titreText")).setText("Titre : " + abonnement.getTitre());
            ((Text) innerContainer.lookup("#prixText")).setText("Prix : " + abonnement.getPrix());
            ((Text) innerContainer.lookup("#dureeText")).setText("Duree : " + abonnement.getDuree());
            ((Text) innerContainer.lookup("#niveauAccessText")).setText("NiveauAccess : " + abonnement.getNiveauAccess());
            ((Text) innerContainer.lookup("#reservationsTotalText")).setText("ReservationsTotal : " + abonnement.getReservationsTotal());
            ((Text) innerContainer.lookup("#reservationsRestantesText")).setText("ReservationsRestantes : " + abonnement.getReservationsRestantes());


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }
}
