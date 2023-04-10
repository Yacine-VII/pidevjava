package com.pdev.services;

import com.pdev.entities.Reservation;
import com.pdev.utils.DatabaseConnection;
import com.pdev.utils.RelationObject;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private static ReservationService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public ReservationService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public List<Reservation> getAll() {
        List<Reservation> listReservation = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM " +
                            "`reservation` AS x " +
                            "RIGHT JOIN `abonnement` AS y ON x.abonnement_id = y.id " +
                            "RIGHT JOIN `joueur` AS j ON x.joueur_id = j.id " +
                            "RIGHT JOIN `entraineur` AS e ON x.entraineur_id = e.ld " +
                            "WHERE x.abonnement_id = y.id " +
                            "AND x.joueur_id = j.id " +
                            "AND x.entraineur_id = e.ld");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listReservation.add(new Reservation(
                        resultSet.getInt("id"),
                        new RelationObject(resultSet.getInt("abonnement_id"), resultSet.getString("y.titre")),
                        new RelationObject(resultSet.getInt("joueur_id"), resultSet.getString("j.email")),
                        new RelationObject(resultSet.getInt("entraineur_id"), resultSet.getString("e.ld")),
                        resultSet.getString("sujet"),
                        LocalDate.parse(String.valueOf(resultSet.getDate("dat"))),
                        resultSet.getString("heure")

                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) reservation : " + exception.getMessage());
        }
        return listReservation;
    }

    public List<RelationObject> getAllAbonnements() {
        List<RelationObject> listAbonnements = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `abonnement`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listAbonnements.add(new RelationObject(resultSet.getInt("id"), resultSet.getString("titre")));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) abonnements : " + exception.getMessage());
        }
        return listAbonnements;
    }

    public List<RelationObject> getAllJoueurs() {
        List<RelationObject> listJoueurs = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `joueur`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listJoueurs.add(new RelationObject(resultSet.getInt("id"), resultSet.getString("email")));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) joueurs : " + exception.getMessage());
        }
        return listJoueurs;
    }

    public List<RelationObject> getAllEntraineurs() {
        List<RelationObject> listAbonnements = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `entraineur`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listAbonnements.add(new RelationObject(resultSet.getInt("ld"), resultSet.getString("ld")));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) entraineurs : " + exception.getMessage());
        }
        return listAbonnements;
    }

    public boolean add(Reservation reservation) {
        String request = "INSERT INTO `reservation`(`abonnement_id`, `joueur_id`, `entraineur_id`, `sujet`, `dat`, `heure`) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, reservation.getAbonnement().getId());
            preparedStatement.setInt(2, reservation.getJoueur().getId());
            preparedStatement.setInt(3, reservation.getEntraineur().getId());
            preparedStatement.setString(4, reservation.getSujet());
            preparedStatement.setDate(5, Date.valueOf(reservation.getDate()));
            preparedStatement.setString(6, reservation.getHeure());

            preparedStatement.executeUpdate();
            System.out.println("Reservation added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) reservation : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Reservation reservation) {

        String request = "UPDATE `reservation` SET `abonnement_id` = ?, `joueur_id` = ?, `entraineur_id` = ?, `sujet` = ?, `dat` = ?, `heure` = ? WHERE `id`=" + reservation.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setInt(1, reservation.getAbonnement().getId());
            preparedStatement.setInt(2, reservation.getJoueur().getId());
            preparedStatement.setInt(3, reservation.getEntraineur().getId());
            preparedStatement.setString(4, reservation.getSujet());
            preparedStatement.setDate(5, Date.valueOf(reservation.getDate()));
            preparedStatement.setString(6, reservation.getHeure());

            preparedStatement.executeUpdate();
            System.out.println("Reservation edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) reservation : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `reservation` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Reservation deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) reservation : " + exception.getMessage());
        }
        return false;
    }
}
