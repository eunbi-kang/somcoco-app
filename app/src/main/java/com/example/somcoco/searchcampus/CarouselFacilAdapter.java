package com.example.somcoco.searchcampus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarouselFacilAdapter {

    Context context;
    ArrayList<CarouselFacilItem> items = new ArrayList<CarouselFacilItem>();

    public CarouselFacilAdapter(Context context) {
        this.context = context;
    }
}
