package com.wayforlife.wayforlife.User;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.wayforlife.wayforlife.Model.Report;
import com.wayforlife.wayforlife.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class Report2 extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int RESULT_LOAD_IMAGE = 12;
    private EditText datePicker, rDescription, rDate;
    private TextView addressFillText;
    private Button addReportButton, clickPictureButton, selectPicture;
    private ImageView clickReport;
    private byte[] imageInByte;
    private double tempLat, tempLong;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;


    private ConstraintLayout constraintLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);

        setTitle("Add your report");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Reports");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //side bar clicks
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        //display location
        addressFillText = (TextView) findViewById(R.id.addressFilledId);
        Intent r2 = getIntent();
        LatLng gotLocation = r2.getExtras().getParcelable("latlng");
        tempLat = gotLocation.latitude;
        tempLong = gotLocation.longitude;

        Geocoder geocoder1 = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> add = geocoder1.getFromLocation(gotLocation.latitude, gotLocation.longitude, 1);
            addressFillText.setText(add.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //set date
        datePicker = (EditText) findViewById(R.id.DateSetId);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSet();
            }
        });


        //click picture
        clickReport = (ImageView) findViewById(R.id.addReportImageId);
        clickPictureButton = (Button) findViewById(R.id.fromCameraButtonId);
        clickPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letsClick();
            }
        });


        //select picture from gallery
        selectPicture = (Button) findViewById(R.id.fromGalleryButtonId);
        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionOfImage();
            }
        });



    }

    private void saveToDB() {

        Report r1 = new Report();
        String id=myRef.push().getKey();

        StorageReference imgRef = mStorageRef.child(id);

        r1.setDate(rDate.getText().toString());
        r1.setLat(tempLat);
        r1.setLong(tempLong);
        r1.setDescription(rDescription.getText().toString());
        r1.setId(id);

        myRef.child(id).setValue(r1);

        checkFilePermission();

        UploadTask uploadTask = imgRef.putBytes(imageInByte);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("failed","failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("success","success");
            }
        });
    }

    private void checkFilePermission() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck = Report2.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck += Report2.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            }
            if (permissionCheck != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
                }
            }
        }else{
            Log.d("No permission", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    private void selectionOfImage() {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);

    }

    private void letsClick() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            clickReport.setImageBitmap(photo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageInByte = stream.toByteArray();
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImageURI = data.getData();

            Picasso.get().load(selectedImageURI).noPlaceholder().centerCrop().fit()
                    .into(clickReport);
            Bitmap bitmap = ((BitmapDrawable) clickReport.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageInByte = baos.toByteArray();

        }
        //submit report
        rDate = (EditText) findViewById(R.id.DateSetId);
        rDescription = (EditText) findViewById(R.id.ReportDescriptionId);
        addReportButton = (Button) findViewById(R.id.ReportButtonId);
        addReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
                Intent in1 = new Intent(Report2.this, MainActivityNavigation.class);
                startActivity(in1);

            }
        });

    }


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

                        datePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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