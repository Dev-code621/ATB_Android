package com.atb.app.activities.profile.boost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.Time;
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
import com.atb.app.adapter.ProfilePinHeaderAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.ConfirmVariationDialog;
import com.atb.app.model.BoostModel;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONObject;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.atb.app.activities.navigationItems.ItemSoldActivity.SHOW_ADAPTER_POSITIONS;

public class ProfilePinActivity extends CommonActivity implements View.OnClickListener {
    NiceSpinner spiner_category_type;
    RecyclerView recyclerView;
    TextView txv_day,txv_hour,txv_min,txv_second;
    LinearLayout lyt_save;
    ImageView imv_back;
    ProfilePinHeaderAdapter soldHeaderAdapter ;
    int county =0,region =0;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pin);
        spiner_category_type = findViewById(R.id.spiner_category_type);
        recyclerView = findViewById(R.id.recyclerView);
        txv_day = findViewById(R.id.txv_day);
        txv_hour = findViewById(R.id.txv_hour);
        txv_min = findViewById(R.id.txv_min);
        txv_second = findViewById(R.id.txv_second);
        lyt_save = findViewById(R.id.lyt_save);
        imv_back = findViewById(R.id.imv_back);

        imv_back.setOnClickListener(this);
        lyt_save.setOnClickListener(this);
        Time conferenceTime = new Time(Time.getCurrentTimezone());
        Calendar c=Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        c.add(Calendar.DATE,7);

        long confMillis = c.getTimeInMillis();

        Time nowTime = new Time(Time.getCurrentTimezone());
        nowTime.setToNow();
        nowTime.normalize(true);
        long nowMillis = nowTime.toMillis(true);

        long milliDiff = confMillis - nowMillis;
        new CountDownTimer(milliDiff, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
               int mDisplayDays = (int) ((millisUntilFinished / 1000) / 86400);
                int mDisplayHours = (int) (((millisUntilFinished / 1000) - (mDisplayDays * 86400)) / 3600);
                int mDisplayMinutes = (int) (((millisUntilFinished / 1000) - ((mDisplayDays * 86400) + (mDisplayHours * 3600))) / 60);
                int mDisplaySeconds = (int) ((millisUntilFinished / 1000) % 60);
                txv_day.setText(String.valueOf(mDisplayDays));
                String hour = String.valueOf(mDisplayHours);
                if(hour.length()==1)
                    hour = "0"+hour;
                txv_hour.setText(hour);
                String min = String.valueOf(mDisplayMinutes);
                if(min.length()==1)
                    min = "0" + min;
                txv_min.setText(min);
                String second = String.valueOf(mDisplaySeconds) ;
                if(second.length()==1)
                    second = "0"+second;
                txv_second.setText(second);
            }

            @Override
            public void onFinish() {
                //TODO: this is where you would launch a subsequent activity if you'd like.  I'm currently just setting the seconds to zero
            }
        }.start();

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

        soldHeaderAdapter = new ProfilePinHeaderAdapter(3, 2, true, false, false, SHOW_ADAPTER_POSITIONS,0,this);
       // soldHeaderAdapter.setHasStableIds(false);
        recyclerView.setAdapter(soldHeaderAdapter);

        getAction(county,region);
        spiner_category_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                getAction(county,region);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.lyt_save:
                finish(this);
                break;
        }
    }

    @Override
    public void getAction(int county, int region) {
        this.county = county;
        this.region = region;
        Map<String, String> params = new HashMap<>();
        params.put("token", Commons.token);
        params.put("type","0");
        params.put("category",spiner_category_type.getSelectedItem().toString());
        params.put("country","United Kingdom");
        params.put("county",Commons.county.get(county));
        params.put("region",Commons.region.get(Commons.county.get(county)).get(region));
        getAuctions(params);
    }

    @Override
    public void getBideers(ArrayList<BoostModel> boostModels) {
        ArrayList<BoostModel>arrayList = new ArrayList<>();
        for(int i =0;i<6;i++){
            BoostModel boostModel = new BoostModel();
            arrayList.add(boostModel);
        }
        for(int i =0;i<boostModels.size();i++){
            BoostModel boostModel = boostModels.get(i);
            switch (boostModel.getBidon()) {
                case 0:
                    if(boostModel.getPosition()==0){
                        arrayList.set(0,boostModel);
                    }else {
                        arrayList.set(1,boostModel);
                    }
                    break;
                case 1:
                    if(boostModel.getPosition()==0){
                        arrayList.set(2,boostModel);
                    }else {
                        arrayList.set(3,boostModel);
                    }
                    break;
                case 2:
                    if(boostModel.getPosition()==0){
                        arrayList.set(4,boostModel);
                    }else {
                        arrayList.set(5,boostModel);
                    }
                    break;
            }
        }
        soldHeaderAdapter.setRoomData(arrayList,county,region);
        if(k>0)
        showAlertDialog("Your bid has been placed successfully.");
        k++;
    }

    @Override
    public void placeBid(int posstion, String price) {
        ConfirmVariationDialog confirmBookingDialog = new ConfirmVariationDialog(3);
        confirmBookingDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                Bid(posstion, price);
            }
        });
        confirmBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");


    }

    void Bid(int posstion, String price){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.PLACEBID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                String approval_link = jsonObject.getJSONObject("extra").getString("approval_link");
                                Bundle bundle = new Bundle();
                                bundle.putString("web_link",approval_link);
                                startActivityForResult(new Intent(ProfilePinActivity.this, ApprovePaymentActivity.class).putExtra("data",bundle),1);
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
                        //showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("type","0");
                params.put("category",spiner_category_type.getSelectedItem().toString());
                params.put("position",String.valueOf(posstion%2));
                if(posstion/2==0)
                    params.put("country","United Kingdom");
                else if(posstion/2==1)
                    params.put("county",Commons.county.get(county));
                else
                    params.put("region",Commons.region.get(Commons.county.get(county)).get(region));
                params.put("price",price);
                Log.d("aaaaaaa",params.toString());
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onResume();
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            getAction(county,region);
        }else {
            showAlertDialog("Payment authorization has been cancelled!");
        }
    }
}