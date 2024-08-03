package com.example.somcoco.tour;

public class FacilityInfoItem {
    String facilityName;
    String facilityInfoKr;
    String facilityInfoEn;
    String facilityTask;

    public FacilityInfoItem(String facilityName, String facilityInfoKr, String facilityInfoEn, String facilityTask) {
        this.facilityName = facilityName;
        this.facilityInfoKr = facilityInfoKr;
        this.facilityInfoEn = facilityInfoEn;
        this.facilityTask = facilityTask;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityInfoKr() {
        return facilityInfoKr;
    }

    public void setFacilityInfoKr(String facilityInfoKr) {
        this.facilityInfoKr = facilityInfoKr;
    }

    public String getFacilityInfoEn() {
        return facilityInfoEn;
    }

    public void setFacilityInfoEn(String facilityInfoEn) {
        this.facilityInfoEn = facilityInfoEn;
    }

    public String getFacilityTask() {
        return facilityTask;
    }

    public void setFacilityTask(String facilityTask) {
        this.facilityTask = facilityTask;
    }
}
