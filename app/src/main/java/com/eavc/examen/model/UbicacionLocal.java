package com.eavc.examen.model;

public class UbicacionLocal {
    int ID_LOCATION;
    String LAT;
    String LOG;

    public UbicacionLocal(int ID_LOCATION, String LAT, String LOG) {
        this.ID_LOCATION = ID_LOCATION;
        this.LAT = LAT;
        this.LOG = LOG;
    }

    public int getID_LOCATION() {
        return ID_LOCATION;
    }

    public void setID_LOCATION(int ID_LOCATION) {
        this.ID_LOCATION = ID_LOCATION;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLOG() {
        return LOG;
    }

    public void setLOG(String LOG) {
        this.LOG = LOG;
    }
}
