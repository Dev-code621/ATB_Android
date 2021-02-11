package com.atb.app.activities.newsfeedpost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Helper;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.dialog.SelectProfileDialog;
import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;


import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.io.File;
import java.util.ArrayList;

public class NewAdviceActivity extends CommonActivity implements View.OnClickListener {
    NiceSpinner spiner_media_type,spiner_category_type;
    LinearLayout lyt_back;
    ImageView imv_profile;
    FrameLayout lyt_image_video,lyt_video,lyt_profile;
    ImageView imv_videothumnail,imv_videoicon,imv_imageicon,imv_business;
    ArrayList<ImageView>imageViews = new ArrayList<>();
    LinearLayout lyt_image;
    EditText edt_title,edt_description;
    TextView txv_post;
    ArrayList<String>returnValue = new ArrayList<>();
    ArrayList<String>completedValue = new ArrayList<>();
    String videovalue ="";
    File uploadThumbImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advice);
        spiner_media_type = findViewById(R.id.spiner_media_type);
        spiner_category_type = findViewById(R.id.spiner_category_type);
        lyt_back = findViewById(R.id.lyt_back);
        imv_profile = findViewById(R.id.imv_profile);
        lyt_image_video = findViewById(R.id.lyt_image_video);
        imv_videothumnail = findViewById(R.id.imv_videothumnail);
        imv_videoicon = findViewById(R.id.imv_videoicon);
        lyt_video = findViewById(R.id.lyt_video);
        imv_imageicon = findViewById(R.id.imv_imageicon);
        imageViews.add(findViewById(R.id.imv_image));
        imageViews.add(findViewById(R.id.imv_image1));
        imageViews.add(findViewById(R.id.imv_image2));
        imageViews.add(findViewById(R.id.imv_image3));
        lyt_image = findViewById(R.id.lyt_image);
        edt_title = findViewById(R.id.edt_title);
        edt_description = findViewById(R.id.edt_description);
        txv_post = findViewById(R.id.txv_post);
        imv_business = findViewById(R.id.imv_business);
        lyt_profile = findViewById(R.id.lyt_profile);

        lyt_back.setOnClickListener(this);
        txv_post.setOnClickListener(this);
        imageViews.get(0).setOnClickListener(this);
        imv_videothumnail.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);

        spiner_media_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                String item = String.valueOf(parent.getItemAtPosition(position));
                if(position==0){
                    lyt_image_video.setVisibility(View.GONE);
                }else if(position==1){
                    lyt_image_video.setVisibility(View.VISIBLE);
                    lyt_image.setVisibility(View.VISIBLE);
                    lyt_video.setVisibility(View.GONE);

                }else{
                    lyt_image_video.setVisibility(View.VISIBLE);
                    lyt_image.setVisibility(View.GONE);
                    lyt_video.setVisibility(View.VISIBLE);
                }
            }
        });

        Keyboard();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.lyt_profile:
                SelectProfileDialog selectProfileDialog = new SelectProfileDialog();
                selectProfileDialog.OnSelectListener(new SelectProfileDialog.OnSelectListener() {
                    @Override
                    public void OnSelectProfile(boolean flag) {
                        if(flag){

                        }else{

                        }
                    }
                });

                selectProfileDialog.show(this.getSupportFragmentManager(), "DeleteMessage");

                break;
            case R.id.txv_post:
                finish(this);
                break;
            case R.id.imv_image:
                selectImage();
                break;
            case R.id.imv_videothumnail:
                selectVideo();
                break;
        }
    }

    void selectImage(){
        SelectMediaDialog selectMediaActionDialog = new SelectMediaDialog();
        selectMediaActionDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
            @Override
            public void OnCamera() {
                if(completedValue.size()==4)return;
                Options options = Options.init()
                        .setRequestCode(100)                                           //Request code for activity results
                        .setCount(4-completedValue.size())                                                   //Number of images to restict selection count
                        .setFrontfacing(false)                                         //Front Facing camera on start
                        .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                        .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                        .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                        .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                        .setPath("/pix/images");                                       //Custom Path For media Storage

                Pix.start(NewAdviceActivity.this, options);
            }

            @Override
            public void OnAlbum() {
                if(completedValue.size()>0)
                    completedValue.remove(0);
                reloadImages();

            }
        },getResources().getString(R.string.what_wouldlike),getResources().getString(R.string.add_media),getResources().getString(R.string.remove_media));
        selectMediaActionDialog.show(getSupportFragmentManager(), "action picker");



    }
    void selectVideo(){

        Options options = Options.init()
                .setRequestCode(200)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Video)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        Pix.start(this, options);
    }


    void Keyboard(){
        ScrollView lytContainer = (ScrollView) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_title.getWindowToken(), 0);
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if(completedValue.size()>3)return;

            completedValue.addAll(returnValue);
            reloadImages();
        }else  if(resultCode == Activity.RESULT_OK && requestCode == 200) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            videovalue = returnValue.get(0);
            reloadVideo();
        }
    }

    void reloadImages(){
        for(int i =0;i<imageViews.size();i++)imageViews.get(i).setImageResource(0);
        if(completedValue.size()>0)imv_imageicon.setVisibility(View.GONE);
        for(int i =0;i<completedValue.size();i++){
            Glide.with(this).load(completedValue.get(i)).placeholder(R.drawable.image_thumnail).dontAnimate().into(imageViews.get(i));

        }
    }
    void reloadVideo(){
        uploadThumbImage = Helper.getThumbnailPathForLocalFile(this, videovalue);

        Glide.with(this).load(uploadThumbImage).placeholder(R.drawable.image_thumnail).dontAnimate().into(imv_videothumnail);

        imv_videoicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_player));

    }
}