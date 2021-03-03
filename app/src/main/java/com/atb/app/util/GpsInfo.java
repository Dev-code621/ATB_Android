package com.atb.app.util;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.atb.app.commons.Commons;


public class GpsInfo extends Service implements LocationListener {
    private final Context mContext;

    // now is Using GPS?
    boolean isGPSEnabled = false;

    // network using?
    boolean isNetworkEnabled = false;

    boolean isGetLocation = false;

    Location location;
    double lat;
    double lon;

    //GPS status value
    private static final long MIN_DISTANCE_UPDATE = 10;

    //GPS info update time
    private static final long MIN_TIME_UPDATES = 1000 * 6 * 1;

    protected LocationManager locationManager;

    public GpsInfo(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled) {
                showSettingsAlert();
            }

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                this.isGetLocation = true;
                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission((Activity)mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((Activity)mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity)mContext, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, 10);
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_UPDATES,
                            MIN_DISTANCE_UPDATE, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // store lon and lat
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {

                    locationManager
                            .requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_UPDATES,
                                    MIN_DISTANCE_UPDATE,
                                    this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public double getLatitude(){

        if (location!=null){
            lat=location.getLatitude();
        }
        return  lat;
    }

    public double getLongitude(){
        if (location!=null){
            lon=location.getLongitude();
        }
        return lon;
    }

    public boolean isGetLocation(){
        return this.isGetLocation;
    }

    // GPS Alert Dialog

    public void showSettingsAlert(){
        AlertDialog.Builder alertDlg=new AlertDialog.Builder(mContext);

        alertDlg.setTitle("Location Services is Disabled");
        alertDlg.setMessage("TmoLebanon needs access to your location.\n Please turn on Location Services in your devicee settings.");
        alertDlg.setPositiveButton("GO TO  SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Commons.traffic=true;
                mContext.startActivity(intent);
            }
        });
        alertDlg.setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Commons.traffic=false;
                dialog.cancel();

            }
        });

        alertDlg.show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
