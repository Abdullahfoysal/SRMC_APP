package com.nishant.mathsample;



public class userRankStatics {

    private String NAME;
    private String INSTITUTION;
    private String SOLVED;
    private String STATUS;
    private String USERNAME;
    private String SOLVING_STRING;

    public String getSOLVING_STRING() {
        return SOLVING_STRING;
    }

    public void setSOLVING_STRING(String SOLVING_STRING) {
        this.SOLVING_STRING = SOLVING_STRING;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setINSTITUTION(String INSTITUTION) {
        this.INSTITUTION = INSTITUTION;
    }

    public void setSOLVED(String SOLVED) {
        this.SOLVED = SOLVED;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }


    public String getNAME() {
        return NAME;
    }

    public String getINSTITUTION() {
        return INSTITUTION;
    }

    public String getSOLVED() {
        return SOLVED;
    }

    public String getSTATUS() {
        return STATUS;
    }
}
