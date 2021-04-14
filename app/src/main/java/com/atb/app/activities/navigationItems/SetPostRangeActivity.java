package com.atb.app.activities.navigationItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.navigationItems.booking.CreateBooking2Activity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.util.GpsInfo;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.theartofdev.edmodo.cropper.CropImage;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SetPostRangeActivity extends CommonActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    ImageView imv_back;
    TextView edt_serach;
    LinearLayout lyt_send;
    IndicatorSeekBar seekbarProgress;
    TextView txv_progress,txv_update;
    SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private LatLng myCordianite;
    float progress = 0.0f;
    long lat,lang;
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
        edt_serach.setOnClickListener(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        imv_back.setOnClickListener(this);
        txv_update.setOnClickListener(this);
        lyt_send.setOnClickListener(this);

        if(!Commons.g_user.getPost_search_region().equals("null")){
            seekbarProgress.setProgress(Float.parseFloat(Commons.g_user.getPost_search_region()));
            txv_progress.setText(Commons.g_user.getPost_search_region() + "KM");
            progress = Float.parseFloat(Commons.g_user.getPost_search_region());

        }
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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(Commons.traffic);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        setUpMap();
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
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18f));

            drawMyLocation();

        }
        else{
            double latitude =0;
            double longitude = 0;

            //create LatLng object
            myCordianite = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myCordianite));

            // zoom the map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18f));
            drawMyLocation();
        }
      //  moveCamera();
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    void drawMyLocation() {
        //showing the current location in map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else{
            googleMap.setMyLocationEnabled(true);

        }
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(myCordianite.latitude, myCordianite.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            edt_serach.setText(address);
        }catch (Exception e){

        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.edt_serach:
                List<Place.Field> fields = Arrays.asList(Place.Field.values());

                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .build(this);
                startActivityForResult(intent, Constants.AUTOCOMPLETE_REQUEST_CODE);
                break;
            case R.id.txv_update:
                setResult(Commons.location_code);
                Commons.location = edt_serach.getText().toString();
                Commons.lat = myCordianite.latitude;
                Commons.lng = myCordianite.longitude;
                finish(this);
                break;
            case R.id.lyt_send:
                setUpMap();
                break;
        }
    }


    void moveCamera(){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myCordianite));

        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(myCordianite)
                .radius(progress*1000)
                .strokeColor(getResources().getColor(R.color.head_color))
                .fillColor(getResources().getColor(R.color.header_color1)));


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Autocomplete.getStatusFromIntent(data).toString();
                edt_serach.setText(place.getAddress());
                myCordianite = place.getLatLng();
                moveCamera();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }

    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}