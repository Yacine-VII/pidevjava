package com.pdev.gui.front.reservation;


import com.pdev.entities.Abonnement;
import com.pdev.entities.Reservation;
import com.pdev.gui.front.MainWindowController;
import com.pdev.services.AbonnementService;
import com.pdev.services.ReservationService;
import com.pdev.utils.AlertUtils;
import com.pdev.utils.Constants;
import com.pdev.utils.RelationObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public ComboBox<Abonnement> abonnementCB;
    @FXML
    public ComboBox<RelationObject> joueurCB;
    @FXML
    public ComboBox<RelationObject> entraineurCB;
    @FXML
    public TextField sujetTF;
    @FXML
    public DatePicker dateDP;
    @FXML
    public TextField heureTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Reservation currentReservation;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (Abonnement abonnement : AbonnementService.getInstance().getAll()) {
            abonnementCB.getItems().add(abonnement);
        }
        for (RelationObject joueur : ReservationService.getInstance().getAllJoueurs()) {
            joueurCB.getItems().add(joueur);
        }
        for (RelationObject entraineur : ReservationService.getInstance().getAllEntraineurs()) {
            entraineurCB.getItems().add(entraineur);
        }

        currentReservation = ShowAllController.currentReservation;

        if (currentReservation != null) {
            topText.setText("Modifier reservation");
            btnAjout.setText("Modifier");

            try {
                abonnementCB.setValue(currentReservation.getAbonnement());
                joueurCB.setValue(currentReservation.getJoueur());
                entraineurCB.setValue(currentReservation.getEntraineur());
                sujetTF.setText(currentReservation.getSujet());
                dateDP.setValue(currentReservation.getDate());
                heureTF.setText(currentReservation.getHeure());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter reservation");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent event) {

        if (controleDeSaisie()) {

            Reservation reservation = new Reservation(
                    abonnementCB.getValue(),
                    joueurCB.getValue(),
                    entraineurCB.getValue(),
                    sujetTF.getText(),
                    dateDP.getValue(),
                    heureTF.getText()
            );

            if (currentReservation == null) {
                Abonnement abonnement = reservation.getAbonnement();
                if (abonnement.getReservationsRestantes() < 1 ){
                    AlertUtils.makeError("Abonnement complet");
                } else {
                    if (ReservationService.getInstance().add(reservation)) {
                        abonnement.setReservationsRestantes(abonnement.getReservationsRestantes() - 1);
                        if (AbonnementService.getInstance().edit(abonnement)) {
                            try {
                                sendMail(reservation.getJoueur().getName());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            AlertUtils.makeSuccessNotification("Reservation ajouté avec succés");
                            MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RESERVATION);
                        }
                    } else {
                        AlertUtils.makeError("Erreur");
                    }
                }
            } else {
                reservation.setId(currentReservation.getId());
                if (ReservationService.getInstance().edit(reservation)) {
                    AlertUtils.makeSuccessNotification("Reservation modifié avec succés");
                    ShowAllController.currentReservation = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RESERVATION);
                } else {
                    AlertUtils.makeError("Erreur");
                }
            }

        }
    }

    public static void sendMail(String recepient) throws Exception {
        System.out.println("Preparing to send email");
        Properties properties = new Properties();

        //Enable authentication
        properties.put("mail.smtp.auth", "true");
        //Set TLS encryption enabled
        properties.put("mail.smtp.starttls.enable", "true");
        //Set SMTP host
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //Set smtp port
        properties.put("mail.smtp.port", "587");

        //Your gmail address
        String myAccountEmail = "pidev.app.esprit@gmail.com";
        //Your gmail password
        String password = "jkemsuddgeptmcsb";

        //Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        //Prepare email message
        Message message = prepareMessage(session, myAccountEmail, recepient);

        //Send mail
        if (message != null) {
            Transport.send(message);
            System.out.println("Mail sent successfully");
        } else {
            System.out.println("Error sending the mail");
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Notification");
            String htmlCode = "<h1>Notification</h1> <br/> <h2><b>Réservation ajouté</b></h2>";
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private boolean controleDeSaisie() {


        if (abonnementCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir abonnement");
            return false;
        }


        if (joueurCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir joueur");
            return false;
        }


        if (entraineurCB.getValue() == null) {
            AlertUtils.makeInformation("Choisir entraineur");
            return false;
        }


        if (sujetTF.getText().isEmpty()) {
            AlertUtils.makeInformation("sujet ne doit pas etre vide");
            return false;
        }


        if (dateDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour date");
            return false;
        }


        if (heureTF.getText().isEmpty()) {
            AlertUtils.makeInformation("heure ne doit pas etre vide");
            return false;
        }


        return true;
    }
}