package com.example.somcoco.searchcampus;

public class CampusListItem {
    int cps_code;
    String cps_name;
    String cps_logo;

    public CampusListItem(int cps_code, String cps_name, String cps_logo) {
        this.cps_code = cps_code;
        this.cps_name = cps_name;
        this.cps_logo = cps_logo;
    }

    public int getCps_code() {
        return cps_code;
    }

    public void setCps_code(int cps_code) {
        this.cps_code = cps_code;
    }

    public String getCps_name() {
        return cps_name;
    }

    public void setCps_name(String cps_name) {
        this.cps_name = cps_name;
    }

    public String getCps_logo() {
        return cps_logo;
    }

    public void setCps_logo(String cps_logo) {
        this.cps_logo = cps_logo;
    }
}
