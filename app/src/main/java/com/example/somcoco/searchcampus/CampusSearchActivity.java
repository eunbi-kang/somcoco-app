package com.example.somcoco.searchcampus;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.somcoco.AppHelper;
import com.example.somcoco.R;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

public class CampusSearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    SpeechRecognizer recognizer;
    ImageView back;
    ImageView mic;
    EditText searchKeyword;
    TextView searchNone;
    ImageView filterBtn;
    CampusListAdapter adapter;
    String keyword;
    Dialog dialog;
    String filter = "";
    ArrayList<String> item = new ArrayList<String>();
    final int PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchingcps);

        searchNone = (TextView) findViewById(R.id.search_search_none);

        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        Intent inIntent = getIntent();
        //keyword = inIntent.getStringExtra("keyword");

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_dialog);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.campus_list_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.campus_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CampusListAdapter(getApplicationContext());

        back = (ImageView) findViewById(R.id.search_back);
        filterBtn = (ImageView) findViewById(R.id.search_filter);
        searchKeyword = (EditText) findViewById(R.id.search_campus_keyword);
        mic = (ImageView) findViewById(R.id.search_mic_btn);
        searchKeyword.setText(keyword);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplication().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        searchKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case IME_ACTION_SEARCH:
                        keyword = searchKeyword.getText().toString();

                        if(keyword.equals("")) {
                            Toast.makeText(getApplicationContext(),"검색어를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        } else {
                            adapter.items.clear();
                            sendRequest(keyword.trim(), filter);
                        }
                    break;

                  default:
                    break;
                }
                return true;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                recognizer.setRecognitionListener(listener);
                recognizer.startListening(intent);
            }
        });

        adapter.setOnItemClickListener(new CampusListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CampusListAdapter.ViewHolder holder, View view, int position) {
                CampusListItem item = adapter.getItem(position);
                int cpsCode = item.getCps_code();
                Intent intent = new Intent(getApplicationContext(), CampusInfoActivity.class);
                intent.putExtra("cpsCode", String.valueOf(cpsCode));
                startActivity(intent);
            }
        });
    }
    public void sendRequest(String reqStr, String filter) {
        String url = "https://somcoco.co.kr/application/campus_search.jsp";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
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
                params.put("msg", reqStr.trim());
                params.put("filter", filter);

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        CampusListArray campusListArray = gson.fromJson(response, CampusListArray.class);

        if (campusListArray != null) {
            for (int i = 0; i < campusListArray.campusList.size(); i++) {
                adapter.addItem(new CampusListItem(campusListArray.getItem(i).cps_code, campusListArray.getItem(i).cps_name, campusListArray.getItem(i).cps_logo));
            }
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

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
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for (int i = 0; i < matches.size(); i++) {
                searchKeyword.setText(matches.get(i));
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
    public void onRefresh() {
        //keyword = searchKeyword.getText().toString();

        if(keyword.equals("")) {
            Toast.makeText(getApplicationContext(),"검색어를 입력해주세요.",Toast.LENGTH_SHORT).show();
        } else {
            adapter.items.clear();
            sendRequest(keyword.trim(), filter);
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    public void showDialog() {
        dialog.show();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button btn = (Button) dialog.findViewById(R.id.submit);

        ArrayList<Chip> items = new ArrayList<Chip>();

        Chip chip1 = (Chip) dialog.findViewById(R.id.chip_seoul);
        Chip chip2 = (Chip) dialog.findViewById(R.id.chip_gyeonggi);
        Chip chip3 = (Chip) dialog.findViewById(R.id.chip_incheon);
        Chip chip4 = (Chip) dialog.findViewById(R.id.chip_daejeon);
        Chip chip5 = (Chip) dialog.findViewById(R.id.chip_sejong);
        Chip chip6 = (Chip) dialog.findViewById(R.id.chip_chungnam);
        Chip chip7 = (Chip) dialog.findViewById(R.id.chip_chungbuk);
        Chip chip8 = (Chip) dialog.findViewById(R.id.chip_gwangju);
        Chip chip9 = (Chip) dialog.findViewById(R.id.chip_jeonnam);
        Chip chip10 = (Chip) dialog.findViewById(R.id.chip_jeonbuk);
        Chip chip11 = (Chip) dialog.findViewById(R.id.chip_busan);
        Chip chip12 = (Chip) dialog.findViewById(R.id.chip_gyeongnam);
        Chip chip13 = (Chip) dialog.findViewById(R.id.chip_ulsan);
        Chip chip14 = (Chip) dialog.findViewById(R.id.chip_daegu);
        Chip chip15 = (Chip) dialog.findViewById(R.id.chip_gyeongbuk);
        Chip chip16 = (Chip) dialog.findViewById(R.id.chip_gangwon);
        Chip chip17 = (Chip) dialog.findViewById(R.id.chip_jeju);

        chip1.setText("서울특별시");
        chip2.setText("경기도");
        chip3.setText("인천광역시");
        chip4.setText("대전광역시");
        chip5.setText("세종특별자치시");
        chip6.setText("충청남도");
        chip7.setText("충청북도");
        chip8.setText("광주광역시");
        chip9.setText("전라남도");
        chip10.setText("전라북도");
        chip11.setText("부산광역시");
        chip12.setText("경상남도");
        chip13.setText("울산광역시");
        chip14.setText("대구광역시");
        chip15.setText("경상북도");
        chip16.setText("강원도");
        chip17.setText("제주특별자치도");

        chip1.setCheckable(true);
        chip2.setCheckable(true);
        chip3.setCheckable(true);
        chip4.setCheckable(true);
        chip5.setCheckable(true);
        chip6.setCheckable(true);
        chip7.setCheckable(true);
        chip8.setCheckable(true);
        chip9.setCheckable(true);
        chip10.setCheckable(true);
        chip11.setCheckable(true);
        chip12.setCheckable(true);
        chip13.setCheckable(true);
        chip14.setCheckable(true);
        chip15.setCheckable(true);
        chip16.setCheckable(true);
        chip17.setCheckable(true);


        chip1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        chip17.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.add(buttonView.getText().toString());
                } else {
                    item.remove(String.valueOf(buttonView.getText().toString()));
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter = String.join("|", item);

                if (keyword != null) {
                    adapter.items.clear();
                    sendRequest(keyword.trim(), filter);
                }
                dialog.dismiss();
            }
        });
    }
}
