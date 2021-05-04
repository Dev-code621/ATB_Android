package com.atb.app.activities.profile.boost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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
import com.atb.app.activities.newsfeedpost.NewSalePostActivity;
import com.atb.app.activities.profile.SearchActivity;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.atb.app.activities.navigationItems.ItemSoldActivity.SHOW_ADAPTER_POSITIONS;

public class PinPointActivity extends CommonActivity implements View.OnClickListener {
    NiceSpinner spiner_category_type;
    RecyclerView recyclerView;
    TextView txv_day,txv_hour,txv_min,txv_second;
    LinearLayout lyt_save;
    ImageView imv_back;
    ProfilePinHeaderAdapter soldHeaderAdapter ;
    EditText edt_tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_point);
        spiner_category_type = findViewById(R.id.spiner_category_type);
        recyclerView = findViewById(R.id.recyclerView);
        txv_day = findViewById(R.id.txv_day);
        txv_hour = findViewById(R.id.txv_hour);
        txv_min = findViewById(R.id.txv_min);
        txv_second = findViewById(R.id.txv_second);
        lyt_save = findViewById(R.id.lyt_save);
        imv_back = findViewById(R.id.imv_back);
        edt_tag = findViewById(R.id.edt_tag);
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

        soldHeaderAdapter = new ProfilePinHeaderAdapter(1, 5, true, false, false, SHOW_ADAPTER_POSITIONS,1,this);
        recyclerView.setAdapter(soldHeaderAdapter);


       loadDate();


        spiner_category_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                loadDate();
            }
        });

        edt_tag.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    loadDate();
                    return true;
                }
                return false;
            }
        });

    }
    void loadDate(){
        Map<String, String> params = new HashMap<>();
        params.put("token", Commons.token);
        params.put("type","1");
        params.put("category",spiner_category_type.getSelectedItem().toString());
        params.put("tags","");
        getAuctions(params);
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
    public void getBideers(ArrayList<BoostModel> boostModels) {
        soldHeaderAdapter.setRoomData(boostModels);
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
                               // showAlertDialog(jsonObject.getString("msg"));
                                loadDate();
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
                params.put("type","1");
                params.put("category",spiner_category_type.getSelectedItem().toString());
                params.put("tags",edt_tag.getText().toString());
                params.put("position",String.valueOf(posstion));
                params.put("price",price);
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