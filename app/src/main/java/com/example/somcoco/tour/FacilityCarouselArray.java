package com.example.somcoco.tour;

import java.util.ArrayList;

public class FacilityCarouselArray {
    ArrayList<FacilityCarouselItem> facilityCarousel = new ArrayList<FacilityCarouselItem>();

    public FacilityCarouselItem getItem(int position) {
        return facilityCarousel.get(position);
    }
}
