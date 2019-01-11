package com.wayforlife.wayforlife.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wayforlife.wayforlife.Model.Report;
import com.wayforlife.wayforlife.R;
import com.wayforlife.wayforlife.User.CalendarEvents;
import com.wayforlife.wayforlife.User.MainActivityNavigation;

public class ReportView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    TextView loc,dat,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Reports");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        loc=(TextView)findViewById(R.id.addressFilledViewId);
        dat=(TextView)findViewById(R.id.DateSetViewId);
        desc=(TextView)findViewById(R.id.ReportDescriptionViewId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //side bar clicks
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        //bottom bar
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationId);

        //bottom bar click
        BottomNavigationView bottomNavigationView1=(BottomNavigationView)findViewById(R.id.bottomNavigationId);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Intent im=getIntent();
        loc.setText(im.getStringExtra("address"));
        dat.setText(im.getStringExtra("date"));
        desc.setText(im.getStringExtra("desc"));

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home1calendar:
                    startActivity(new Intent(ReportView.this,home_admin.class));
                    return true;
                case R.id.events:
                    startActivity(new Intent(ReportView.this,CalendarEvents.class));
                    return true;

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
