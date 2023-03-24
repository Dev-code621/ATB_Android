package com.atb.app.activities.navigationItems.business;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.adapter.SliderBusinessSplashAdapter;
import com.atb.app.adapter.SliderImageAdapter;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.submodel.SlideModel;
import com.braintreepayments.api.models.VenmoAccountNonce;
import com.fxn.pix.Pix;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpgradeBusinessSplashActivity extends CommonActivity {
    ImageView imv_back;
    SliderView imageSlider;
    SliderBusinessSplashAdapter setSliderAdapter;
    ArrayList<SlideModel>slideModels = new ArrayList<>();
    TextView txv_next;
    int subscriptionType ; //0 first , 1 : user , 2;business
    Map<String, String> payment_params = new HashMap<>();
    int REQUEST_PAYMENT_CODE =10034;

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
            txv_next.setText("Upgrade Now Only £0.99/Week");
        }
        for(int i =0;i<Constants.slideImage.length;i++){
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
                    finish(UpgradeBusinessSplashActivity.this);
                   // goTo(UpgradeBusinessSplashActivity.this,BusinessProductPostActivity.class,true);

            }
        });
        txv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscriptionType == 0)
                    goTo(UpgradeBusinessSplashActivity.this, UpdateBusinessActivity.class,true);
                else {
//                    getPaymentToken("4.99", new NewsFeedEntity(),0,new ArrayList<>());
                    payment_params.clear();
                    payment_params.put("token", Commons.token);
                    paymentProcessing(payment_params,1);
                }
            }
        });
    }


    @Override
    public void processPayment(String price, String client_id,String clicnet_token,NewsFeedEntity newsFeedEntity1,int deliveryOption1, ArrayList<String> selected_Variation1){
        payment_params.clear();
        payment_params.put("token",Commons.token);
        payment_params.put("customerId",Commons.g_user.getBt_customer_id());
//        DropInRequest dropInRequest = new DropInRequest()
//                .clientToken(clicnet_token)
//                .cardholderNameStatus(CardForm.FIELD_OPTIONAL)
//                .collectDeviceData(true)
//                .vaultManager(true);
//        startActivityForResult(dropInRequest.getIntent(this), REQUEST_PAYMENT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PAYMENT_CODE) {
//            if (resultCode == RESULT_OK) {
//                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
//                payment_params.put("paymentMethodNonce", Objects.requireNonNull(result.getPaymentMethodNonce()).getNonce());
//                if(result.getPaymentMethodType().name().equals("PAYPAL")){
//                    payment_params.put("paymentMethod","Paypal");
//                }else {
//                    payment_params.put("paymentMethod","Card");
//                }
//                paymentProcessing(payment_params,1);
//
//                String deviceData = result.getDeviceData();
//                if (result.getPaymentMethodType() == PaymentMethodType.PAY_WITH_VENMO) {
//                    VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) result.getPaymentMethodNonce();
//                    String venmoUsername = venmoAccountNonce.getUsername();
//                }
//                // use the result to update your UI and send the payment method nonce to your server
//            } else if (resultCode == RESULT_CANCELED) {
//                // the user canceled
//            } else {
//                // handle errors here, an exception may be available in
//                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
//                Log.d("error:", error.toString());
//            }
        }
    }
    @Override
    public void finishPayment(String transaction_id){
        Commons.g_user.getBusinessModel().setPaid(1);
        setResult(Commons.subscription_code);

        finish(UpgradeBusinessSplashActivity.this);
    }


}