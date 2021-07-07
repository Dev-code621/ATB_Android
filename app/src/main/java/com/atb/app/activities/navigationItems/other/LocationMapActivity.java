package com.atb.app.activities.navigationItems.other;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class LocationMapActivity extends CommonActivity implements  OnMapReadyCallback  {
    ImageView imv_back;
    SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);
        imv_back= findViewById(R.id.imv_back);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(LocationMapActivity.this);
            }
        });
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                latLng =  new LatLng(bundle.getDouble("lat"),bundle.getDouble("lang"));

            }
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(Commons.traffic);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
    }



}