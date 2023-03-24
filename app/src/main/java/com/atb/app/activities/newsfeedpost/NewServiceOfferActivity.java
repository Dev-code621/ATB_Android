package com.atb.app.activities.newsfeedpost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.SetPostRangeActivity;
import com.atb.app.activities.profile.boost.ApprovePaymentActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.ConfirmVariationDialog;
import com.atb.app.dialog.DraftDialog;
import com.atb.app.dialog.GenralConfirmDialog;
import com.atb.app.dialog.SelectInsuranceDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.submodel.InsuranceModel;
import com.atb.app.util.CustomMultipartRequest;
import com.atb.app.util.RoundedCornersTransformation;

import com.braintreepayments.api.models.VenmoAccountNonce;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.gson.Gson;
import com.zcw.togglebutton.ToggleButton;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.atb.app.commons.Commons.REQUEST_PAYMENT_CODE;

import androidx.annotation.NonNull;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnErrorListener;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;
import gun0912.tedimagepicker.builder.listener.OnSelectedListener;
import gun0912.tedimagepicker.builder.type.MediaType;

public class NewServiceOfferActivity extends CommonActivity implements View.OnClickListener {

    LinearLayout lyt_back,lyt_image,lyt_deposit;
    FrameLayout lyt_video,lyt_profile;
    NiceSpinner spiner_media_type,spiner_category_type;
    ImageView imv_videothumnail,imv_videoicon,imv_imageicon,imv_profile;
    EditText edt_title,edt_description,edt_price,edt_deposit;
    TextView txv_location;
    ArrayList<ImageView> imageViews = new ArrayList<>();
    TextView txv_minus,txv_plus,txt_cancelday,txv_post;
    ToggleButton toggle_quality,toggle_insurance,toggle_cash,toggle_paypal,toggle_deposit;
    int media_type = 1;
    ArrayList<String>returnValue = new ArrayList<>();
    ArrayList<String>completedValue = new ArrayList<>();
    ArrayList<InsuranceModel>insuranceModels = new ArrayList<>();
    ArrayList<InsuranceModel>qualifications = new ArrayList<>();

    TextView txv_quality,txv_quality_name,txv_quality_time,txv_insurance_time,txv_insurance_name,txv_insurance;
    LinearLayout lyt_qualitfied,lyt_qualitfied_minus,lyt_qualitfied_plus,lyt_insurance_plus,lyt_insurance_minus,lyt_insurance;
    String videovalue ="";
    int maxImagecount = 9;
     int insurance_id = -1,qualitfication_id = -1;
     boolean cash = false, paypal = false;
     int is_deposit_required = 0,candellation = 14;
     float deposit_amount =0.00f;
     int isPosting;
     RelativeLayout lyt_product,lyt_post;
     ImageView imv_back;
    boolean editable =false;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();

