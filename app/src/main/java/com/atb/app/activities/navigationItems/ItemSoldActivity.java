package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.atb.app.adapter.SoldHeaderAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.TransactionEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemSoldActivity extends CommonActivity {
    FrameLayout lyt_profile;
    ImageView imv_profile;
    TextView txv_ok;
    RecyclerView recyclerView;
    public static final boolean SHOW_ADAPTER_POSITIONS = true;
    SoldHeaderAdapter soldHeaderAdapter ;
    CardView card_business;
    boolean business_user = false;
    ArrayList<TransactionEntity> transactionEntities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sold);
        lyt_profile = findViewById(R.id.lyt_profile);
        imv_profile = findViewById(R.id.imv_profile);
        txv_ok = findViewById(R.id.txv_ok);
        recyclerView = findViewById(R.id.recyclerView);

        txv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(ItemSoldActivity.this);
            }
        });

        lyt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectprofileDialog(ItemSoldActivity.this);
            }
        });
        initLayout();

        StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        recyclerView.setLayoutManager(stickyHeaderLayoutManager);
        // set a header position callback to set elevation on sticky headers, because why not
        stickyHeaderLayoutManager.setHeaderPositionChangedCallback(new StickyHeaderLayoutManager.HeaderPositionChangedCallback() {
            @Override
            public void onHeaderPositionChanged(int sectionIndex, View header, StickyHeaderLayoutManager.HeaderPosition oldPosition, StickyHeaderLayoutManager.HeaderPosition newPosition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
                    header.setElevation(elevated ? 8 : 0);
                }
            }
        });

        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                business_user= bundle.getBoolean("bussiness");
            }
        }


    }

    @Override
    public boolean selectProfile(boolean flag){
        business_user = flag;
        initLayout();
        return flag;
    }


    void initLayout(){
        if(business_user)
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        else
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        if(Commons.g_user.getAccount_type()==0) card_business.setVisibility(View.GONE);
        loadItemSold();
    }

    void loadItemSold(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_ITEMS_SOLD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();

                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                transactionEntities.clear();
                                for(int i =0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    TransactionEntity transactionEntity = new TransactionEntity();
                                    transactionEntity.setId(object.getInt("id"));
                                    transactionEntity.setUser_id(object.getInt("user_id"));
                                    transactionEntity.setIs_business(object.getInt("is_business"));
                                    transactionEntity.setTransaction_id(object.getString("transaction_id"));
                                    transactionEntity.setTransaction_type(object.getString("transaction_type"));
                                    transactionEntity.setTarget_id(object.getString("target_id"));
                                    transactionEntity.setAmount(object.getDouble("amount"));
                                    transactionEntity.setPayment_method(object.getString("payment_method"));
                                    transactionEntity.setPayment_source(object.getString("payment_source"));
                                    transactionEntity.setQuantity(object.getInt("quantity"));
                                    transactionEntity.setPurchase_type(object.getString("purchase_type"));
                                    if(!object.getString("delivery_option").equals("null"))
                                        transactionEntity.setDelivery_option(object.getInt("delivery_option"));
                                    transactionEntity.setCreated_at(object.getLong("created_at"));
                                    transactionEntity.setImv_url(object.getJSONArray("product").getJSONObject(0).getJSONArray("post_imgs").getJSONObject(0).getString("path"));
                                    transactionEntity.setTitle(object.getJSONArray("product").getJSONObject(0).getString("title"));
                                    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                                    newsFeedEntity.initModel(object.getJSONArray("product").getJSONObject(0));
                                        transactionEntity.setNewsFeedEntity(newsFeedEntity);
                                    transactionEntities.add(transactionEntity);
                                }
                                if(transactionEntities.size()>0){
                                    soldHeaderAdapter = new SoldHeaderAdapter(ItemSoldActivity.this,true, false, false, SHOW_ADAPTER_POSITIONS,transactionEntities);
                                    recyclerView.setAdapter(soldHeaderAdapter);
                                }
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
                        showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                if(business_user)
                    params.put("is_business", "1");
                else
                    params.put("is_business", "0");
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