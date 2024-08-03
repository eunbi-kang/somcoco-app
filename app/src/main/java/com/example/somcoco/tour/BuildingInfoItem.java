package com.example.somcoco.tour;

public class BuildingInfoItem {
    String buildingName;
    String buildingImage;
    String buildingAudio;
    String buildingIntroKr;
    String buildingIntroEn;

    public BuildingInfoItem(String buildingName, String buildingImage, String buildingAudio, String buildingIntroKr, String buildingIntroEn) {
        this.buildingName = buildingName;
        this.buildingImage = buildingImage;
        this.buildingAudio = buildingAudio;
        this.buildingIntroKr = buildingIntroKr;
        this.buildingIntroEn = buildingIntroEn;
    }

    public String getBuildingImage() {
        return buildingImage;
    }

    public void setBuildingImage(String buildingImage) {
        this.buildingImage = buildingImage;
    }

    public String getBuildingAudio() {
        return buildingAudio;
    }

    public void setBuildingAudio(String buildingAudio) {
        this.buildingAudio = buildingAudio;
    }

    public String getBuildingIntroKr() {
        return buildingIntroKr;
    }

    public void setBuildingIntroKr(String buildingIntroKr) {
        this.buildingIntroKr = buildingIntroKr;
    }

    public String getBuildingIntroEn() {
        return buildingIntroEn;
    }

    public void setBuildingIntroEn(String buildingIntroEn) {
        this.buildingIntroEn = buildingIntroEn;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}
