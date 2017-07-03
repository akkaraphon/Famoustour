package com.cpc.famoustour.model;

/**
 * Created by macbook on 5/3/17.
 */

public class GPS {
    /**
     * ID_GPS : 6
     * TIMESTAMP_GPS : 2017-05-03 08:36:52
     * LAT_GPS : 13.871202
     * LONG_GPS : 100.53278478
     * STATUS_GPS : 1
     * ID_USER : 20
     * ID_PGTOUR : 7
     */

    private String TIMESTAMP_GPS;
    private double LAT_GPS;
    private double LONG_GPS;
    private int STATUS_GPS;
    private String ID_USER;
    private String ID_PGTOUR;
    private String TOKEN_USER;
    private String NAME;
    private String TEL_USER;
    private String ID_GPS;
    private String TYPE_USER;

    public double getLAT_GPS() {
        return LAT_GPS;
    }

    public void setLAT_GPS(double LAT_GPS) {
        this.LAT_GPS = LAT_GPS;
    }

    public double getLONG_GPS() {
        return LONG_GPS;
    }

    public void setLONG_GPS(double LONG_GPS) { this.LONG_GPS = LONG_GPS; }

    public int getSTATUS_GPS() {
        return STATUS_GPS;
    }

    public void setSTATUS_GPS(int STATUS_GPS) { this.STATUS_GPS = STATUS_GPS; }

    public String getID_USER() { return ID_USER; }

    public void setID_USER(String ID_USER) {
        this.ID_USER = ID_USER;
    }

    public String getTOKEN_USER() { return TOKEN_USER; }

    public void setTOKEN_USER(String TOKEN_USER) {
        this.TOKEN_USER = TOKEN_USER;
    }

    public String getNAME() { return NAME; }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getTEL_USER() { return TEL_USER; }

    public void setTEL_USER(String TEL_USER) {
        this.TEL_USER = TEL_USER;
    }

    public String getID_GPS() { return ID_GPS; }

    public void setID_GPS(String ID_GPS) { this.ID_GPS = ID_GPS; }

    public String getTYPE_USER() { return TYPE_USER; }

    public void setTYPE_USER(String TYPE_USER) { this.TYPE_USER = TYPE_USER; }
}
