package com.atb.app.activities.navigationItems.business;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.adapter.SliderBusinessSplashAdapter;
import com.atb.app.adapter.SliderImageAdapter;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.model.submodel.SlideModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class UpgradeBusinessSplashActivity extends CommonActivity {
    ImageView imv_back;
    SliderView imageSlider;
    SliderBusinessSplashAdapter setSliderAdapter;
    ArrayList<SlideModel>slideModels = new ArrayList<>();
    TextView txv_next;
    int subscriptionType ; //0 first , 1 : user , 2;business
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_business_splash);
        imv_back = findViewById(R.id.imv_back);
        imageSlider = findViewById(R.id.imageSlider);
        txv_next = findViewById(R.id.txv_next);
        setSliderAdapter = new SliderBusinessSplashAdapter(this);
        imageSlider.setSliderAdapter(setSliderAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setScrollTimeInSec(3);
        imageSlider.setAutoCycle(true);
        imageSlider.startAutoCycle();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                subscriptionType= bundle.getInt("subScriptionType");
            }
        }
        if(subscriptionType>0){
            txv_next.setText("Upgrade Now Only £4.99/Month");
        }
        for(int i =0;i<9;i++){
            SlideModel slideModel = new SlideModel();
            slideModel.setImv_pic(Constants.slideImage[i]);
            slideModel.setTitle(Constants.slideTitle[i]);
            slideModel.setDescription(Constants.slideDescription[i]);
            slideModels.add(slideModel);
        }
        setSliderAdapter.renewItems(slideModels);

        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscriptionType>0)
                    finish(UpgradeBusinessSplashActivity.this);
                else
                    goTo(UpgradeBusinessSplashActivity.this,BusinessProductPostActivity.class,true);
            }
        });
        txv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscriptionType == 0)
                    goTo(UpgradeBusinessSplashActivity.this, UpdateBusinessActivity.class,true);
                else {
                    setResult(Commons.subscription_code);
                    finish(UpgradeBusinessSplashActivity.this);


                }
            }
        });
    }
}