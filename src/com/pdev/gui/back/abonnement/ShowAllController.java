package com.pdev.gui.back.abonnement;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

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
            ((Button) innerContainer.lookup("#generatePdfButton")).setOnAction((event) -> genererPDF(abonnement));


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

    private void genererPDF(Abonnement abonnement) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("abonnement.pdf")));
            document.open();

            com.itextpdf.text.Font font = new com.itextpdf.text.Font();
            font.setSize(20);

            document.add(new Paragraph("- Abonnement -"));
            document.add(new Paragraph("Type : " + abonnement.getType()));
            document.add(new Paragraph("Titre : " + abonnement.getTitre()));
            document.add(new Paragraph("Prix : " + abonnement.getPrix()));
            document.add(new Paragraph("Duree : " + abonnement.getDuree()));
            document.add(new Paragraph("NiveauAccess : " + abonnement.getNiveauAccess()));
            document.add(new Paragraph("ReservationsTotal : " + abonnement.getReservationsTotal()));
            document.add(new Paragraph("ReservationsRestantes : " + abonnement.getReservationsRestantes()));


            document.newPage();
            document.close();

            writer.close();

            Desktop.getDesktop().open(new File("abonnement.pdf"));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }


    public void genererExcel(ActionEvent actionEvent) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            FileChooser chooser = new FileChooser();
            // Set extension filter
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel Files(.xls)", ".xls");
            chooser.getExtensionFilters().add(filter);

            HSSFSheet workSheet = workbook.createSheet("sheet 0");
            workSheet.setColumnWidth(1, 25);

            HSSFFont fontBold = workbook.createFont();
            fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFCellStyle styleBold = workbook.createCellStyle();
            styleBold.setFont(fontBold);

            Row row1 = workSheet.createRow((short) 0);
            row1.createCell(0).setCellValue("Id");
            row1.createCell(1).setCellValue("Type");
            row1.createCell(2).setCellValue("Titre");
            row1.createCell(3).setCellValue("Prix");
            row1.createCell(4).setCellValue("Duree");
            row1.createCell(5).setCellValue("Niveau Access");
            row1.createCell(6).setCellValue("Reservations Total");
            row1.createCell(7).setCellValue("Reservations Restantes");

            int i = 0;
            for (Abonnement abonnement : listAbonnement) {
                i++;
                Row row2 = workSheet.createRow((short) i);
                row2.createCell(0).setCellValue(abonnement.getId());
                row2.createCell(1).setCellValue(abonnement.getType());
                row2.createCell(2).setCellValue(abonnement.getTitre());
                row2.createCell(3).setCellValue(abonnement.getPrix());
                row2.createCell(4).setCellValue(abonnement.getDuree());
                row2.createCell(5).setCellValue(abonnement.getNiveauAccess());
                row2.createCell(6).setCellValue(abonnement.getReservationsTotal());
                row2.createCell(7).setCellValue(abonnement.getReservationsRestantes());
            }

            workSheet.autoSizeColumn(0);
            workSheet.autoSizeColumn(1);
            workSheet.autoSizeColumn(2);
            workSheet.autoSizeColumn(3);
            workSheet.autoSizeColumn(4);
            workSheet.autoSizeColumn(5);
            workSheet.autoSizeColumn(6);
            workSheet.autoSizeColumn(7);

            workbook.write(Files.newOutputStream(Paths.get("reclamation.xls")));
            Desktop.getDesktop().open(new File("reclamation.xls"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
