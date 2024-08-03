package com.example.somcoco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.somcoco.community.CommunityActivity;
import com.example.somcoco.etc.Calendar;
import com.example.somcoco.etc.EtcFragment;
import com.example.somcoco.home.HomeFragment;
import com.example.somcoco.maplocation.CampusLocationActivity;
import com.example.somcoco.searchcampus.CampusSearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    HomeFragment homeFragment;
    EtcFragment etcFragment;
    NavigationView navigationView;
    ImageView search;
    ImageView draw;
    ImageView titleImg;
    TextView titleText;
    final int PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSION);
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        draw = (ImageView) findViewById(R.id.draw_menu);
        search = (ImageView) findViewById(R.id.main_search);
        titleText = (TextView) findViewById(R.id.title_name);
        titleImg = (ImageView) findViewById(R.id.title_img);
        homeFragment = new HomeFragment();
        etcFragment = new EtcFragment();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#FFC230"));
        //toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.app_name, R.string.app_name);
        //drawer.addDrawerListener(toggle);
        //toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, homeFragment)
                .commit();

        bottomNavigationView.setItemIconTintList(null);

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        titleImg.setVisibility(View.VISIBLE);
                        titleText.setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.campus_location:
                        Intent intent_loc = new Intent(getApplicationContext(), CampusLocationActivity.class);
                        intent_loc.putExtra("check",false);
                        startActivity(intent_loc);
                        return true;
                    case R.id.community:
                        Intent intent_comm = new Intent(getApplicationContext(), CommunityActivity.class);
                        startActivity(intent_comm);
                        return true;
                    case R.id.etc:
                        titleText.setText("더보기");
                        titleImg.setVisibility(View.INVISIBLE);
                        titleText.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, etcFragment).commit();
                        return true;
                    case R.id.menu_calendar:
                        Intent intent_cal = new Intent(getApplicationContext(), Calendar.class);
                        startActivity(intent_cal);
                        return true;
                }
                return false;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CampusSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search:
                Intent intent = new Intent(getApplicationContext(), CampusSearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.toolbar_calendar:
                Intent intent1 = new Intent(getApplicationContext(), Calendar.class);
                startActivity(intent1);
            default:
                return super.onOptionsItemSelected(item);
        }

    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}