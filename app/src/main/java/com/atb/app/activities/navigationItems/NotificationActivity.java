package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.SplashActivity;
import com.atb.app.activities.navigationItems.booking.BookingViewActivity;
import com.atb.app.activities.navigationItems.booking.MyBookingViewActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ReviewActivity;
import com.atb.app.activities.profile.boost.PinPointActivity;
import com.atb.app.activities.profile.boost.ProfilePinActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.activities.register.forgotPassword.ForgotPasswordActivity;
import com.atb.app.adapter.NotificationAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.NotiEntity;
import com.atb.app.model.UserModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends CommonActivity{

    ImageView imv_back;
    ListView list_notification;
    NotificationAdapter notificationAdapter ;
    ArrayList<NotiEntity>notiEntities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        imv_back = findViewById(R.id.imv_back);
        list_notification = findViewById(R.id.list_notification);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(NotificationActivity.this);
            }
        });

        notificationAdapter = new NotificationAdapter(this);
        list_notification.setAdapter(notificationAdapter);
        loadNotification();

        list_notification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotiEntity notiEntity = notiEntities.get(position);
                if(notiEntity.getRead_status()==0)
                    readNotification(notiEntity);

                int noti_type = notiEntity.getType();
                int related_id = notiEntity.getRelated_id();
                if(noti_type == 1 || noti_type == 2 || noti_type ==3 ){
                    Bundle bundle = new Bundle();
                    bundle.putInt("postId",related_id);
                    bundle.putBoolean("CommentVisible",true);
                    startActivityForResult(new Intent(NotificationActivity.this, NewsDetailActivity.class).putExtra("data",bundle),1);
                }else if(noti_type ==4){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("bussiness",(Commons.g_user.getAccount_type()==1)? true : false);
                    goTo(NotificationActivity.this, ItemSoldActivity.class,false,bundle);
                }else if(noti_type ==6 || noti_type == 7 || noti_type ==8 || noti_type ==9){
                    getBookingByID(noti_type,related_id);
                }else if(noti_type ==10 ){
                    getuserProfile(related_id,1);
                }
                else if( noti_type ==13){
                    Gson gson = new Gson();
                    String usermodel = gson.toJson(Commons.g_user);
                    Bundle bundle = new Bundle();
                    bundle.putString("userModel",usermodel);
                    goTo(NotificationActivity.this,ReviewActivity.class,false,bundle);
                }else if(noti_type == 11 || noti_type == 18 || noti_type == 19 ){
                    //get service api
                    getProduct(noti_type, related_id);
                }else if(noti_type == 12){
                    goTo(NotificationActivity.this, TransactionHistoryActivity.class,false);
                }else if(noti_type ==14){

                }else if(noti_type ==15){
                    Bundle bundle = new Bundle();
                    bundle.putString("type", String.valueOf(30));
                    bundle.putString("related_id", String.valueOf(related_id));
                    goTo(NotificationActivity.this, SplashActivity.class,true,bundle);
                }else if(noti_type ==16) {
                    goTo(NotificationActivity.this, ProfileBusinessNaviagationActivity.class, false);
                }else if(noti_type ==17) {
                    getuserProfile(related_id,0);

                }else if( noti_type == 20  || noti_type == 21 || noti_type == 22){
                    //18,19: product and service id

                    Bundle bundle = new Bundle();
                    bundle.putInt("postId",related_id);
                    bundle.putBoolean("CommentVisible",true);
                    startActivityForResult(new Intent(NotificationActivity.this, NewsDetailActivity.class).putExtra("data",bundle),1);
                } else if(noti_type ==23) {
                    goTo(NotificationActivity.this, ProfileBusinessNaviagationActivity.class, false);
                }else if(noti_type ==24) {
                    goTo(NotificationActivity.this, ProfilePinActivity.class, false);
                }else if(noti_type ==25) {
                    goTo(NotificationActivity.this, PinPointActivity.class, false);
                }else if(noti_type ==26) {
                }else if(noti_type ==27) {
                    Commons.g_mainActivity.selectIcon = 1;
                    finish();
                }else if(noti_type ==28) {
                    goTo(NotificationActivity.this, ProfilePinActivity.class, false);
                }else if(noti_type ==29) {
                    goTo(NotificationActivity.this, PinPointActivity.class, false);
                }else if(noti_type ==30) {
                    goTo(NotificationActivity.this, ProfileBusinessNaviagationActivity.class, false);
                }
            }
        });
    }
    void getProduct(int noti_type,int related_id){

        String api_link =  API.GET_PRODUCT;
        if(noti_type == 11 || noti_type == 19)
            api_link =  API.GET_SERVICE;
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                api_link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                            newsFeedEntity.initDetailModel(jsonObject.getJSONObject("extra"));
                            Bundle bundle = new Bundle();
                            bundle.putInt("postId",related_id);
                            bundle.putBoolean("CommentVisible",false);
                            Gson gson = new Gson();
                            String usermodel = gson.toJson(newsFeedEntity);
                            bundle.putString("newfeedEntity",usermodel);
                            startActivityForResult(new Intent(NotificationActivity.this, NewsDetailActivity.class).putExtra("data",bundle),1);

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
                if(noti_type==18)
                    params.put("product_id", String.valueOf(related_id));
                else
                    params.put("service_id", String.valueOf(related_id));
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
    public void UserProfile(UserModel userModel, int usertype){
        Gson gson = new Gson();
        String usermodel = gson.toJson(userModel);
        Bundle bundle = new Bundle();
        bundle.putString("userModel",usermodel);
        goTo(this,ReviewActivity.class,false,bundle);
    }
    void getBookingByID(int noti_type,int related_id){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_INDIVIDUAL_BOOKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            BookingEntity bookingEntity = new BookingEntity();
                            JSONArray jsonArray = jsonObject.getJSONArray("extra");
                            if(jsonArray.length()>0)
                                bookingEntity.initModel(jsonArray.getJSONObject(0));
                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            String bookingModel = gson.toJson(bookingEntity);
                            bundle = new Bundle();
                            bundle.putString("bookModel",bookingModel);

                            if(noti_type ==9){
                                startActivityForResult(new Intent(NotificationActivity.this, MyBookingViewActivity.class).putExtra("data", bundle), 1);
                                overridePendingTransition(0, 0);
                            }else {
                                startActivityForResult(new Intent(NotificationActivity.this, BookingViewActivity.class).putExtra("data", bundle), 1);
                                overridePendingTransition(0, 0);
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
                params.put("booking_id", String.valueOf(related_id));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void loadNotification(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try{
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("msg");
                            for(int i =0;i<jsonArray.length();i++){
                                NotiEntity notiEntity = new NotiEntity();
                                notiEntity.initModel(jsonArray.getJSONObject(i));
                                notiEntities.add(notiEntity);
                            }
                            notificationAdapter.setRoomData(notiEntities);

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
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void readNotification(NotiEntity notiEntity){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.READ_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("aaaa",json);
                        try{


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
                params.put("notification_id", String.valueOf(notiEntity.getId()));
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