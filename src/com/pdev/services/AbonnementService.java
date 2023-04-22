package com.pdev.services;

import com.pdev.entities.Abonnement;
import com.pdev.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbonnementService {

    private static AbonnementService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public AbonnementService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static AbonnementService getInstance() {
        if (instance == null) {
            instance = new AbonnementService();
        }
        return instance;
    }

    public List<Abonnement> getAll() {
        List<Abonnement> listAbonnement = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `abonnement`");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listAbonnement.add(new Abonnement(
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getString("titre"),
                        resultSet.getFloat("prix"),
                        resultSet.getInt("duree"),
                        resultSet.getString("niveau_access"),
                        resultSet.getInt("reservations_total"),
                        resultSet.getInt("reservations_restantes")

                ));
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) abonnement : " + exception.getMessage());
        }
        return listAbonnement;
    }

    public boolean add(Abonnement abonnement) {

        String request = "INSERT INTO `abonnement`(`type`, `titre`, `prix`, `duree`, `niveau_access`, `reservations_total`, `reservations_restantes`) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, abonnement.getType());
            preparedStatement.setString(2, abonnement.getTitre());
            preparedStatement.setFloat(3, abonnement.getPrix());
            preparedStatement.setInt(4, abonnement.getDuree());
            preparedStatement.setString(5, abonnement.getNiveauAccess());
            preparedStatement.setInt(6, abonnement.getReservationsTotal());
            preparedStatement.setInt(7, abonnement.getReservationsRestantes());

            preparedStatement.executeUpdate();
            System.out.println("Abonnement added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) abonnement : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Abonnement abonnement) {

        String request = "UPDATE `abonnement` SET `type` = ?, `titre` = ?, `prix` = ?, `duree` = ?, `niveau_access` = ?, `reservations_total` = ?, `reservations_restantes` = ? WHERE `id`=" + abonnement.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, abonnement.getType());
            preparedStatement.setString(2, abonnement.getTitre());
            preparedStatement.setFloat(3, abonnement.getPrix());
            preparedStatement.setInt(4, abonnement.getDuree());
            preparedStatement.setString(5, abonnement.getNiveauAccess());
            preparedStatement.setInt(6, abonnement.getReservationsTotal());
            preparedStatement.setInt(7, abonnement.getReservationsRestantes());

            preparedStatement.executeUpdate();
            System.out.println("Abonnement edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) abonnement : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `abonnement` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Abonnement deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) abonnement : " + exception.getMessage());
        }
        return false;
    }
}
