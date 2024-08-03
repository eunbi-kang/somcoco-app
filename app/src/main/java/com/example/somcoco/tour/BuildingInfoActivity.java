package com.example.somcoco.tour;

import android.content.Intent;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.somcoco.PlayState;
import com.example.somcoco.R;
import com.example.somcoco.TextPlayer;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BuildingInfoActivity extends AppCompatActivity
implements TextPlayer, View.OnClickListener {

    final Bundle params = new Bundle();
    final BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.YELLOW);
    PlayState playState = PlayState.STOP;
    Spannable spannable;
    int standbyIndex = 0;
    int lastPlayIndex = 0;
    TextToSpeech tts;

    String cps;
    String tagID;
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    NavigationView navigationView;
    RecyclerView recyclerView;
    RecyclerView facCarousel;
    Toolbar toolbar;
    DrawerLayout drawer;
    //FrameLayout content;
    ActionBarDrawerToggle toggle;
    MediaPlayer mp;
    FacilityListAdapter adapter;
    FacilityCarouselAdapter carouselAdapter;
    TextView buildName;
    ImageView buildImg;
    ImageView buildPlay;
    ImageView buildPause;
    ImageView buildStop;
    Switch buildLang;
    TextView buildContents;
    TextView buildTitle;
    String kor;
    String eng;
    RelativeLayout buildingInfo;
    NestedScrollView facilityInfo;
    Switch facilLang;
    TextView facilContents;
    TextView facilTask;
    String noticeUrl;
    MaterialButtonToggleGroup buildToggle;
    MaterialButtonToggleGroup facilToggle;
    Button buildKor;
    Button buildEng;
    Button facilKor;
    Button facilEng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildinginfo);

        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        initTTS();

        Intent inIntent = getIntent();
        tagID = inIntent.getStringExtra("tagID");
        cps = inIntent.getStringExtra("cps");

        buildToggle = (MaterialButtonToggleGroup) findViewById(R.id.build_toggle);
        buildToggle.check(R.id.build_lang_kor);
        facilToggle = (MaterialButtonToggleGroup) findViewById(R.id.facil_toggle);
        facilToggle.check(R.id.facil_lang_kor);

        buildKor = (Button) findViewById(R.id.build_lang_kor);
        buildEng = (Button) findViewById(R.id.build_lang_eng);
        facilKor = (Button) findViewById(R.id.facil_lang_kor);
        facilEng = (Button) findViewById(R.id.facil_lang_eng);


        buildingInfo = (RelativeLayout) findViewById(R.id.building_info);
        buildName = (TextView) findViewById(R.id.build_info_name);
        buildImg = (ImageView) findViewById(R.id.build_img);

        buildPlay = (ImageView) findViewById(R.id.build_play);
        buildPause = (ImageView) findViewById(R.id.build_pause);
        buildStop = (ImageView) findViewById(R.id.build_stop);

        buildPlay.setOnClickListener(this);
        buildPause.setOnClickListener(this);
        buildStop.setOnClickListener(this);

        buildContents = (TextView) findViewById(R.id.build_contents);
        buildTitle = (TextView) findViewById(R.id.build_title);

        facilityInfo = (NestedScrollView) findViewById(R.id.facility_info);
        //facilLang = (Switch) findViewById(R.id.facil_lang);
        facilContents = (TextView) findViewById(R.id.facil_contents);
        facilTask = (TextView) findViewById(R.id.facil_task);

        toolbar = (Toolbar) findViewById(R.id.build_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setTitle(null);

        //content = (FrameLayout) findViewById(R.id.build_container);
        drawer = (DrawerLayout) findViewById(R.id.build_drawer);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);

        navigationView = (NavigationView) findViewById(R.id.build_nav);

        facCarousel = (RecyclerView) findViewById(R.id.facil_carousel);
        LinearLayoutManager facManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        facCarousel.setLayoutManager(facManager);

        carouselAdapter = new FacilityCarouselAdapter(getApplicationContext());

        carouselAdapter.setOnItemClickListener(new FacilityCarouselAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FacilityCarouselAdapter.ViewHolder holder, View view, int position) {
                FacilityCarouselItem item = carouselAdapter.getItem(position);

                Uri uri = Uri.parse(item.getImageLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.facil_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FacilityListAdapter(getApplicationContext());

        adapter.setOnItemClickListener(new FacilityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FacilityListAdapter.ViewHolder holder, View view, int position) {
                FacilityListItem item = adapter.getItem(position);

//                if (mp != null) {
//                    mp.pause();
//                    mp.release();
//                    mp = null;
//                }

                sendRequestFacility(tagID, cps, String.valueOf(item.getFacilityNum()));
                carouselAdapter.items.clear();
                sendRequestFacCarousel(tagID, cps, String.valueOf(item.getFacilityNum()));
                buildingInfo.setVisibility(View.INVISIBLE);
                facilityInfo.setVisibility(View.VISIBLE);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


        buildKor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildContents.setText(kor);
                tts.setLanguage(Locale.KOREAN);
            }
        });

        buildEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildContents.setText(eng);
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        facilKor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facilContents.setText(kor);
            }
        });

        facilEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facilContents.setText(eng);
            }
        });

        buildTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    mp.pause();
                    mp.release();
                    mp = null;
                }
                sendRequestBuild(tagID, cps);
                buildingInfo.setVisibility(View.VISIBLE);
                facilityInfo.setVisibility(View.INVISIBLE);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        adapter.items.clear();
        sendRequestBuild(tagID, cps);
        sendRequestFacList(tagID, cps);

