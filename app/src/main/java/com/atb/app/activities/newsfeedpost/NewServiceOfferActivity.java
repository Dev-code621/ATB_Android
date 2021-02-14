package com.atb.app.activities.newsfeedpost;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.base.CommonActivity;
import com.zcw.togglebutton.ToggleButton;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;

public class NewServiceOfferActivity extends CommonActivity implements View.OnClickListener {

    LinearLayout lyt_back,lyt_image;
    FrameLayout lyt_image_video,lyt_video,lyt_profile;
    NiceSpinner spiner_media_type,spiner_category_type;
    ImageView imv_videothumnail,imv_videoicon,imv_imageicon;
    EditText edt_title,edt_description,edt_price,edt_location;
    ArrayList<ImageView> imageViews = new ArrayList<>();
    TextView txv_minus,txv_plus,txt_cancelday,txv_post;
    ToggleButton toggle_quality,toggle_insurance,toggle_cash,toggle_paypal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_offer);
        spiner_media_type = findViewById(R.id.spiner_media_type);
        spiner_category_type = findViewById(R.id.spiner_category_type);
        lyt_back = findViewById(R.id.lyt_back);
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
        lyt_profile = findViewById(R.id.lyt_profile);
        edt_price = findViewById(R.id.edt_price);
        edt_location = findViewById(R.id.edt_location);
        txv_minus = findViewById(R.id.txv_minus);
        txv_plus = findViewById(R.id.txv_plus);
        txt_cancelday = findViewById(R.id.txt_cancelday);
        txv_post = findViewById(R.id.txv_post);
        toggle_quality = findViewById(R.id.toggle_quality);
        toggle_insurance = findViewById(R.id.toggle_insurance);
        toggle_cash = findViewById(R.id.toggle_cash);
        toggle_paypal = findViewById(R.id.toggle_paypal);

        lyt_back.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        txv_post.setOnClickListener(this);
        spiner_media_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                String item = String.valueOf(parent.getItemAtPosition(position));
                if(position==0){
                    lyt_image.setVisibility(View.VISIBLE);
                    lyt_video.setVisibility(View.GONE);

                }else{
                    lyt_image.setVisibility(View.GONE);
                    lyt_video.setVisibility(View.VISIBLE);
                }
            }
        });
        Keyboard();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.txv_post:
                finish(this);
                break;
            case R.id.lyt_profile:
                SelectprofileDialog(this);
                break;
        }
    }
}