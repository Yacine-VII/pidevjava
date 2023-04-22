package com.pdev.gui.back.abonnement;

import com.pdev.entities.Abonnement;
import com.pdev.gui.back.MainWindowController;
import com.pdev.services.AbonnementService;
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
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Abonnement currentAbonnement;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public ComboBox<String> sortCB;

    List<Abonnement> listAbonnement;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listAbonnement = AbonnementService.getInstance().getAll();
        sortCB.getItems().addAll("Type", "Titre", "Prix", "Duree", "NiveauAccess", "ReservationsTotal", "ReservationsRestantes");
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
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_ABONNEMENT)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#typeText")).setText("Type : " + abonnement.getType());
            ((Text) innerContainer.lookup("#titreText")).setText("Titre : " + abonnement.getTitre());
            ((Text) innerContainer.lookup("#prixText")).setText("Prix : " + abonnement.getPrix());
            ((Text) innerContainer.lookup("#dureeText")).setText("Duree : " + abonnement.getDuree());
            ((Text) innerContainer.lookup("#niveauAccessText")).setText("NiveauAccess : " + abonnement.getNiveauAccess());
            ((Text) innerContainer.lookup("#reservationsTotalText")).setText("ReservationsTotal : " + abonnement.getReservationsTotal());
            ((Text) innerContainer.lookup("#reservationsRestantesText")).setText("ReservationsRestantes : " + abonnement.getReservationsRestantes());

            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierAbonnement(abonnement));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerAbonnement(abonnement));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterAbonnement(ActionEvent event) {
        currentAbonnement = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_ABONNEMENT);
    }

    private void modifierAbonnement(Abonnement abonnement) {
        currentAbonnement = abonnement;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_ABONNEMENT);
    }

    private void supprimerAbonnement(Abonnement abonnement) {
        currentAbonnement = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer abonnement ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            if (AbonnementService.getInstance().delete(abonnement.getId())) {
                MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_ABONNEMENT);
            } else {
                AlertUtils.makeError("Could not delete abonnement");
            }
        }
    }


    @FXML
    public void sort(ActionEvent actionEvent) {
        Constants.compareVar = sortCB.getValue();
        Collections.sort(listAbonnement);
        displayData();
    }
}