    TextView txt_duration,txv_duration_plus,txv_duration_minus;
    Double duration = 0.5;
    LinearLayout lyt_duration;
    ToggleButton toggle_duration;
    int isDraft = 0;
    int saveDraft = 0 ;
    ImageView img_deposit,img_stripe;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_offer);

        lyt_duration = findViewById(R.id.lyt_duration);
        toggle_duration = findViewById(R.id.toggle_duration);
        txt_duration = findViewById(R.id.txt_duration);
        txv_duration_plus = findViewById(R.id.txv_duration_plus);
        txv_duration_minus = findViewById(R.id.txv_duration_minus);
        lyt_product = findViewById(R.id.lyt_product);
        lyt_post = findViewById(R.id.lyt_post);
        imv_back = findViewById(R.id.imv_back);
        txv_quality = findViewById(R.id.txv_quality);
        txv_quality_name = findViewById(R.id.txv_quality_name);
        txv_quality_time = findViewById(R.id.txv_quality_time);
        txv_insurance_time = findViewById(R.id.txv_insurance_time);
        txv_insurance_name = findViewById(R.id.txv_insurance_name);
        txv_insurance = findViewById(R.id.txv_insurance);
        lyt_qualitfied = findViewById(R.id.lyt_qualitfied);
        lyt_qualitfied_minus = findViewById(R.id.lyt_qualitfied_minus);
        lyt_qualitfied_plus = findViewById(R.id.lyt_qualitfied_plus);
        lyt_insurance_plus = findViewById(R.id.lyt_insurance_plus);
        lyt_insurance_minus = findViewById(R.id.lyt_insurance_minus);
        lyt_insurance = findViewById(R.id.lyt_insurance);
        spiner_media_type = findViewById(R.id.spiner_media_type);
        spiner_category_type = findViewById(R.id.spiner_category_type);
        lyt_back = findViewById(R.id.lyt_back);
        imv_videothumnail = findViewById(R.id.imv_videothumnail);
        imv_videoicon = findViewById(R.id.imv_videoicon);
        lyt_video = findViewById(R.id.lyt_video);
        imageViews.add(findViewById(R.id.imv_image));
        imageViews.add(findViewById(R.id.imv_image1));
        imageViews.add(findViewById(R.id.imv_image2));
        imageViews.add(findViewById(R.id.imv_image3));
        imageViews.add(findViewById(R.id.imv_image4));
        imageViews.add(findViewById(R.id.imv_image5));
        imageViews.add(findViewById(R.id.imv_image6));
        imageViews.add(findViewById(R.id.imv_image7));
        imageViews.add(findViewById(R.id.imv_image8));
        lyt_deposit = findViewById(R.id.lyt_deposit);
        lyt_image = findViewById(R.id.lyt_image);
        edt_title = findViewById(R.id.edt_title);
        edt_description = findViewById(R.id.edt_description);
        lyt_profile = findViewById(R.id.lyt_profile);
        edt_price = findViewById(R.id.edt_price);
        edt_deposit = findViewById(R.id.edt_deposit);
        txv_location = findViewById(R.id.txv_location);
        txv_minus = findViewById(R.id.txv_minus);
        txv_plus = findViewById(R.id.txv_plus);
        txt_cancelday = findViewById(R.id.txt_cancelday);
        txv_post = findViewById(R.id.txv_post);
        toggle_quality = findViewById(R.id.toggle_quality);
        toggle_insurance = findViewById(R.id.toggle_insurance);
        toggle_cash = findViewById(R.id.toggle_cash);
        toggle_paypal = findViewById(R.id.toggle_paypal);
        imv_profile = findViewById(R.id.imv_profile);
        imv_imageicon = findViewById(R.id.imv_imageicon);
        toggle_deposit = findViewById(R.id.toggle_deposit);
        img_deposit = findViewById(R.id.img_deposit);
        img_stripe = findViewById(R.id.img_stripe);

        imv_videothumnail.setOnClickListener(this);
        lyt_back.setOnClickListener(this);
        txv_post.setOnClickListener(this);
        txv_location. setOnClickListener(this);
        lyt_qualitfied_minus.setOnClickListener(this);
        lyt_insurance_minus.setOnClickListener(this);
        lyt_qualitfied_plus.setOnClickListener(this);
        lyt_insurance_plus.setOnClickListener(this);
        txv_minus.setOnClickListener(this);
        txv_plus.setOnClickListener(this);
        imv_back.setOnClickListener(this);
        txv_duration_minus.setOnClickListener(this);
        txv_duration_plus.setOnClickListener(this);
        img_deposit.setOnClickListener(this);
        img_stripe.setOnClickListener(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                isPosting= bundle.getInt("isPosting");
                editable= bundle.getBoolean("edit");
                String string = bundle.getString("newsFeedEntity");
                Gson gson = new Gson();
                newsFeedEntity = gson.fromJson(string, NewsFeedEntity.class);
                isDraft = bundle.getInt("draft");
            }
        }

        spiner_media_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                String item = String.valueOf(parent.getItemAtPosition(position));
                media_type = position+1;
                if(position==0){
                    lyt_image.setVisibility(View.VISIBLE);
                    lyt_video.setVisibility(View.GONE);

                }else{
                    lyt_image.setVisibility(View.GONE);
                    lyt_video.setVisibility(View.VISIBLE);
                }
            }
        });

        String str = "Submit for Approval in ";
        if(editable) str = "Update in ";
        txv_post.setText(str +  spiner_category_type.getSelectedItem().toString());
        String finalStr = str;
        spiner_category_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {

                String text = finalStr +  spiner_category_type.getSelectedItem().toString();
                SpannableString ss = new SpannableString(text);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                ss.setSpan(boldSpan, 0, finalStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                txv_post.setText(ss);
                changePostButton();
            }
        });
        toggle_deposit.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    lyt_deposit.setVisibility(View.VISIBLE);
                    is_deposit_required = 1;
                    changePostButton();
                }
                else {
                    lyt_deposit.setVisibility(View.GONE);
                    is_deposit_required =0;
                    changePostButton();
                }
            }
        });
        toggle_cash.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                cash = on;
                changePostButton();
            }
        });
        toggle_paypal.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                paypal = on;
                changePostButton();
            }
        });
        toggle_duration.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {

                if(on) {
                    lyt_duration.setVisibility(View.GONE);
                    duration = Double.valueOf(99);
                    initLayout();
                }
                else {
                    lyt_duration.setVisibility(View.VISIBLE);
                    duration = Double.valueOf(0.5);
                    initLayout();
                }
            }
        });

        toggle_quality.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
               if(on){
                   if(qualifications.size()==0){
                       showAlertDialog("You don't have any qualification to add");
                       toggle_quality.setToggleOff();
                       return;
                   }

                   selectInsuranceDialog(1);

               }else {
                    qualitfication_id = -1;
               }
                initLayout();
            }
        });
        toggle_insurance.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    if(qualifications.size()==0){
                        showAlertDialog("You don't have any insurance to add");
                        toggle_insurance.setToggleOff();
                        return;
                    }
                    selectInsuranceDialog(0);
                }else {
                    insurance_id = -1;
                }
                initLayout();
            }
        });

        Keyboard();
        for(int i =0;i<imageViews.size();i++){
            int finalI = i;
            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage(finalI);
                }
            });
        }
        txv_post.setTextColor(_context.getResources().getColor(R.color.white_transparent));
        txv_post.setEnabled(false);
        loadingQalification_Insurance();
        initLayout();
    }
    void initLayout(){
        if(isPosting == 1){
            lyt_post.setVisibility(View.VISIBLE);
            lyt_product.setVisibility(View.GONE);
        }else {
            lyt_post.setVisibility(View.GONE);
            lyt_product.setVisibility(View.VISIBLE);
        }
        Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        if(qualitfication_id ==-1 ){
            txv_quality.setVisibility(View.VISIBLE);
            lyt_qualitfied.setVisibility(View.GONE);
        }else {
            txv_quality.setVisibility(View.GONE);
            lyt_qualitfied.setVisibility(View.VISIBLE);
            txv_quality_name.setText(qualifications.get(qualitfication_id).getCompany() + " " + qualifications.get(qualitfication_id).getReference() );
            txv_quality_time.setText("Qualified Since " + qualifications.get(qualitfication_id).getExpiry());
        }
        if(insurance_id ==-1 ){
            txv_insurance.setVisibility(View.VISIBLE);
            lyt_insurance.setVisibility(View.GONE);
        }else {
            txv_insurance.setVisibility(View.GONE);
            lyt_insurance.setVisibility(View.VISIBLE);
            txv_insurance_name.setText(insuranceModels.get(insurance_id).getCompany() + " " + insuranceModels.get(insurance_id).getReference() );
            txv_insurance_time.setText("Expires\n" + insuranceModels.get(insurance_id).getExpiry());
        }
        txt_cancelday.setText(String.valueOf(candellation));
        txt_duration.setText(String.valueOf(duration));

        edt_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changePostButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changePostButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changePostButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txv_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changePostButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_deposit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changePostButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    void selectInsuranceDialog(int type){
        ArrayList<InsuranceModel>arrayList = insuranceModels;
        if(type == 1)arrayList = qualifications;
        SelectInsuranceDialog selectInsuranceDialog = new SelectInsuranceDialog();
        selectInsuranceDialog.setCancelable(false);
        selectInsuranceDialog.setOnActionClick(new SelectInsuranceDialog.OnActionListener() {
            @Override
            public void OnSelect(int posstion) {
                if(type ==0)
                    insurance_id = posstion;
                else
                    qualitfication_id = posstion;
                initLayout();
            }
        },arrayList);
        selectInsuranceDialog.show(getSupportFragmentManager(), "action picker");
    }

    void loadEdit(){
        if(editable) {
            completedValue.clear();
            for (int i = 0; i < newsFeedEntity.getPostImageModels().size(); i++) {
                if (Commons.mediaVideoType(newsFeedEntity.getPostImageModels().get(i).getPath())) {
                    videovalue = newsFeedEntity.getPostImageModels().get(i).getPath();
                } else {
                    completedValue.add(newsFeedEntity.getPostImageModels().get(i).getPath());
                }
            }
            media_type = newsFeedEntity.getMedia_type();
            spiner_media_type.setSelectedIndex(media_type-1);
            if (media_type == 1)
                reloadImages();
            else if (media_type == 2)
                reloadVideo();

            edt_title.setText(newsFeedEntity.getTitle());
            edt_description.setText(newsFeedEntity.getDescription());
            String[] array = getResources().getStringArray(R.array.category_type);
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(newsFeedEntity.getCategory_title())) {
                    spiner_category_type.setSelectedIndex(i);
                    break;
                }
            }
            edt_price.setText(newsFeedEntity.getPrice());
            edt_deposit.setText(newsFeedEntity.getDeposit());
            candellation = Integer.parseInt(newsFeedEntity.getCancellations());
            txv_location.setText(newsFeedEntity.getPost_location().split("\\|")[0]);
            location = newsFeedEntity.getPost_location();
            txt_duration.setText(newsFeedEntity.getDuration());
            if(Double.parseDouble(newsFeedEntity.getDeposit())>0)
                toggle_deposit.setToggleOn();
            if(newsFeedEntity.getQualifications().size()>0){
                toggle_quality.setToggleOn();
                for(int i =0;i<qualifications.size();i++)
                    if(newsFeedEntity.getQualifications().get(0).getId() == qualifications.get(i).getId()){
                        qualitfication_id = i;
                        break;
                    }
            }
            if(newsFeedEntity.getInsuranceModels().size()>0){
                toggle_insurance.setToggleOn();
                for(int i =0;i<insuranceModels.size();i++)
                    if(newsFeedEntity.getInsuranceModels().get(0).getId() == insuranceModels.get(i).getId()){
                        insurance_id = i;
                        break;
                    }
            }

            if(newsFeedEntity.getPayment_options()== 3){
                toggle_cash.setToggleOn();
                toggle_paypal.setToggleOn();
                cash = true; paypal =true;
            }else  if(newsFeedEntity.getPayment_options() ==2){
                paypal =true;
                toggle_paypal.setToggleOn();
            }else if(newsFeedEntity.getPayment_options() ==1) {
                cash = true;
                toggle_cash.setToggleOn();
            }
            if(newsFeedEntity.getDuration().equals("99")){
                toggle_duration.setToggleOn();
                lyt_duration.setVisibility(View.GONE);
                duration = Double.valueOf(99);
            }
            txv_post.setTextColor(_context.getResources().getColor(R.color.white));
            txv_post.setEnabled(true);
            initLayout();
        }
    }
    void loadingQalification_Insurance(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GETSERVICEFILES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("extra");
                            for(int i =0;i<jsonArray.length();i++){
                                InsuranceModel insuranceModel = new InsuranceModel();
                                insuranceModel.initModel(jsonArray.getJSONObject(i));

                                if(insuranceModel.getType()==0)
                                    insuranceModels.add(insuranceModel);
                                else
                                    qualifications.add(insuranceModel);
                                loadEdit();
                            }

                        }catch (Exception e){
                            Log.d("aaaaaa",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        //showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
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
                if(isDraft == 0){
                    if(completedValue.size()>0 || edt_title.getText().toString().length()>0 || edt_description.getText().toString().length()>0 || edt_price.getText().toString().length()>0){
                        DraftDialog draftDialog = new DraftDialog();
                        draftDialog.setOnConfirmListener(new DraftDialog.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                saveDraft = 98;
                                postService();
                            }

                            @Override
                            public void ondisCard() {
                                finish(NewServiceOfferActivity.this);
                            }
                        });
                        draftDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                    }else{
                        finish(this);
                    }
                }else{
                    finish(this);
                }
                break;
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.txv_post:
                postVerification();
                break;
            case R.id.imv_videothumnail:
                videovalue = "";
                selectVideo();
                break;
            case R.id.txv_location:
                Bundle bundle = new Bundle();
                bundle.putBoolean("flag", true);
                startActivityForResult(new Intent(this, SetPostRangeActivity.class).putExtra("data",bundle),1);
                overridePendingTransition(0, 0);
                break;
            case R.id.lyt_insurance_plus:
               selectInsuranceDialog(0);
                break;
            case R.id.lyt_qualitfied_plus:
                selectInsuranceDialog(1);
                break;
            case R.id.lyt_insurance_minus:
                toggle_insurance.setToggleOff();
                insurance_id = -1;
                initLayout();
                break;
            case R.id.lyt_qualitfied_minus:
                toggle_quality.setToggleOff();
                qualitfication_id = -1;
                initLayout();
                break;
            case R.id.txv_minus:
                if(candellation ==0) break;
                candellation--;
                initLayout();
                break;
            case R.id.txv_plus:
                if(candellation==30)break;
                candellation++;
                initLayout();
                break;
            case R.id.txv_duration_minus:
                if(duration ==0.5) break;
                duration =  duration- 0.5;
                initLayout();
                break;
            case R.id.txv_duration_plus:
                if(candellation==8)break;
                duration+= 0.5;
                initLayout();
                break;
            case R.id.img_deposit:
                ConfirmVariationDialog confirmBookingDialog = new ConfirmVariationDialog(1);
                confirmBookingDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                    }
                });
                confirmBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.img_stripe:
                confirmBookingDialog = new ConfirmVariationDialog(4);
                confirmBookingDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                    }
                });
                confirmBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
        }
    }

    void changePostButton(){
        Boolean status = true;
        if(media_type==1 && completedValue.size() ==0){
            status = false;
        }else if(media_type==2 && videovalue.length() ==0){
            status = false;
        }
        else if(edt_title.getText().toString().length()==0){
            status = false;
        }else if(edt_description.getText().toString().length()==0){
            status = false;
        }else if(edt_price.getText().toString().length() ==0){
            status = false;
        }else if(Integer.parseInt(edt_price.getText().toString())<20){
            status = false;
        }else if(is_deposit_required == 1 && Integer.parseInt(edt_deposit.getText().toString())<10){
            status = false;
        }else if(txv_location.getText().toString().length()==0){
            status = false;
        }else if(!cash && !paypal){
            status = false;
        }
        if(status){
            txv_post.setTextColor(_context.getResources().getColor(R.color.white));
            txv_post.setEnabled(true);
        }else{
            txv_post.setTextColor(_context.getResources().getColor(R.color.white_transparent));
            txv_post.setEnabled(false);
        }
    }

    void postVerification(){
        if(media_type==1 && completedValue.size() ==0){
            showAlertDialog("Please add image for your service");
            return;
        }else if(media_type==2 && videovalue.length() ==0){
            showAlertDialog("Please add video for your service");
            return;
        }
        else if(edt_title.getText().toString().length()==0){
            showAlertDialog("Please add the service title ");
            return;
        }else if(edt_description.getText().toString().length()==0){
            showAlertDialog("Please input the description");
            return;
        }else if(edt_price.getText().toString().length() ==0){
            showAlertDialog("Please input price.");
            return;
        }else if(Integer.parseInt(edt_price.getText().toString())<20){
            showAlertDialog("Minimum service price shold be £20");
            return;
        }else if(is_deposit_required == 1 && Integer.parseInt(edt_deposit.getText().toString())<10){
            showAlertDialog("Deposit amount need to start from £10");
            return;
        }else if(txv_location.getText().toString().length()==0){
            showAlertDialog("Please input the location");
            return;
        }else if(!cash && !paypal){
            showAlertDialog("Please input payment option.");
            return;
        }else if(is_deposit_required == 1 && !paypal){
            showAlertDialog("Please select stripe option.");
            return;
        }
//        if(paypal && Commons.g_user.getBt_paypal_account().equals("")){
//            GenralConfirmDialog confirmDialog = new GenralConfirmDialog();
//            confirmDialog.setOnConfirmListener(new GenralConfirmDialog.OnConfirmListener() {
//                @Override
//                public void onConfirm() {
//                    getPaymentToken();
//                }
//            },"Setup Paypal Account", "To be able to use the PayPal payment method and take payment for your item directly in the app you will need to add your PayPal.","Add Paypal", "Cancel");
//            confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
//        }else
//            postService();
        if(Commons.g_user.getStripe_connect_account().length()>0){

            retrieveCard();

        }
        else{
            GenralConfirmDialog confirmDialog = new GenralConfirmDialog();
            confirmDialog.setOnConfirmListener(new GenralConfirmDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    addCard();
                }
            },"Setup Payment card", "To be able to use the card payment method and take payment for your item directly in the app you will need to add your card.","Add Card", "Cancel");
            confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
        }
    }

    @Override
    public void successAddCard() {
        super.successAddCard();
        postService();
    }

    @Override
    public void InputCardDetail(String link) {
        super.InputCardDetail(link);
        Bundle bundle = new Bundle();
        bundle.putString("web_link",link);
        startActivityForResult(new Intent(this, ApprovePaymentActivity.class).putExtra("data",bundle),1);
        overridePendingTransition(0, 0);
    }
    @SuppressLint("SuspiciousIndentation")
    void postService(){

        showProgress();
        try {
            String API_LINK =API.ADD_SERVICE,imageTitle = "post_imgs";
            Map<String, String> params = new HashMap<>();
            if(editable) {
                API_LINK = API.UPDATE_SERVICE;
                params.put("id", String.valueOf(newsFeedEntity.getService_id()));
            }
            deposit_amount = 0.00f;
            if(edt_deposit.getText().toString().length()>0)
            deposit_amount = Float.parseFloat(edt_deposit.getText().toString());

            params.put("token", Commons.token);
            params.put("poster_profile_type", "1");
            params.put("media_type", String.valueOf(media_type));
            params.put("title", edt_title.getText().toString());
            params.put("description", edt_description.getText().toString());
            params.put("price", edt_price.getText().toString());
            params.put("is_deposit_required", String.valueOf(is_deposit_required));
            params.put("deposit_amount", String.valueOf(deposit_amount));
            params.put("cancellations", String.valueOf(candellation));
            params.put("category_title", spiner_category_type.getSelectedItem().toString());
            params.put("location_id", location);
            params.put("lat", String.valueOf(Commons.lat));
            params.put("lng", String.valueOf(Commons.lng));
            params.put("duration", String.valueOf(duration));
            if(insurance_id ==-1)
                params.put("insurance_id", "");
            else
                params.put("insurance_id", String.valueOf(insuranceModels.get(insurance_id).getId()));
            if(qualitfication_id ==-1)
                params.put("qualification_id", "");
            else
                params.put("qualification_id", String.valueOf(qualifications.get(insurance_id).getId()));
            // 0 - none selected, 1 - Cash on Colleciton, 2 - PayPal, 3 - both Cash and PayPal
            if(cash && paypal)
                params.put("payment_options", "3");
            else if(cash)
                params.put("payment_options", "1");
            else
                params.put("payment_options", "2");
            params.put("post_tags", "");
            params.put("brand", "");
            params.put("delivery_option", "0");
            params.put("delivery_cost", "0");
            params.put("item_title", "");
            params.put("post_condition", "");
            params.put("make_post", String.valueOf(isPosting));
            if(saveDraft == 98){
                params.put("is_active", "98");

            }else{
                if(isDraft == 1){
                    params.put("is_draft", "1");

                }
            }
            //File part
            ArrayList<File> post = new ArrayList<>();
            String post_image_uris = "";
            if(media_type ==1) {
                for (int i = 0; i < completedValue.size(); i++) {
                    if(URLUtil.isNetworkUrl(completedValue.get(i)))
                        post_image_uris += completedValue.get(i) + ",";
                    else {
                        post_image_uris  +="data" +",";
                        File file = new File(completedValue.get(i));
                        post.add(file);
                    }
                }
                post_image_uris = post_image_uris.substring(0,post_image_uris.length()-1);
            }else {
                if(URLUtil.isNetworkUrl(videovalue)){
                    post_image_uris = videovalue;
                }else {
                    post_image_uris = "data";
                    File file = new File(videovalue);
                    post.add(file);
                }
            }
            if(editable){
                params.put("post_img_uris", post_image_uris);
            }

            Map<String, String> mHeaderPart= new HashMap<>();
            mHeaderPart.put("Content-type", "multipart/form-data; boundary=<calculated when request is sent>");
            mHeaderPart.put("Accept", "application/json");

            Log.d("bbbbbbbbb", params.toString());

            CustomMultipartRequest mCustomRequest = new CustomMultipartRequest(Request.Method.POST, this, API_LINK, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    closeProgress();
                    try {
                        if(jsonObject.getBoolean("result")) {
                            ConfirmDialog confirmDialog = new ConfirmDialog();
                            String str = "ATB admin is currently reviewing your post, the review process can take up to 24 hours so please be patient.";
                            if(saveDraft == 98) str = "The post has been successfully saved as a draft, you can update it later";
                            confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    setResult(RESULT_OK);
                                    finish(NewServiceOfferActivity.this);
                                }
                            },str,"Thanks");
                            confirmDialog.show(getSupportFragmentManager(), "DeleteMessage");

                        }else {
                            showAlertDialog(jsonObject.getString("msg"));
                        }

                    }catch (Exception e){

                    }
                }
            }, new Response.ErrorListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    showToast("File upload failed");
                    closeProgress();
                }

            }, post, params, mHeaderPart,imageTitle);
            mCustomRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(mCustomRequest, API_LINK);

        } catch (Exception e) {

            e.printStackTrace();
            closeProgress();
            showAlertDialog("Photo Upload is failed . Please try again.");
        }
    }

    void selectVideo(){
        TedImagePicker.with(NewServiceOfferActivity.this)
                .mediaType(MediaType.VIDEO)
                .video()
                .title("Select Video")

                .errorListener(new OnErrorListener() {
                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Log.d("error======" , throwable.toString());
                    }
                })
                .start(new OnSelectedListener() {
                    @Override
                    public void onSelected(@NotNull Uri uri) {
                        videovalue = getRealVideoPathFromURI(uri);
                        reloadVideo();
                    }
                });
    }


    void selectImage(int posstion){
        SelectMediaDialog selectMediaActionDialog = new SelectMediaDialog();
        selectMediaActionDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
            @Override
            public void OnCamera() {
                if(completedValue.size()==maxImagecount)return;
                TedImagePicker.with(NewServiceOfferActivity.this)
                        .max(maxImagecount,"You can select only " + String.valueOf(maxImagecount-completedValue.size()) + " Images")
                        .startMultiImage(new OnMultiSelectedListener() {
                            @Override
                            public void onSelected(@NotNull List<? extends Uri> uriList) {

                                if(completedValue.size()>maxImagecount)return;
                                for(int i = 0 ; i <uriList.size() ; i ++){
                                    completedValue.add(  getRealPathFromURI(uriList.get(i).toString()));
                                }
                                reloadImages();
                            }
                        });
            }

            @Override
            public void OnAlbum() {
                if(completedValue.size()>posstion)
                    completedValue.remove(posstion);
                reloadImages();

            }
        },getResources().getString(R.string.what_wouldlike),getResources().getString(R.string.add_media),getResources().getString(R.string.remove_media));
        selectMediaActionDialog.show(getSupportFragmentManager(), "action picker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            successAddCard();
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if(completedValue.size()>maxImagecount)return;

            completedValue.addAll(returnValue);
            reloadImages();
        }else  if(resultCode == Activity.RESULT_OK && requestCode == 200) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            videovalue = returnValue.get(0);
            reloadVideo();
        }else if(resultCode == Commons.location_code){
            location = Commons.location;
            txv_location.setText(Commons.location.split("\\|")[0]);
            changePostButton();
        }else if (requestCode == REQUEST_PAYMENT_CODE) {
//            if (resultCode == RESULT_OK) {
//                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
//                Map<String, String> payment_params = new HashMap<>();
//                payment_params.put("token",Commons.token);
//                payment_params.put("paymentMethodNonce", Objects.requireNonNull(result.getPaymentMethodNonce()).getNonce());
//                payment_params.put("customerId",Commons.g_user.getBt_customer_id());
////                if(result.getPaymentMethodType().name().equals("PAYPAL")){
////                    payment_params.put("paymentMethod","Paypal");
////                }else {
////                    payment_params.put("paymentMethod","Card");
////                }
////                paymentProcessing(payment_params,0);
//
//                String deviceData = result.getDeviceData();
//                if (result.getPaymentMethodType() == PaymentMethodType.PAY_WITH_VENMO) {
//                    VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) result.getPaymentMethodNonce();
//                    String venmoUsername = venmoAccountNonce.getUsername();
//                }
//                retrievePayPal(payment_params);
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

    void retrievePayPal(Map<String, String> payment_params){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_PP_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                Commons.g_user.setBt_paypal_account(jsonObject.getString("msg"));
                                postService();
                            }else {
                                showAlertDialog(jsonObject.getString("msg"));
                            }
                        }catch (Exception e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        //showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return payment_params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void reloadImages(){
        imageViews.get(0).setImageResource(0);
        for(int i =1;i<imageViews.size();i++)imageViews.get(i).setImageResource(R.drawable.icon_image1);
        if(completedValue.size()>0)imv_imageicon.setVisibility(View.GONE);
        else imv_imageicon.setVisibility(View.VISIBLE);
        for(int i =0;i<completedValue.size();i++){
            Glide.with(this).load(completedValue.get(i)).placeholder(R.drawable.icon_image1).dontAnimate().into(imageViews.get(i));

        }
        changePostButton();
    }
    void reloadVideo(){
        //uploadThumbImage = Helper.getThumbnailPathForLocalFile(this, videovalue);

        Glide.with(this).load(videovalue).placeholder(R.drawable.image_thumnail).dontAnimate().into(imv_videothumnail);

        imv_videoicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_player));
        changePostButton();

    }

    void getPaymentToken(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_BRAINTREE_CLIENT_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("afafa",json);
                        try{
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                String clicent_token = jsonObject.getJSONObject("msg").getString("client_token");
                                String clicent_id = jsonObject.getJSONObject("msg").getString("customer_id");
                                Commons.g_user.setBt_customer_id(clicent_id);
//                                DropInRequest dropInRequest = new DropInRequest()
//                                        .clientToken(clicent_token)
//                                        .cardholderNameStatus(CardForm.FIELD_OPTIONAL)
//                                        .collectDeviceData(true)
//                                        .vaultManager(true);
//                                startActivityForResult(dropInRequest.getIntent(NewServiceOfferActivity.this), REQUEST_PAYMENT_CODE);
                            }else {
                                showAlertDialog("Server Connection Error!");

                            }
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        //showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

}