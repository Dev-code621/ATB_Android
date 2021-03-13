package com.atb.app.activities.newsfeedpost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.atb.app.adapter.PollEmageAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.util.CustomMultipartRequest;
import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;

import org.angmarch.views.NiceSpinner;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewPollVotingActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back,lyt_addimage,lyt_imvselect;
    FrameLayout lyt_profile;
    RecyclerView recyclerView_images;
    EditText edt_question,edt_duration;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll_voting);
        lyt_back = findViewById(R.id.lyt_back);
        lyt_addimage = findViewById(R.id.lyt_addimage);
        lyt_profile = findViewById(R.id.lyt_profile);
        recyclerView_images = findViewById(R.id.recyclerView_images);
        edt_question = findViewById(R.id.edt_question);
        edt_duration = findViewById(R.id.edt_duration);
        imv_selector = findViewById(R.id.imv_selector);
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

        initLayout();
    }

    void initLayout(){
        for(int i =0;i<imageViews.size();i++){
            imageViews.get(i).setVisibility(View.VISIBLE);
            if(!business_user && i>3)imageViews.get(i).setVisibility(View.GONE);
        }
        if(business_user)
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().into(imv_profile);
        else
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.icon_image1).dontAnimate().into(imv_profile);
        if(Commons.g_user.getAccount_type()==0) card_business.setVisibility(View.GONE);

    }

    @Override
    public boolean selectProfile(boolean flag){
        business_user = flag;
        initLayout();
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
        }else if(!imv_selector.isEnabled()){
            showAlertDialog("Please confirm the poll duration");
            return;
        }
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
            if(completedValue.size()>=0)
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
            params.put("poll_day", edt_duration.getText().toString());
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
            String API_LINK = API.CREATE_POST_API,imageTitle = "post_imgs";

            Map<String, String> mHeaderPart= new HashMap<>();
            mHeaderPart.put("Content-type", "multipart/form-data; boundary=<calculated when request is sent>");
            mHeaderPart.put("Accept", "application/json");

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
                imv_selector.setEnabled(!imv_selector.isEnabled());
                break;
            case R.id.lyt_profile:
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

    void selectImage(){
        Options options = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(5-completedValue.size())                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        Pix.start(NewPollVotingActivity.this, options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            completedValue.addAll(returnValue);
            reloadImages();
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