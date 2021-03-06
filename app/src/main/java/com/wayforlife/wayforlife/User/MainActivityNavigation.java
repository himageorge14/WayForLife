package com.wayforlife.wayforlife.User;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.wayforlife.wayforlife.R;
import com.wayforlife.wayforlife.Admin.home_admin;

public class MainActivityNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //map display
        MapFragment mf=new MapFragment();
        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.replaceLayout, mf).commit();

        //bottom bar
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationId);

        //report sign click
        ImageView reportImg=(ImageView)findViewById(R.id.reportSignId);
        reportImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(MainActivityNavigation.this,Report1.class);
                startActivity(i1);
            }
        });

        //side bar clicks
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //bottom bar click
        BottomNavigationView bottomNavigationView1=(BottomNavigationView)findViewById(R.id.bottomNavigationId);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home1:
                    startActivity(new Intent(MainActivityNavigation.this,MainActivityNavigation.class));
                    return true;
                case R.id.events:
                    startActivity(new Intent(MainActivityNavigation.this,CalendarEvents.class));
                    return true;
                case R.id.notification:
                    startActivity(new Intent(MainActivityNavigation.this,home_admin.class));
            }
            return false;
        }
    };



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
