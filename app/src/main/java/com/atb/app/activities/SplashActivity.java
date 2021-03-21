package com.atb.app.activities;import androidx.appcompat.app.AppCompatActivity;import android.os.Bundle;import android.os.Handler;import android.util.Log;import com.atb.app.R;import com.atb.app.base.CommonActivity;import com.atb.app.commons.Commons;import com.atb.app.preference.PrefConst;import com.atb.app.preference.Preference;import com.google.android.libraries.places.api.Places;import com.google.android.libraries.places.api.net.PlacesClient;import java.text.DateFormatSymbols;import java.util.Collections;import java.util.Locale;public class SplashActivity extends CommonActivity {    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_splash);        Commons.Months = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();        getPhoneSize();        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));        PlacesClient placesClient = Places.createClient(this);        gotoNextpage();    }    void gotoNextpage() {        new Handler().postDelayed(new Runnable() {            @Override            public void run() {                goTo(SplashActivity.this, LoginActivity.class, true);            }        }, 3000);    }}