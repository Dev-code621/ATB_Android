package com.atb.app.base;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.atb.app.BuildConfig;
import com.atb.app.R;
import com.atb.app.activities.chat.ChatActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.GenralConfirmDialog;
import com.atb.app.dialog.SelectProfileDialog;
import com.atb.app.model.BoostModel;
import com.atb.app.model.BusinessModel;
import com.atb.app.model.FollowerModel;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.RoomModel;
import com.atb.app.model.UserModel;
import com.atb.app.model.submodel.DisableSlotModel;
import com.atb.app.model.submodel.FeedInfoModel;
import com.atb.app.model.submodel.HolidayModel;
import com.atb.app.model.submodel.OpeningTimeModel;
import com.atb.app.model.submodel.SocialModel;
import com.atb.app.preference.PrefConst;
import com.atb.app.preference.Preference;
import com.atb.app.util.RoundedCornersTransformation;
import com.atb.app.view.zoom.ZoomAnimation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.lky.toucheffectsmodule.factory.TouchEffectsFactory;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNReconnectionPolicy;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNSetChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.member.PNSetChannelMembersResult;
import com.pubnub.api.models.consumer.objects_api.member.PNUUID;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
//import com.stripe.android.PaymentConfiguration;
//import com.stripe.android.paymentsheet.PaymentSheet;
//import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils;

public abstract class CommonActivity extends BaseActivity {
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        TouchEffectsFactory.initTouchEffects(this);
        super.onCreate(savedInstanceState);
        Commons.g_commentActivity = this;
        paymentSheet =  new PaymentSheet((ComponentActivity) _context, this::onPaymentSheetResult);

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
                if(flag)
                    Commons.userType = 1;
                else
                    Commons.userType = 0;

                Preference.getInstance().put(context, PrefConst.PREFKEY_SELECTED_USERTYPE, Commons.userType);

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
                        //showToast(error.getMessage());

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
                        //showToast(error.getMessage());

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

    public void paymentProcessing( Map<String, String> payment_params ,int type){
        String api_link = API.MAKE_CASH_PAYMENT;
        //Stripe Payment
        if(type == 1){
            api_link = API.STRIPE_SUBSCRIPTION;
        }else if(type == 0){
            api_link = API.MAKE_STRIPE_PAYMENT;
        }
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                api_link ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            Log.d("aaaaa",json.toString());
                            if(!jsonObject.getBoolean("result"))
                                showAlertDialog(jsonObject.getString("msg"));
                            else {
                                String id ="";
                                String customer_id = jsonObject.getJSONObject("extra").getString("customer_id");
                                String ephemeral_key_secret = jsonObject.getJSONObject("extra").getString("ephemeral_key_secret");
                                String payment_intent_client_secret = jsonObject.getJSONObject("extra").getString("payment_intent_client_secret");
                                String publishable_key = jsonObject.getJSONObject("extra").getString("publishable_key");


                                PaymentConfiguration.init(_context, publishable_key);
                                paymentIntentClientSecret = payment_intent_client_secret;
                                customerConfig = new PaymentSheet.CustomerConfiguration(
                                        customer_id,
                                        ephemeral_key_secret
                                );
                                PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("My ATB")
                                        .customer(customerConfig)
                                        .allowsDelayedPaymentMethods(true)
                                        .build();

                                paymentSheet.presentWithPaymentIntent(
                                        paymentIntentClientSecret,
                                        configuration
                                );

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
                        //showToast(error.getMessage());

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
        // Paypal payment
//        if(type == 1){
//            api_link = API.ADD_PP_SUB;
//        }else if(type == 0){
//            api_link = API.MAKE_PP_PAYMENT;
//        }
//        showProgress();
//        StringRequest myRequest = new StringRequest(
//                Request.Method.POST,
//                api_link ,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String json) {
//                        closeProgress();
//                        try {
//                            JSONObject jsonObject = new JSONObject(json);
//                            if(!jsonObject.getBoolean("result"))
//                                showAlertDialog(jsonObject.getString("msg"));
//                            else {
//                                String id ="";
//                                if(type ==0 ) {
//                                    JSONArray jsonArray = jsonObject.getJSONArray("msg");
//                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        JSONObject object = jsonArray.getJSONArray(i).getJSONObject(0);
//                                        if (object.getString("transaction_type").equals("Sale")) {
//                                            id = object.getString("id");
//                                            break;
//                                        }
//
//                                    }
//                                }
//                                finishPayment(id);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        closeProgress();
//                        //showToast(error.getMessage());
//
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params  =   payment_params;
//                return params;
//            }
//        };
//        myRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d("aaaaa","Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            finishPayment("");
        }
    }
    public void processPayment(String price, String clicent_id, String clicnet_token,NewsFeedEntity newsFeedEntity,int deliveryOption,ArrayList<String> selected_Variation){

    }

    public void finishPayment(String transaction_id){

    }

