package com.atb.app.activities.navigationItems.booking;

import static com.atb.app.activities.navigationItems.ItemSoldActivity.SHOW_ADAPTER_POSITIONS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.adapter.MyBookingHeaderAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.BookingEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FutureBookingActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_profile;
    TextView txv_ok;
    RecyclerView recyclerView;
    ArrayList<BookingEntity> bookingEntities = new ArrayList<>();
    LinearLayout lyt_pastbooking;
    TextView txv_pastbooking;
    MyBookingHeaderAdapter myBookingHeaderAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_booking);

        imv_profile = findViewById(R.id.imv_profile);
        txv_ok = findViewById(R.id.txv_ok);
        txv_ok.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        txv_pastbooking = findViewById(R.id.txv_pastbooking);
        txv_pastbooking.setOnClickListener(this);
        lyt_pastbooking = findViewById(R.id.lyt_pastbooking);
        loadLayout();


        getBooking();
    }

    void loadLayout(){
        Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
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
    }
    void getBooking(){
        lyt_pastbooking.setVisibility(View.VISIBLE);
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_BOOKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            Log.d("aaaaaa",json);
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray arrayList = jsonObject.getJSONArray("extra");
                            bookingEntities.clear();
                            for(int i =0;i<arrayList.length();i++){
                                BookingEntity bookingEntity = new BookingEntity();
                                bookingEntity.initModel(arrayList.getJSONObject(i));
                                if(bookingEntity.getState().equals("cancelled") )continue;
                                bookingEntities.add(bookingEntity);

                            }
                            myBookingHeaderAdapter = new MyBookingHeaderAdapter(FutureBookingActivity.this,true, false, false, SHOW_ADAPTER_POSITIONS,bookingEntities,1);
                            recyclerView.setAdapter(myBookingHeaderAdapter);
                            myBookingHeaderAdapter.setHasStableIds(true);


                        }catch (Exception e){

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
                params.put("user_id", String.valueOf(Commons.g_user.getId()));
                params.put("is_business", "1");
                params.put("month", "");
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
            case R.id.txv_ok:
                finish(this);
                break;
            case R.id.txv_pastbooking:
                lyt_pastbooking.setVisibility(View.GONE);
                myBookingHeaderAdapter.viewPastBooking();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onResume();
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            getBooking();
        }
    }
}