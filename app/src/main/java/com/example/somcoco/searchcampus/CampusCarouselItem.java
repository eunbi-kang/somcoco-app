package com.example.somcoco.searchcampus;

public class CampusCarouselItem {
    String nfcCode;
    String buildingIntro;
    String buildingName;
    String buildingImg2;

    public CampusCarouselItem(String nfcCode, String buildingIntro, String buildingName, String buildingImg2) {
        this.nfcCode = nfcCode;
        this.buildingIntro = buildingIntro;
        this.buildingName = buildingName;
        this.buildingImg2 = buildingImg2;
    }

    public String getNfcCode() {
        return nfcCode;
    }

    public void setNfcCode(String nfcCode) {
        this.nfcCode = nfcCode;
    }

    public String getBuildingIntro() {
        return buildingIntro;
    }

    public void setBuildingIntro(String buildingIntro) {
        this.buildingIntro = buildingIntro;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingImg2() {
        return buildingImg2;
    }

    public void setBuildingImg2(String buildingImg2) {
        this.buildingImg2 = buildingImg2;
    }
}
