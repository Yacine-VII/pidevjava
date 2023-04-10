package com.pdev.entities;

import com.pdev.utils.Constants;

public class Abonnement implements Comparable<Abonnement> {

    private int id;
    private String type;
    private String titre;
    private float prix;
    private int duree;
    private String niveauAccess;
    private int reservationsTotal;
    private int reservationsRestantes;

    public Abonnement(int id, String type, String titre, float prix, int duree, String niveauAccess, int reservationsTotal, int reservationsRestantes) {
        this.id = id;
        this.type = type;
        this.titre = titre;
        this.prix = prix;
        this.duree = duree;
        this.niveauAccess = niveauAccess;
        this.reservationsTotal = reservationsTotal;
        this.reservationsRestantes = reservationsRestantes;
    }

    public Abonnement(String type, String titre, float prix, int duree, String niveauAccess, int reservationsTotal, int reservationsRestantes) {
        this.type = type;
        this.titre = titre;
        this.prix = prix;
        this.duree = duree;
        this.niveauAccess = niveauAccess;
        this.reservationsTotal = reservationsTotal;
        this.reservationsRestantes = reservationsRestantes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getNiveauAccess() {
        return niveauAccess;
    }

    public void setNiveauAccess(String niveauAccess) {
        this.niveauAccess = niveauAccess;
    }

    public int getReservationsTotal() {
        return reservationsTotal;
    }

    public void setReservationsTotal(int reservationsTotal) {
        this.reservationsTotal = reservationsTotal;
    }

    public int getReservationsRestantes() {
        return reservationsRestantes;
    }

    public void setReservationsRestantes(int reservationsRestantes) {
        this.reservationsRestantes = reservationsRestantes;
    }


    @Override
    public int compareTo(Abonnement abonnement) {
        switch (Constants.compareVar) {
            case "Type":
                return abonnement.getType().compareTo(this.getType());
            case "Titre":
                return abonnement.getTitre().compareTo(this.getTitre());
            case "Prix":
                return Float.compare(abonnement.getPrix(), this.getPrix());
            case "Duree":
                return Integer.compare(abonnement.getDuree(), this.getDuree());
            case "NiveauAccess":
                return abonnement.getNiveauAccess().compareTo(this.getNiveauAccess());
            case "ReservationsTotal":
                return Integer.compare(abonnement.getReservationsTotal(), this.getReservationsTotal());
            case "ReservationsRestantes":
                return Integer.compare(abonnement.getReservationsRestantes(), this.getReservationsRestantes());

            default:
                return 0;
        }
    }

}