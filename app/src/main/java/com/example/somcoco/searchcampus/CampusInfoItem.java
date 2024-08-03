package com.example.somcoco.searchcampus;

public class CampusInfoItem {
    String cps_name;
    String cps_addr;
    String cps_webaddr;
    String cps_president;
    String cps_openingday;
    String cps_introduction;
    String cps_logo;
    String cps_fore;

    public CampusInfoItem(String cps_name, String cps_addr, String cps_webaddr, String cps_president, String cps_openingday, String cps_introduction, String cps_logo, String cps_fore) {
        this.cps_name = cps_name;
        this.cps_addr = cps_addr;
        this.cps_webaddr = cps_webaddr;
        this.cps_president = cps_president;
        this.cps_openingday = cps_openingday;
        this.cps_introduction = cps_introduction;
        this.cps_logo = cps_logo;
        this.cps_fore = cps_fore;
    }

    public String getCps_name() {
        return cps_name;
    }

    public void setCps_name(String cps_name) {
        this.cps_name = cps_name;
    }

    public String getCps_addr() {
        return cps_addr;
    }

    public void setCps_addr(String cps_addr) {
        this.cps_addr = cps_addr;
    }

    public String getCps_webaddr() {
        return cps_webaddr;
    }

    public void setCps_webaddr(String cps_webaddr) {
        this.cps_webaddr = cps_webaddr;
    }

    public String getCps_president() {
        return cps_president;
    }

    public void setCps_president(String cps_president) {
        this.cps_president = cps_president;
    }

    public String getCps_openingday() {
        return cps_openingday;
    }

    public void setCps_openingday(String cps_openingday) {
        this.cps_openingday = cps_openingday;
    }

    public String getCps_introduction() {
        return cps_introduction;
    }

    public void setCps_introduction(String cps_introduction) {
        this.cps_introduction = cps_introduction;
    }

    public String getCps_logo() {
        return cps_logo;
    }

    public void setCps_logo(String cps_logo) {
        this.cps_logo = cps_logo;
    }

    public String getCps_fore() {
        return cps_fore;
    }

    public void setCps_fore(String cps_fore) {
        this.cps_fore = cps_fore;
    }
}
