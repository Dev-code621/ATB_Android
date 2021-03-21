package com.atb.app.base;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.core.content.FileProvider;
import androidx.transition.ChangeBounds;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.newsfeedpost.NewAdviceActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.SelectProfileDialog;
import com.atb.app.preference.PrefConst;
import com.atb.app.preference.Preference;
import com.atb.app.util.CustomMultipartRequest;
import com.atb.app.view.zoom.ImageZoomButton;
import com.atb.app.view.zoom.ZoomAnimation;
import com.google.android.gms.common.internal.service.Common;
import com.lky.toucheffectsmodule.factory.TouchEffectsFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;



public abstract class CommonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        TouchEffectsFactory.initTouchEffects(this);
        super.onCreate(savedInstanceState);
        Commons.g_commentActivity = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public static String getCurrentTime(){
        Timestamp timestamp = new Timestamp(new Date().getTime());
        try {
            long currentTime = timestamp.getTime();
            Date df = new Date(currentTime);
            String timeStampString = new SimpleDateFormat("dd/MM/yyyy h:mm aa").format(df);
            return timeStampString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public Uri getUri(String filepath){
        File file = new File(filepath);
        if(file.exists()) {
            Uri imageUri = FileProvider.getUriForFile(
                    this,
                    "com.atb.app.fileprovider", //(use your app signature + ".provider" )
                    file);
            return imageUri;

        }
        return null;
    }

    public String getUTCDate(String date)  {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String time = format.format(d);
        return time;
    }

    public String getLocaltime(String date){

        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String localtime= new SimpleDateFormat("hh:mm a").format(d);
        return localtime;
    }
    public boolean emailInvalied(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public void activityAnimation(Scene anotherScene,int layout_id){
        TransitionSet set = new TransitionSet();
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(layout_id);
        set.addTransition(slide);
        set.addTransition(new ChangeBounds());
        set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        set.setDuration(350);
        TransitionManager.go(anotherScene, set);
    }

    public void SelectprofileDialog(Context context){
        SelectProfileDialog selectProfileDialog = new SelectProfileDialog();
        selectProfileDialog.OnSelectListener(new SelectProfileDialog.OnSelectListener() {
            @Override
            public void OnSelectProfile(boolean flag) {
                selectProfile(flag);
            }
        });
        selectProfileDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
    }

    public boolean selectProfile(boolean flag){
        return flag;
    }

    private ZoomAnimation zoomAnimation;
    private boolean isZoom=false;
    public void showPicture(View view, String url) {
        zoomAnimation = new ZoomAnimation(this);
        zoomAnimation.zoom(view, url, 400, R.drawable.image_thumnail, this);
        isZoom = true;
    }

    public boolean isValidMakePost(){
        if(Commons.g_user.getBusinessModel().getApproved()==1)return  true;
        else{
            String title = "You didn't subscribe for your business account yet!\\nWould you like to subscribe now?";
            if(Commons.g_user.getBusinessModel().getPaid()==1){
                if(Commons.g_user.getBusinessModel().getApproved()==0)
                    title = "Your business account is currently pending for approval.\\nATB admin will review your account and update soon!";
                else
                    title = "Your business profile has been rejected!";
                showAlertDialog(title);
            }else {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {

                    }
                },title);
                confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
            }
        }
        return false;
    }
    protected void getPhoneSize(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Commons.phone_height = displayMetrics.heightPixels;
        Commons.phone_width = displayMetrics.widthPixels;
    }


}
