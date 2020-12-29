package com.eavc.examen.model;

public class EmpleadoLocal {

    int ID_EMPLOYEE;
    String NAME;
    String MAIL;

    public EmpleadoLocal(int ID_EMPLOYEE, String NAME, String MAIL) {
        this.ID_EMPLOYEE = ID_EMPLOYEE;
        this.NAME = NAME;
        this.MAIL = MAIL;
    }

    public int getID_EMPLOYEE() {
        return ID_EMPLOYEE;
    }

    public void setID_EMPLOYEE(int ID_EMPLOYEE) {
        this.ID_EMPLOYEE = ID_EMPLOYEE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getMAIL() {
        return MAIL;
    }

    public void setMAIL(String MAIL) {
        this.MAIL = MAIL;
    }
}
