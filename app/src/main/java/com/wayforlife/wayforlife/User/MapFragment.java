package com.wayforlife.wayforlife.User;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wayforlife.wayforlife.Admin.ReportView;
import com.wayforlife.wayforlife.DataHandler.DatabaseHandler;
import com.wayforlife.wayforlife.Model.Report;
import com.wayforlife.wayforlife.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    private ArrayList<Report> initialMapMarkers;
    DatabaseReference databaseReference;
    String key;
    double rlat;
    double rlong;
    String rDate,sendAddress;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        //initialize array;
        initialMapMarkers=new ArrayList<>();

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        refreshData();

    }

    private void refreshData(){
        if(!initialMapMarkers.isEmpty()){
            initialMapMarkers.clear();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Reports");
        key = databaseReference.getKey();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Report myReport=dataSnapshot1.getValue(Report.class);
                    initialMapMarkers.add(myReport);
                    Log.d("report description",myReport.getDescription());
                    LatLng tempLatLng=new LatLng(myReport.getLat(),myReport.getLong());
                    mMap.addMarker(new MarkerOptions().position(tempLatLng)
                            .title(myReport.getDescription().toString())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.black_marker))).showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(tempLatLng));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final String tit=marker.getTitle();

                for(int i=0;i<initialMapMarkers.size();i++){
                    if(tit.equals(initialMapMarkers.get(i).getDescription())){
                        rlat=initialMapMarkers.get(i).getLat();
                        rlong=initialMapMarkers.get(i).getLong();
                        rDate=initialMapMarkers.get(i).getDate();
                        Log.d("match found","yipeeee");
                        break;
                    }
                }

                Geocoder geocoder1 = new Geocoder(getContext(), Locale.getDefault());
                try {
                    List<Address> add = geocoder1.getFromLocation(rlat, rlong, 1);
                    sendAddress=add.get(0).getAddressLine(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent im=new Intent(getContext(),ReportView.class);
                im.putExtra("address",sendAddress);
                im.putExtra("desc",tit);
                im.putExtra("date",rDate);
                startActivity(im);
                return true;
            }
        });


    }

}