    public void loginPubNub(){

        PNConfiguration pnConfiguration = new PNConfiguration();

        String pubKey = BuildConfig.PUB_KEY;
        String subKey = BuildConfig.SUB_KEY;
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSubscribeKey(subKey);

        pnConfiguration.setUuid("user_" + String.valueOf(Commons.g_user.getId()));

        pnConfiguration.setLogVerbosity(PNLogVerbosity.BODY);
        pnConfiguration.setReconnectionPolicy(PNReconnectionPolicy.LINEAR);
        pnConfiguration.setMaximumReconnectionRetries(20);

        Commons.mPubNub = new PubNub(pnConfiguration);
//        Commons.mPubNub.whereNow()
////                .uuid("some-other-uuid") // uuid of the user we want to spy on.
//                .async(new PNCallback<PNWhereNowResult>() {
//                    @Override
//                    public void onResponse(PNWhereNowResult result, PNStatus status) {
//                        // returns a pojo with channels // channel groups which "some-other-uuid" part of.
//
//                        Log.d("aaaaaaaa",result.toString());
//                    }
//                });

    }
    public void setChannelMetadata(String channelName , String title, String description,  Map<String, Object> custom){

        Commons.mPubNub.setChannelMetadata()
                .channel(channelName)
                .name(channelName)
                .description(description) /// this is description, but i am using image
                .custom(custom)
                .includeCustom(true)
                .async(new PNCallback<PNSetChannelMetadataResult>() {
                    @Override
                    public void onResponse(@Nullable final PNSetChannelMetadataResult result, @NotNull final PNStatus status) {
                        if (status.isError()) {
                            Log.d("aaaaaaaaaaa",status.toString());

                        } else {
                            Log.d("bbbbbbb",result.toString());

                        }
                    }
                });
    }




