package com.atb.app.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.other.ProfileRatingActivity;
import com.atb.app.adapter.ReviewAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.ReviewModel;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diffey.view.progressview.ProgressView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends CommonActivity {
    ArrayList<ProgressView>progressview = new ArrayList<>();
    ArrayList<TextView>textViews = new ArrayList<>();
    ImageView imv_back,imv_profile;
    SimpleRatingBar imv_star ;
    TextView txv_name,txv_id,txv_star,txv_count,txv_rate;
    ListView list_review;
    UserModel userModel = new UserModel();
    ArrayList<ReviewModel>reviewModels = new ArrayList<>();
    ReviewAdapter reviewAdapter;
    Boolean editable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        imv_back = findViewById(R.id.imv_back);
        imv_profile = findViewById(R.id.imv_profile);
        imv_star = findViewById(R.id.imv_star);
        txv_name = findViewById(R.id.txv_name);
        txv_id = findViewById(R.id.txv_id);
        txv_rate = findViewById(R.id.txv_rate);
        txv_star = findViewById(R.id.txv_star);
        txv_count = findViewById(R.id.txv_count);
        list_review = findViewById(R.id.list_review);
        progressview.add(findViewById(R.id.progressview1));
        progressview.add(findViewById(R.id.progressview2));
        progressview.add(findViewById(R.id.progressview3));
        progressview.add(findViewById(R.id.progressview4));
        progressview.add(findViewById(R.id.progressview5));
        textViews.add(findViewById(R.id.txv_star1));
        textViews.add(findViewById(R.id.txv_star2));
        textViews.add(findViewById(R.id.txv_star3));
        textViews.add(findViewById(R.id.txv_star4));
        textViews.add(findViewById(R.id.txv_star5));
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(ReviewActivity.this);
            }
        });


        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                String user= bundle.getString("userModel");
                editable= bundle.getBoolean("editable");
                Gson gson = new Gson();
                userModel = gson.fromJson(user, UserModel.class);
            }
        }

        txv_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String usermodel = gson.toJson(userModel);
                Bundle bundle = new Bundle();
                bundle.putString("userModel",usermodel);
                goTo(ReviewActivity.this, ProfileRatingActivity.class,false,bundle);
            }
        });

        reviewAdapter = new ReviewAdapter(this);
        list_review.setAdapter(reviewAdapter);


    }
    void initLayout(){
        if(Commons.g_user.getId() == userModel.getId())
            txv_rate.setVisibility(View.GONE);

        txv_name.setText(userModel.getBusinessModel().getBusiness_name());
        txv_id.setText(userModel.getBusinessModel().getBusiness_website());
        Glide.with(this).load(userModel.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#FFFFFF", Commons.glide_boder))).into(imv_profile);
        txv_count.setText(String.valueOf(reviewModels.size()) + " reviews");
        float rate = 0.0f;
        HashMap<Integer, Integer>hashMap = new HashMap<>();
        for(int i=0;i<5;i++)hashMap.put(i+1,0);
        for(int i =0;i<reviewModels.size();i++){
            rate = rate +  reviewModels.get(i).getRating();
            hashMap.put(reviewModels.get(i).getRating(),hashMap.get(reviewModels.get(i).getRating())+1);
        }
        if(reviewModels.size()>0) {
            rate = rate/reviewModels.size();
            txv_star.setText(String.valueOf(new DecimalFormat("##.#").format(rate)));
        }
        for(int i =0;i<5;i++){
            textViews.get(i).setText(String.valueOf(hashMap.get(i+1)));
            if(reviewModels.size()>0)
                progressview.get(i).setProgress(hashMap.get(i+1)*100/reviewModels.size());
            progressview.get(i).setProgressColor(Color.WHITE);
        }

        imv_star.setRating(rate/5.0f);
        canRateBusiness();
//        if(!editable) txv_rate.setVisibility(View.GONE);
    }

    void canRateBusiness(){
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.CANRATEBUSINESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getJSONObject("extra").getInt("can_rate") == 1){
                                txv_rate.setVisibility(View.VISIBLE);
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
                        showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("toUserId", String.valueOf(userModel.getId()));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void getRatings(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_BUSINESS_REVIEWS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        closeProgress();
                        reviewModels.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("msg");
                            for(int i =0;i<jsonArray.length();i++){
                                ReviewModel reviewModel = new ReviewModel();
                                reviewModel.initModel(jsonArray.getJSONObject(i));
                                reviewModels.add(reviewModel);
                            }
                            initLayout();
                            reviewAdapter.setRoomData(reviewModels);

                        }catch (Exception e){
                            Log.d("aaaaaa",e.toString());
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
                params.put("business_id", String.valueOf(userModel.getBusinessModel().getId()));
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
    protected void onResume() {
        super.onResume();
        getRatings();
    }
}