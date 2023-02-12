package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.adapter.MainFeedAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.TransactionEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SavePostActivity extends CommonActivity {
    ImageView imv_back;
    ListView list_main;
    ArrayList<NewsFeedEntity> newsFeedEntities = new ArrayList<>();
    MainFeedAdapter mainFeedAdapter;
    int type ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_post);
        imv_back = findViewById(R.id.imv_back);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(SavePostActivity.this);
            }
        });
        list_main = findViewById(R.id.list_main);
        mainFeedAdapter = new MainFeedAdapter(SavePostActivity.this);
        list_main.setAdapter(mainFeedAdapter);
        initLayout();
    }

    void initLayout(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_USER_BOOKMARKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try{
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                for(int i =0;i<jsonArray.length();i++){
                                    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                                    JSONObject newsObject = jsonArray.getJSONObject(i);
                                    newsFeedEntity.initDetailModel(newsObject);
                                    newsFeedEntity.setProfile_name(newsFeedEntity.getUserModel().getUserName());
                                    newsFeedEntity.setProfile_image(newsFeedEntity.getUserModel().getImvUrl());
                                    if(newsFeedEntity.getPoster_profile_type()==1){
                                        newsFeedEntity.setProfile_name(newsFeedEntity.getUserModel().getBusinessModel().getBusiness_profile_name());
                                        newsFeedEntity.setProfile_image(newsFeedEntity.getUserModel().getBusinessModel().getBusiness_logo());
                                    }
                                    newsFeedEntities.add(newsFeedEntity);
                                }

                                mainFeedAdapter.setData(newsFeedEntities);

                            }
                        }catch (Exception e){
                            Log.d("Exception", e.toString());
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
                params.put("user_id",String.valueOf(Commons.g_user.getId()));
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
        type = Commons.selectUsertype;
        Commons.selectUsertype =-1;

    }

    @Override
    protected void onDestroy() {
        Commons.selectUsertype =type;
        Log.d("aaa",String.valueOf(type));
        super.onDestroy();
    }

}