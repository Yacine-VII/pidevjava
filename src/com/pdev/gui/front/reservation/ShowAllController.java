package com.pdev.gui.front.reservation;

import com.pdev.entities.Reservation;
import com.pdev.gui.front.MainWindowController;
import com.pdev.services.ReservationService;
import com.pdev.utils.AlertUtils;
import com.pdev.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Reservation currentReservation;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;

    List<Reservation> listReservation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listReservation = ReservationService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listReservation);

        if (!listReservation.isEmpty()) {
            for (Reservation reservation : listReservation) {
                    mainVBox.getChildren().add(makeReservationModel(reservation));
            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeReservationModel(
            Reservation reservation
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_RESERVATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#abonnementText")).setText("Abonnement : " + reservation.getAbonnement());
            ((Text) innerContainer.lookup("#joueurText")).setText("Abonnement : " + reservation.getJoueur());
            ((Text) innerContainer.lookup("#entraineurText")).setText("Abonnement : " + reservation.getEntraineur());
            ((Text) innerContainer.lookup("#sujetText")).setText("Sujet : " + reservation.getSujet());
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + reservation.getDate());
            ((Text) innerContainer.lookup("#heureText")).setText("Heure : " + reservation.getHeure());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierReservation(reservation));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerReservation(reservation));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterReservation(ActionEvent event) {
        currentReservation = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_RESERVATION);
    }

    private void modifierReservation(Reservation reservation) {
        currentReservation = reservation;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_RESERVATION);
    }

    private void supprimerReservation(Reservation reservation) {
        currentReservation = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer reservation ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            if (ReservationService.getInstance().delete(reservation.getId())) {
                MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RESERVATION);
            } else {
                AlertUtils.makeError("Could not delete reservation");
            }
        }
    }
}
