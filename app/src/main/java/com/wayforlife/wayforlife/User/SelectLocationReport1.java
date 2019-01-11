package com.wayforlife.wayforlife.User;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wayforlife.wayforlife.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectLocationReport1 extends Fragment implements OnMapReadyCallback {

    Geocoder geocoder;
    TextView locationAddress;
    GoogleMap mMap;
    ImageView submitLocation;
    public static LatLng demoVal;
    public String finalLocation;


    public SelectLocationReport1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_select_location_report1, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        geocoder=new Geocoder(this.getContext(),Locale.getDefault());

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                demoVal=latLng;
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Clears the previously touched position
                mMap.clear();

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);


                try {
                    List<Address> address=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    //displaying address
                    String address1 = address.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = address.get(0).getLocality();
                    String state = address.get(0).getAdminArea();
                    String postalCode = address.get(0).getPostalCode();
                    String knownName = address.get(0).getFeatureName();
                    finalLocation="Location: "+address1;
                    locationAddress=(TextView)getActivity().findViewById(R.id.locationDisplayId);
                    locationAddress.setText(finalLocation);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });
    }

}