//        buildPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buildSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        if(fromUser)
//                            mp.seekTo(progress);
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//
//                    }
//                });
//                mp.start();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while(mp.isPlaying()){
//                            try {
//                                //Thread.sleep(1000);
//                                buildSeekbar.setProgress(mp.getCurrentPosition());
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();
//            }
//        });
//
//        buildPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mp.pause();
//            }
//        });
    }

    private void initTTS() {
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int state) {
                if (state == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.KOREAN);
                } else {
                    showState("TTS 객체 초기화 중 문제가 발생했습니다.");
                }
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                clearAll();
            }

            @Override
            public void onError(String s) {
                showState("재생 중 에러가 발생했습니다.");
            }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                changeHighlight(standbyIndex + start, standbyIndex + end);
                lastPlayIndex = start;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.build_play:
                startPlay();
                break;

            case R.id.build_pause:
                pausePlay();
                break;

            case R.id.build_stop:
                stopPlay();
                break;
        }
        //showState(playState.getState());
    }

    @Override
    public void startPlay() {
        String content = buildContents.getText().toString();
        if (playState.isStopping() && !tts.isSpeaking()) {
            setContentFromEditText(content);
            startSpeak(content);
        } else if (playState.isWaiting()) {
            standbyIndex += lastPlayIndex;
            startSpeak(content.substring(standbyIndex));
        }
        playState = PlayState.PLAY;
    }

    @Override
    public void pausePlay() {
        if (playState.isPlaying()) {
            playState = PlayState.WAIT;
            tts.stop();
        }
    }

    @Override
    public void stopPlay() {
        tts.stop();
        clearAll();
    }

    private void changeHighlight(final int start, final int end) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spannable.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        });
    }

    private void setContentFromEditText(String content) {
        buildContents.setText(content, TextView.BufferType.SPANNABLE);
        spannable = (SpannableString) buildContents.getText();
    }

    private void startSpeak(final String text) {
        tts.speak(text, TextToSpeech.QUEUE_ADD, params, text);
    }

    private void clearAll() {
        playState = PlayState.STOP;
        standbyIndex = 0;
        lastPlayIndex = 0;

        if (spannable != null) {
            changeHighlight(0, 0); // remove highlight
        }
    }

    private void showState(final String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mp != null) {
//            mp.pause();
//            mp.release();
//            mp = null;
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mp != null) {
//            mp.pause();
//            mp.release();
//            mp = null;
//        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

//            if (mp != null) {
//                mp.pause();
//                mp.release();
//                mp = null;
//            }
            finish();
        }
    }

    public void sendRequestBuild(String nfc, String cps){
        String url = "https://somcoco.co.kr/application/building_info.jsp";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            processResponseBuild(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("nfc", nfc);
                params.put("cps", cps);

                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponseBuild(String response) throws IOException {
        Gson gson = new Gson();
        BuildingInfoItem buildingInfoItem = gson.fromJson(response, BuildingInfoItem.class);

        kor = buildingInfoItem.getBuildingIntroKr();
        eng = buildingInfoItem.getBuildingIntroEn();

        buildName.setText(buildingInfoItem.getBuildingName());
        buildTitle.setText(buildingInfoItem.getBuildingName());
        Glide.with(this)
                .load(buildingInfoItem.getBuildingImage())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(buildImg);
        buildContents.setText(buildingInfoItem.getBuildingIntroKr());

//        try{
//            mp = new MediaPlayer();
//            mp.setDataSource(buildingInfoItem.getBuildingAudio());
//            mp.prepare();
//            buildSeekbar.setMax(mp.getDuration());
//        }catch (IOException e){
//            e.printStackTrace();
//        }

    }

    public void sendRequestFacList(String nfc, String cps){
        String url = "https://somcoco.co.kr/application/facility_list.jsp";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponseFacList(response);
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
                params.put("nfc", nfc);
                params.put("cps", cps);

                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponseFacList(String response){
        Gson gson = new Gson();
        FacilityListArray facilityListArray = gson.fromJson(response, FacilityListArray.class);

        if (facilityListArray != null) {
            for (int i = 0; i < facilityListArray.facilityList.size(); i++){
                adapter.addItem(new FacilityListItem(facilityListArray.getItem(i).getFacilityNum(), facilityListArray.getItem(i).getFacilityName()));
            }
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

    public void sendRequestFacility(String nfc, String cps, String num){
        String url = "https://somcoco.co.kr/application/facility_info.jsp";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            processResponseFacility(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("nfc",nfc);
                params.put("cps",cps);
                params.put("num",num);

                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponseFacility(String response) throws IOException {
        Gson gson = new Gson();
        FacilityInfoItem facilityInfoItem = gson.fromJson(response, FacilityInfoItem.class);

        kor = facilityInfoItem.getFacilityInfoKr();
        eng = facilityInfoItem.getFacilityInfoEn();

        buildName.setText(facilityInfoItem.getFacilityName());
        facilContents.setText(facilityInfoItem.getFacilityInfoKr());
        facilTask.setText(facilityInfoItem.getFacilityTask());
    }

    public void sendRequestFacCarousel(String nfc, String cps, String num) {
        String url = "https://somcoco.co.kr/application/facility_carousel.jsp";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponseFacCarousel(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("nfc",nfc);
                params.put("cps",cps);
                params.put("num",num);

                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponseFacCarousel(String response){
        Gson gson = new Gson();
        FacilityCarouselArray carouselArray = gson.fromJson(response, FacilityCarouselArray.class);

        if (carouselArray != null) {
            for (int i = 0; i < carouselArray.facilityCarousel.size(); i++){
                carouselAdapter.addItem(new FacilityCarouselItem(carouselArray.getItem(i).getNoticeImage(), carouselArray.getItem(i).getImageLink()));
            }
            carouselAdapter.notifyDataSetChanged();
            facCarousel.setAdapter(carouselAdapter);
        }
    }
}
