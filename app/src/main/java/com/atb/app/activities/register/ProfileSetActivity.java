package com.atb.app.activities.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.transition.Scene;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.SplashActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Helper;
import com.atb.app.dialog.PickImageDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.util.ImageUtils;
import com.atb.app.util.MediaPicker;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.util.List;

public class ProfileSetActivity extends CommonActivity implements View.OnClickListener{
    ViewGroup sceneRoot;
    Scene aScene,anotherScene;
    SimpleDraweeView imv_profile;
    EditText edt_username,edt_firstname,edt_lastname,edt_location,edt_birthday,edt_invitecode;
    TextView txv_male,txv_female,txv_next;
    ImageView imv_selector2,imv_selector1;
    FrameLayout lyt_paste;
    private ImageUtils imageUtils;
    private File profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        aScene = Scene.getSceneForLayout(sceneRoot, R.layout.signup_animation1, this);
        anotherScene =
                Scene.getSceneForLayout(sceneRoot, R.layout.layout_complete_profile, this);

        activityAnimation(aScene,R.id.lyt_container);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityAnimation(anotherScene,R.id.lyt_container);
                loadLayout();
                Keyboard();

            }
        }, 1000);
    }

    void loadLayout(){
        imv_profile = sceneRoot.findViewById(R.id.imv_profile);
        edt_username = sceneRoot.findViewById(R.id.edt_username);
        edt_firstname = sceneRoot.findViewById(R.id.edt_firstname);
        edt_lastname = sceneRoot.findViewById(R.id.edt_lastname);
        edt_location = sceneRoot.findViewById(R.id.edt_location);
        edt_birthday = sceneRoot.findViewById(R.id.edt_birthday);
        edt_invitecode = sceneRoot.findViewById(R.id.edt_invitecode);
        txv_male = sceneRoot.findViewById(R.id.txv_male);
        txv_female = sceneRoot.findViewById(R.id.txv_female);
        imv_selector2 = sceneRoot.findViewById(R.id.imv_selector2);
        imv_selector1 = sceneRoot.findViewById(R.id.imv_selector1);
        lyt_paste  = sceneRoot.findViewById(R.id.lyt_paste);
        txv_next  = sceneRoot.findViewById(R.id.txv_next);

        imv_profile.setOnClickListener(this);
        txv_female.setOnClickListener(this);
        txv_male.setOnClickListener(this);
        lyt_paste.setOnClickListener(this);
        txv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_profile:
                choicePicture();
                break;
            case R.id.txv_female:
                imv_selector1.setVisibility(View.GONE);
                imv_selector2.setVisibility(View.VISIBLE);
                break;
            case R.id.txv_male:
                imv_selector1.setVisibility(View.VISIBLE);
                imv_selector2.setVisibility(View.GONE);
//                txv_male.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_rectangle_round));
//                txv_female.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_rectangle_round1));
                break;
            case R.id.lyt_paste:

                break;
            case R.id.txv_next:
                goTo(this, CreateFeedActivity.class,false);
                break;

        }
    }

    void Keyboard() {
        ScrollView lytContainer = sceneRoot.findViewById(R.id.lyt_scroll);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_username.getWindowToken(), 0);
                return false;
            }
        });
    }

    void choicePicture(){

    }

}