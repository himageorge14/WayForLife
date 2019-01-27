package com.wayforlife.wayforlife.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wayforlife.wayforlife.EventDetailsAdmin;
import com.wayforlife.wayforlife.Model.Report;
import com.wayforlife.wayforlife.R;
import com.wayforlife.wayforlife.User.CalendarEvents;
import com.wayforlife.wayforlife.User.MainActivityNavigation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminCalendar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    CompactCalendarView compactCalendarView;
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMMM yyyy",Locale.getDefault());
    TextView monthDisplay;
    ImageView eventButton;
    String key;
    private DatabaseReference myRef;
    ArrayList<com.wayforlife.wayforlife.Model.Event> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //calendar
//        final ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setTitle(null);

        eventButton=(ImageView)findViewById(R.id.addEventIconId);
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminEventAdd.class));
            }
        });

        compactCalendarView=(CompactCalendarView)findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        monthDisplay=(TextView)findViewById(R.id.monthTitleId) ;
        Date d=new Date();
        monthDisplay.setText(simpleDateFormat.format(d));
        refreshEvents();
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                List<Event> ev=compactCalendarView.getEvents(dateClicked);

                checkDate(dateClicked);

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthDisplay.setText(simpleDateFormat.format(firstDayOfNewMonth));

            }
        });


        //bottom bar
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationId);

        //side bar clicks
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //bottom bar click
        BottomNavigationView bottomNavigationView1=(BottomNavigationView)findViewById(R.id.bottomNavigationId);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void checkDate(Date dateClicked) {

        myRef = FirebaseDatabase.getInstance().getReference("Events");
        key = myRef.getKey();
        eventsList=new ArrayList<>();
        String dCheck="";
        try {
            Date d1=new SimpleDateFormat("dd/MM/yyyy").parse(dateClicked.toString());
            dCheck=d1.toString();
            Log.d("inTry",dCheck.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String finalDCheck = dCheck;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    com.wayforlife.wayforlife.Model.Event myEvent=dataSnapshot1.getValue(com.wayforlife.wayforlife.Model.Event.class);
                    Log.d("dateClicked", finalDCheck);
                    if(finalDCheck.equals(myEvent.getEventDate().toString())){

                        Intent i2=new Intent(getApplicationContext(),EventDetailsAdmin.class);
                        i2.putExtra("ename",myEvent.getEventName());
                        i2.putExtra("edesc",myEvent.getDescription());
                        i2.putExtra("edate",myEvent.getEventDate());
                        startActivity(i2);

                    }
            }

        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home1calendar:
                    startActivity(new Intent(AdminCalendar.this,home_admin.class));
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

    public void refreshEvents(){
        myRef = FirebaseDatabase.getInstance().getReference("Events");
        key = myRef.getKey();
        eventsList=new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    com.wayforlife.wayforlife.Model.Event myEvent=dataSnapshot1.getValue(com.wayforlife.wayforlife.Model.Event.class);
                    eventsList.add(myEvent);
                    Log.d("eventName",myEvent.getEventName());

                    long t=0;
                    try {
                        Date d1=new SimpleDateFormat("dd/MM/yyyy").parse(myEvent.getEventDate().toString());
                        t=d1.getTime();
                        //Log.d("eeettttime",String.valueOf(d1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        //Log.d("eeeeeetxception",e.toString());
                    }

                    Event ev1=new Event(Color.RED,t,myEvent.getEventName().toString());
                    compactCalendarView.addEvent(ev1);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
