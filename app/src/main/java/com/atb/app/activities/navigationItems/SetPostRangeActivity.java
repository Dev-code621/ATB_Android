package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.navigationItems.booking.CreateBooking2Activity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.util.GpsInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SetPostRangeActivity extends CommonActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    ImageView imv_back;
    EditText edt_serach;
    LinearLayout lyt_send;
    IndicatorSeekBar seekbarProgress;
    TextView txv_progress,txv_update;
    SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private LatLng myCordianite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_post_range);
        imv_back = findViewById(R.id.imv_back);
        edt_serach = findViewById(R.id.edt_serach);
        lyt_send = findViewById(R.id.lyt_send);
        seekbarProgress = findViewById(R.id.seekbarProgress);
        txv_update = findViewById(R.id.txv_update);
        txv_progress = findViewById(R.id.txv_progress);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        imv_back.setOnClickListener(this);
        txv_update.setOnClickListener(this);
        lyt_send.setOnClickListener(this);
        Keyboard();
        seekbarProgress.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                txv_progress.setText(String.valueOf(seekParams.progress)+"KM");
                if(seekParams.progress==300)
                    txv_progress.setText("KM");
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f-(seekParams.progress/30)));


            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(Commons.traffic);
        mMap.animateCamera( CameraUpdateFactory.zoomTo( Commons.zoom ));
        googleMap.setOnMarkerClickListener(this);
        setUpMap();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hideKeyboard();
            }
        });


    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_serach.getWindowToken(), 0);
    }

    private void setUpMap() {

        GpsInfo gpsInfo = new GpsInfo(this);

//        gpsInfo.showSettingsAlert();
        // is enable gps?
        if (gpsInfo.isGetLocation()) {
            double latitude = gpsInfo.getLatitude();
            double longitude = gpsInfo.getLongitude();
            //create LatLng object
            myCordianite = new LatLng(latitude, longitude);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myCordianite));

            // zoom the map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(4f));

            drawMyLocation();

        }
        else{
            double latitude =0;
            double longitude = 0;

            //create LatLng object
            myCordianite = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myCordianite));

            // zoom the map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(4f));
            drawMyLocation();
        }
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    void drawMyLocation() {

        //showing the current location in map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else{
            googleMap.setMyLocationEnabled(true);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.txv_update:
                setResult(Commons.location_code);
                Commons.location = edt_serach.getText().toString();
                finish(this);
                break;
            case R.id.lyt_send:

                break;
        }
    }

    void Keyboard(){
        LinearLayout lytContainer = (LinearLayout) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_serach.getWindowToken(), 0);
                return false;
            }
        });


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}