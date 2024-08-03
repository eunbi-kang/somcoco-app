package com.example.somcoco.maplocation;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.somcoco.R;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;

public class CampusDirectionActivity extends AppCompatActivity
        implements TMapView.OnLongClickListenerCallback,
        LocationListener, TMapGpsManager.onLocationChangedCallback {

    TMapView tMapView;
    TMapGpsManager gpsManager;
    TMapData tMapData;
    TMapCircle tMapCircle;
    Intent inIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campusdirection);

        inIntent = getIntent();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.my_marker);
        bitmap = bitmap.createScaledBitmap(bitmap, 55, 55, true);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.direc_map);

        // Tmap API 설정 부분
        tMapView = new TMapView(this);
        tMapCircle = new TMapCircle();
        tMapView.setSKTMapApiKey("l7xx1329ea6bfcbf4eccb8beb6ec6c3c74e5");

        tMapView.setIcon(bitmap);
        tMapView.setIconVisibility(true);

        gpsManager = new TMapGpsManager(this);
        gpsManager.setMinTime(1);
        gpsManager.setMinDistance(1);
        gpsManager.setProvider(gpsManager.NETWORK_PROVIDER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
        }
        gpsManager.OpenGps();

        tMapData = new TMapData();

        tMapView.setZoomLevel(15);
        tMapView.setSightVisible(true);
        tMapView.setTrackingMode(true);
        tMapView.setCompassMode(true);
        layout.addView(tMapView);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapCircle.setCenterPoint(tMapView.getCenterPoint());
        tMapCircle.setRadius(30);
        tMapCircle.setAreaColor(Color.BLUE);
        tMapCircle.setLineColor(Color.BLUE);
        tMapCircle.setCircleWidth(5);
        tMapCircle.setAreaAlpha(50);
        tMapCircle.setRadiusVisible(true);

        TMapPoint point1 = tMapView.getCenterPoint();       // 현재 사용자의 위치 값
        TMapPoint point2 = new TMapPoint(inIntent.getDoubleExtra("lati",0), inIntent.getDoubleExtra("logi",0));     // 찾고자 하는 장소의 위치 값

        // Tmap API 보행자 경로 안내 메소드
        tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,
                point1, point2, new TMapData.FindPathDataListenerCallback(){
                    @Override
                    public void onFindPathData(TMapPolyLine tMapPolyLine) {
                        tMapPolyLine.setLineWidth(20);
                        tMapPolyLine.setOutLineColor(Color.LTGRAY);
                        tMapView.addTMapPath(tMapPolyLine);
                    }
                });
    }

    @Override
    public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {

    }
}