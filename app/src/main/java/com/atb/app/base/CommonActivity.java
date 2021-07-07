package com.atb.app.base;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.transition.ChangeBounds;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

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
import com.applozic.mobicomkit.listners.AlLogoutHandler;
import com.applozic.mobicomkit.listners.AlPushNotificationHandler;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.newsfeedpost.NewAdviceActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.SelectProfileDialog;
import com.atb.app.model.BoostModel;
import com.atb.app.model.FollowerModel;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.UserModel;
import com.atb.app.preference.PrefConst;
import com.atb.app.preference.Preference;
import com.atb.app.util.CustomMultipartRequest;
import com.atb.app.view.zoom.ImageZoomButton;
import com.atb.app.view.zoom.ZoomAnimation;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.maps.model.LatLng;
import com.lky.toucheffectsmodule.factory.TouchEffectsFactory;
import com.opencsv.CSVReader;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils;


public abstract class CommonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        TouchEffectsFactory.initTouchEffects(this);
        super.onCreate(savedInstanceState);
        Commons.g_commentActivity = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public static String getCurrentTime(){
        Timestamp timestamp = new Timestamp(new Date().getTime());
        try {
            long currentTime = timestamp.getTime();
            Date df = new Date(currentTime);
            String timeStampString = new SimpleDateFormat("dd/MM/yyyy h:mm aa").format(df);
            return timeStampString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public Uri getUri(String filepath){
        File file = new File(filepath);
        if(file.exists()) {
            Uri imageUri = FileProvider.getUriForFile(
                    this,
                    "com.atb.app.fileprovider", //(use your app signature + ".provider" )
                    file);
            return imageUri;

        }
        return null;
    }

    public String getUTCDate(String date)  {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String time = format.format(d);
        return time;
    }

    public String getLocaltime(String date){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String localtime= new SimpleDateFormat("hh:mm a").format(d);
        return localtime;
    }


    public boolean emailInvalied(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public void activityAnimation(Scene anotherScene,int layout_id){
        TransitionSet set = new TransitionSet();
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(layout_id);
        set.addTransition(slide);
        set.addTransition(new ChangeBounds());
        set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        set.setDuration(350);
        TransitionManager.go(anotherScene, set);
    }

    public void SelectprofileDialog(Context context){
        SelectProfileDialog selectProfileDialog = new SelectProfileDialog();
        selectProfileDialog.OnSelectListener(new SelectProfileDialog.OnSelectListener() {
            @Override
            public void OnSelectProfile(boolean flag) {
                selectProfile(flag);
            }
        });
        selectProfileDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
    }

    public boolean selectProfile(boolean flag){
        return flag;
    }

    private ZoomAnimation zoomAnimation;
    private boolean isZoom=false;
    public void showPicture(View view, String url) {
        zoomAnimation = new ZoomAnimation(this);
        zoomAnimation.zoom(view, url, 400, R.drawable.image_thumnail, this);
        isZoom = true;
    }

    public boolean isValidMakePost(){
        if(Commons.g_user.getBusinessModel().getApproved()==1)return  true;
        else{
            String title = "You didn't subscribe for your business account yet!\\nWould you like to subscribe now?";
            if(Commons.g_user.getBusinessModel().getPaid()==1){
                if(Commons.g_user.getBusinessModel().getApproved()==0)
                    title = "Your business account is currently pending for approval.\\nATB admin will review your account and update soon!";
                else
                    title = "Your business profile has been rejected!";
                showAlertDialog(title);
            }else {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {

                    }
                },title);
                confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
            }
        }
        return false;
    }
    protected void getPhoneSize(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Commons.phone_height = displayMetrics.heightPixels;
        Commons.phone_width = displayMetrics.widthPixels;
    }

    public void getuserProfile(int id,int userType){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_PROFILE_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            UserModel userModel = new UserModel();
                            userModel.initModel(jsonObject.getJSONObject("msg").getJSONObject("profile"));
                            getFollow(userModel,userType);

                        }catch (Exception e){
                            closeProgress();
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
                params.put("user_id", String.valueOf(id));

                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void getFollow(UserModel userModel,int userType){
        showProgress();

        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_FOLLOWER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                userModel.getFollowerModels().clear();
                                for(int i =0;i<jsonArray.length();i++){
                                    JSONObject follow = jsonArray.getJSONObject(i);
                                    FollowerModel followerModel = new FollowerModel();
                                    followerModel.initModel(follow);
                                    userModel.getFollowerModels().add(followerModel);

                                }
                                UserProfile(userModel,userType);
                            }
                        }catch (Exception e){
                            Log.d("Exception ",e.toString());
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
                params.put("follower_user_id", String.valueOf(userModel.getId()));
                params.put("follower_business_id", "0");
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    public void UserProfile(UserModel userModel,int usertype){

    }

    public void showToolTip(String str,View view, boolean imageType){

        final SimpleTooltip tooltip = new SimpleTooltip.Builder(this)
                .anchorView(view)
                .text(str)
                .gravity(Gravity.TOP)
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(true)
                .modal(true)
                .animated(true)
                .animationDuration(2000)
                .animationPadding(SimpleTooltipUtils.pxFromDp(1))
                .contentView(R.layout.tooltip_custom, R.id.tv_text)
                .build();
        final ImageView imv_image = tooltip.findViewById(R.id.imv_image);
        if(!imageType)
            imv_image.setVisibility(View.GONE);
        tooltip.show();

    }


    public void getPaymentToken(String price,NewsFeedEntity newsFeedEntity,int deliveryOption,  ArrayList<String> selected_Variation){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_BRAINTREE_CLIENT_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("afafa",json);
                        try{
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                String clicent_token = jsonObject.getJSONObject("msg").getString("client_token");
                                String clicent_id = jsonObject.getJSONObject("msg").getString("customer_id");
                                Commons.g_user.setBt_customer_id(clicent_id);
                                processPayment(price,clicent_id,clicent_token,newsFeedEntity,deliveryOption,selected_Variation);
                            }else {
                                showAlertDialog("Server Connection Error!");

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
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    public void paymentProcessing( Map<String, String> payment_params ,int type){
        String api_link = API.MAKE_PP_PAYMENT;
        if(type == 1){
            api_link = API.ADD_PP_SUB;
        }
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                api_link ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        Log.d("aaaaa",json);
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("msg");
                            if(!jsonObject.getBoolean("result"))
                                showAlertDialog(jsonObject.getString("msg"));
                            else {
                                String id ="";
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONArray(i).getJSONObject(0);
                                    if(object.getString("transaction_type").equals("Sale")) {
                                        id = object.getString("id");
                                        break;
                                    }

                                }
                                finishPayment(id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params  =   payment_params;
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    public void processPayment(String price, String clicent_id, String clicnet_token,NewsFeedEntity newsFeedEntity,int deliveryOption,ArrayList<String> selected_Variation){

    }

    public void finishPayment(String transaction_id){

    }


    public void loginApplozic(boolean flag){
        //showProgress();
        Applozic.logoutUser(this, new AlLogoutHandler(){
            @Override
            public void onSuccess(Context context) {
                loginChatsever(flag);
            }

            @Override
            public void onFailure(Exception exception) {
                loginChatsever(flag);
            }
        });
    }
    void loginChatsever(boolean flag){
        User user = new User();
        if(flag){
            user.setUserId(String.valueOf(Commons.g_user.getBusinessModel().getId()) +"_"+ String.valueOf(Commons.g_user.getId()));
            user.setImageLink(Commons.g_user.getBusinessModel().getBusiness_logo());
            user.setDisplayName(Commons.g_user.getBusinessModel().getBusiness_name());
            user.setPassword(String.valueOf(Commons.g_user.getBusinessModel().getId()) +"_"+ String.valueOf(Commons.g_user.getId()));
        }else {
            user.setUserId( String.valueOf(Commons.g_user.getId()));
            user.setImageLink(Commons.g_user.getImvUrl());
            user.setDisplayName(Commons.g_user.getUserName());
            user.setPassword(String.valueOf(Commons.g_user.getId()));
        }
        user.setEmail(Commons.g_user.getEmail());
        Applozic.loginUser(this, user, new AlLoginHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                Applozic.registerForPushNotification(CommonActivity.this, new AlPushNotificationHandler() {
                    @Override
                    public void onSuccess(RegistrationResponse registrationResponse) {

                    }

                    @Override
                    public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

                    }

                });
                login();
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                AlertDialog alertDialog = new AlertDialog.Builder(CommonActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(exception.toString());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Alert",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                if (!isFinishing()) {
                    alertDialog.show();
                }
            }
        });
    }

    public void login(){

    }

    public void gotochat(Context context, int userType,UserModel userModel){
        Intent intent = new Intent(this, ConversationActivity.class);
        String user_id = String.valueOf(userModel.getId());
        String display_name = userModel.getUserName();
        if(userType==1){
            user_id = String.valueOf(userModel.getBusinessModel().getId())+"_" + String.valueOf(userModel.getId());
            display_name = userModel.getBusinessModel().getBusiness_name();
        }

        intent.putExtra(ConversationUIService.USER_ID, user_id);
        intent.putExtra(ConversationUIService.DISPLAY_NAME, display_name); //put it for displaying the title.
        intent.putExtra(ConversationUIService.TAKE_ORDER,true); //Skip chat list for showing on back press
        startActivity(intent);
    }

    public void disableSlot(int time,boolean flag){

    }
    public void selectBooking(int posstion){

    }

    public void readCSV(){
        try {
            InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.uk_towns));

            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String county = line.split(",")[0];
                String region = line.split(",")[5];
                if(Commons.region.get(county) ==null){
                    Commons.region.put(county, new ArrayList<>());
                }
                LatLng latLng = new LatLng(Double.parseDouble(line.split(",")[2]),Double.parseDouble(line.split(",")[3]));
                Commons.LatLang.put(region + ", " + county +", United Kingdom" ,latLng);
                Commons.region.get(county).add(region);
                if(duplicateCounty(county))
                    continue;
                Commons.county.add(county);
            }
        } catch (IOException e) {
            Log.d("aaaaaaaa",e.toString());
        }
    }

    boolean duplicateCounty(String county){
        for(int i =0;i<Commons.county.size();i++){
            if(Commons.county.get(i).equals(county))return true;
        }
        return false;
    }

    public void getAuctions(Map<String, String> input){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.AUCTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("aaaaa",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray =jsonObject.getJSONArray("extra");
                            ArrayList<BoostModel>boostModels = new ArrayList<>();
                            for(int i =0;i<jsonArray.length();i++){
                                BoostModel boostModel = new BoostModel();
                                boostModel.initModel(jsonArray.getJSONObject(i));
                                boostModels.add(boostModel);
                            }
                            getBideers(boostModels);
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
                params = input;
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }
    public void getBideers(ArrayList<BoostModel>boostModels){

    }

    public void placeBid(int posstion, String price){

    }

    public void getAction(int county, int region){

    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(String pagename) {
        String FACEBOOK_URL = "https://www.facebook.com/" + pagename;

        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + pagename;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public Bitmap generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            //saveImage(bmp);
            pdfiumCore.closeDocument(pdfDocument); // important!
            return bmp;
        } catch(Exception e) {
            //todo with exception
            Log.d("Excetption====", e.toString());
        }
        return null;
    }
}
