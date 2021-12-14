package com.atb.app.activities.newsfeedpost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.PurchasesActivity;
import com.atb.app.activities.navigationItems.booking.BookFromPostActivity;
import com.atb.app.activities.navigationItems.other.LocationMapActivity;
import com.atb.app.activities.newpost.SelectProductCategoryActivity;
import com.atb.app.activities.profile.FollowerAndFollowingActivity;
import com.atb.app.activities.profile.ReportPostActivity;
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.activities.register.ProfileSetActivity;
import com.atb.app.adapter.CommentAdapter;
import com.atb.app.adapter.PollEmageAdapter;
import com.atb.app.adapter.SelectOneItemAdapter;
import com.atb.app.adapter.SliderImageAdapter;
import com.atb.app.adapter.VotingListAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.commons.Helper;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.ConfirmVariationDialog;
import com.atb.app.dialog.DepositDialog;
import com.atb.app.dialog.FeedDetailDialog;
import com.atb.app.dialog.InsuranceViewDialog;
import com.atb.app.dialog.PaymentBookingDialog;
import com.atb.app.dialog.PaymentSuccessDialog;
import com.atb.app.dialog.ProductVariationSelectDialog;
import com.atb.app.dialog.SelectDeliveryoptionDialog;
import com.atb.app.model.CommentModel;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.UserModel;
import com.atb.app.model.VariationModel;
import com.atb.app.model.submodel.VotingModel;
import com.atb.app.util.CustomMultipartRequest;
import com.atb.app.util.RoundedCornersTransformation;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.models.VenmoAccountNonce;
import com.braintreepayments.cardform.view.CardForm;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.LinkProperties;

