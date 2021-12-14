package com.atb.app.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.listners.AlLoginHandler;
import com.applozic.mobicomkit.listners.AlPushNotificationHandler;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.BookingActivity;
import com.atb.app.activities.navigationItems.SetPostRangeActivity;
import com.atb.app.activities.navigationItems.TellYourFriendActivity;
import com.atb.app.activities.navigationItems.booking.MyBookingActivity;
import com.atb.app.activities.navigationItems.business.UpdateBusinessActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.activities.register.forgotPassword.ForgotPasswordActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.BusinessModel;
import com.atb.app.model.submodel.DisableSlotModel;
import com.atb.app.model.submodel.FeedInfoModel;
import com.atb.app.model.submodel.HolidayModel;
import com.atb.app.model.submodel.OpeningTimeModel;
import com.atb.app.model.submodel.SocialModel;
import com.atb.app.model.UserModel;
import com.atb.app.preference.PrefConst;
import com.atb.app.preference.Preference;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends CommonActivity implements View.OnClickListener {
    TextView txv_login,txv_signup;
    LinearLayout lyt_facebook;
    EditText  edt_email,edt_password;
    ImageView imv_forgot_password;
    CallbackManager callbackManager;
    private LoginManager loginManager;
    LoginButton loginButton;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getIntent() != null) {
            bundle = getIntent().getBundleExtra("data");
        }
        txv_login = findViewById(R.id.txv_login);
        txv_signup = findViewById(R.id.txv_signup);
        lyt_facebook = findViewById(R.id.lyt_facebook);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        txv_login.setOnClickListener(this);
        lyt_facebook.setOnClickListener(this);
        txv_signup.setOnClickListener(this);
        imv_forgot_password = findViewById(R.id.imv_forgot_password);
        imv_forgot_password.setOnClickListener(this);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        List< String > permissionNeeds = Arrays.asList( "email");
        loginButton.setPermissions(permissionNeeds);
        loginButton.registerCallback(callbackManager,
                new FacebookCallback < LoginResult > () {@Override
                public void onSuccess(LoginResult loginResult) {

                    System.out.println("onSuccess");

                   Commons.fbtoken = "fb_"+ loginResult.getAccessToken()
                            .getUserId();
                    Log.i("accessToken", Commons.fbtoken);
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {@Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                Log.d("aaaaa",object.toString());
                                try {
                                    String id = object.getString("id");
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + id + "/picture?type=large");
                                        Log.i("profile_pic",
                                                profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                loginManager.getInstance().logOut();
                                gotoLogin(0);
                            }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields",
                            "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailInvalied(edt_email.getText().toString()) && edt_password.getText().toString().length()>5){
                    txv_login.setTextColor(_context.getResources().getColor(R.color.white));
                    txv_login.setEnabled(true);
                }else {
                    txv_login.setTextColor(_context.getResources().getColor(R.color.white_transparent));
                    txv_login.setEnabled(false);
                }            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailInvalied(edt_email.getText().toString()) && edt_password.getText().toString().length()>5){
                    txv_login.setTextColor(_context.getResources().getColor(R.color.white));
                    txv_login.setEnabled(true);
                }else {
                    txv_login.setTextColor(_context.getResources().getColor(R.color.white_transparent));
                    txv_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Keyboard();
        loadLayout();
    }

    void loadLayout(){
        edt_email.setText(Preference.getInstance().getValue(this, PrefConst.PREFKEY_USEREMAIL, ""));
        edt_password.setText(Preference.getInstance().getValue(this, PrefConst.PREFKEY_USERPWD, ""));
        int type = Preference.getInstance().getValue(this, PrefConst.PREFKEY_TYPE, 1);
        if(type == 1) {
            if (edt_email.getText().toString().length() > 0 && edt_password.getText().toString().length() > 0)
                gotoLogin(1);
        }else {
            Commons.fbtoken = Preference.getInstance().getValue(this, PrefConst.PREFKEY_FBTOKEN, "");
            gotoLogin(0);
        }
    }
    void Keyboard(){
        RelativeLayout lytContainer = (RelativeLayout) findViewById(R.id.lyt_container);
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
            case R.id.txv_login:

                gotoLogin(1);
                break;
            case R.id.lyt_facebook:
                loginButton.performClick();
                break;
            case R.id.txv_signup:
                goTo(this, Signup1Activity.class,false);
                break;
            case R.id.imv_forgot_password:
                goTo(this, ForgotPasswordActivity.class,false);
                break;
        }
    }

    void gotoLogin(int type){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.LOGIN_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        parseResponse(json,type);
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
                if(type == 1) {
                    params.put("email", edt_email.getText().toString());
                    params.put("pwd", edt_password.getText().toString());
                }else {
                    params.put("email", "");
                    params.put("pwd", "");
                }
                params.put("fbToken",Commons.fbtoken);
                params.put("fcmtoken",Commons.fcmtoken);

                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }

    void parseResponse(String json,int type){
        try {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.getBoolean("result")== false)
                showAlertDialog(jsonObject.getString("msg"));
            else {
                Commons.token = jsonObject.getString("msg");
                JSONObject userObject = jsonObject.getJSONObject("extra").getJSONObject("profile");
                UserModel userModel = new UserModel();
                userModel.setId(userObject.getInt("id"));
                userModel.setUserName(userObject.getString("user_name"));
                userModel.setComplete(userObject.getInt("complete"));
                userModel.setFb_user_id(userObject.getString("fb_user_id"));
                userModel.setFacebook_token(userObject.getString("facebook_token"));
                userModel.setEmail(userObject.getString("user_email"));
                userModel.setPassword(edt_password.getText().toString());
                userModel.setImvUrl(userObject.getString("pic_url"));
                userModel.setFirstname(userObject.getString("first_name"));
                userModel.setLastname(userObject.getString("last_name"));
                userModel.setLocation(userObject.getString("country"));
                userModel.setBirhtday(userObject.getString("birthday"));
                userModel.setSex(userObject.getString("gender"));
                userModel.setDescription(userObject.getString("description"));
                userModel.setPost_search_region(userObject.getString("post_search_region"));
                userModel.setAccount_type(userObject.getInt("account_type"));
                userModel.setStatus(userObject.getInt("status"));
                userModel.setStatus_reason(userObject.getString("status_reason"));
                userModel.setOnline(userObject.getInt("online"));
                userModel.setUpdate_at(userObject.getLong("updated_at"));
                userModel.setCreated_at(userObject.getLong("created_at"));
//                userModel.setLatitude(userObject.getDouble("latitude"));
//                userModel.setLongitude(userObject.getDouble("longitude"));
                userModel.setStripe_customer_token(userObject.getString("stripe_customer_token"));
                userModel.setStripe_connect_account(userObject.getString("stripe_connect_account"));
                userModel.setPush_tokenm(userObject.getString("push_token"));
                userModel.setInvitecode(userObject.getString("invite_code"));
                userModel.setInvited_by(userObject.getString("invited_by"));
                userModel.setPost_count(userObject.getInt("post_count"));
                userModel.setFollow_count(userObject.getInt("follow_count"));
                userModel.setFollowers_count(userObject.getInt("followers_count"));
                if(userObject.has("bt_customer_id")){
                    userModel.setBt_customer_id(userObject.getString("bt_customer_id"));
                    userModel.setBt_paypal_account(userObject.getString("bt_paypal_account"));

                }
                if(!jsonObject.getJSONObject("extra").getString("business_info").equals("null")){
                    JSONObject business_info = jsonObject.getJSONObject("extra").getJSONObject("business_info");
                    BusinessModel businessModel = new BusinessModel();
                    businessModel.setId(business_info.getInt("id"));
                    businessModel.setUser_id(business_info.getInt("user_id"));
                    businessModel.setBusiness_logo(business_info.getString("business_logo"));
                    businessModel.setBusiness_name(business_info.getString("business_name"));
                    businessModel.setBusiness_website(business_info.getString("business_website"));
                    businessModel.setBusiness_bio(business_info.getString("business_bio"));
                    businessModel.setBusiness_profile_name(business_info.getString("business_profile_name"));
                    businessModel.setPaid(business_info.getInt("paid"));
                    businessModel.setApproved(business_info.getInt("approved"));
                    businessModel.setApproval_reason(business_info.getString("approval_reason"));
                    businessModel.setUpdated_at(business_info.getLong("updated_at"));
                    businessModel.setCreated_at(business_info.getLong("created_at"));
                    JSONArray opening_times = business_info.getJSONArray("opening_times");
                    for(int i =0;i<opening_times.length();i++){
                        JSONObject opening_time = opening_times.getJSONObject(i);
                        OpeningTimeModel openingTimeModel = new OpeningTimeModel();
                        openingTimeModel.setId(opening_time.getInt("id"));
                        openingTimeModel.setUser_id(opening_time.getInt("user_id"));
                        openingTimeModel.setDay(opening_time.getInt("day"));
                        openingTimeModel.setIs_available(opening_time.getInt("is_available"));
                        openingTimeModel.setStart(opening_time.getString("start"));
                        openingTimeModel.setEnd(opening_time.getString("end"));
                        openingTimeModel.setUpdated_at(opening_time.getLong("updated_at"));
                        openingTimeModel.setCreated_at(opening_time.getLong("created_at"));
                        businessModel.getOpeningTimeModels().add(openingTimeModel);
                    }
                    JSONArray holidays = business_info.getJSONArray("holidays");
                    for(int i =0;i<holidays.length();i++){
                        JSONObject holiday = holidays.getJSONObject(i);
                        HolidayModel holidayModel = new HolidayModel();
                        holidayModel.setId(holiday.getInt("id"));
                        holidayModel.setUser_id(holiday.getInt("user_id"));
                        holidayModel.setName(holiday.getString("name"));
                        holidayModel.setDay_off(holiday.getLong("day_off"));
                        holidayModel.setUpdated_at(holiday.getLong("updated_at"));
                        holidayModel.setCreated_at(holiday.getLong("created_at"));
                        businessModel.getHolidayModels().add(holidayModel);
                    }
                    JSONArray disabled_slots = business_info.getJSONArray("disabled_slots");
                    for(int i =0;i<disabled_slots.length();i++){
                        JSONObject disable_slot = disabled_slots.getJSONObject(i);
                        DisableSlotModel disableSlotModel = new DisableSlotModel();
                        disableSlotModel.setId(disable_slot.getInt("id"));
                        disableSlotModel.setUser_id(disable_slot.getInt("user_id"));
                        disableSlotModel.setDay_timestamp(disable_slot.getLong("day_timestamp"));
                        disableSlotModel.setStart(disable_slot.getString("start"));
                        disableSlotModel.setEnd(disable_slot.getString("end"));
                        disableSlotModel.setCreated_at(disable_slot.getLong("created_at"));
                        disableSlotModel.setUpdated_at(disable_slot.getLong("updated_at"));
                        businessModel.getDisableSlotModels().add(disableSlotModel);
                    }
                    JSONArray socials = business_info.getJSONArray("socials");
                    for(int i =0;i<socials.length();i++){
                        JSONObject social = socials.getJSONObject(i);
                        SocialModel socialModel = new SocialModel();
                        socialModel.setId(social.getInt("id"));
                        socialModel.setUser_id(social.getInt("user_id"));
                        socialModel.setSocial_name(social.getString("social_name"));
                        socialModel.setType(social.getInt("type"));
                        socialModel.setCreated_at(social.getLong("created_at"));
                        businessModel.getSocialModels().add(socialModel);
                    }
                    businessModel.setPost_count(business_info.getInt("post_count"));
                    businessModel.setFollowers_count(business_info.getInt("followers_count"));
                    businessModel.setFollow_count(business_info.getInt("follow_count"));
                    userModel.setBusinessModel(businessModel);

                }

                if(!jsonObject.getJSONObject("extra").getString("setting_info").equals("null")){
                    JSONObject setting_info = jsonObject.getJSONObject("extra").getJSONObject("setting_info");
                }
                if(!jsonObject.getJSONObject("extra").getString("feed_info").equals("null")){
                    JSONArray feed_info = jsonObject.getJSONObject("extra").getJSONArray("feed_info");

                    for(int i =0;i<feed_info.length();i++){
                        JSONObject feed = feed_info.getJSONObject(i);
                        FeedInfoModel feedInfoModel = new FeedInfoModel();
                        feedInfoModel.setId(feed.getInt("id"));
                        feedInfoModel.setUser_id(feed.getInt("user_id"));
                        feedInfoModel.setDescription(feed.getString("description"));
                        userModel.getFeedInfoModels().add(feedInfoModel);
                    }
                }
                Commons.g_user = userModel;
                finishAffinity();
                Preference.getInstance().put(this, PrefConst.PREFKEY_TYPE, type);
                if(type == 1) {
                    Preference.getInstance().put(this, PrefConst.PREFKEY_USEREMAIL, edt_email.getText().toString());
                    Preference.getInstance().put(this, PrefConst.PREFKEY_USERPWD, edt_password.getText().toString());
                }else {
                    Preference.getInstance().put(this, PrefConst.PREFKEY_FBTOKEN, Commons.fbtoken);
                }

                if(Commons.g_user.getAccount_type() ==1)
                    loginPubNub(true);
                else
                    loginPubNub(false);

                goTo(LoginActivity.this, MainActivity.class,true,bundle);
                //goTo(LoginActivity.this, UpdateBusinessActivity.class,true);

            }
        }catch (Exception e){
            Log.d("bbbbb, " , e.toString());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}