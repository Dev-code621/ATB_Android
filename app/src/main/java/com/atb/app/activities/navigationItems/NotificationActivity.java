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
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.activities.register.forgotPassword.ForgotPasswordActivity;
import com.atb.app.adapter.NotificationAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NotiEntity;

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
                switch (notiEntity.getType()) {
                    case 2:// comment
                        Bundle bundle = new Bundle();
                        bundle.putInt("postId",notiEntity.getRelated_id());
                        bundle.putBoolean("CommentVisible",true);
                        startActivityForResult(new Intent(NotificationActivity.this, NewsDetailActivity.class).putExtra("data",bundle),1);
                        break;
                    case 9://rating request

                        break;
                    case 10: //payment request
                        break;
                    case 6: // payment
                        bundle = new Bundle();
                        bundle.putBoolean("bussiness",true);
                        goTo(NotificationActivity.this, ItemSoldActivity.class,false,bundle);
                        break;
                    case 4: // booking

                        break;
                }
            }
        });
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