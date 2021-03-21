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
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

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

        soldHeaderAdapter = new SoldHeaderAdapter(2, 5, true, false, false, SHOW_ADAPTER_POSITIONS);
        recyclerView.setAdapter(soldHeaderAdapter);

        initLayout();
    }

    @Override
    public boolean selectProfile(boolean flag){
        business_user = flag;
        initLayout();
        return flag;
    }


    void initLayout(){
        if(Commons.g_user.getAccount_type()==1)
            business_user = true;
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
                        Log.d("aaaa",json);
                        closeProgress();

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