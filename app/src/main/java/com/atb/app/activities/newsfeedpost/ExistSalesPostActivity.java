package com.atb.app.activities.newsfeedpost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;

import com.atb.app.adapter.PostFeedAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ExistSalesPostActivity extends CommonActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    int type = 0;
    LinearLayout lyt_back,lyt_selectall;
    TextView txv_title,txv_product_description,txv_post;
    FrameLayout lyt_profile;
    ListView list_content;
    ImageView imv_calender,imv_selector,imv_profile;
    PostFeedAdapter postFeedAdapter;
    CardView card_business;
    ArrayList<NewsFeedEntity>newsFeedEntities = new ArrayList<>();
    boolean business_user = false;
    int flag = 0;
    String group_id;
    long scheduledOn = 0;
    int postCount = 0;
    int year,month,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_sales_post);


        lyt_back = findViewById(R.id.lyt_back);
        lyt_selectall = findViewById(R.id.lyt_selectall);
        txv_title = findViewById(R.id.txv_title);
        txv_product_description = findViewById(R.id.txv_product_description);
        txv_post = findViewById(R.id.txv_post);
        lyt_profile = findViewById(R.id.lyt_profile);
        list_content = findViewById(R.id.list_content);
        imv_calender = findViewById(R.id.imv_calender);
        imv_selector = findViewById(R.id.imv_selector);
        card_business = findViewById(R.id.card_business);
        imv_profile = findViewById(R.id.imv_profile);

        imv_selector.setEnabled(false);
        lyt_back.setOnClickListener(this);
        lyt_selectall.setOnClickListener(this);
        txv_post.setOnClickListener(this);
        imv_calender.setOnClickListener(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                type= bundle.getInt("type");
            }
        }
        postFeedAdapter = new PostFeedAdapter(this);
        list_content.setAdapter(postFeedAdapter);

        list_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newsFeedEntities.get(position).setSelect(!newsFeedEntities.get(position).isSelect());
                postFeedAdapter.setData(newsFeedEntities);
            }
        });
        getUserPosts();
        initLayout();

    }

    void initLayout(){

        if(type == 3){
            txv_title.setText(getResources().getString(R.string.post_a_service));
            txv_product_description.setText(getResources().getString(R.string.all_service));
            imv_calender.setVisibility(View.VISIBLE);
            card_business.setVisibility(View.GONE);
            business_user = true;
        }else {
            lyt_profile.setOnClickListener(this);
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
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Calendar now = Calendar.getInstance();
        now.set(year,month,day,hourOfDay,minute,second);
        scheduledOn = now.getTimeInMillis()/1000L;
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        this.year = year; month= monthOfYear;day = dayOfMonth;
        Calendar now = Calendar.getInstance();
        TimePickerDialog time = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        time.setTitle("Please choice time");
        time.show(this.getSupportFragmentManager(), "Timepickerdialog");
    }

    void addCalender(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ExistSalesPostActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
// If you're calling this from a support Fragment
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.lyt_selectall:
                imv_selector.setEnabled(!imv_selector.isEnabled());
                for(int i =0;i<newsFeedEntities.size();i++){
                    newsFeedEntities.get(i).setSelect(imv_selector.isEnabled());
                    postFeedAdapter.setData(newsFeedEntities);
                }
                break;
            case R.id.lyt_profile:
                if(Commons.g_user.getAccount_type()==1)
                    SelectprofileDialog(this);

                break;
            case R.id.txv_post:
                multiServicePost();
                break;
            case R.id.imv_calender:
                addCalender();
                break;
        }
    }

    @Override
    public boolean selectProfile(boolean flag){
        business_user = flag;
        initLayout();

        return flag;
    }


    void multiServicePost(){
        flag = 0;postCount = 0;
        for(int i =0;i<newsFeedEntities.size();i++){
            if(newsFeedEntities.get(i).isSelect()){
                flag ++;
            }
        }
        if(flag==0){
            if(type==3)
                showAlertDialog("Please selet a service to post");
            else
                showAlertDialog("Please selet a product to post");
            return;
        }
        if(flag == 1) {
            for (int i = 0; i < newsFeedEntities.size(); i++) {
                if (newsFeedEntities.get(i).isSelect()) createPost(i);
            }
        }else {
            getGroupid();
        }

    }
    void getGroupid(){
        showProgress();
        String apiLink = API.GET_MULTI_GROUP_ID;
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apiLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            group_id = jsonObject.getString("msg");
                            for (int i = 0; i < newsFeedEntities.size(); i++) {
                                if (newsFeedEntities.get(i).isSelect()) createPost(i);
                            }
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        showToast(error.getMessage());

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
    void createPost(int posstion){
        NewsFeedEntity newsFeedEntity = newsFeedEntities.get(posstion);
        showProgress();
        String apiLink = API.CREATE_POST_API;
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apiLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                postCount++ ;
                                if(postCount == flag ){
                                    closeProgress();
                                    setResult(RESULT_OK);
                                    finish(ExistSalesPostActivity.this);
                                }
                            }else {
                                closeProgress();
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
                        showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("type", String.valueOf(type));
                params.put("media_type",String.valueOf(newsFeedEntity.getMedia_type()));
                if(business_user)
                    params.put("profile_type", "1");
                else
                    params.put("profile_type", "0");
                params.put("title", newsFeedEntity.getTitle());
                params.put("description", newsFeedEntity.getDescription());
                params.put("brand", newsFeedEntity.getBrand());
                params.put("price", newsFeedEntity.getPrice());
                params.put("category_title", newsFeedEntity.getCategory_title());
                params.put("post_condition", newsFeedEntity.getPost_condition());
                if(newsFeedEntity.getPost_tags().equals("null"))
                    params.put("post_tags", "");
                else
                    params.put("post_tags", newsFeedEntity.getPost_tags());
                params.put("item_title", newsFeedEntity.getItem_title());
                params.put("payment_options", String.valueOf(newsFeedEntity.getPayment_options()));
                params.put("location_id", newsFeedEntity.getLocation_id());
                params.put("delivery_option", String.valueOf(newsFeedEntity.getDelivery_option()));
                params.put("delivery_cost", newsFeedEntity.getDelivery_cost());
                params.put("deposit", newsFeedEntity.getDeposit());
                params.put("lat", String.valueOf(newsFeedEntity.getLat()));
                params.put("lng", String.valueOf(newsFeedEntity.getLng()));
                if(flag ==1)
                    params.put("is_multi", "0");
                else {
                    params.put("is_multi", "1");
                    params.put("multi_pos", String.valueOf(posstion));
                    params.put("multi_group", group_id);
                }
                if(scheduledOn>0)
                    params.put("scheduled", String.valueOf(scheduledOn));
                if(newsFeedEntity.getPostImageModels().size()>0) {
                    String url = newsFeedEntity.getPostImageModels().get(0).getPath();
                    for (int i = 1; i < newsFeedEntity.getPostImageModels().size(); i++) {
                        url = url + " ," + newsFeedEntity.getPostImageModels().get(i).getPath();
                    }
                    params.put("post_img_uris", url);

                }
                if(type ==2){
                    params.put("product_id", String.valueOf(newsFeedEntity.getId()));
                    params.put("stock_level", String.valueOf(newsFeedEntity.getStock_level()));
                }else {
                    params.put("is_deposit_required", newsFeedEntity.getIs_deposit_required());
                    params.put("cancellations", newsFeedEntity.getCancellations());
                    params.put("insurance_id", newsFeedEntity.getInsurance_id());
                    params.put("qualification_id", newsFeedEntity.getQualification_id());
                    params.put("service_id",String.valueOf(newsFeedEntity.getId()));
                }
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    void getUserPosts(){
        showProgress();
        String apiLink = API.GET_USER_SERVICES;
        if(type ==2 )apiLink = API.GET_USER_PRODUCTS;
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apiLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray;
                            if(type ==3)
                                jsonArray = jsonObject.getJSONArray("extra");
                            else {
                                jsonArray = jsonObject.getJSONArray("msg");
                            }
                            newsFeedEntities.clear();
                            for(int i =0;i<jsonArray.length();i++){
                                JSONObject jsonPost = jsonArray.getJSONObject(i);
                                NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                                newsFeedEntity.initModel(jsonPost);
                                newsFeedEntity.setPost_type(type);
                                if(newsFeedEntity.getIs_active() == 1)
                                    newsFeedEntities.add(newsFeedEntity);
                            }
                            postFeedAdapter.setData(newsFeedEntities);

                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("user_id", String.valueOf(Commons.g_user.getId()));
                if(type ==2){
                    if(business_user)
                      params.put("is_business", "1");
                    else
                        params.put("is_business", "0");
                }
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}