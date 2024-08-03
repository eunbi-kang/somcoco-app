package com.example.somcoco;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

public class SplashActivity extends AppCompatActivity {

    Handler handler = new Handler();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = (ImageView) findViewById(R.id.splash);

        Glide.with(this)
                .load(R.raw.splash)
                .into(imageView);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

                finish();
            }
        },3300);
    }
}
