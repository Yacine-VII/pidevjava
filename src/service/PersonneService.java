/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entite.Personne;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DataSource;

/**
 *
 * @author wiemhjiri
 */
public class PersonneService implements IService<Personne> {

    private Connection conn;

    public PersonneService() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void insert(Personne t) {
        String requete = "insert into personne (nom,prenom,age) values"
                + "('" + t.getNom() + "','" + t.getPrenom() + "'," + t.getAge() + ")";
        try {
            Statement ste = conn.createStatement();
            ste.executeUpdate(requete);
        } catch (SQLException ex) {
            Logger.getLogger(PersonneService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insertPst(Personne p) {
        String requete = "insert into personne(nom,prenom,age) values(?,?,?)";
        try {
           // Date dns=Date.valueOf(p.getDns());
            
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.setInt(3, p.getAge());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PersonneService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Personne t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Personne readById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Personne> readAll() {
        String requete = "select * from personne";
        ArrayList<Personne> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while(rs.next()){
                Personne p=new Personne(rs.getInt(1), rs.getString("nom")
                        , rs.getString(3), rs.getInt("age"));
                list.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PersonneService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
