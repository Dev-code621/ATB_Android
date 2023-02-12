package com.atb.app.activities.navigationItems;

import static com.atb.app.activities.navigationItems.ItemSoldActivity.SHOW_ADAPTER_POSITIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.atb.app.activities.navigationItems.business.BusinessProfilePaymentActivity;
import com.atb.app.activities.newsfeedpost.EditSalesPostActivity;
import com.atb.app.activities.newsfeedpost.NewAdviceActivity;
import com.atb.app.activities.newsfeedpost.NewServiceOfferActivity;
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.adapter.DraftPostAdapter;
import com.atb.app.adapter.NotificationAdapter;
import com.atb.app.adapter.SoldHeaderAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.TransactionEntity;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DraftPostActivity extends CommonActivity {
    ImageView imv_profile;
    TextView txv_ok;
    ListView recyclerView;
    int business_user = Commons.selectUsertype;
    DraftPostAdapter draftPostAdapter;
    ArrayList<NewsFeedEntity> newsFeedEntities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_post);
        imv_profile = findViewById(R.id.imv_profile);
        txv_ok = findViewById(R.id.txv_ok);
        recyclerView = findViewById(R.id.recyclerView);
        draftPostAdapter = new DraftPostAdapter(this);
        recyclerView.setAdapter(draftPostAdapter);
        txv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(DraftPostActivity.this);
            }
        });
        FrameLayout lyt_profile = findViewById(R.id.lyt_profile);
        lyt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Commons.g_user.getAccount_type()==1)
                    SelectprofileDialog(DraftPostActivity.this);
            }
        });

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeedEntity newsFeedEntity = newsFeedEntities.get(position);
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String string = gson.toJson(newsFeedEntity);
                bundle.putBoolean("edit",true);
                bundle.putString("newsFeedEntity",string);
                bundle.putInt("draft", 1);
                if(newsFeedEntity.getPost_type()==2){
                    goTo(DraftPostActivity.this, EditSalesPostActivity.class,true,bundle);

                }else if(newsFeedEntity.getPost_type() ==3){
                    goTo(DraftPostActivity.this, NewServiceOfferActivity.class,true,bundle);

                }
            }
        });
        initLayout();
        loadDate();

    }


    void initLayout(){
        if (business_user == 1)
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        else
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
    }
    @Override
    public boolean selectProfile(boolean flag){
        if(flag)
            business_user = 1;
        else
            business_user = 0;
        initLayout();
        loadDate();
        return flag;
    }
    void loadDate(){
        newsFeedEntities.clear();
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_DRAFTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("aaaa,",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                JSONArray jsonArray = jsonObject.getJSONObject("extra").getJSONArray("items");
                                if(jsonArray.length()==0){
                                    showAlertDialog("You don't have any drafts yet");
                                    draftPostAdapter.setRoomData(newsFeedEntities);

                                    return;
                                }
                                for(int i =0;i<jsonArray.length();i++){
                                    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                                    newsFeedEntity.initModel(jsonArray.getJSONObject(i));
                                    newsFeedEntities.add(newsFeedEntity);
                                }
                                draftPostAdapter.setRoomData(newsFeedEntities);



                            }
                        }catch (Exception e){
                            Log.d("exception", e.toString());
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
                params.put("is_business", String.valueOf(business_user));

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