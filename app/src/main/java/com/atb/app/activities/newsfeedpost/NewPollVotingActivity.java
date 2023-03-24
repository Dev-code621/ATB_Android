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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.business.BusinessProfilePaymentActivity;
import com.atb.app.activities.navigationItems.business.UpgradeBusinessSplashActivity;
import com.atb.app.adapter.PollEmageAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.util.CustomMultipartRequest;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class NewPollVotingActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back,lyt_addimage,lyt_imvselect;
    FrameLayout lyt_profile;
    RecyclerView recyclerView_images;
    EditText edt_question;
    ImageView imv_selector,imv_profile;
    NiceSpinner spiner_category_type;
    CardView card_business;
    TextView txv_post;
    ArrayList<EditText>editTexts = new ArrayList<>();
    ArrayList<ImageView>imageViews = new ArrayList<>();
    ArrayList<LinearLayout>linearLayouts = new ArrayList<>();
    ArrayList<LinearLayout>container_layout = new ArrayList<>();
    int viewcount = 2;
    ArrayList<String>completedValue = new ArrayList<>();
    ArrayList<String>returnValue = new ArrayList<>();
    PollEmageAdapter pollEmageAdapter;
    boolean business_user = false;
    TextView txv_duration;
    long upload_Second;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll_voting);
        lyt_back = findViewById(R.id.lyt_back);
        lyt_addimage = findViewById(R.id.lyt_addimage);
        lyt_profile = findViewById(R.id.lyt_profile);
        recyclerView_images = findViewById(R.id.recyclerView_images);
        edt_question = findViewById(R.id.edt_question);
        imv_selector = findViewById(R.id.imv_selector);
        txv_duration = findViewById(R.id.txv_duration);
        imv_selector.setEnabled(false);
        lyt_imvselect = findViewById(R.id.lyt_imvselect);
        spiner_category_type = findViewById(R.id.spiner_category_type);
        txv_post = findViewById(R.id.txv_post);
        editTexts.add(findViewById(R.id.edit_option1));
        editTexts.add(findViewById(R.id.edit_option2));
        editTexts.add(findViewById(R.id.edit_option3));
        editTexts.add(findViewById(R.id.edit_option4));
        editTexts.add(findViewById(R.id.edit_option5));
        linearLayouts.add(findViewById(R.id.lyt_option1));
        linearLayouts.add(findViewById(R.id.lyt_option2));
        linearLayouts.add(findViewById(R.id.lyt_option3));
        linearLayouts.add(findViewById(R.id.lyt_option4));
        linearLayouts.add(findViewById(R.id.lyt_option5));
        imageViews.add(findViewById(R.id.imv_optionselector1));
        imageViews.add(findViewById(R.id.imv_optionselector2));
        imageViews.add(findViewById(R.id.imv_optionselector3));
        imageViews.add(findViewById(R.id.imv_optionselector4));
        imageViews.add(findViewById(R.id.imv_optionselector5));
        container_layout.add(findViewById(R.id.lyt_container1));
        container_layout.add(findViewById(R.id.lyt_container2));
        container_layout.add(findViewById(R.id.lyt_container3));
        container_layout.add(findViewById(R.id.lyt_container4));
        container_layout.add(findViewById(R.id.lyt_container5));
        card_business = findViewById(R.id.card_business);
        imv_profile = findViewById(R.id.imv_profile);
        lyt_back.setOnClickListener(this);
        lyt_addimage.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        lyt_imvselect.setOnClickListener(this);
        txv_post.setOnClickListener(this);
        visibleLayout(viewcount);
        for(int i =0;i<5;i++){
            int finalI = i;
            linearLayouts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( finalI ==viewcount-1 && finalI!=4){
                        viewcount++;
                        visibleLayout(viewcount);

                    }
                    else{
                        editTexts.get(finalI).setText("");
                        if(viewcount>2){
                            viewcount--;
                            visibleLayout(viewcount);
                        }
                    }
                }
            });
            editTexts.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    visibleLayout(viewcount);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
        Keyboard();

        pollEmageAdapter = new PollEmageAdapter(this, new PollEmageAdapter.SelectListener() {
            @Override
            public void onClose(int posstion) {
                completedValue.remove(posstion);
                reloadImages();
            }

            @Override
            public void addImage() {
                selectImage();
            }
        });
        recyclerView_images.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView_images.setAdapter(pollEmageAdapter);
        txv_post.setText("Post in " +  spiner_category_type.getSelectedItem().toString());
        spiner_category_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String text ="Post in " +  spiner_category_type.getSelectedItem().toString();
                SpannableString ss = new SpannableString(text);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                ss.setSpan(boldSpan, 0, "Post in ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                txv_post.setText(ss);
            }
        });
        Calendar now = Calendar.getInstance();
        upload_Second = now.getTimeInMillis()/1000 + 24*3600 ;


        initLayout();
    }

    void initLayout(){
        for(int i =0;i<imageViews.size();i++){
            imageViews.get(i).setVisibility(View.VISIBLE);
            if(!business_user && i>3)imageViews.get(i).setVisibility(View.GONE);
        }
        if(business_user)
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        else
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        if(Commons.g_user.getAccount_type()==0) card_business.setVisibility(View.GONE);

    }

    @Override
    public boolean selectProfile(boolean flag){
        if(Commons.g_user.getBusinessModel().getPaid()==0){
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    Bundle bundle = new Bundle();
                    bundle.putInt("subScriptionType",2);
                    startActivityForResult(new Intent(NewPollVotingActivity.this, BusinessProfilePaymentActivity.class).putExtra("data",bundle),1);
                    overridePendingTransition(0, 0);
                }
            },getString(R.string.subscription_alert));
            confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
        }else {
            business_user = flag;
            initLayout();
        }
        return flag;
    }

    void visibleLayout(int n){
        for(int i =0;i<5;i++){
            if(i<n)container_layout.get(i).setVisibility(View.VISIBLE);
            else {
                container_layout.get(i).setVisibility(View.GONE);
            }
            if(editTexts.get(i).getText().length()>0) imageViews.get(i).setVisibility(View.VISIBLE);
            else imageViews.get(i).setVisibility(View.GONE);
            if(i==n-1 && i<4){
                imageViews.get(i).setEnabled(false);
            }else {
                imageViews.get(i).setEnabled(true);
            }

        }
    }

    void Keyboard(){
        LinearLayout lytContainer =  findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_question.getWindowToken(), 0);
                return false;
            }
        });
    }

    void postPoll(){
        if(edt_question.getText().toString().length()==0){
            showAlertDialog("Please input the question");
            return;
        }
//        else if(!imv_selector.isEnabled()){
//            showAlertDialog("Please confirm the poll duration");
//            return;
//        }
        int count =0;
        for(int i =0;i<viewcount;i++){
            if(editTexts.get(i).getText().toString().length()>0)count++;
            if(editTexts.get(i).getText().toString().length()==0)continue;
            for(int j=i+1;j<viewcount;j++){
                if(editTexts.get(i).getText().toString().equals(editTexts.get(j).getText().toString())){
                    showAlertDialog("Options should be unique");
                    return;
                }

            }
        }
        if(count<2){
            showAlertDialog("A poll needs to have two options at least");
            return;
        }
        showProgress();
        try {
            Map<String, String> params = new HashMap<>();
            params.put("token", Commons.token);
            params.put("type", "4");
            if(completedValue.size()>0)
                params.put("media_type", "1");
            else
                params.put("media_type", "0");
            if(business_user)
                params.put("profile_type", "1");
            else
                params.put("profile_type", "0");
            params.put("title", edt_question.getText().toString());
            params.put("description", "");
            params.put("brand", "");
            params.put("price", "0.00");
            params.put("category_title", spiner_category_type.getSelectedItem().toString());
            params.put("item_title", "");
            params.put("payment_options", "0");
            params.put("location_id", "0");
            params.put("delivery_option", "0");
            params.put("delivery_cost", "0");
            params.put("poll_day", String.valueOf(upload_Second));
            String poll_options ="";
            for(int i =0;i<viewcount;i++){
                if(editTexts.get(i).getText().toString().length()>0){
                    poll_options = poll_options +editTexts.get(i).getText().toString() +  "|";
                }
            }
            params.put("poll_options",poll_options.substring(0,poll_options.length()-1));
            //File part
            ArrayList<File> post = new ArrayList<>();
            for (int i = 0; i < completedValue.size(); i++) {
                File file = new File(completedValue.get(i));
                post.add(file);
            }
            String API_LINK = API.CREATE_POST_API;
            String imageTitle = "post_imgs";

            Map<String, String> mHeaderPart= new HashMap<>();
            mHeaderPart.put("Content-type", "multipart/form-data; boundary=<calculated when request is sent>");
            mHeaderPart.put("Accept", "application/json");
            Log.d("aaa",params.toString());
            CustomMultipartRequest mCustomRequest = new CustomMultipartRequest(Request.Method.POST, this, API_LINK, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    closeProgress();
                    try {
                        if(jsonObject.getBoolean("result")) {
                            setResult(RESULT_OK);
                            finish(NewPollVotingActivity.this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.lyt_imvselect:
                selectDate();
                break;
            case R.id.lyt_profile:
                if(Commons.g_user.getAccount_type()==1)
                    SelectprofileDialog(this);
                break;
            case R.id.txv_post:
                postPoll();
                break;
            case R.id.lyt_addimage:
                selectImage();
                break;
        }
    }

    void selectDate(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                        TimePickerDialog time = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                        DateFormat formart = new SimpleDateFormat("hh:mm a");
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year,monthOfYear,dayOfMonth,hourOfDay,minute);
                                        long distance =  (calendar.getTimeInMillis() - now.getTimeInMillis())/1000;

                                        txv_duration.setText(String.valueOf(distance/(24*3600)) + " days " + String.valueOf((distance%(24*3600))/3600) + "hours");
                                        upload_Second = calendar.getTimeInMillis()/1000;



                                        //txv_starttime.get(finalI).setText(formart.format(calendar.getTime()));
                                    }
                                },
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                false
                        );
                        time.setTitle("Please choice time");
                        time.show(getSupportFragmentManager(), "Timepickerdialog");
                    }
                },
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");


    }


    void selectImage(){
        if(completedValue.size()==5)return;
        TedImagePicker.with(NewPollVotingActivity.this)
                .max(5,"You can select only " + String.valueOf(5-completedValue.size()) + " Images")
                .startMultiImage(new OnMultiSelectedListener() {
                    @Override
                    public void onSelected(@NotNull List<? extends Uri> uriList) {

                        if(completedValue.size()>5)return;
                        for(int i = 0 ; i <uriList.size() ; i ++){
                            completedValue.add(  getRealPathFromURI(uriList.get(i).toString()));
                        }
                        reloadImages();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            completedValue.addAll(returnValue);
            reloadImages();
        }else if(resultCode == Commons.subscription_code){
            business_user = true;
            initLayout();
        }
    }

    void reloadImages(){
        if(completedValue.size()>0){
            lyt_addimage.setVisibility(View.GONE);
            recyclerView_images.setVisibility(View.VISIBLE);
            pollEmageAdapter.setData(completedValue);
        }else {
            lyt_addimage.setVisibility(View.VISIBLE);
            recyclerView_images.setVisibility(View.GONE);
        }
    }
}