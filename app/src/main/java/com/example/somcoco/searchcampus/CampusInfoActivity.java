package com.example.somcoco.searchcampus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.somcoco.AppHelper;
import com.example.somcoco.R;
import com.example.somcoco.maplocation.CampusLocationActivity;
import com.example.somcoco.tour.NFCActivity;
import com.google.gson.Gson;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CampusInfoActivity extends AppCompatActivity {

    ArrayList<String> arrBuilding;
    TMapView tMapView;
    LinearLayout mapCampus;
    RecyclerView recyclerView;
    CampusCarouselAdapter adapter;
    ImageView fullscreen;
    ImageView tourStart;
    ImageView infoback;
    ImageView cpsLogo;
    ImageView cpsFore;
    String cpsCode;
    TextView cpsName;
    TextView cpsIntroduction;
    TextView cpsWebaddr;
    TextView cpsAddr;
    TextView cpsOpeningday;
    TextView cpsPresident;

    String website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campusinfo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.campus_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        infoback = (ImageView) findViewById(R.id.infoback);
        cpsLogo = (ImageView) findViewById(R.id.cps_info_logo);
        cpsFore = (ImageView) findViewById(R.id.cps_info_foreground);
        cpsName = (TextView) findViewById(R.id.cps_info_name);
        cpsIntroduction = (TextView) findViewById(R.id.cps_info_introduction);
        cpsWebaddr = (TextView) findViewById(R.id.cps_info_webaddr);
        cpsAddr = (TextView) findViewById(R.id.cps_info_addr);
        cpsOpeningday = (TextView) findViewById(R.id.cps_info_openingday);
        cpsPresident = (TextView) findViewById(R.id.cps_info_president);
        mapCampus = (LinearLayout) findViewById(R.id.map_campus);
        fullscreen = (ImageView) findViewById(R.id.fullscreen);
        tourStart = (ImageView) findViewById(R.id.tour_start);
        recyclerView = (RecyclerView) findViewById(R.id.campus_building_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CampusCarouselAdapter(getApplicationContext());

        infoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cpsWebaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(website);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Intent inIntent = getIntent();
        cpsCode = inIntent.getStringExtra("cpsCode");

        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        sendRequest();
        sendRequestCarousel();

        arrBuilding = new ArrayList<>();

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx1329ea6bfcbf4eccb8beb6ec6c3c74e5");
        tMapView.setZoomLevel(15);
        mapCampus.addView(tMapView);

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CampusLocationActivity.class);
                intent.putExtra("title",cpsName.getText().toString());
                intent.putExtra("check",true);
                startActivity(intent);
            }
        });

        tourStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NFCActivity.class);
                intent.putExtra("cps",cpsCode);
                startActivity(intent);
            }
        });
    }

    public void sendRequest() {
        String url = "https://somcoco.co.kr/application/campus_info.jsp";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("msg",cpsCode);

                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        CampusInfoItem campusInfoItem = gson.fromJson(response, CampusInfoItem.class);

        website = campusInfoItem.cps_webaddr;

        cpsName.setText(campusInfoItem.cps_name);
        cpsIntroduction.setText(campusInfoItem.cps_introduction);
        cpsWebaddr.setText(campusInfoItem.cps_webaddr);
        cpsAddr.setText(campusInfoItem.cps_addr);
        cpsOpeningday.setText(campusInfoItem.cps_openingday);
        cpsPresident.setText(campusInfoItem.cps_president);

        Glide.with(this)
                .load(campusInfoItem.getCps_logo())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .fallback(R.drawable.noimage)
                .into(cpsLogo);
        Glide.with(this)
                .load(campusInfoItem.getCps_fore())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .fallback(R.drawable.noimage)
                .centerCrop()
                .into(cpsFore);

        arrBuilding.clear();
        arrBuilding.add(campusInfoItem.getCps_name());
        searchPOI(arrBuilding);
    }

    private void searchPOI(ArrayList<String> arrPOI){
        final TMapData tMapData = new TMapData();
        final ArrayList<TMapPoint> arrTmapPoint = new ArrayList<>();
        final ArrayList<String> arrTitle = new ArrayList<>();
        final ArrayList<String> arrAddress = new ArrayList<>();

        for (int i = 0; i < arrPOI.size(); i++) {
            tMapData.findTitlePOI(arrPOI.get(i), new TMapData.FindTitlePOIListenerCallback() {
                @Override
                public void onFindTitlePOI(ArrayList<TMapPOIItem> arrayList) {
                    for (int j = 0; j < arrayList.size(); j++){
                        TMapPOIItem tMapPOIItem = arrayList.get(j);
                        arrTmapPoint.add(tMapPOIItem.getPOIPoint());
                        arrTitle.add(tMapPOIItem.getPOIName());
                        arrAddress.add(tMapPOIItem.upperAddrName + " " +
                                tMapPOIItem.middleAddrName + " " + tMapPOIItem.lowerAddrName);
                    }
                    tMapView.setCenterPoint(arrTmapPoint.get(0).getLongitude(),arrTmapPoint.get(0).getLatitude());
                    setMultiMarkers(arrTmapPoint, arrTitle, arrAddress);
                }
            });
        }
    }
    private void setMultiMarkers(ArrayList<TMapPoint> arrTPoint, ArrayList<String> arrTitle, ArrayList<String> arrAddress){
        for (int i = 0; i < arrTPoint.size(); i++){
            Bitmap bitmap = createMarkerIcon(R.drawable.outline_location_on_black_24dp);

            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap);

            tMapMarkerItem.setTMapPoint(arrTPoint.get(i));

            tMapView.addMarkerItem("markerItem" + i, tMapMarkerItem);

            setBalloonView(tMapMarkerItem, arrTitle.get(i), arrAddress.get(i));
        }
    }
    private void setBalloonView(TMapMarkerItem marker, String title, String address){
        marker.setCanShowCallout(true);

        if(marker.getCanShowCallout()){
            marker.setCalloutTitle(title);
            marker.setCalloutSubTitle(address);

            Bitmap bitmap = createMarkerIcon(R.drawable.outline_expand_more_black_24dp);
            marker.setCalloutRightButtonImage(bitmap);
        }
    }

    private Bitmap createMarkerIcon(int image) {
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),image);
        bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, false);

        return bitmap;
    }

    public void sendRequestCarousel() {
        String url = "https://somcoco.co.kr/application/campus_carousel.jsp";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponseCarousel(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("msg", cpsCode);

                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponseCarousel(String response) {
        Gson gson = new Gson();
        CampusCarouselArray campusCarouselArray = gson.fromJson(response, CampusCarouselArray.class);

        if (campusCarouselArray != null) {
            for (int i = 0; i < campusCarouselArray.carouselList.size(); i++) {
                adapter.addItem(new CampusCarouselItem(campusCarouselArray.getItem(i).getNfcCode(), campusCarouselArray.getItem(i).getBuildingIntro(), campusCarouselArray.getItem(i).getBuildingName(), campusCarouselArray.getItem(i).getBuildingImg2()));
            }
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }
}
