package com.example.somcoco.maplocation;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.somcoco.R;
import com.google.android.material.snackbar.Snackbar;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

public class CampusLocationActivity extends AppCompatActivity
        implements TMapView.OnCalloutRightButtonClickCallback,
        TMapGpsManager.onLocationChangedCallback {

    LocationManager locationManager;
    RelativeLayout layout;
    RelativeLayout relativeLayout;
    SpeechRecognizer recognizer;
    EditText searchBox;
    ImageView mic;
    ImageView submit;
    ImageView myLoc;
    ImageView exploreLoc;
    ImageView backArrow;
    Toolbar toolbar;
    TMapView tMapView;
    TMapGpsManager gpsManager;
    TMapCircle tMapCircle;
    Intent inIntent;
    Dialog dialog;
    double logi;
    double lati;
    String curKeyword;
    //final int PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campuslocation);

        inIntent = getIntent();

        relativeLayout = (RelativeLayout) findViewById(R.id.location_parent);
        layout = (RelativeLayout) findViewById(R.id.loc_map);
        searchBox = (EditText) findViewById(R.id.loc_search_keyword);
        submit = (ImageView) findViewById(R.id.loc_search_btn);
        mic = (ImageView) findViewById(R.id.loc_search_mic);
        myLoc = (ImageView) findViewById(R.id.loc_myloc);
        exploreLoc = (ImageView) findViewById(R.id.loc_explore);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checkLocationService();

        // 마커 이미지 및 크기 설정
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.my_marker);
        bitmap = bitmap.createScaledBitmap(bitmap, 55, 55, true);

        // 길 찾기 다이얼로그 선언
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_map);

        // 툴바 설정
        toolbar = (Toolbar) findViewById(R.id.loc_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#FFC230"));
        getSupportActionBar().setTitle("캠퍼스 찾기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Tmap API 사용 설정
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx1329ea6bfcbf4eccb8beb6ec6c3c74e5");
        tMapView.setIcon(bitmap);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setSightVisible(true);
        tMapView.setCenterPoint(126.94660656887974,37.21241601317397);
        tMapView.setLocationPoint(126.94660656887974,37.21241601317397);

        gpsManager = new TMapGpsManager(this);
        gpsManager.setMinTime(5);
        gpsManager.setMinDistance(100);
        gpsManager.setProvider(gpsManager.NETWORK_PROVIDER);
        gpsManager.OpenGps();

        layout.addView(tMapView);

        // 음성인식기 설정
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplication().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");       // 사용 언어 설정

        ArrayList<String> arrBuilding = new ArrayList<>();      // 검색 키워드를 담는 변수

        // 캠퍼스 정보에서 Tmap 지도의 전체화면 아이콘을 클릭 했을 시
        if (inIntent.getBooleanExtra("check", false)) {
            arrBuilding.clear();
            curKeyword = inIntent.getStringExtra("title");        // 캠퍼스 이름 받아오기
            arrBuilding.add(curKeyword);                                // 받아온 캠퍼스 이름 변수에 담기
            searchBox.setText(curKeyword);                              // 검색창 텍스트값 캠퍼스이름으로 설정
            searchPOI(arrBuilding);                                     // 캠퍼스 이름을 매개변수로 전달하여 캠퍼스 찾기 메소드 실행
        }

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {       // 검색창 입력시 키보드 엔터버튼을 검색 버튼으로 바꾸는 메소드
                switch(actionId){
                    case IME_ACTION_SEARCH:
                        if (searchBox.getText().toString().equals("")){         // 검색어가 비어있는 경우 토스트 메시지 출력
                            Toast.makeText(getApplicationContext(),"검색어를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        } else {
                            arrBuilding.clear();
                            curKeyword = searchBox.getText().toString();
                            arrBuilding.add(curKeyword);
                            searchPOI(arrBuilding);                             // 검색키워드를 매개변수로 전달하여 캠퍼스 찾기 메소드 실행
                        }
                        break;

                    default:
                        break;
                }
                return true;
            }
        });


        // 검색 아이콘 클릭시 실행되는 메소드
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchBox.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"검색어를 입력해주세요.",Toast.LENGTH_SHORT).show();
                } else {
                    arrBuilding.clear();
                    curKeyword = searchBox.getText().toString();
                    arrBuilding.add(curKeyword);
                    searchPOI(arrBuilding);
                }
            }
        });

        // 마이크 아이콘 클릭시 실행되는 메소드
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());      // STT 객체 생성
                recognizer.setRecognitionListener(listener);                                        // 객체에 리스너 설정
                recognizer.startListening(intent);                                                  // 음성인식 시작
            }
        });

        //
        myLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 위치 기능이 비활성화 되어 있는 경우 하단에 활성화 요청 안내바 띄우기
                if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Snackbar snackbar = Snackbar.make(relativeLayout,"원활한 서비스 이용을 위해 위치서비스를 활성화 해주세요.",5000)    // 안내문 내용 및 보여줄 시간 설정
                            .setAction("확인", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));    // 확인 버튼 클릭 시 안드로이드 위치 설정 창으로 이동
                                }
                            });
                    View snackbarView = snackbar.getView();
                    TextView snackbarText = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbar.setActionTextColor(Color.GREEN);       // 하단바 안내 내용 텍스트 색 설정
                    snackbar.show();
                }else{
                    tMapView.setCenterPoint(gpsManager.getLocation().getLongitude(), gpsManager.getLocation().getLatitude());       // 지도의 중심점을 현재 위치로 설정
                    tMapView.setLocationPoint(gpsManager.getLocation().getLongitude(), gpsManager.getLocation().getLatitude());     // 지도에 표시될 위치를 현재 위치로 설정
                }

            }
        });

        exploreLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curKeyword == null){
                    Toast.makeText(getApplicationContext(),"검색된 장소가 없습니다",Toast.LENGTH_SHORT).show();       // 검색이 선행되지 않은 경우 토스트 메시지 출력
                }else{
                    arrBuilding.clear();
                    arrBuilding.add(curKeyword);
                    searchPOI(arrBuilding);                 // 검색키워드를 매개변수로 전달하여 캠퍼스 찾기 메소드 실행
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // 캠퍼스 찾기 메소드
    private void searchPOI(ArrayList<String> arrPOI){
        final TMapData tMapData = new TMapData();
        final ArrayList<TMapPoint> arrTmapPoint = new ArrayList<>();    // 찾은 캠퍼스 및 내부 건물의 경위도 좌표값을 담는 변수
        final ArrayList<String> arrTitle = new ArrayList<>();           // 찾은 캠퍼스 및 내부 건물의 이름을 담는 변수
        final ArrayList<String> arrAddress = new ArrayList<>();         // 찾은 캠퍼스 및 내부 건물의 주소를 담는 변수

        for (int i = 0; i < arrPOI.size(); i++) {
            tMapData.findTitlePOI(arrPOI.get(i), new TMapData.FindTitlePOIListenerCallback() {
                @Override
                public void onFindTitlePOI(ArrayList<TMapPOIItem> arrayList) {      // 검색어가 포함된 장소를 찾는 Tmap API 메소드
                    for (int j = 0; j < arrayList.size(); j++){
                        TMapPOIItem tMapPOIItem = arrayList.get(j);
                        arrTmapPoint.add(tMapPOIItem.getPOIPoint());
                        arrTitle.add(tMapPOIItem.getPOIName());
                        arrAddress.add(tMapPOIItem.upperAddrName + " " +
                                tMapPOIItem.middleAddrName + " " + tMapPOIItem.lowerAddrName);
                    }
                    tMapView.setCenterPoint(arrTmapPoint.get(0).getLongitude(),arrTmapPoint.get(0).getLatitude());  // 가장 먼저 검색된 장소를 지도의 중심점으로 설정
                    setMultiMarkers(arrTmapPoint, arrTitle, arrAddress);
                }
            });
        }
    }

    // 검색된 장소에 마커를 찍는 메소드
    private void setMultiMarkers(ArrayList<TMapPoint> arrTPoint, ArrayList<String> arrTitle, ArrayList<String> arrAddress){
        for (int i = 0; i < arrTPoint.size(); i++){
            Bitmap bitmap = createMarkerIcon(R.drawable.outline_location_on_black_24dp);        // 마커 이미지 불러오기

            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap);                                                     // 마커 이미지 설정

            tMapMarkerItem.setTMapPoint(arrTPoint.get(i));

            tMapView.addMarkerItem("markerItem" + i, tMapMarkerItem);

            setBalloonView(tMapMarkerItem, arrTitle.get(i), arrAddress.get(i));
        }
    }

    // 마커 클릭 시 뜨는 벌룬뷰 설정
    private void setBalloonView(TMapMarkerItem marker, String title, String address){
        marker.setCanShowCallout(true);

        if(marker.getCanShowCallout()){
            marker.setCalloutTitle(title);              // 벌룬뷰 제목을 장소이름으로 설정
            marker.setCalloutSubTitle(address);         // 벌룬뷰 소제목을 장소주소로 설정

            Bitmap bitmap = createMarkerIcon(R.drawable.outline_expand_more_black_24dp);
            marker.setCalloutRightButtonImage(bitmap);
        }
    }

    // 마커 이미지를 불러오고 설정하는 메소드
    private Bitmap createMarkerIcon(int image) {
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),image);     // 이미지 리소스 불러오기
        bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, false);              // 이미지 크기 등 설정

        return bitmap;
    }

    @Override
    public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
        logi = tMapMarkerItem.getTMapPoint().getLongitude();                        // 길 찾기 아이콘 클릭시 해당 장소의 경도값 변수에 담기
        lati = tMapMarkerItem.getTMapPoint().getLatitude();                         // 길 찾기 아이콘 클릭시 해당 장소의 위도값 변수에 담기

        showDialog();                                   // 길 찾기 다이얼로그 띄우기
    }

    // 길 찾기 다이얼로그 메소드
    public void showDialog(){
        dialog.show();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button noBtn = dialog.findViewById(R.id.noBtn);
        Button yesBtn = dialog.findViewById(R.id.yesBtn);

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CampusDirectionActivity.class);
                intent.putExtra("logi",logi);       // 경도값 인텐트 전달값으로 담기
                intent.putExtra("lati",lati);       // 위도값 인텐트 전달값으로 담기
                startActivity(intent);      // 길 찾기 액티비티로 이동

                dialog.dismiss();
            }
        });
    }

    // 음성 인식 리스너 메소드
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String message;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);   // 음성 인식 결과값을 텍스트형태로 변수에 담기

            for (int i = 0; i < matches.size(); i++) {
                searchBox.setText(matches.get(i));          // 음성 인식 결과 텍스트를 검색창 텍스트에 설정
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    @Override
    public void onLocationChange(Location location) {
        //tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        //tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationService();
    }

    // 위치 기능 활성화 여부를 체크하는 메소드
    public void checkLocationService() {
        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Snackbar snackbar = Snackbar.make(relativeLayout,"원활한 서비스 이용을 위해 위치서비스를 활성화 해주세요.",5000)
                    .setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
            View snackbarView = snackbar.getView();
            TextView snackbarText = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
    }
}
