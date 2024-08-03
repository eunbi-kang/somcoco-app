package com.example.somcoco.searchcampus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.somcoco.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;

public class CampusMapActivity extends AppCompatActivity {

    Intent inIntent;
    LinearLayout mapView;
    TMapView tMapView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);

        inIntent = getIntent();

        mapView = (LinearLayout) findViewById(R.id.mapView);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx1329ea6bfcbf4eccb8beb6ec6c3c74e5");
        tMapView.setZoomLevel(15);
        mapView.addView(tMapView);

        ArrayList<String> arrBuilding = new ArrayList<>();
        arrBuilding.add(inIntent.getStringExtra("title"));
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
        bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, false);

        return bitmap;
    }
}
