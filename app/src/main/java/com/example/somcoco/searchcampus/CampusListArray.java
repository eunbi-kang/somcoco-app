package com.example.somcoco.searchcampus;

import java.util.ArrayList;

public class CampusListArray {
    ArrayList<CampusListItem> campusList = new ArrayList<CampusListItem>();

    public CampusListItem getItem(int position) {
        return campusList.get(position);
    }
}
