package com.atb.app.activities.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.atb.app.adapter.FollowerAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.FollowerModel;
import com.atb.app.model.UserModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FollowerAndFollowingActivity extends CommonActivity implements View.OnClickListener {
    boolean isFollower;
    int userType;
    ImageView imv_back,imv_profile;
    TextView txv_name,txv_id,txv_follower,txv_follower_description,txv_following,txv_following_description;
    LinearLayout lyt_follower,lyt_following;
    ListView list_follower;
    ArrayList<FollowerModel>followerModels = new ArrayList<>();
    ArrayList<FollowerModel>guser_followerModels = new ArrayList<>();
    FollowerAdapter followerAdapter;
    UserModel selected_user = new UserModel();
    int Fllowcount =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_and_following);
        imv_back = findViewById(R.id.imv_back);
        imv_profile = findViewById(R.id.imv_profile);
        txv_name = findViewById(R.id.txv_name);
        txv_id = findViewById(R.id.txv_id);
        txv_follower = findViewById(R.id.txv_follower);
        txv_follower_description = findViewById(R.id.txv_follower_description);
        txv_following = findViewById(R.id.txv_following);
        txv_following_description = findViewById(R.id.txv_following_description);
        lyt_follower = findViewById(R.id.lyt_follower);
        lyt_following = findViewById(R.id.lyt_following);
        list_follower = findViewById(R.id.list_follower);
        imv_back.setOnClickListener(this);
        lyt_follower.setOnClickListener(this);
        lyt_following.setOnClickListener(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                isFollower= bundle.getBoolean("isFollower");
                userType = bundle.getInt("userType");
                String user= bundle.getString("userModel");
                Gson gson = new Gson();
                selected_user = gson.fromJson(user, UserModel.class);
            }
        }
        followerAdapter = new FollowerAdapter(this,selected_user);
        list_follower.setAdapter(followerAdapter);

        list_follower.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(followerModels.get(position).getUserModel().getId()==Commons.g_user.getId()){
                    finish(FollowerAndFollowingActivity.this);
                }else {
                    int type = 0;
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String usermodel = gson.toJson(followerModels.get(position).getUserModel());
                    bundle.putString("userModel",usermodel);
                    if(!isFollower && followerModels.get(position).getUserModel().getAccount_type()==1)
                        type = 1;

                    bundle.putInt("userType",type);
                    goTo(FollowerAndFollowingActivity.this, OtherUserProfileActivity.class,true,bundle);
                }
            }
        });

        initLayout();
    }

    void initLayout() {
        if (userType == 1){
            Glide.with(this).load(selected_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().into(imv_profile);
            txv_name.setText(selected_user.getBusinessModel().getBusiness_name());
            txv_id.setText(selected_user.getBusinessModel().getBusiness_website());
        }else{
            Glide.with(this).load(selected_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().into(imv_profile);
            txv_name.setText(selected_user.getFirstname() + " " + selected_user.getLastname());
            txv_id.setText("@"+selected_user.getUserName());
        }
        txv_follower.setText(String.valueOf(selected_user.getFollowers_count()));
        txv_following.setText(String.valueOf(selected_user.getFollow_count()));
        if(isFollower){
            lyt_follower.setBackground(getResources().getDrawable(R.drawable.round_button_theme));
            txv_follower.setTextColor(getResources().getColor(R.color.white));
            txv_follower_description.setTextColor(getResources().getColor(R.color.white));
            lyt_following.setBackground(getResources().getDrawable(R.drawable.round_button1));
            txv_following.setTextColor(getResources().getColor(R.color.txt_color));
            txv_following_description.setTextColor(getResources().getColor(R.color.txt_color));
        }else {
            lyt_follower.setBackground(getResources().getDrawable(R.drawable.round_button1));
            txv_follower.setTextColor(getResources().getColor(R.color.txt_color));
            txv_follower_description.setTextColor(getResources().getColor(R.color.txt_color));
            lyt_following.setBackground(getResources().getDrawable(R.drawable.round_button_theme));
            txv_following.setTextColor(getResources().getColor(R.color.white));
            txv_following_description.setTextColor(getResources().getColor(R.color.white));
        }
        loadingFollowerDate(0);
    }

    public void unFollower(int followerid,int posstion,int type){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.DELETE_FOLLOWER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                Log.d("remove",json+ "   " + String.valueOf(type));
                                if(type ==0) {
                                    followerModels.remove(posstion);
                                }else {
                                    guser_followerModels.remove(posstion);
                                    Commons.g_user.setFollowerModels(guser_followerModels);
                                }
                                followerAdapter.setRoomData(followerModels, isFollower);
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
                if(type ==0 ) {
                    if (isFollower) {
                        params.put("follow_user_id", String.valueOf(followerid));
                        params.put("follower_user_id", String.valueOf(Commons.g_user.getId()));

                    } else {
                        params.put("follower_user_id", String.valueOf(followerid));
                        params.put("follow_user_id", String.valueOf(Commons.g_user.getId()));
                    }
                }else {
                    params.put("follower_user_id", String.valueOf(followerid));
                    params.put("follow_user_id", String.valueOf(Commons.g_user.getId()));
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
    public void addFollow(int followerid){
        String apilink = API.ADD_FOLLOW;
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apilink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        Log.d("add",json);
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                FollowerModel followerModel = new FollowerModel();
                                followerModel.setFollow_user_id(Commons.g_user.getId());
                                followerModel.setFollower_user_id(followerid);
                                guser_followerModels.add(followerModel);
                                Commons.g_user.setFollowerModels(guser_followerModels);
                                followerAdapter.setRoomData(followerModels, isFollower);
                            }
                        }catch (Exception e){
                            Log.d("Exception ",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("follow_user_id", String.valueOf(Commons.g_user.getId()));
                params.put("follower_user_id", String.valueOf(followerid));
                params.put("follow_business_id", "0");
                params.put("follower_business_id", "0");

                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void loadingFollowerDate(int type){
        String api_link = API.GET_FOLLOW;
        if(isFollower && type ==1)
            api_link = API.GET_FOLLOWER;
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                api_link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        try{
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                if(type ==1) {
                                    followerModels.clear();
                                    followerAdapter.setRoomData(followerModels, isFollower);
                                    if (jsonArray.length() == 0) {
                                        if (isFollower)
                                            showAlertDialog("You don't have any followers yet");
                                        else showAlertDialog("You are not following anyone yet");
                                        return;
                                    }
                                    Fllowcount = jsonArray.length();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        FollowerModel followerModel = new FollowerModel();
                                        followerModel.initModel(jsonArray.getJSONObject(i));
                                        getProfile(followerModel);
                                    }
                                }else {
                                    guser_followerModels.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        FollowerModel followerModel = new FollowerModel();
                                        followerModel.initModel(jsonArray.getJSONObject(i));
                                        guser_followerModels.add(followerModel);
                                    }
                                    Commons.g_user.setFollowerModels(guser_followerModels);
                                    loadingFollowerDate(1);
                                }

                            }else
                                showAlertDialog(jsonObject.getString("msg"));
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
                if(type == 1) {
                    if (isFollower) {
                        params.put("follower_user_id", String.valueOf(selected_user.getId()));
                        params.put("follower_business_id", "0");
                    } else {
                        params.put("follow_user_id", String.valueOf(selected_user.getId()));
                        params.put("follow_business_id", "0");
                    }
                }else {
                    params.put("follow_user_id", String.valueOf(Commons.g_user.getId()));
                    params.put("follow_business_id", "0");
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

    void getProfile(FollowerModel followerModel){

        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_PROFILE_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            UserModel userModel = new UserModel();
                            userModel.initModel(jsonObject.getJSONObject("msg").getJSONObject("profile"));
                            followerModel.setUserModel(userModel);
                            followerModels.add(followerModel);
                            Fllowcount --;
                            if(Fllowcount==0)
                                followerAdapter.setRoomData(followerModels,isFollower);
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
                if(isFollower)
                    params.put("user_id", String.valueOf(followerModel.getFollow_user_id()));
                else
                    params.put("user_id", String.valueOf(followerModel.getFollower_user_id()));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.lyt_follower:
                isFollower = true;
                initLayout();
                break;
            case R.id.lyt_following:
                isFollower = false;
                initLayout();
                break;
        }
    }
}