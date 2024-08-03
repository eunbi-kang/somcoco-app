package com.example.somcoco.tour;

import java.util.ArrayList;

public class FacilityListArray {
    ArrayList<FacilityListItem> facilityList = new ArrayList<FacilityListItem>();

    public FacilityListItem getItem(int position){
        return facilityList.get(position);
    }
}
