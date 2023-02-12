package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.adapter.SoldHeaderAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.TransactionEntity;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.atb.app.activities.navigationItems.ItemSoldActivity.SHOW_ADAPTER_POSITIONS;

public class PurchasesActivity extends CommonActivity {
    ImageView imv_profile;
    TextView txv_ok;
    SoldHeaderAdapter soldHeaderAdapter ;
    RecyclerView recyclerView;
    ArrayList<TransactionEntity> transactionEntities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);
        imv_profile = findViewById(R.id.imv_profile);
        txv_ok = findViewById(R.id.txv_ok);
        recyclerView = findViewById(R.id.recyclerView);

        txv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(PurchasesActivity.this);
            }
        });

        Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);

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
        loadLayout();
    }

    void loadLayout(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_PURCHASES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("aaaa,",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                if(jsonArray.length()==0){
                                    showAlertDialog("No any purchase yet!");
                                    return;
                                }
                                transactionEntities.clear();
                                for(int i =0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    TransactionEntity transactionEntity = new TransactionEntity();
                                    transactionEntity.setId(object.getInt("id"));
                                    transactionEntity.setUser_id(object.getInt("user_id"));
                                    transactionEntity.setIs_business(object.getInt("is_business"));
                                    transactionEntity.setTransaction_id(object.getString("transaction_id"));
                                    transactionEntity.setTarget_id(object.getString("target_id"));
                                    transactionEntity.setAmount(object.getDouble("amount")/100);
                                    transactionEntity.setPayment_method(object.getString("payment_method"));
                                    transactionEntity.setPayment_source(object.getString("payment_source"));
                                    transactionEntity.setQuantity(object.getInt("quantity"));
                                    transactionEntity.setPurchase_type(object.getString("purchase_type"));
                                    if(!object.getString("delivery_option").equals("null"))
                                        transactionEntity.setDelivery_option(object.getInt("delivery_option"));
                                    transactionEntity.setCreated_at(object.getLong("created_at"));
                                    transactionEntity.setImv_url(object.getJSONArray("product").getJSONObject(0).getJSONArray("post_imgs").getJSONObject(0).getString("path"));
                                    transactionEntity.setPoster_profile_type(object.getJSONArray("product").getJSONObject(0).getInt("poster_profile_type"));
                                    transactionEntity.setTitle(object.getJSONArray("product").getJSONObject(0).getString("title"));
                                    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                                    newsFeedEntity.initDetailModel(object.getJSONArray("product").getJSONObject(0));
                                    UserModel user = new UserModel();
                                    user.initModel(object.getJSONObject("user"));
                                    newsFeedEntity.setUserModel(user);
                                    newsFeedEntity.setPost_type(2);

                                    transactionEntity.setNewsFeedEntity(newsFeedEntity);

                                    if(object.has("user")){
                                        UserModel userModel = new UserModel();
                                        userModel.initModel(object.getJSONObject("user"));
                                        transactionEntity.setUserModel(userModel);
                                    }

                                    transactionEntities.add(transactionEntity);
                                }
                                if(transactionEntities.size()>0) {
                                    soldHeaderAdapter = new SoldHeaderAdapter(PurchasesActivity.this, true, false, false, SHOW_ADAPTER_POSITIONS, transactionEntities,"purchase");
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
                        //showToast(error.getMessage());

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

    @Override
    public void UserProfile(UserModel userModel, int usertype){
        Gson gson = new Gson();
        String usermodel = gson.toJson(userModel);
        Bundle bundle = new Bundle();
        bundle.putString("userModel",usermodel);
        bundle.putInt("userType",usertype);
        goTo(this, OtherUserProfileActivity.class,false,bundle);
    }
}