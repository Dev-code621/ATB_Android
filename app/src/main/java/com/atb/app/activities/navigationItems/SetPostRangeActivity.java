package com.atb.app.activities.navigationItems;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.atb.app.util.search.ColorSuggestion;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.util.search.ColorWrapper;
import com.atb.app.util.search.DataHelper;
import com.atb.app.util.GpsInfo;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

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
    FloatingSearchView mSearchView ;
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private String mLastQuery = "";

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
        mSearchView = findViewById(R.id.floating_search_view);
        mapFragment.getMapAsync(this);
        setupSearchBar();
        if(!Commons.g_user.getLocation().equals("null")){
            txv_progress.setText(50 + "KM");
            seekbarProgress.setProgress(50);
            myCordianite = Commons.LatLang.get(Commons.g_user.getLocation());
        }
        seekbarProgress.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                txv_progress.setText(String.valueOf(seekParams.progress)+"KM");
                if(seekParams.progress==80)
                    txv_progress.setText("∞KM");
//                if(seekParams.progress==80)
//                    txv_progress.setText("KM");
                moveCamera();

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

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
        //setUpMap();
        moveCamera();
    }


    void setupSearchBar(){
        DataHelper.init();
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    mSearchView.showProgress();
                    DataHelper.findSuggestions(SetPostRangeActivity.this, newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<ColorSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                mLastQuery = searchSuggestion.getBody();
                mSearchView.setSearchBarTitle(mLastQuery);
                myCordianite = Commons.LatLang.get(mLastQuery);
                edt_serach.setText(mLastQuery);
                mSearchView.clearSuggestions();
                moveCamera();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(DataHelper.getHistory(SetPostRangeActivity.this, 3));

            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());
            }
        });
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
            getAddress(addresses);

        }catch (Exception e){

        }
    }
    void getAddress(  List<Address> addresses){
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        edt_serach.setText(city +", " + state + ", "+ country);
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
                //setUpMap();
                break;
        }
    }


    void moveCamera(){
        googleMap.clear();
        double radius = 1000*seekbarProgress.getProgress();
        double scale = radius / 500;
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myCordianite));
        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(myCordianite)
                .radius(radius)
                .strokeColor(getResources().getColor(R.color.head_color))
                .fillColor(getResources().getColor(R.color.header_color1)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo((float)(14 - Math.log(scale) / Math.log(2))), 2000, null);
        googleMap.addMarker(new MarkerOptions()
                .position(myCordianite)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mappin)));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    Autocomplete.getStatusFromIntent(data).toString();
                    myCordianite = place.getLatLng();
                    moveCamera();
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    getAddress(addresses);
                }catch (Exception e){

                }

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