public class NewsDetailActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_back,imv_profile,imv_navigation,imv_camera,imv_send,imv_close,imv_like,imv_bookmark;
    LinearLayout lyt_reply,lyt_like;
    FrameLayout lyt_image;
    TextView txv_name,txv_id,txv_replyname,txv_like,txv_comment_number,txv_like1;
    SliderView imageSlider;
    SliderImageAdapter setSliderAdapter;
    ListView list_comment;
    RecyclerView recyclerView_images;
    EditText edt_comment;
    PollEmageAdapter pollEmageAdapter;
    ArrayList<String>completedValue = new ArrayList<>();
    ArrayList<String>returnValue = new ArrayList<>();
    CommentAdapter commentAdapter ;
    int comment_level =0;
    NestedScrollView scrollView;
    int parentPosstion =0;
    int postId;
    boolean commentVisible;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    CommentModel parentModel = new CommentModel();

    TextView txv_advicename1,txv_advicename1_description,txv_title,txv_title_description,txv_category,txv_startprice,txv_depist_price,txv_cancelday,txv_areacovered;
    LinearLayout lyt_advice_image,lyt_text,lyt_offered,lyt_deposit,lyt_cancel,lyt_area,lyt_insured,lyt_qualitfied,lyt_sale_button;
    ImageView imv_txv_type,imv_qualitfied,imv_insure;
    ListView lyt_votelist;
    VotingListAdapter votingListAdapter;
    TextView txv_book_service,txv_insure,txv_qualitfied;
    ImageView imv_bubble;
    LinearLayout lyt_sale_post,lyt_location,lyt_book_service;
    TextView txv_brand,txv_price,txv_postage_cost,txv_location,txv_buy_sale;
    ArrayList<RecyclerView> recycler_view_attribue = new ArrayList<>();
    ArrayList<LinearLayout> lyt_attribute = new ArrayList<>();
    ArrayList<TextView> txv_attribute = new ArrayList<>();
    ImageView imv_cart,imv_videoplay;
    int flowerid = -1;
    ArrayList<String> selected_Variation = new ArrayList<>();
    int REQUEST_PAYMENT_CODE =10034;
    int REQUEST_EDITFEED =23232;
    Map<String, String> payment_params = new HashMap<>();
    int deliveryOption = 0;
    TextView txv_buy_mesasge,txv_quantity,txv_condition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        lyt_sale_post = findViewById(R.id.lyt_sale_post);
        lyt_location = findViewById(R.id.lyt_location);
        txv_brand = findViewById(R.id.txv_brand);
        txv_price = findViewById(R.id.txv_price);
        txv_buy_mesasge = findViewById(R.id.txv_buy_mesasge);
        txv_postage_cost = findViewById(R.id.txv_postage_cost);
        txv_location = findViewById(R.id.txv_location);
        txv_buy_sale = findViewById(R.id.txv_buy_sale);
        recycler_view_attribue.add( findViewById(R.id.recycler_view_color));
        recycler_view_attribue.add(findViewById(R.id.recycler_view_size));
        recycler_view_attribue.add(findViewById(R.id.recycler_view_material));
        lyt_attribute.add(findViewById(R.id.lyt_attribute1));
        lyt_attribute.add(findViewById(R.id.lyt_attribute2));
        lyt_attribute.add(findViewById(R.id.lyt_attribute3));
        txv_attribute.add(findViewById(R.id.txv_attribute1));
        txv_attribute.add(findViewById(R.id.txv_attribute2));
        txv_attribute.add(findViewById(R.id.txv_attribute3));
        imv_cart = findViewById(R.id.imv_cart);
        imv_videoplay = findViewById(R.id.imv_videoplay);
        imv_back = findViewById(R.id.imv_back);
        imv_profile = findViewById(R.id.imv_profile);
        imv_navigation = findViewById(R.id.imv_navigation);
        lyt_image = findViewById(R.id.lyt_image);
        txv_name = findViewById(R.id.txv_name);
        txv_id = findViewById(R.id.txv_id);
        imageSlider = findViewById(R.id.imageSlider);
        imv_camera = findViewById(R.id.imv_camera);
        imv_send = findViewById(R.id.imv_send);
        list_comment = findViewById(R.id.list_comment);
        recyclerView_images = findViewById(R.id.recyclerView_images);
        edt_comment = findViewById(R.id.edt_comment);
        scrollView = findViewById(R.id.scrollView);
        imv_close = findViewById(R.id.imv_close);
        lyt_reply = findViewById(R.id.lyt_reply);
        txv_replyname = findViewById(R.id.txv_replyname);
        txv_like = findViewById(R.id.txv_like);
        txv_comment_number = findViewById(R.id.txv_comment_number);
        imv_like = findViewById(R.id.imv_like);
        txv_like1 = findViewById(R.id.txv_like1);
        imv_bookmark = findViewById(R.id.imv_bookmark);
        txv_advicename1_description = findViewById(R.id.txv_advicename1_description);
        txv_advicename1 = findViewById(R.id.txv_advicename1);
        lyt_advice_image = findViewById(R.id.lyt_advice_image);
        txv_title = findViewById(R.id.txv_title);
        txv_title_description = findViewById(R.id.txv_title_description);
        lyt_text = findViewById(R.id.lyt_text);
        imv_txv_type = findViewById(R.id.imv_txv_type);
        lyt_votelist = findViewById(R.id.lyt_votelist);
        txv_category = findViewById(R.id.txv_category);
        txv_startprice = findViewById(R.id.txv_startprice);
        txv_depist_price = findViewById(R.id.txv_depist_price);
        txv_cancelday = findViewById(R.id.txv_cancelday);
        txv_areacovered = findViewById(R.id.txv_areacovered);
        lyt_offered = findViewById(R.id.lyt_offered);
        lyt_deposit = findViewById(R.id.lyt_deposit);
        lyt_deposit.setOnClickListener(this);
        lyt_cancel = findViewById(R.id.lyt_cancel);
        lyt_cancel.setOnClickListener(this);
        lyt_area = findViewById(R.id.lyt_area);
        lyt_area.setOnClickListener(this);
        lyt_insured = findViewById(R.id.lyt_insured);
        lyt_qualitfied = findViewById(R.id.lyt_qualitfied);
        imv_bubble = findViewById(R.id.imv_bubble);
        txv_book_service = findViewById(R.id.txv_book_service);
        lyt_sale_button = findViewById(R.id.lyt_sale_button);
        lyt_book_service = findViewById(R.id.lyt_book_service);
        lyt_like = findViewById(R.id.lyt_like);
        imv_insure = findViewById(R.id.imv_insure);
        imv_qualitfied = findViewById(R.id.imv_qualitfied);
        txv_insure = findViewById(R.id.txv_insure);
        txv_qualitfied = findViewById(R.id.txv_qualitfied);
        txv_quantity = findViewById(R.id.txv_quantity);
        txv_condition = findViewById(R.id.txv_condition);
        txv_book_service.setOnClickListener(this);
        lyt_book_service.setOnClickListener(this);
        imv_bubble.setOnClickListener(this);
        txv_buy_sale.setOnClickListener(this);
        lyt_like.setOnClickListener(this);
        lyt_location.setOnClickListener(this);
        txv_buy_mesasge.setOnClickListener(this);
        imv_bookmark.setOnClickListener(this);
        setSliderAdapter = new SliderImageAdapter(this);
        imageSlider.setSliderAdapter(setSliderAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorSelectedColor(Color.WHITE);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        imageSlider.setScrollTimeInSec(3);
        imageSlider.setAutoCycle(true);
        imageSlider.startAutoCycle();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                postId= bundle.getInt("postId");
                commentVisible= bundle.getBoolean("CommentVisible");
                if(commentVisible == false){
                    String user= bundle.getString("newfeedEntity");
                    Gson gson = new Gson();
                    newsFeedEntity = gson.fromJson(user, NewsFeedEntity.class);
                    initialLayout();

                }else {
                    loadLayout();
                }
            }
        }
        imv_back.setOnClickListener(this);
        imv_profile.setOnClickListener(this);
        imv_navigation.setOnClickListener(this);
        imv_camera.setOnClickListener(this);
        imv_send.setOnClickListener(this);
        imv_close.setOnClickListener(this);
        edt_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                imv_send.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_send));
                if(edt_comment.getText().length()>0) {
                    imv_send.setColorFilter(_context.getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                }
                else {
                    imv_send.clearColorFilter();
                    imv_send.setColorFilter(_context.getResources().getColor(R.color.boder_color), PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pollEmageAdapter = new PollEmageAdapter(this, new PollEmageAdapter.SelectListener() {
            @Override
            public void onClose(int posstion) {
                completedValue.remove(posstion);
                reloadImages();
            }

            @Override
            public void addImage() {
                selectImage();
            }
        },3);
        recyclerView_images.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView_images.setAdapter(pollEmageAdapter);
        commentAdapter = new CommentAdapter(this);
        list_comment.setAdapter(commentAdapter);
        Keyboard();

    }
    void loadLayout(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_POST_DETAIL_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            newsFeedEntity = new NewsFeedEntity();
                            newsFeedEntity.initDetailModel(jsonObject.getJSONObject("extra"));
                            initialLayout();
                            getSavedList();

                        }catch (Exception e){
                            Log.d("Exception ",e.toString());
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
                params.put("post_id", String.valueOf(postId));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }

    void getSavedList(){
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_USER_BOOKMARKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                for(int i =0;i<jsonArray.length();i++) {
                                    if(jsonArray.getJSONObject(i).getInt("id") == postId){
                                        newsFeedEntity.setFeedSave(true);
                                        break;
                                    }
                                }
                                initialLayout();
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
    void initialLayout(){
        selected_Variation.clear();
        setSliderAdapter.renewItems(newsFeedEntity.getPostImageModels());
        if(newsFeedEntity.getPoster_profile_type()==0) {
            Glide.with(NewsDetailActivity.this).load(newsFeedEntity.getUserModel().getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
            txv_name.setText(newsFeedEntity.getUserModel().getFirstname() + " " + newsFeedEntity.getUserModel().getLastname());
        }else {
            Glide.with(NewsDetailActivity.this).load(newsFeedEntity.getUserModel().getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
            txv_name.setText(newsFeedEntity.getUserModel().getBusinessModel().getBusiness_name());
        }
        txv_id.setText("@"+newsFeedEntity.getUserModel().getUserName());
        txv_like.setText(String.valueOf(newsFeedEntity.getLikes()));
        txv_comment_number.setText(String.valueOf(newsFeedEntity.getComments()));
        lyt_advice_image.setVisibility(View.GONE);
        lyt_text.setVisibility(View.GONE);
        lyt_offered.setVisibility(View.GONE);
        lyt_sale_post.setVisibility(View.GONE);
        imv_txv_type.setImageResource(Constants.postType[newsFeedEntity.getPost_type()]);
        txv_advicename1.setText(newsFeedEntity.getTitle());
        txv_advicename1_description.setText(newsFeedEntity.getDescription());
        txv_category.setText(newsFeedEntity.getCategory_title() +" >");
        if(newsFeedEntity.getPostImageModels().size()>0){
            if(Commons.mediaVideoType(newsFeedEntity.getPostImageModels().get(0).getPath()))
                imv_videoplay.setVisibility(View.VISIBLE);
        }
        if(!commentVisible){
            FrameLayout lyt_bookmark = findViewById(R.id.lyt_bookmark);
            lyt_bookmark.setVisibility(View.GONE);
            LinearLayout lyt_comment_send = findViewById(R.id.lyt_comment_send);
            lyt_comment_send.setVisibility(View.GONE);
        }

        switch (newsFeedEntity.getPost_type()) {
            case 1:
                if(newsFeedEntity.getPostImageModels().size() ==0){
                    lyt_text.setVisibility(View.VISIBLE);
                    txv_title.setText(newsFeedEntity.getTitle());
                    txv_title_description.setText(newsFeedEntity.getDescription());
                    lyt_image.setVisibility(View.GONE);
                }else {
                    lyt_advice_image.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                lyt_advice_image.setVisibility(View.VISIBLE);
                txv_category.setVisibility(View.VISIBLE);
                lyt_sale_post.setVisibility(View.VISIBLE);
                txv_brand.setText(newsFeedEntity.getPost_brand());
                if(newsFeedEntity.getIs_sold()==1){
                    txv_price.setTextColor(getResources().getColor(R.color.discard_color));
                    txv_price.setText("SOLD");
                }else {
                    txv_price.setText("£" + newsFeedEntity.getPrice());
                    txv_price.setTextColor(getResources().getColor(R.color.txt_color));
                }
                txv_postage_cost.setText("£" + newsFeedEntity.getDelivery_cost());
                txv_condition.setText(newsFeedEntity.getPost_condition());

                String[] location = newsFeedEntity.getPost_location().split(",");
                txv_location.setText(location[location.length-1]);
                for(int i=0;i<newsFeedEntity.getAttribute_map().size();i++){
                    final int finaI = i;

                    txv_attribute.get(i).setText(newsFeedEntity.getAttribute_titles().get(i));
                    lyt_attribute.get(i).setVisibility(View.VISIBLE);
                    recycler_view_attribue.get(i).setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    recycler_view_attribue.get(i).setAdapter(new SelectOneItemAdapter(this, newsFeedEntity.getAttribute_map().get(newsFeedEntity.getAttribute_titles().get(i)), new SelectOneItemAdapter.OnSelectListener() {
                        @Override
                        public void onSelect(String path) {
                            if(selected_Variation.size()==finaI)
                                selected_Variation.add("");
                            selected_Variation.set(finaI,path);
                            if(selected_Variation.size()==newsFeedEntity.getAttribute_map().size()) {
                                VariationModel variationModel = newsFeedEntity.productHasStock(selected_Variation);
                                txv_quantity.setText(String.valueOf(variationModel.getStock_level()));
                            }

                        }
                    }));
                }
                break;
            case 3:
                lyt_advice_image.setVisibility(View.VISIBLE);
                lyt_offered.setVisibility(View.VISIBLE);
                txv_startprice.setText(newsFeedEntity.getPrice());
                txv_depist_price.setText("£"+ newsFeedEntity.getDeposit());
                txv_depist_price.setText("£"+newsFeedEntity.getDeposit());
                txv_cancelday.setText(newsFeedEntity.getCancellations()+" days");
                txv_areacovered.setText(newsFeedEntity.getPost_location());
                if(newsFeedEntity.getInsuranceModels().size()==0){
                    txv_insure.setText("No");
                    imv_insure.setVisibility(View.GONE);
                }else {
                    txv_insure.setText("Yes");
                    imv_insure.setVisibility(View.VISIBLE);
                    lyt_insured.setOnClickListener(this);
                }

                  if(newsFeedEntity.getQualifications().size()==0){
                    txv_qualitfied.setText("No");
                    imv_qualitfied.setVisibility(View.GONE);
                }else {
                      txv_qualitfied.setText("Yes");
                      imv_qualitfied.setVisibility(View.VISIBLE);
                      lyt_qualitfied.setOnClickListener(this);
                }
                break;
            case 4:
                txv_title_description.setVisibility(View.GONE);
                if(newsFeedEntity.getPostImageModels().size() ==0) {
                    lyt_image.setVisibility(View.GONE);
                }else
                    imv_txv_type.setVisibility(View.GONE);

                lyt_text.setVisibility(View.VISIBLE);
                txv_title.setText(newsFeedEntity.getTitle());
                txv_title_description.setText(newsFeedEntity.getDescription());
                votingListAdapter = new VotingListAdapter(this);
                lyt_votelist.setAdapter(votingListAdapter);
                votingListAdapter.setRoomData(newsFeedEntity.getPoll_options());
                Helper.getListViewSize(lyt_votelist);
                lyt_votelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        VotingModel votingModel = newsFeedEntity.getPoll_options().get(position);
                        for(int i =0;i<newsFeedEntity.getPoll_options().size();i++) {
                            if (Commons.myVoting(newsFeedEntity.getPoll_options().get(i))) {
                                showAlertDialog("You've aleady voted on this poll!");
                                return;
                            }
                        }

                        /// change poll event
                        addVoting(votingModel.getPost_id(),votingModel.getPoll_value(),position);
                    }
                });
                break;
        }

        if(newsFeedEntity.isFeedLike()){
            imv_like.setImageDrawable(getResources().getDrawable(R.drawable.icon_heart));
            imv_like.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
            txv_like1.setTextColor(getResources().getColor(R.color.head_color));
            txv_like.setTextColor(getResources().getColor(R.color.head_color));
        }else {
            imv_like.setImageDrawable(getResources().getDrawable(R.drawable.icon_health_unselect));
            imv_like.setColorFilter(getResources().getColor(R.color.txt_color), PorterDuff.Mode.SRC_IN);
            txv_like1.setTextColor(getResources().getColor(R.color.txt_color));
            txv_like.setTextColor(getResources().getColor(R.color.txt_color));
        }

        if(newsFeedEntity.isFeedSave()){
            imv_bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmark_fill));
            imv_bookmark.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }else {
            imv_bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmar_unfill));
            imv_bookmark.setColorFilter(getResources().getColor(R.color.txt_color), PorterDuff.Mode.SRC_IN);
        }
        if(newsFeedEntity.getUser_id()==Commons.g_user.getId() || newsFeedEntity.getIs_sold() ==1 ) {
            imv_bookmark.setVisibility(View.GONE);
            lyt_sale_button.setVisibility(View.GONE);
            lyt_book_service.setVisibility(View.GONE);
            for(int i =0;i<lyt_attribute.size();i++)
                lyt_attribute.get(i).setVisibility(View.GONE);
        }
        if(newsFeedEntity.getCommentModels().size()>0)
        commentAdapter.setRoomData(newsFeedEntity.getCommentModels());
        Helper.getListViewSize(list_comment);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                //setResult(RESULT_OK);
                finish(this);
                break;
            case R.id.imv_close:
                comment_level = 0;
                hideKeyboard();
                initComment();
                break;
            case R.id.imv_profile:
                if(newsFeedEntity.getUser_id() != Commons.g_user.getId())
                    getuserProfile(newsFeedEntity.getUser_id(),newsFeedEntity.getPoster_profile_type());

                else {
                    if(newsFeedEntity.getPoster_profile_type() ==1)
                        startActivityForResult(new Intent(this, ProfileBusinessNaviagationActivity.class),1);
                    else
                        goTo(this, ProfileUserNavigationActivity.class,false);
                }
                break;
            case R.id.imv_navigation:
                if(newsFeedEntity.getUser_id() != Commons.g_user.getId() && flowerid==-1)
                    getFollow();
                else
                    gotoSettingPopup();
                break;
            case R.id.imv_camera:
                selectImage();
                break;
            case R.id.imv_send:
                if(completedValue.size()>0|| edt_comment.getText().toString().length()>0)
                    sendComment();
                break;
            case R.id.lyt_like:
                if(newsFeedEntity.getUser_id() == Commons.g_user.getId()){
                    showAlertDialog("You can not like your own post");
                    return;
                }
                addLike();
                break;
            case R.id.imv_bookmark:
               savePost();
                break;
            case R.id.txv_buy_mesasge:
                gotochat(this,newsFeedEntity.getPoster_profile_type(),newsFeedEntity.getUserModel());

                break;
            case R.id.txv_buy_sale:

                if(selected_Variation.size()==newsFeedEntity.getAttribute_map().size()){
                    selectDeliveryDialog();
                }else{
                     ProductVariationSelectDialog productVariationSelectDialog = new ProductVariationSelectDialog();
                    productVariationSelectDialog.setOnConfirmListener(new ProductVariationSelectDialog.OnConfirmListener() {
                        @Override
                        public void onPurchase( ArrayList<String> Variation) {
                            selected_Variation = Variation;
                            selectDeliveryDialog();
                        }
                    },newsFeedEntity,selected_Variation);
                    productVariationSelectDialog.show(getSupportFragmentManager(), "DeleteMessage");
                }
                break;
            case R.id.imv_bubble:
                gotochat(this,newsFeedEntity.getPoster_profile_type(),newsFeedEntity.getUserModel());
                break;
            case R.id.lyt_book_service:
                Bundle bundle = new Bundle();

                Gson gson = new Gson();
                String newfeedentity = gson.toJson(newsFeedEntity);
                bundle.putString("newsFeedEntity",newfeedentity);
                startActivityForResult(new Intent(this, BookFromPostActivity.class).putExtra("data",bundle),1);
                break;
            case R.id.lyt_deposit:
                ConfirmVariationDialog confirmBookingDialog = new ConfirmVariationDialog(1);
                confirmBookingDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                    }
                });
                confirmBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.lyt_area:
                bundle = new Bundle();
                bundle.putDouble("lat",newsFeedEntity.getLat());
                bundle.putDouble("lang" ,newsFeedEntity.getLang());
                goTo(this, LocationMapActivity.class,false,bundle);
                break;
            case R.id.lyt_cancel:
                confirmBookingDialog = new ConfirmVariationDialog(2);
                confirmBookingDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                    }
                });
                confirmBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.lyt_insured:

                InsuranceViewDialog insuranceViewDialog = new InsuranceViewDialog();
                insuranceViewDialog.setOnConfirmListener(new InsuranceViewDialog.OnConfirmListener() {
                    @Override
                    public void onDownload() {
                        new DownloadFileFromURL().execute(newsFeedEntity.getInsuranceModels().get(0).getFile());
                    }
                },newsFeedEntity.getInsuranceModels(),0);
                insuranceViewDialog.show(getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.lyt_qualitfied:
                insuranceViewDialog = new InsuranceViewDialog();
                insuranceViewDialog.setOnConfirmListener(new InsuranceViewDialog.OnConfirmListener() {
                    @Override
                    public void onDownload() {
                        new DownloadFileFromURL().execute(newsFeedEntity.getQualifications().get(0).getFile());
                    }
                },newsFeedEntity.getQualifications(),1);
                insuranceViewDialog.show(getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.lyt_location:
                bundle = new Bundle();
                bundle.putDouble("lat",newsFeedEntity.getLat());
                bundle.putDouble("lang" ,newsFeedEntity.getLang());
                goTo(this, LocationMapActivity.class,false,bundle);
                break;
        }
    }
    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                Log.d("aaaaa",path.getPath());
                String[] str = f_url[0].split("/");
                File file = new File(path, str[str.length-1]);

                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                closeProgress();
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            closeProgress();
            showAlertDialog("The file has been downloaded and saved successfully!");

        }

    }
    void selectDeliveryDialog() {
        if (selected_Variation.size() > 0){
            VariationModel variationModel = newsFeedEntity.productHasStock(selected_Variation);
            if (variationModel.getStock_level() == 0) {
                showAlertDialog("The product is out of stock!");
                return;
            }
        }
        SelectDeliveryoptionDialog deliveryoptionDialog = new SelectDeliveryoptionDialog();
        deliveryoptionDialog.setOnConfirmListener(new SelectDeliveryoptionDialog.OnConfirmListener() {
            @Override
            public void onConfirm(int type) {
                confirmPaymentDialog(type);
                deliveryOption = type;
            }
        },newsFeedEntity);
        deliveryoptionDialog.show(getSupportFragmentManager(), "DeleteMessage");
    }
    void confirmPaymentDialog(int type){
        PaymentBookingDialog paymentBookingDialog = new PaymentBookingDialog();
        paymentBookingDialog.setOnConfirmListener(new PaymentBookingDialog.OnConfirmListener() {
            @Override
            public void onConfirm(int payment_type,double price) {
                if(payment_type ==1){
                    gotochat(NewsDetailActivity.this,newsFeedEntity.getPoster_profile_type(),newsFeedEntity.getUserModel());
                }else {
                    getPaymentToken(String.valueOf(price),newsFeedEntity,deliveryOption,selected_Variation);
                }
            }
        },newsFeedEntity,type,selected_Variation);
        paymentBookingDialog.show(getSupportFragmentManager(), "DeleteMessage");
    }

    @Override
    public void processPayment(String price, String client_id,String clicnet_token, NewsFeedEntity newsFeedEntity1,int deliveryOption1, ArrayList<String> selected_Variation1){
        payment_params.clear();
        payment_params.put("token",Commons.token);
        payment_params.put("customerId",Commons.g_user.getBt_customer_id());
        payment_params.put("amount",price);
        payment_params.put("toUserId", String.valueOf(newsFeedEntity.getUser_id()));
        payment_params.put("is_business",String.valueOf(newsFeedEntity.getPoster_profile_type() ));
        payment_params.put("quantity","1");
        payment_params.put("delivery_option",String.valueOf(deliveryOption));
        if(selected_Variation.size()>0){
            VariationModel variationModel = newsFeedEntity.productHasStock(selected_Variation);
            payment_params.put("variation_id",String.valueOf(variationModel.getId()));
           // payment_params.put("product_id",String.valueOf(variationModel.getProduct_id()));
        }else {
            payment_params.put("product_id",String.valueOf(newsFeedEntity.getProduct_id()));
        }
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clicnet_token)
                .cardholderNameStatus(CardForm.FIELD_OPTIONAL)
                .collectDeviceData(true)
                .vaultManager(true);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_PAYMENT_CODE);
    }

    @Override
    public void finishPayment(String transaction_id) {
        PaymentSuccessDialog paymentSuccessDialog = new PaymentSuccessDialog();
        paymentSuccessDialog.setOnConfirmListener(new PaymentSuccessDialog.OnConfirmListener() {
            @Override
            public void onPurchase() {
                goTo(NewsDetailActivity.this, PurchasesActivity.class,false);
            }
        },newsFeedEntity);
        paymentSuccessDialog.show(getSupportFragmentManager(), "DeleteMessage");
    }

    @Override
    public void UserProfile(UserModel userModel,int usertype){
        Gson gson = new Gson();
        String usermodel = gson.toJson(userModel);
        Bundle bundle = new Bundle();
        bundle.putString("userModel",usermodel);
        bundle.putInt("userType",usertype);
        goTo(this, OtherUserProfileActivity.class,false,bundle);
    }

    void deletePost(){
        showProgress();
        String api_link = API.DELETE_POST;
        if(!commentVisible){
            if(newsFeedEntity.getPost_type() == 2)
                api_link = API.DELETE_PRODUCT;
            else if(newsFeedEntity.getPost_type()==3)
                api_link = API.DELETE_SERVICE;
        }
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                api_link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                setResult(RESULT_OK);
                                finish(NewsDetailActivity.this);
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
                if(commentVisible) {
                    params.put("post_id", String.valueOf(postId));
                }else{
                    params.put("id", String.valueOf(postId));
                }
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    void addFollow(){
        String apilink = API.ADD_FOLLOW;
        if(flowerid == 1)
            apilink = API.DELETE_FOLLOWER;
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apilink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                               if(flowerid ==0){
                                   flowerid = 1;
                                   showAlertDialog("You are following this user!");
                               }
                               else{
                                   flowerid = 0;
                                   showAlertDialog("You removed this follow!");
                               }
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("follow_user_id", String.valueOf(Commons.g_user.getId()));
                params.put("follower_user_id", String.valueOf(newsFeedEntity.getUser_id()));
                if(flowerid!=1){
                    params.put("follow_business_id", "0");
                    params.put("follower_business_id", "0");
                }
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void gotoSettingPopup(){
        FeedDetailDialog feedDetailDialog = new FeedDetailDialog();
        boolean type = true;
        if(newsFeedEntity.getUser_id() == Commons.g_user.getId())
            type = false;
        boolean finalType = type;
        feedDetailDialog.setOnConfirmListener(new FeedDetailDialog.OnConfirmListener() {
            @Override
            public void onReportPost() {
                if(finalType) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("postId", postId);
                    goTo(NewsDetailActivity.this, ReportPostActivity.class, false);
                }else
                    stockOut();
            }

            @Override
            public void onBlockUser() {
                if(finalType){
                    //block user
                }else {
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            deletePost();
                        }
                    },"Would you like to remove this post?");
                    confirmDialog.show(getSupportFragmentManager(), "DeleteMessage");
                }
            }

            @Override
            public void onFollowUser() {
                if(finalType){
                    addFollow();
                }else {
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String string = gson.toJson(newsFeedEntity);
                    bundle.putBoolean("edit",true);
                    bundle.putString("newsFeedEntity",string);
                    switch (newsFeedEntity.getPost_type()) {
                        case 1:
                            startActivityForResult(new Intent(NewsDetailActivity.this, NewAdviceActivity.class).putExtra("data",bundle),REQUEST_EDITFEED);
                            overridePendingTransition(0, 0);
                            break;
                        case 2:
                            startActivityForResult(new Intent(NewsDetailActivity.this, EditSalesPostActivity.class).putExtra("data",bundle),REQUEST_EDITFEED);
                            overridePendingTransition(0, 0);
                            break;
                        case 3:
                            startActivityForResult(new Intent(NewsDetailActivity.this, NewServiceOfferActivity.class).putExtra("data",bundle),REQUEST_EDITFEED);
                            overridePendingTransition(0, 0);
                            break;
                        case 4:
                            break;
                    }

                }
            }
            @Override
            public void onShare() {
                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("nav_here",String.valueOf(newsFeedEntity.getId()))
                        .addControlParameter("android_url", "https://play.google.com/store");
                String path = "";
                if(newsFeedEntity.getPostImageModels().size()>0)
                    path = newsFeedEntity.getPostImageModels().get(0).getPath();
                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        .setCanonicalIdentifier("content/" + String.valueOf(newsFeedEntity.getId()))
                        .setTitle(newsFeedEntity.getTitle())
                        .setContentDescription(newsFeedEntity.getDescription())
                        .setContentImageUrl(path)
                        .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                        .addContentMetadata("property1", "blue")
                        .addContentMetadata("property2", "red");

                String url  = branchUniversalObject.getShortUrl(NewsDetailActivity.this,
                        linkProperties);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(url));
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        },type,flowerid,newsFeedEntity);
        feedDetailDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
    }

    void stockOut(){
        showProgress();
        String api_link =  API.SET_SOLD;
        if(newsFeedEntity.getIs_sold()==1)
            api_link = API.RELIST;
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                api_link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            if(newsFeedEntity.getIs_sold()==0)
                                newsFeedEntity.setIs_sold(1);
                            else
                                newsFeedEntity.setIs_sold(0);
                            initialLayout();
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
                params.put("product_id", String.valueOf(newsFeedEntity.getProduct_id()));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    void getFollow(){
        showProgress();

        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_FOLLOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                int posterID = 0;
                                if(newsFeedEntity.getPoster_profile_type()==1)
                                    posterID = newsFeedEntity.getUserModel().getId();
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                for(int i =0;i<jsonArray.length();i++){
                                    int followingUserID = jsonArray.getJSONObject(i).getInt("follower_user_id");
                                    int follower_user_id = jsonArray.getJSONObject(i).getInt("follower_user_id");
                                    if(followingUserID== newsFeedEntity.getUser_id() && follower_user_id ==posterID){
                                        flowerid = 1;
                                        gotoSettingPopup();
                                        return;
                                    }
                                }
                                flowerid = 0;
                                gotoSettingPopup();
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
                params.put("follow_user_id", String.valueOf(Commons.g_user.getId()));
                params.put("follow_business_id", "0");
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    void savePost(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.ADD_USER_BOOKMARK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                newsFeedEntity.setFeedSave(!newsFeedEntity.isFeedSave());
                                initialLayout();
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
                params.put("post_id", String.valueOf(postId));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void addLike(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.POST_LIKE_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                if(newsFeedEntity.isFeedLike())
                                    newsFeedEntity.setLikes(newsFeedEntity.getLikes()-1);
                                else
                                    newsFeedEntity.setLikes(newsFeedEntity.getLikes()+1);
                                newsFeedEntity.setFeedLike(!newsFeedEntity.isFeedLike());
                                initialLayout();
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
                params.put("post_id", String.valueOf(postId));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void  addVoting(int post_id, String poll_value,int posstion){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.ADD_VOTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                newsFeedEntity.getPoll_options().get(posstion).getVotes().add(Commons.g_user.getId());
                                votingListAdapter.setRoomData(newsFeedEntity.getPoll_options());

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
                params.put("post_id", String.valueOf(post_id));
                params.put("poll_value",poll_value);
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }

    void sendComment(){
        showProgress();
        try {
            Map<String, String> params = new HashMap<>();
            params.put("token", Commons.token);
            String API_LINK ="",imageTitle = "";
            //File part
            ArrayList<File> post = new ArrayList<>();
            for(int i =0;i<completedValue.size();i++){
                File file = new File(completedValue.get(i));
                post.add(file);
            }
            if(comment_level==1){
                API_LINK =  API.REPLY_COMMENT_API;
                params.put("comment_id", String.valueOf(parentModel.getId()));
                params.put("reply_user_id", String.valueOf(Commons.g_user.getId()));
                params.put("reply", StringEscapeUtils.escapeJava(edt_comment.getText().toString()));
                imageTitle = "reply_imgs";
            }
            else {
                API_LINK =  API.WRITE_COMMENT_API;
                params.put("post_id", String.valueOf(postId));
                params.put("user_id", String.valueOf(Commons.g_user.getId()));
                params.put("comment", StringEscapeUtils.escapeJava(edt_comment.getText().toString()));
                imageTitle = "comment_imgs";
            }

            Map<String, String> mHeaderPart= new HashMap<>();
            mHeaderPart.put("Content-type", "multipart/form-data; boundary=<calculated when request is sent>");
            mHeaderPart.put("Accept", "application/json");

            CustomMultipartRequest mCustomRequest = new CustomMultipartRequest(Request.Method.POST, this, API_LINK, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    closeProgress();
                    try {
                        CommentModel commentModel = new CommentModel();
                        commentModel.setComment(edt_comment.getText().toString());
                        commentModel.setLevel(comment_level);
                        commentModel.setUserName(Commons.g_user.getUserName());
                        commentModel.setUserImage(Commons.g_user.getImvUrl());
                        commentModel.setImage_url(completedValue);
                        if(comment_level==1)
                            commentModel.setParentPosstion(parentPosstion);
                        else
                            commentModel.setParentPosstion(newsFeedEntity.getCommentModels().size());

                        commentModel.setRead_created(jsonObject.getJSONObject("extra").getString("read_created"));
                        commentModel.setCommenter_user_id(Commons.g_user.getId());
                        if(comment_level==0) {
                            newsFeedEntity.getCommentModels().add(commentModel);

                        }else {
                            newsFeedEntity.getCommentModels().get(parentPosstion).getReplies().add(commentModel);
                        }
                        newsFeedEntity.setComments( newsFeedEntity.getComments()+1);
                        initialLayout();
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(View.FOCUS_DOWN);
                                hideKeyboard();
                            }
                        });
                        initComment();

                    }catch (Exception e){

                    }
                }
            }, new Response.ErrorListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    showToast("File upload failed");
                    closeProgress();
                }

            }, post, params, mHeaderPart,imageTitle);
            mCustomRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(mCustomRequest, API_LINK);

        } catch (Exception e) {

            e.printStackTrace();
            closeProgress();
            showAlertDialog("Photo Upload is failed . Please try again.");
        }
    }


    public void replyComment(int posstion,CommentModel commentModel){
        if(commentModel.getCommenter_user_id() == Commons.g_user.getId()){
            showAlertDialog(getResources().getString(R.string.your_own_comment));
            return;
        }
        comment_level = 1;
        parentPosstion = posstion;
        lyt_reply.setVisibility(View.VISIBLE);
        txv_replyname.setText(newsFeedEntity.getCommentModels().get(posstion).getUserName());
        parentModel = commentModel;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(edt_comment.getWindowToken(), 0);
    }

    public void likeComment(int posstion,CommentModel commentModel){
        if(commentModel.getCommenter_user_id() == Commons.g_user.getId()){
            showAlertDialog(getResources().getString(R.string.your_own_comment_like));
            return;
        }
        String api_link = API.POST_COMMENT_LIKE_API;
        if(commentModel.getLevel()==1)
            api_link = API.POST_COMMENT_REPLY_LIKE_API;
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                api_link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                commentModel.setLike(!commentModel.isLike());
                                commentAdapter.notifyDataSetChanged();
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
                if(commentModel.getLevel()==1)
                    params.put("reply_id", String.valueOf(commentModel.getId()));
                else
                    params.put("comment_id", String.valueOf(commentModel.getId()));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void  initComment(){
        edt_comment.setText("");
        completedValue.clear();
        reloadImages();
        comment_level =0;
        lyt_reply.setVisibility(View.GONE);
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_comment.getWindowToken(), 0);
    }
    void Keyboard(){
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_comment.getWindowToken(), 0);
                return false;
            }
        });


    }

    void selectImage(){
        Options options = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(3-completedValue.size())                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        Pix.start(this, options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            completedValue.addAll(returnValue);
            reloadImages();
        }else  if (requestCode == REQUEST_PAYMENT_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                payment_params.put("paymentNonce",Objects.requireNonNull(result.getPaymentMethodNonce()).getNonce());
                if(result.getPaymentMethodType().name().equals("PAYPAL")){
                    payment_params.put("paymentMethod","Paypal");
                }else {
                    payment_params.put("paymentMethod","Card");
                }
                paymentProcessing(payment_params,0);

                String deviceData = result.getDeviceData();
                if (result.getPaymentMethodType() == PaymentMethodType.PAY_WITH_VENMO) {
                    VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) result.getPaymentMethodNonce();
                    String venmoUsername = venmoAccountNonce.getUsername();
                }
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("error:", error.toString());
            }
        }else if(resultCode ==-200){
            finish(this);
        }else  if(resultCode == RESULT_OK && requestCode == REQUEST_EDITFEED){
            onResume();
            loadLayout();
        }

    }

    void reloadImages(){
        if(completedValue.size()>0){
            pollEmageAdapter.setData(completedValue);
            recyclerView_images.setVisibility(View.VISIBLE);
        }else {
            recyclerView_images.setVisibility(View.GONE);
        }
    }
}