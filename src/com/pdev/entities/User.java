package com.pdev.entities;

public class User {

    private int id;
    private boolean admin;

    public User(int id, boolean admin) {
        this.id = id;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}