    public void gotochat(Context context, int userType,UserModel userModel){
        String channel = "", image = "",name = "",public_id;
        Map<String, Object> custom = new HashMap<>();
        int id = Commons.g_user.getId(),receiver_id = userModel.getId();
        boolean swap = false;
        if(id>userModel.getId()) {
            id = userModel.getId();
            receiver_id = Commons.g_user.getId();
            swap = true;
        }
        if(userType==1){
            String str = String.valueOf(receiver_id)+"#" + String.valueOf(userModel.getBusinessModel().getId());

            if(swap){
                str = String.valueOf(id)+"#" + String.valueOf(userModel.getBusinessModel().getId());
                channel = str +"_"+ String.valueOf(Commons.g_user.getId());
            }else{
                channel =  String.valueOf(Commons.g_user.getId()) + "_" + str;

            }
            image =  userModel.getBusinessModel().getBusiness_logo();
            name = userModel.getBusinessModel().getBusiness_name();
            public_id = "business_" + userModel.getBusinessModel().getId();
        }else{

            channel = String.valueOf(id) +"_"+ String.valueOf(receiver_id);
            image =  userModel.getImvUrl();
            name = userModel.getFirstname() + " " + userModel.getLastname();
            public_id = "user_" + userModel.getId();
        }
        JSONArray members = new JSONArray();
        try {
            JSONObject channelMember = new JSONObject();
            channelMember.put("id","user_" + Commons.g_user.getId());
            channelMember.put("name", Commons.g_user.getFirstname()+Commons.g_user.getLastname());
            channelMember.put("imageUrl", Commons.g_user.getImvUrl());
            members.put(channelMember);
            JSONObject channelMember1 = new JSONObject();
            channelMember1.put("id",public_id);
            channelMember1.put("name",name);
            channelMember1.put("imageUrl",image);
            members.put(channelMember1);
            custom.put("members",members.toString());
        }catch (Exception e){

        }

        Log.d("channel" , members.toString());

        Commons.mPubNub.subscribe().channels(Arrays.asList(channel));
        setChannelMetadata(channel,name,"0",custom);
        createMembership(channel,userModel,userType);

        RoomModel roomModel = new RoomModel();
        roomModel.setChannelId(channel);
        roomModel.setImage(image);
        roomModel.setName(name);
        roomModel.setType(userType);
        roomModel.setAnotherUserID(userModel.getId());
        Gson gson = new Gson();
        String usermodel = gson.toJson(roomModel);
        Bundle bundle = new Bundle();
        bundle.putString("roomModel",usermodel);
        if(Commons.userType == 1 ){
            Commons.senderImage = Commons.g_user.getBusinessModel().getBusiness_logo();
            Commons.senderName = Commons.g_user.getBusinessModel().getBusiness_name();
            Commons.senderID = "business_" + String.valueOf(Commons.g_user.getBusinessModel().getId());


        }else {
            Commons.senderID = "user_" + String.valueOf(Commons.g_user.getId());

            Commons.senderImage = Commons.g_user.getImvUrl();
            Commons.senderName = Commons.g_user.getFirstname() + " " + Commons.g_user.getLastname();
        }
        ((CommonActivity)context).goTo(context, ChatActivity.class,false,bundle);

    }
    void createMembership(String channnel, UserModel userModel , int userType){
        String member = "user_" + String.valueOf(userModel.getId());
        Map<String, Object> custom = new HashMap<>();
        if(userType == 1){
            member = "business_" + String.valueOf(userModel.getId());
            custom.put("user_Name", userModel.getBusinessModel().getBusiness_name());
            custom.put("user_profile",userModel.getBusinessModel().getBusiness_logo());
        }else{
            custom.put("user_Name", userModel.getFirstname()+ " " + userModel.getLastname());
            custom.put("user_profile",userModel.getImvUrl());

        }


        Commons.mPubNub.setChannelMembers()
                .channel(channnel)
                .uuids(Arrays.asList(PNUUID.uuidWithCustom(member, custom)))
                .async(new PNCallback<PNSetChannelMembersResult>() {
                    @Override
                    public void onResponse(@Nullable final PNSetChannelMembersResult result, @NotNull final PNStatus status) {
                        if (status.isError()) {
                            //handle error
                        } else {
                            //handle result
                        }
                    }
                });
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
                Commons.postalCode.put(line.split(",")[4],region + ", " + county +", United Kingdom");
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
                        //showToast(error.getMessage());

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

    public void getFirebaseToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("Exception", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        Log.d("Token=====",task.getResult());
                        Commons.fcmtoken = task.getResult();
                        uploadToken();
                    }
                });
    }

    void uploadToken(){
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.UPDATE_NOTIFCATION_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

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
                params.put("token",Commons.token);
                params.put("push_token",Commons.fcmtoken);
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    public void reLogin(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.LOGIN_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        parseResponse(json);
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
                params.put("email", Commons.g_user.getEmail());
                params.put("pwd", Commons.g_user.getPassword());
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
    void parseResponse(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.getBoolean("result")== false)
                showAlertDialog(jsonObject.getString("msg"));
            else {
                Commons.token = jsonObject.getString("msg");
                Log.d("aaaaaaa===",  Commons.token);
                JSONObject userObject = jsonObject.getJSONObject("extra").getJSONObject("profile");
                UserModel userModel = new UserModel();
                userModel.setId(userObject.getInt("id"));
                userModel.setUserName(userObject.getString("user_name"));
                userModel.setComplete(userObject.getInt("complete"));
                userModel.setFb_user_id(userObject.getString("fb_user_id"));
                userModel.setFacebook_token(userObject.getString("facebook_token"));
                userModel.setEmail(userObject.getString("user_email"));
                userModel.setPassword(Commons.g_user.getPassword());
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
                    businessModel.setGroup_title(business_info.getString("group_title"));
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
                successRelogin();
                //goTo(LoginActivity.this, UpdateBusinessActivity.class,true);

            }
        }catch (Exception e){
            Log.d("bbbbb, " , e.toString());
        }
    }
    public void successRelogin(){

    }

    public void retrieveCard(){

        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.RETRIVE_CARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            Log.d("aaaa",json);
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
//                                Commons.g_user.setBt_paypal_account(jsonObject.getString("msg"));
                                Boolean charge_enable = jsonObject.getJSONObject("extra").getBoolean("charges_enabled");
                                if(charge_enable)
                                    successAddCard();
                                else{
                                    GenralConfirmDialog confirmDialog = new GenralConfirmDialog();
                                    confirmDialog.setOnConfirmListener(new GenralConfirmDialog.OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            addCard();
                                        }
                                    },"Setup Payment card", "To be able to use the card payment method and take payment for your item directly in the app you will need to add your card.","Add Card", "Cancel");
                                    confirmDialog.show(getSupportFragmentManager(), "DeleteMessage");
                                }
                            }else {
//                                showAlertDialog(jsonObject.getString("msg"));
                                GenralConfirmDialog confirmDialog = new GenralConfirmDialog();
                                confirmDialog.setOnConfirmListener(new GenralConfirmDialog.OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        addCard();
                                    }
                                },"Setup Payment card", "To be able to use the card payment method and take payment for your item directly in the app you will need to add your card.","Add Card", "Cancel");
                                confirmDialog.show(getSupportFragmentManager(), "DeleteMessage");
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
                Map<String, String> payment_params = new HashMap<>();
                payment_params.put("token",Commons.token);
                return payment_params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    public void addCard(){

        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.ADD_CARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            Log.d("aaaa",json);
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                String account_link = jsonObject.getJSONObject("extra").getString("account_link");
                                Commons.g_user.setStripe_connect_account(jsonObject.getJSONObject("extra").getString("connect_id"));
                                InputCardDetail(account_link);
                            }else {
                                showAlertDialog(jsonObject.getString("msg"));
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
                Map<String, String> payment_params = new HashMap<>();
                payment_params.put("token",Commons.token);
                return payment_params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }


    public void successAddCard(){

    }
    public void InputCardDetail(String link){

    }

    public String getDaystr(int day){
        if(day == 1 || day ==21 || day == 31){
            return String.valueOf(day)+"st";
        }else if(day == 2 || day == 22 ){
            return String.valueOf(day)+"nd";
        }else if(day ==3 || day == 23){
            return String.valueOf(day)+"rd";
        }else{
            return String.valueOf(day)+"th";
        }
    }
}
