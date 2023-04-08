/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connexion;

import entite.Personne;
import service.PersonneService;
import util.DataSource;

/**
 *
 * @author wiemhjiri
 */
public class Connexion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        DataSource ds1 = DataSource.getInstance();
//        System.out.println(ds1);
//        DataSource ds2 = DataSource.getInstance();
//        System.out.println(ds2);
//
//        DataSource ds3 = DataSource.getInstance();
//        System.out.println(ds3);

        Personne p1=new Personne("test", "3a44", 20);
        
        PersonneService ps=new PersonneService();
       // ps.insert(p1);
      // ps.insertPst(p1);
      ps.readAll().forEach(System.out::println);

    }

}
