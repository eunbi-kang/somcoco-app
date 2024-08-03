package com.example.somcoco.etc;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.somcoco.AppHelper;
import com.example.somcoco.R;

import java.util.HashMap;
import java.util.Map;

public class Cs extends AppCompatActivity {

    Dialog dial;
    LinearLayout select;
    TextView cso;
    EditText title, content, email;
    Button reset, submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs);


        select = (LinearLayout) findViewById(R.id.select);
        cso = (TextView)findViewById(R.id.cso);
        title = (EditText) findViewById(R.id.cs_title);
        content = (EditText) findViewById(R.id.content);
        email = (EditText)findViewById(R.id.email);
        reset = (Button)findViewById(R.id.reset);
        submit = (Button)findViewById(R.id.submit);

        reset.setBackgroundColor(Color.LTGRAY);

        dial = new Dialog(Cs.this);
        dial.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dial.setContentView(R.layout.cs_dialog);

        ImageView back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDial();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    public void showDial() {
        dial.show();

        TextView d1, d2, d3, d4, d5, d6, d7, d8, d9;

        d1 = (TextView) dial.findViewById(R.id.d1);
        d2 = (TextView) dial.findViewById(R.id.d2);
        d3 = (TextView) dial.findViewById(R.id.d3);
        d4 = (TextView) dial.findViewById(R.id.d4);
        d5 = (TextView) dial.findViewById(R.id.d5);
        d6 = (TextView) dial.findViewById(R.id.d6);
        d7 = (TextView) dial.findViewById(R.id.d7);
        d8 = (TextView) dial.findViewById(R.id.d8);
        d9 = (TextView) dial.findViewById(R.id.d9);

        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cso.setText(d1.getText());
                dial.dismiss();
            }
        });

        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cso.setText(d2.getText());
                dial.dismiss();
            }
        });

        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cso.setText(d3.getText());
                dial.dismiss();
            }
        });

        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cso.setText(d4.getText());
                dial.dismiss();
            }
        });

        d5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cso.setText(d5.getText());
                dial.dismiss();
            }
        });

        d6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cso.setText(d6.getText());
                dial.dismiss();
            }
        });

        d7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cso.setText(d7.getText());
                dial.dismiss();
            }
        });

        d8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cso.setText(d8.getText());
                dial.dismiss();
            }
        });

        d9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cso.setText(d9.getText());
                dial.dismiss();
            }
        });
    }

    public void sendRequest() {
        String url = "http:somcoco.co.kr/application/cs.jsp";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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

                params.put("cso", cso.getText().toString());
                params.put("title", title.getText().toString());
                params.put("content", content.getText().toString());
                params.put("email", email.getText().toString());

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
}