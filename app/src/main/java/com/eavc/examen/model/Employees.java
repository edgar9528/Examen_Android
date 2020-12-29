package com.eavc.examen.model;

public class Employees {
    public int id;
    public String name;
    public Ubicacion location;
    public String mail;

    public Employees() {
    }

    public Employees(int id, String name, Ubicacion location, String mail) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.mail = mail;
    }
}
