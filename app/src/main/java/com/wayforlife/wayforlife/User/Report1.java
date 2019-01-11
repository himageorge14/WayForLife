package com.wayforlife.wayforlife.User;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.wayforlife.wayforlife.R;

public class Report1 extends AppCompatActivity {
    ImageView locationSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report1);

        setTitle("Select Location");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //side bar clicks
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        //map display
        SelectLocationReport1 mf1=new SelectLocationReport1();
        android.support.v4.app.FragmentManager fm1=getSupportFragmentManager();
        fm1.beginTransaction().replace(R.id.replaceLayout, mf1).commit();

        locationSubmit=(ImageView)findViewById(R.id.submitLocationId);
        locationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s1=new Intent(Report1.this,Report2.class);
                s1.putExtra("latlng",SelectLocationReport1.demoVal);
                startActivity(s1);
                finish();
            }
        });



        //bottom bar
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationId);

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
