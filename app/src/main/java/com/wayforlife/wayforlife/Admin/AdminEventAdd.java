package com.wayforlife.wayforlife.Admin;

import android.app.DatePickerDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wayforlife.wayforlife.Model.Event;
import com.wayforlife.wayforlife.Model.Report;
import com.wayforlife.wayforlife.R;
import com.wayforlife.wayforlife.User.CalendarEvents;
import com.wayforlife.wayforlife.User.MainActivityNavigation;
import com.wayforlife.wayforlife.User.MapFragment;
import com.wayforlife.wayforlife.User.Report2;

import java.util.Calendar;

public class AdminEventAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText eventName,eventDate,eventDesc;
    Button eventSubmit;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Events");

        eventDate=(EditText)findViewById(R.id.DateSetId);
        eventName=(EditText)findViewById(R.id.EventNameId);
        eventDesc=(EditText)findViewById(R.id.EventDescriptionId);
        eventSubmit=(Button)findViewById(R.id.AddEventtButtonId);

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSet();
            }
        });
        
        eventSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
                Intent in1 = new Intent(getApplicationContext(), AdminCalendar.class);
                startActivity(in1);
            }
        });

        //bottom bar
        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationId);

        //side bar clicks
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        
        


    }

    private void saveToDB() {

        Event e1 = new Event();
        String id=myRef.push().getKey();
        e1.setEventDate(eventDate.getText().toString());
        e1.setEventName(eventName.getText().toString());
        e1.setDescription(eventDesc.getText().toString());

        myRef.child(id).setValue(e1);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home1:
                    startActivity(new Intent(AdminEventAdd.this,home_admin.class));
                    return true;
                case R.id.events:
                    startActivity(new Intent(AdminEventAdd.this,CalendarEvents.class));
                    return true;
            }
            return false;
        }
    };

    private void dateSet() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        eventDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }



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
