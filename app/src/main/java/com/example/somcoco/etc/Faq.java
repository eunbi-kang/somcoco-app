package com.example.somcoco.etc;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.somcoco.AppHelper;
import com.example.somcoco.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Faq extends AppCompatActivity {

    FAQAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        ImageView back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FAQAdapter();

        sendRequest();


    }

    public void sendRequest() {
        String url = "https://somcoco.co.kr/application/faq.jsp";

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

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        NoticeListArray noticeListArray = gson.fromJson(response, NoticeListArray.class);

        if (noticeListArray != null) {
            for (int i = 0; i < noticeListArray.faqList.size(); i++) {
                adapter.addItem(new NoticeItem(noticeListArray.faqItem(i).title, noticeListArray.faqItem(i).content, noticeListArray.faqItem(i).date));
            }
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }
}