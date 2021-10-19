package com.atb.app.activities.navigationItems.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.CancelBookingConfirmDialog;
import com.atb.app.dialog.ConfirmBookingDialog;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.RequestRatingDialog;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateBooking2Activity extends CommonActivity implements View.OnClickListener {
    ImageView imv_back,imv_image,imv_video,imv_profile,imv_close,check_email,check_phone,check_name;
    TextView txv_date,txv_time,txv_name,txv_price,txv_create,txv_username,txv_useremail,txv_exist;
    EditText edt_email,edt_name,edt_phone;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    LinearLayout lyt_exist,lyt_user,lyt_phone,lyt_name;
    UserModel userModel = new UserModel();
    int type = 0;
    int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking2);
        imv_back = findViewById(R.id.imv_back);
        imv_image = findViewById(R.id.imv_image);
        txv_date = findViewById(R.id.txv_date);
        txv_time = findViewById(R.id.txv_time);
        txv_name = findViewById(R.id.txv_name);
        txv_price = findViewById(R.id.txv_price);
        txv_create = findViewById(R.id.txv_create);
        edt_email = findViewById(R.id.edt_email);
        edt_name = findViewById(R.id.edt_name);
        edt_phone = findViewById(R.id.edt_phone);
        imv_video = findViewById(R.id.imv_video);
        imv_profile = findViewById(R.id.imv_profile);
        imv_close = findViewById(R.id.imv_close);
        txv_username = findViewById(R.id.txv_username);
        txv_useremail = findViewById(R.id.txv_useremail);
        txv_exist = findViewById(R.id.txv_exist);
        lyt_exist = findViewById(R.id.lyt_exist);
        lyt_user = findViewById(R.id.lyt_user);
        check_name = findViewById(R.id.check_name);
        check_email = findViewById(R.id.check_email);
        check_phone = findViewById(R.id.check_phone);
        lyt_name = findViewById(R.id.lyt_name);
        lyt_phone = findViewById(R.id.lyt_phone);

        lyt_user.setOnClickListener(this);
        imv_close.setOnClickListener(this);
        imv_back.setOnClickListener(this);
        txv_create.setOnClickListener(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                String serviceModel= bundle.getString("serviceModel");
                Gson gson = new Gson();
                time = bundle.getInt("time");
                newsFeedEntity = gson.fromJson(serviceModel, NewsFeedEntity.class);

            }
        }
        userModel.setId(-1);
        edt_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                userModel = new UserModel();
                userModel.setId(-1);
                if(emailInvalied(edt_email.getText().toString())) {
                    check_email.setVisibility(View.VISIBLE);
                    searchEmail();
                }
                else{
                    txv_exist.setText("Please enter a valied email address");
                    txv_exist.setTextColor(getResources().getColor(R.color.discard_color));
                    txv_exist.setVisibility(View.VISIBLE);
                    lyt_user.setVisibility(View.GONE);
                    lyt_exist.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lyt_exist.setVisibility(View.GONE);
                check_email.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(edt_name.getText().toString().length()>3)
                        check_name.setVisibility(View.VISIBLE);
                    else
                        check_name.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt_phone.getText().toString().length()>6)
                    check_phone.setVisibility(View.VISIBLE);
                else
                    check_phone.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Keyboard();
        loadLayout();

    }

    void searchEmail(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.SEARCH_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                JSONArray jsonArray = jsonObject.getJSONArray("extra");
                                userModel = new UserModel();
                                userModel.setId(-1);
                                if(jsonArray.length()>0){
                                    userModel.initModel(jsonArray.getJSONObject(0));
                                    txv_exist.setText(getResources().getString(R.string.exist_user));
                                    txv_exist.setTextColor(getResources().getColor(R.color.green));
                                    lyt_user.setVisibility(View.VISIBLE);
                                    lyt_exist.setVisibility(View.VISIBLE);
                                    Glide.with(_context).load(userModel.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                                            new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A6BFDE", Commons.glide_boder))).into(imv_profile);
                                    txv_useremail.setText(userModel.getEmail());
                                    txv_username.setText(userModel.getFirstname() + " " + userModel.getLastname());
                                }else {
                                    txv_exist.setText(getResources().getString(R.string.unexist_user));
                                    txv_exist.setTextColor(getResources().getColor(R.color.discard_color));
                                    lyt_user.setVisibility(View.GONE);
                                    lyt_exist.setVisibility(View.VISIBLE);
                                }
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
                params.put("email", edt_email.getText().toString().toLowerCase());
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }

    void loadLayout(){
        txv_date.setText(getDisplayDate(time));
        txv_time.setText(Commons.gettimeFromMilionSecond(time));
        Glide.with(_context).load(newsFeedEntity.getPostImageModels().get(0).getPath()).placeholder(R.drawable.profile_pic).into(imv_image);
        if(Commons.mediaVideoType(newsFeedEntity.getPostImageModels().get(0).getPath()))
            imv_video.setVisibility(View.VISIBLE);
        else
            imv_video.setVisibility(View.GONE);
        txv_name.setText(newsFeedEntity.getTitle());
        txv_price.setText("£" +newsFeedEntity.getPrice());
    }

    static String getDisplayDate(long milionsecond){
        String date = "";
        Date d = new Date(milionsecond*1000l);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMM");
        date = formatter.format(d);
        return date;
    }
    void Keyboard(){
        LinearLayout lytContainer = (LinearLayout) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_email.getWindowToken(), 0);
                return false;
            }
        });


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);

                break;
            case R.id.txv_create:
                createBooking();

                break;
            case R.id.lyt_user:
                edt_email.setVisibility(View.GONE);
                check_email.setVisibility(View.GONE);
                lyt_phone.setVisibility(View.GONE);
                lyt_name.setVisibility(View.GONE);
                txv_exist.setVisibility(View.GONE);
                type = 1;
                break;
            case R.id.imv_close:
                UserModel userModel = new UserModel();
                userModel.setId(-1);
                lyt_exist.setVisibility(View.GONE);
                lyt_phone.setVisibility(View.VISIBLE);
                lyt_name.setVisibility(View.VISIBLE);
                if(type ==1){
                    edt_email.setVisibility(View.VISIBLE);
                    check_email.setVisibility(View.GONE);
                    edt_email.setText("");
                }
                type = 0;
                break;
        }
    }

    void createBooking(){
        if(type==0){
            if(!emailInvalied(edt_email.getText().toString())){
                showAlertDialog("Please input valied email address");
                return;
            }else if(edt_name.getText().toString().length()<3){
                showAlertDialog("Please input name");
                return;
            }else if(edt_phone.getText().toString().length()<6){
                showAlertDialog("Please input phone number");
                return;
            }
        }
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.CREATE_BOOKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                ConfirmBookingDialog confirmBookingDialog = new ConfirmBookingDialog();
                                confirmBookingDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        setResult(RESULT_OK);
                                        finish(CreateBooking2Activity.this);
                                    }
                                });
                                confirmBookingDialog.show(getSupportFragmentManager(), "DeleteMessage");
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
                params.put("business_user_id", String.valueOf(Commons.g_user.getId()));
                params.put("service_id", String.valueOf(newsFeedEntity.getService_id()));
                params.put("booking_datetime", String.valueOf(time));
                params.put("is_reminder_enabled", "0");
                params.put("total_cost", newsFeedEntity.getPrice());
                if(userModel.getId()<0){
                    params.put("email",edt_email.getText().toString());
                    params.put("full_name", edt_name.getText().toString());
                    params.put("phone", edt_phone.getText().toString());
                }else {
                    params.put("user_id", String.valueOf(userModel.getId()));
                }
                Log.d("aaaaaa",params.toString());
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