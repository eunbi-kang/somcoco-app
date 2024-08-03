package com.example.somcoco.community;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.somcoco.R;

public class CommunityActivity extends AppCompatActivity {
    WebView commuWeb;
    ProgressBar progressBar;
    EditText urlEdt;
    ImageView search;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        commuWeb = (WebView) findViewById(R.id.community_web);
        progressBar = (ProgressBar) findViewById(R.id.community_prog);
        //urlEdt = (EditText) findViewById(R.id.community_keyword);
        //search = (ImageView) findViewById(R.id.community_search);
        close = (ImageView) findViewById(R.id.community_close);

        progressBar.setVisibility(View.GONE);

        initWebView();

//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                commuWeb.loadUrl("http://" + urlEdt.getText().toString() + "");
//            }
//        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void initWebView() {
        commuWeb.setWebViewClient(new WebViewClient(){

            // 웹뷰에 웹페이지가 로딩이 시작될 때 실행되는 메소드
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            // 웹뷰에 웹페이지 로딩이 완료 된 후 실행되는 메소드
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            // 현재 페이지의 url 주소를 읽어오는 메소드
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings ws = commuWeb.getSettings();
        ws.setJavaScriptEnabled(true);                          // 자바스크립트 사용 유무 설정
        commuWeb.loadUrl("http://somcoco.co.kr/");
    }

    @Override
    public void onBackPressed() {
        if (commuWeb.canGoBack()){                              // 스마트폰의 뒤로가기 키 입력시 뒤로갈 페이지가 있으면 이전페이지 이동
            commuWeb.goBack();
        } else {
            super.onBackPressed();                              // 뒤로갈 페이지가 없으면 액티비티 종료
        }

    }
}
