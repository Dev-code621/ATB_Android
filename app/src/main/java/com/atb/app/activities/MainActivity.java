package com.atb.app.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.chat.ChatActivity;
import com.atb.app.activities.navigationItems.ItemSoldActivity;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.activities.navigationItems.booking.BookingViewActivity;
import com.atb.app.activities.navigationItems.booking.MyBookingViewActivity;
import com.atb.app.activities.navigationItems.business.UpdateBusinessActivity;
import com.atb.app.activities.navigationItems.business.UpgradeBusinessSplashActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.activities.profile.ReviewActivity;
import com.atb.app.activities.profile.boost.BoostActivity;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.activities.profile.boost.PinPointActivity;
import com.atb.app.activities.profile.boost.ProfilePinActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.adapter.BoostItemAdapter;
import com.atb.app.adapter.BusinessItemAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.SelectCategoryDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.fragement.ChatFragment;
import com.atb.app.fragement.MainListFragment;
import com.atb.app.fragement.SearchFragment;
import com.atb.app.model.AutoCompleteModel;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.BoostModel;
import com.atb.app.model.NotiEntity;
import com.atb.app.model.RoomModel;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.kittinunf.fuel.core.BodyKt;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResult;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_search ,imv_profile,imv_feed,imv_post,imv_chat;
    FragmentTransaction ft;
    boolean main_flag = false;
    SelectCategoryDialog selectCategoryDialog;
    TextView txv_category;
    EditText edt_serach;
    LinearLayout lyt_title,lyt_profile;
    MainListFragment mainListFragment;
    public int selectIcon= 0 ;
    TextView txv_username;
    FrameLayout frame_noti,frame_chat;
    CardView card_unread_noti,card_unread_chat;
    RecyclerView recycler_view_boost;
    HashMap<String,  ArrayList<BoostModel>>boostModels = new HashMap<>();
    BoostItemAdapter boostAdapter ;
    BusinessItemAdapter businessItemAdapter ;
    ChatFragment chatFragment;
    ImageView imv_title;
    int noti_type;
    String related_id = "-1";
    int busines_pager = 1;
    ArrayList<UserModel>businessUsers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Commons.g_mainActivity = this;
        setContentView(R.layout.activity_main);
        imv_search = findViewById(R.id.imv_search);
        imv_profile = findViewById(R.id.imv_profile);
        imv_feed = findViewById(R.id.imv_feed);
        imv_post = findViewById(R.id.imv_post);
        imv_chat = findViewById(R.id.imv_chat);
        txv_category = findViewById(R.id.txv_category);
        lyt_title = findViewById(R.id.lyt_title);
        edt_serach = findViewById(R.id.edt_serach);
        txv_username = findViewById(R.id.txv_username);
        lyt_profile = findViewById(R.id.lyt_profile);
        frame_noti = findViewById(R.id.frame_noti);
        frame_chat = findViewById(R.id.frame_chat);
        card_unread_noti = findViewById(R.id.card_unread_noti);
        card_unread_chat = findViewById(R.id.card_unread_chat);
        recycler_view_boost = findViewById(R.id.recycler_view_boost);
        imv_title = findViewById(R.id.imv_title);
        imv_title.setOnClickListener(this);

        imv_search.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        imv_feed.setOnClickListener(this);
        imv_post.setOnClickListener(this);
        mainListFragment = new MainListFragment();
        frame_chat.setOnClickListener(this);
        frame_noti.setOnClickListener(this);
        setPubnubToken();
        Commons.mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
            @Override
            public void onPlayerItemChanged(MetaData metaData) {

            }
        });

        recycler_view_boost.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        boostAdapter = new BoostItemAdapter(this,  new BoostItemAdapter.OnSelectListener() {
//            @Override
//            public void onSelectItem(BoostModel boostModel) {
//
//                if(Commons.g_user.getAccount_type() == 0){
//                    SelectMediaDialog selectMediaActionDialog = new SelectMediaDialog();
//                    selectMediaActionDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
//                        @Override
//                        public void OnCamera() {
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("subScriptionType",0);
//                            goTo(MainActivity.this, UpgradeBusinessSplashActivity.class,false,bundle);
//                        }
//
//                        @Override
//                        public void OnAlbum() {
//
//                        }
//                    },getResources().getString(R.string.upgrade_account),getResources().getString(R.string.yes),getResources().getString(R.string.no));
//                    selectMediaActionDialog.show(getSupportFragmentManager(), "action picker");
//                }else {
//                    if (!boostModel.isEmptyModel())
//                        goTo(MainActivity.this, BoostActivity.class, false);
//                    else {
//                        if (boostModel.getUserModel().getId() == Commons.g_user.getId())
//                            startActivityForResult(new Intent(MainActivity.this, ProfileBusinessNaviagationActivity.class), 1);
//                        else
//                            getuserProfile(boostModel.getUserModel().getId(), 1);
//
//                    }
//                }
//            }
//
//        });
       // boostAdapter.setHasStableIds(true);
        recycler_view_boost.setItemAnimator(null);
        //recycler_view_boost.setAdapter(boostAdapter);
        //getProfilepines();
        businessItemAdapter = new BusinessItemAdapter(this, new BusinessItemAdapter.OnSelectListener() {
            @Override
            public void onSelectItem(UserModel boostModel) {
                if(boostModel.getId() != Commons.g_user.getId())
                    getuserProfile(boostModel.getId(), 1);

                else {
                    startActivityForResult(new Intent(MainActivity.this, ProfileBusinessNaviagationActivity.class),1);

                }
            }
        });
        recycler_view_boost.setAdapter(businessItemAdapter);

        Timer timer= new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        getAllbusiness();
                    }
                });
            }
        }, 1000, 10000);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                try{
                    noti_type= Integer.parseInt(bundle.getString("type"));
                    related_id= bundle.getString("related_id");
                    processNotification();
                }catch (Exception e){

                }

            }
        }

    }

    void  setPubnubToken(){

        List<String> channels = new ArrayList<>();
        Commons.mPubNub.getAllChannelsMetadata()
                .includeCustom(true)
                .async(new PNCallback<PNGetAllChannelsMetadataResult>() {
                    @Override
                    public void onResponse(@Nullable final PNGetAllChannelsMetadataResult result, @NotNull final PNStatus status) {

                        if (status.isError()) {

                        } else {
                                                       List<PNChannelMetadata> channelMetadata = new ArrayList<>();
                            channelMetadata.clear();
                            channelMetadata = result.getData();
                            for(int i = 0 ;i<channelMetadata.size();i++){

                                PNChannelMetadata channel = channelMetadata.get(i);
                                try {
                                    JsonObject custom = (JsonObject) channel.getCustom();
                                    RoomModel roomModel = new RoomModel();
                                    String str = channel.getId();
                                    String[] array = str.split("_");
                                    if(array.length<1)continue;
                                    if(custom.get("owner_id").getAsInt() == Commons.g_user.getId()){
                                        channels.add(channel.getId());
                                    }else if(array[1].equals(String.valueOf(Commons.g_user.getId()))){
                                        channels.add(channel.getId());
                                    }else{
                                        if(Commons.g_user.getAccount_type() == 1){
                                            String business_account = String.valueOf(Commons.g_user.getId())+"#"+ String.valueOf(Commons.g_user.getBusinessModel().getId());
                                            if(str.contains(business_account)){
                                                channels.add(channel.getId());

                                            }
                                        }
                                    }
                                }catch (Exception e){

                                    Log.d("Exception==" ,e.toString());
                                }

                            }
                            Commons.pubnub_channels = channels;
                            Commons.mPubNub.addPushNotificationsOnChannels()
                                    .pushType(PNPushType.FCM)
                                    .channels(channels)
                                    .deviceId(Commons.fcmtoken)
                                    .async(new PNCallback<PNPushAddChannelResult>() {
                                        @Override
                                        public void onResponse(PNPushAddChannelResult result, PNStatus status) {

                                        }
                                    });

                        }
                    }
                });

//        Commons.mPubNub.grant()
//                .channels(Commons.pubnub_channels)
//                .uuids(Arrays.asList(Commons.senderID))
//                .authKeys(Arrays.asList(Commons.senderID))
//                .write(true)
//                .read(true)
//                .ttl(1440)
//                .async(new PNCallback<PNAccessManagerGrantResult>() {
//                    @Override
//                    public void onResponse(PNAccessManagerGrantResult result, PNStatus status) {
//                        System.out.println(result);
//                        System.out.println(status);
//                    }
//                });

    }
    void loadNotification(){
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try{
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("msg");
                           boolean flag =false; ;
                            for(int i =0;i<jsonArray.length();i++){
                               if(jsonArray.getJSONObject(i).getInt("read_status") == 0  ){
                                   flag = true;
                                   break;
                               }
                            }
                            if(flag){
                                card_unread_noti.setVisibility(View.VISIBLE);
                            }else
                                card_unread_noti.setVisibility(View.GONE);

                            Commons.notification_count = jsonArray.length();

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
                params.put("user_id", String.valueOf(Commons.g_user.getId()));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void processNotification(){
        if(noti_type == 1 || noti_type == 2 || noti_type ==3 ){
            Bundle bundle = new Bundle();
            bundle.putInt("postId",Integer.parseInt(related_id));
            bundle.putBoolean("CommentVisible",true);
            startActivityForResult(new Intent(this, NewsDetailActivity.class).putExtra("data",bundle),1);
        }else if(noti_type ==4){
            Bundle bundle = new Bundle();
            bundle.putBoolean("bussiness",(Commons.g_user.getAccount_type()==1)? true : false);
            goTo(this, ItemSoldActivity.class,false,bundle);
        }else if(noti_type ==6 || noti_type == 7 || noti_type ==8 || noti_type ==9){
            getBookingByID();
        }
        else if(noti_type ==10) {
            getuserProfile(Integer.parseInt(related_id),1);
        }
        else if( noti_type ==13){
            Gson gson = new Gson();
            String usermodel = gson.toJson(Commons.g_user);
            Bundle bundle = new Bundle();
            bundle.putString("userModel",usermodel);
            bundle.putBoolean("editable",false);
            goTo(this,ReviewActivity.class,false,bundle);
        }else if(noti_type == 11 ){
            //get service api
            Bundle bundle = new Bundle();
            bundle.putInt("postId",Integer.parseInt(related_id));
            bundle.putBoolean("CommentVisible",true);
            startActivityForResult(new Intent(this, NewsDetailActivity.class).putExtra("data",bundle),1);
        }else if(noti_type == 12){
            goTo(this, TransactionHistoryActivity.class,false);
        }else if(noti_type ==14){

        }else if(noti_type ==15){
            reLogin();
        }else if(noti_type ==16) {
            goTo(this, ProfileBusinessNaviagationActivity.class, false);
        }else if(noti_type ==17) {
            getuserProfile(Integer.parseInt(related_id),0);

        }else if(noti_type == 18 || noti_type == 19 || noti_type == 20  || noti_type == 21 || noti_type == 22){

            //18,19: product and service id

            Bundle bundle = new Bundle();
            bundle.putInt("postId",Integer.parseInt(related_id));
            bundle.putBoolean("CommentVisible",true);
            startActivityForResult(new Intent(this, NewsDetailActivity.class).putExtra("data",bundle),1);
        } else if(noti_type ==23) {
            goTo(this, ProfileBusinessNaviagationActivity.class, false);
        }else if(noti_type ==24) {
            goTo(this, ProfilePinActivity.class, false);
        }else if(noti_type ==25) {
            goTo(this, PinPointActivity.class, false);
        }else if(noti_type ==26) {
        }else if(noti_type ==27) {
            setColor(1);
        }else if(noti_type ==28) {
            goTo(this, ProfilePinActivity.class, false);
        }else if(noti_type ==29) {
            goTo(this, PinPointActivity.class, false);
        }else if(noti_type ==30 || noti_type ==31 || noti_type == 32) {
            goTo(this, ProfileBusinessNaviagationActivity.class, false);
        }else if (noti_type == 100){
            Bundle bundle = new Bundle();
            bundle.putString("roomModel",related_id);
            goTo(this, ChatActivity.class,false,bundle);
        }
    }
    void getBookingByID(){
    showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_INDIVIDUAL_BOOKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            BookingEntity bookingEntity = new BookingEntity();
                            JSONArray jsonArray = jsonObject.getJSONArray("extra");
                            if(jsonArray.length()>0)
                                bookingEntity.initModel(jsonArray.getJSONObject(0));
                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            String bookingModel = gson.toJson(bookingEntity);
                            bundle = new Bundle();
                            bundle.putString("bookModel",bookingModel);

                            if(noti_type ==9){
                                startActivityForResult(new Intent(MainActivity.this, MyBookingViewActivity.class).putExtra("data", bundle), 1);
                                overridePendingTransition(0, 0);
                            }else {
                                startActivityForResult(new Intent(MainActivity.this, BookingViewActivity.class).putExtra("data", bundle), 1);
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
                        showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("booking_id", String.valueOf(related_id));
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
            case R.id.imv_search:
                setColor(1);
                break;
            case R.id.lyt_profile:
                if(Commons.userType==1)
                    startActivityForResult(new Intent(this, ProfileBusinessNaviagationActivity.class),1);
                else
                    goTo(this, ProfileUserNavigationActivity.class,false);
                break;
            case R.id.imv_post:
                setColor(2);
                startActivityForResult(new Intent(this, SelectPostCategoryActivity.class),1);
                overridePendingTransition(0, 0);
                break;
            case R.id.imv_feed:
                if(main_flag)selectCategory();
                setColor(0);
                selectIcon = 0;
                break;
            case R.id.frame_chat:
                setColor(3);
                break;
            case R.id.frame_noti:
                goTo(this, NotificationActivity.class,false);
                break;
            case R.id.imv_title:
                if(mainListFragment!=null)
                    mainListFragment.uiRefresh();
                break;
        }
    }


    void selectCategory(){

        selectCategoryDialog = new SelectCategoryDialog(this);
        selectCategoryDialog.show(getSupportFragmentManager(), "action picker");
    }
    public void selectItem(int posstion){
        selectCategoryDialog.dismiss();
        if(posstion<0){
            txv_category.setText(getResources().getString(R.string.my_atb));
            Commons.main_category = getResources().getString(R.string.my_atb);
        }else {
            txv_category.setText(Constants.category_word[posstion]);
            Commons.main_category = Constants.category_word[posstion];
        }
        //getProfilepines();
        mainListFragment.getList();


    }
    public void getAllbusiness(){
        //  showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GETBUSINESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        // closeProgress();

                        try {
                            businessUsers.clear();
                            JSONObject jsonObject = new JSONObject(json);
                            jsonObject = jsonObject.getJSONObject("extra");
                            JSONArray jsonArray =jsonObject.getJSONArray("spotlight");
                            for(int i =0;i<jsonArray.length();i++){
                                UserModel userModel = new UserModel();
                                userModel.initModel(jsonArray.getJSONObject(i));
                                businessUsers.add(userModel);
                            }
                            businessItemAdapter.setRoomData(businessUsers);
                            if(busines_pager*6>=jsonObject.getInt("total_rows"))
                                busines_pager = 1;
                            else
                                busines_pager++;
                        }catch (Exception e){
                            Log.d("aaaaaa",e.toString());
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
                params.put("category", txv_category.getText().toString());
                params.put("page", String.valueOf(busines_pager));
                params.put("per_page", "6");
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    public void getProfilepines(){
      //  showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GETPROFILEPINES_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                       // closeProgress();
                        Log.d("aaaaa",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray =jsonObject.getJSONArray("extra");
                            boostModels.clear();
                            for(int i =0;i<Constants.category_word.length;i++){
                                ArrayList<BoostModel> arrayList = new ArrayList<>();
                                for(int j =0;j<6;j++){
                                    BoostModel boostModel = new BoostModel();
                                   arrayList.add(boostModel);
                                }
                                boostModels.put(Constants.category_word[i],arrayList);
                            }
                            for(int i =0;i<jsonArray.length();i++){
                                BoostModel boostModel = new BoostModel();
                                boostModel.initModel(jsonArray.getJSONObject(i));
                                //Log.d("bbbbbbbb",String.valueOf(boostModels.get(boostModel.getCategory()).size()));
                                switch (boostModel.getBidon()) {
                                    case 0:
                                        if(boostModel.getPosition()==0){
                                            boostModels.get(boostModel.getCategory()).set(3,boostModel);
                                        }else {
                                            boostModels.get(boostModel.getCategory()).set(0,boostModel);
                                        }
                                        break;
                                    case 1:
                                        if(boostModel.getPosition()==0){
                                            boostModels.get(boostModel.getCategory()).set(1,boostModel);
                                        }else {
                                            boostModels.get(boostModel.getCategory()).set(5,boostModel);
                                        }
                                        break;
                                    case 2:
                                        if(boostModel.getPosition()==0){
                                            boostModels.get(boostModel.getCategory()).set(2,boostModel);
                                        }else {
                                            boostModels.get(boostModel.getCategory()).set(4,boostModel);
                                        }
                                        break;
                                }

                            }
                            boostAdapter.setRoomData(boostModels);
                        }catch (Exception e){
                            Log.d("aaaaaa",e.toString());
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
                params.put("category", txv_category.getText().toString());
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    @SuppressLint("ResourceAsColor")
    public void setColor(int id){
        lyt_title.setVisibility(View.VISIBLE);
        if(Commons.userType ==1 ){
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }else {
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }
        if(id==0){
            main_flag = true;
            Commons.selectUsertype = -1;
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mainListFragment).commit();
            txv_category.setText(Commons.main_category);
        }
        else if(id==1){
            lyt_title.setVisibility(View.GONE);
            main_flag = false;
            selectIcon = 1;
            SearchFragment mFragment = new SearchFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mFragment).commit();
        }
        else if(id ==3){
            selectIcon = 3;
            main_flag = false;
            chatFragment = new ChatFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,chatFragment).commit();
            lyt_title.setVisibility(View.GONE);

        }
        else if( id ==4){
            MainListFragment mFragment = new MainListFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mFragment).commit();
        }

        imv_feed.setImageDrawable(getResources().getDrawable(R.drawable.icon_home));
        imv_post.setImageDrawable(getResources().getDrawable(R.drawable.icon_addpost));
        imv_search.setImageDrawable(getResources().getDrawable(R.drawable.icon_search));
        imv_chat.setImageDrawable(getResources().getDrawable(R.drawable.icon_message));
        imv_feed.clearColorFilter();
        imv_post.clearColorFilter();
        imv_search.clearColorFilter();
        imv_chat.clearColorFilter();
        if(id==2){
            imv_post.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }else if(id==1){
            imv_search.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);

        }else if(id == 0) {
            imv_feed.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }else if(id ==3){
            imv_chat.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }
    }



    @Override
    public void onBackPressed() {
        gotoExit();
    }

    void gotoExit(){
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                onExit();
            }
        },getString(R.string.finish_app));
        confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            onResume();
            mainListFragment.getList();
        }
    }


    void getChangeState(){
        String channel  = "ATB/Admin/business/" + Commons.g_user.getId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(channel).child("approved");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    String value = dataSnapshot.getValue().toString();
                    Log.d("TAG", "Value is: " + value);
                    if (value != null)
                        Commons.g_user.setStatus(Integer.parseInt(value));
                    ;
                }catch (Exception e){


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }
    void getAllusers(){
        //  showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GETALLUSER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        // closeProgress();

                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            Commons.AllUsers.clear();
                            Commons.Appusers.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("extra");
                            for(int i =0;i<jsonArray.length();i++){
                                UserModel userModel = new UserModel();
                                userModel.initModel(jsonArray.getJSONObject(i));
                                AutoCompleteModel autoCompleteModel = new AutoCompleteModel();
                                autoCompleteModel.setId(userModel.getId());
                                autoCompleteModel.setName(userModel.getUserName());
                                autoCompleteModel.setPic_url(userModel.getImvUrl());
                                Commons.AllUsers.add(autoCompleteModel);

                                if(userModel.getAccount_type() == 1){
                                    autoCompleteModel = new AutoCompleteModel();
                                    autoCompleteModel.setId(userModel.getId());
                                    autoCompleteModel.setName(userModel.getBusinessModel().getBusiness_name());
                                    autoCompleteModel.setPic_url(userModel.getBusinessModel().getBusiness_logo());
                                    autoCompleteModel.setIs_business(1);
                                    Commons.AllUsers.add(autoCompleteModel);
                                }
                                Commons.Appusers.add(userModel);
                            }

                        }catch (Exception e){
                            Log.d("aaaaaa",e.toString());
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
    @Override
    protected void onResume() {
        super.onResume();
        getChangeState();
        Commons.selected_user = Commons.g_user;
        setColor(selectIcon);
        getFirebaseToken();
        loadNotification();
        getAllusers();

    }
    @Override
    public void UserProfile(UserModel userModel,int usertype){
        try{
            if(Integer.parseInt(related_id) == 10){
                if( noti_type == 10){
                    Gson gson = new Gson();
                    String usermodel = gson.toJson(userModel);
                    Bundle bundle = new Bundle();
                    bundle.putString("userModel",usermodel);
                    bundle.putBoolean("editable",true);
                    goTo(this,ReviewActivity.class,false,bundle);
                    return;
                }
            }
        }catch (Exception e){

        }
        if(userModel.getId() == Commons.g_user.getId()){
            if(usertype == 0 ){
                goTo(this,ProfileUserNavigationActivity.class,false);
            }else{
                goTo(this,ProfileBusinessNaviagationActivity.class,false);
            }
        }else {
            Gson gson = new Gson();
            String usermodel = gson.toJson(userModel);
            Bundle bundle = new Bundle();
            bundle.putString("userModel",usermodel);
            bundle.putInt("userType",usertype);
            goTo(this, OtherUserProfileActivity.class,false,bundle);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("aaaa","aaaa");
        Commons.mVideoPlayerManager.stopAnyPlayback();
    }

    @Override
    public boolean selectProfile(boolean flag){
        if(chatFragment!=null)
            chatFragment.setProfile(flag);
        return  flag;
    }
    @Override
    public void successRelogin(){
        startActivityForResult(new Intent(MainActivity.this, ProfileBusinessNaviagationActivity.class),1);

    }
}