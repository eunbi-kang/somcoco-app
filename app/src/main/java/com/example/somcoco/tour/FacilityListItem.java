package com.example.somcoco.tour;

public class FacilityListItem {
    int facilityNum;
    String facilityName;

    public FacilityListItem(int facilityNum, String facilityName) {
        this.facilityNum = facilityNum;
        this.facilityName = facilityName;
    }

    public int getFacilityNum() {
        return facilityNum;
    }

    public void setFacilityNum(int facilityNum) {
        this.facilityNum = facilityNum;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
}
