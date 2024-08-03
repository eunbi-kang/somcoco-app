package com.example.somcoco.searchcampus;


import java.util.ArrayList;

public class CampusCarouselArray {
    ArrayList<CampusCarouselItem> carouselList = new ArrayList<CampusCarouselItem>();

    public CampusCarouselItem getItem(int position) {
        return carouselList.get(position);
    }
}
