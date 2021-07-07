package com.atb.app.activities.newsfeedpost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.transition.Scene;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.SetPostRangeActivity;
import com.atb.app.activities.navigationItems.booking.CreateBooking2Activity;
import com.atb.app.activities.navigationItems.business.UpdateBusinessActivity;
import com.atb.app.activities.navigationItems.business.UpgradeBusinessSplashActivity;
import com.atb.app.adapter.MultiPostFeedAdapter;
import com.atb.app.adapter.PostFeedAdapter;
import com.atb.app.adapter.StockAdapter;
import com.atb.app.adapter.VariationAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Helper;
import com.atb.app.dialog.AddVariationDialog;
import com.atb.app.dialog.ConfirmBookingDialog;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.ConfirmVariationDialog;
import com.atb.app.dialog.GenralConfirmDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.VariationModel;
import com.atb.app.model.submodel.AttributeModel;
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
import com.zcw.togglebutton.ToggleButton;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.atb.app.commons.Commons.REQUEST_PAYMENT_CODE;

public class NewSalePostActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back,lyt_header;
    FrameLayout lyt_profile;
    TextView txv_title,txv_singlepost,txv_multpost;
    ImageView imv_profile;
    ViewGroup sceneRoot;
    Scene aScene,anotherScene;
    int multitype = 0;
    ArrayList<ImageView> imageViews = new ArrayList<>();
    boolean business_user = false;
    CardView card_business;
    int maxImagecount = 3;
    String videovalue ="";
    ArrayList<String>completedValue = new ArrayList<>();
    ArrayList<String>returnValue = new ArrayList<>();
    ImageView imv_imageicon,  imv_videoicon ,imv_videothumnail,imv_stocker_check;
    LinearLayout lyt_stock,lyt_business_stock,lyt_addvariation,lyt_selectall,lyt_unselect_stock,lyt_deliver;
    EditText edt_stock,edt_item,edt_tag,edt_price,edt_brand,edt_description,edt_title,edt_deliver_cost;
    TextView txv_location,txv_post;
    NiceSpinner spiner_condition,spiner_category_type,spiner_media_type;
    ListView list_variation,list_stock;
    ToggleButton toggle_cash,toggle_paypal,toggle_free_postage,toggle_buyer_collects,toggle_will_deliver;
    HashMap<String, ArrayList<String>>hashMap = new HashMap<>();
    HashMap<String, VariationModel>stockMap = new HashMap<>();
    ArrayList<String>stock_name = new ArrayList<>();
    VariationAdapter variationAdapter;
    StockAdapter stockAdapter;
    int isPosting;
    int media_type = 1;
    boolean cash = false, paypal = false,postage=false,collect=false,deliver =false;
    ArrayList<NewsFeedEntity> newsFeedEntities  = new ArrayList<>();
    MultiPostFeedAdapter postFeedAdapter;
    ImageView imv_variation_description;
    LinearLayout lyt_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sale_post);
        lyt_back = findViewById(R.id.lyt_back);
        txv_title = findViewById(R.id.txv_title);
        txv_singlepost = findViewById(R.id.txv_singlepost);
        txv_multpost = findViewById(R.id.txv_multpost);
        imv_profile = findViewById(R.id.imv_profile);
        lyt_profile = findViewById(R.id.lyt_profile);
        card_business = findViewById(R.id.card_business);
        lyt_header = findViewById(R.id.lyt_header);
        sceneRoot = (ViewGroup)findViewById(R.id.scene_root);
        aScene = Scene.getSceneForLayout(sceneRoot, R.layout.layout_salespost, this);
        anotherScene =
                Scene.getSceneForLayout(sceneRoot, R.layout.layout_multisales, this);


        lyt_back.setOnClickListener(this);
        txv_singlepost.setOnClickListener(this);
        txv_multpost.setOnClickListener(this);
        txv_multpost.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_round1));
        lyt_profile.setOnClickListener(this);
        activityAnimation(aScene,R.id.lyt_container);
        variationAdapter = new VariationAdapter(this);
        stockAdapter = new StockAdapter(this);
        postFeedAdapter = new MultiPostFeedAdapter(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                isPosting= bundle.getInt("isPosting");
            }
        }

        loadLayout();
        Keyboard();
    }
    void initLayout(){
        for(int i =0;i<imageViews.size();i++){
            imageViews.get(i).setVisibility(View.VISIBLE);
            if(!business_user && i>3)imageViews.get(i).setVisibility(View.GONE);
        }
        if(business_user) {
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
            lyt_business_stock.setVisibility(View.VISIBLE);
        }
        else {
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
            lyt_stock.setVisibility(View.VISIBLE);
            lyt_business_stock.setVisibility(View.GONE);
        }
        if(hashMap.size()>0){
            lyt_stock.setVisibility(View.GONE);
        }else
            lyt_stock.setVisibility(View.VISIBLE);
        if(Commons.g_user.getAccount_type()==0) card_business.setVisibility(View.GONE);

        list_variation.setAdapter(variationAdapter);
        list_variation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddVariationDialog addVariationDialog = new AddVariationDialog(hashMap,position);
                addVariationDialog.setOnConfirmListener(new AddVariationDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String name ,ArrayList<String> arrayList) {
                        ArrayList<String> list = new ArrayList<>();
                        for(int i =0;i<arrayList.size();i++){
                            if(arrayList.get(i).length()!=0)list.add(arrayList.get(i));
                        }
                        int index = 0;
                        for ( String key : hashMap.keySet() ) {
                            if( index == position && !key.equals(name)){
                                hashMap.remove(key);
                                break;
                            }
                            index++;
                        }
                        hashMap.put(name,list);
                        stock_name.clear();
                        getStockname(hashMap,0,"");
                        initLayout();
                    }
                });
                addVariationDialog.show(NewSalePostActivity.this.getSupportFragmentManager(), "DeleteMessage");
            }
        });
        variationAdapter.setRoomData(hashMap);
        Helper.getListViewSize(list_variation);
        stockMap.clear();
        for(int i =0;i<stock_name.size();i++){
            VariationModel variationModel = new VariationModel();
            int index = 0;
            ArrayList<AttributeModel>attributeModels = new ArrayList<>();
            for ( String key : hashMap.keySet() ) {
                AttributeModel attributeModel = new AttributeModel();
                attributeModel.setAttribute_title(key);
                attributeModel.setVariant_attirbute_value(stock_name.get(i).split(",")[index]);
                attributeModels.add(attributeModel);
                index++;
            }
            variationModel.setAttributeModels(attributeModels);
            stockMap.put(stock_name.get(i),variationModel);
        }
        list_stock.setAdapter(stockAdapter);
        stockAdapter.setRoomData(stockMap);
        Helper.getListViewSize(list_stock);
    }
    void loadLayout(){
        imageViews.clear();
        completedValue.clear();
        returnValue.clear();
        hashMap.clear();
        stockMap.clear();
        stock_name.clear();
        media_type = 1;
        RelativeLayout lyt_addtitle = sceneRoot.findViewById(R.id.lyt_addtitle);
        TextView txv_add = sceneRoot.findViewById(R.id.txv_add);
        ImageView icon_back = sceneRoot.findViewById(R.id.icon_back);
        TextView txv_discard = sceneRoot.findViewById(R.id.txv_discard);
        imv_videothumnail = sceneRoot.findViewById(R.id.imv_videothumnail);
        imv_videoicon = sceneRoot.findViewById(R.id.imv_videoicon);
        FrameLayout lyt_video = sceneRoot.findViewById(R.id.lyt_video);
        imv_imageicon = sceneRoot.findViewById(R.id.imv_imageicon);
        imageViews.add(sceneRoot.findViewById(R.id.imv_image));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image1));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image2));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image3));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image4));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image5));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image6));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image7));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image8));
        LinearLayout lyt_image = sceneRoot.findViewById(R.id.lyt_image);
        lyt_business_stock = sceneRoot.findViewById(R.id.lyt_business_stock);
        lyt_stock = sceneRoot.findViewById(R.id.lyt_stock);
        edt_stock = sceneRoot.findViewById(R.id.edt_stock);
        imv_stocker_check = sceneRoot.findViewById(R.id.imv_stocker_check);
        lyt_addvariation = sceneRoot.findViewById(R.id.lyt_addvariation);
        lyt_selectall = sceneRoot.findViewById(R.id.lyt_selectall);
        lyt_unselect_stock = sceneRoot.findViewById(R.id.lyt_unselect_stock);
        edt_item = sceneRoot.findViewById(R.id.edt_item);
        edt_tag = sceneRoot.findViewById(R.id.edt_tag);
        edt_price = sceneRoot.findViewById(R.id.edt_price);
        edt_brand = sceneRoot.findViewById(R.id.edt_brand);
        edt_description = sceneRoot.findViewById(R.id.edt_description);
        edt_title = sceneRoot.findViewById(R.id.edt_title);
        txv_location = sceneRoot.findViewById(R.id.txv_location);
        txv_post = sceneRoot.findViewById(R.id.txv_post);
        spiner_condition = sceneRoot.findViewById(R.id.spiner_condition);
        spiner_category_type = sceneRoot.findViewById(R.id.spiner_category_type);
        spiner_media_type = sceneRoot.findViewById(R.id.spiner_media_type);
        list_variation = sceneRoot.findViewById(R.id.list_variation);
        list_stock = sceneRoot.findViewById(R.id.list_stock);
        toggle_cash = sceneRoot.findViewById(R.id.toggle_cash);
        toggle_paypal = sceneRoot.findViewById(R.id.toggle_paypal);
        toggle_free_postage = sceneRoot.findViewById(R.id.toggle_free_postage);
        toggle_buyer_collects = sceneRoot.findViewById(R.id.toggle_buyer_collects);
        toggle_will_deliver = sceneRoot.findViewById(R.id.toggle_will_deliver);
        toggle_free_postage = sceneRoot.findViewById(R.id.toggle_free_postage);
        edt_deliver_cost = sceneRoot.findViewById(R.id.edt_deliver_cost);
        lyt_deliver = sceneRoot.findViewById(R.id.lyt_deliver);
        imv_variation_description = sceneRoot.findViewById(R.id.imv_variation_description);
        imv_variation_description.setOnClickListener(this);
        spiner_media_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                String item = String.valueOf(parent.getItemAtPosition(position));
                media_type = position+1;
                 if(position==0){
                    lyt_image.setVisibility(View.VISIBLE);
                    lyt_video.setVisibility(View.GONE);

                }else{
                    lyt_image.setVisibility(View.GONE);
                    lyt_video.setVisibility(View.VISIBLE);
                }
            }
        });
        txv_post.setText("Post in " +  spiner_category_type.getSelectedItem().toString());
        spiner_category_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String text ="Post in " +  spiner_category_type.getSelectedItem().toString();
                SpannableString ss = new SpannableString(text);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                ss.setSpan(boldSpan, 0, "Post in ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                txv_post.setText(ss);
            }
        });


        toggle_cash.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                cash = on;
            }
        });
        toggle_paypal.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                paypal = on;
            }
        });
        toggle_free_postage.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                postage = on;
            }
        });
        toggle_buyer_collects.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                collect = on;
            }
        });
        toggle_will_deliver.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                deliver = on;
                if(deliver){
                    lyt_deliver.setVisibility(View.VISIBLE);
                }else {
                    lyt_deliver.setVisibility(View.GONE);
                    edt_deliver_cost.setText("");
                }
            }
        });

        if(multitype>0) lyt_addtitle.setVisibility(View.VISIBLE);

        txv_add.setOnClickListener(this);
        icon_back.setOnClickListener(this);
        txv_discard.setOnClickListener(this);

        lyt_addvariation.setOnClickListener(this);
        for(int i =0;i<imageViews.size();i++){
            int finalI = i;
            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage(finalI);
                }
            });
        }
        imv_videothumnail.setOnClickListener(this);
        txv_post.setOnClickListener(this);
        txv_location.setOnClickListener(this);
        lyt_selectall.setOnClickListener(this);
        initLayout();
    }
    void loadlayout1(){
        lyt_header.setVisibility(View.VISIBLE);
        ListView list_multipost = sceneRoot.findViewById(R.id.list_multipost);
        LinearLayout lyt_addproduct = sceneRoot.findViewById(R.id.lyt_addproduct);
        LinearLayout lyt_publish = sceneRoot.findViewById(R.id.lyt_publish);
        lyt_text= sceneRoot.findViewById(R.id.lyt_text);
        list_multipost.setAdapter(postFeedAdapter);
        postFeedAdapter.setData(newsFeedEntities);
        lyt_addproduct.setOnClickListener(this);
        lyt_publish.setOnClickListener(this);
        if(newsFeedEntities.size()>0) {
            lyt_publish.setVisibility(View.VISIBLE);
            lyt_text.setVisibility(View.GONE);
        }else {
            lyt_publish.setVisibility(View.GONE);
            lyt_text.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.txv_singlepost:
                txv_singlepost.setBackground(getResources().getDrawable(R.drawable.button_rectangle_round));
                txv_multpost.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_round1));
                txv_singlepost.setTextColor(getResources().getColor(R.color.white));
                txv_multpost.setTextColor(getResources().getColor(R.color.txt_color));
                txv_title.setText(getResources().getString(R.string.create_oneproduct));
                activityAnimation(aScene,R.id.lyt_container);
                multitype = 0;
                loadLayout();
                break;
            case R.id.txv_multpost:
                txv_singlepost.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_round1));
                txv_multpost.setBackground(getResources().getDrawable(R.drawable.button_rectangle_round));
                txv_singlepost.setTextColor(getResources().getColor(R.color.txt_color));
                txv_multpost.setTextColor(getResources().getColor(R.color.white));
                txv_title.setText(getResources().getString(R.string.create_multiproduct));
                activityAnimation(anotherScene,R.id.lyt_container);
                multitype = 1;
                newsFeedEntities.clear();
                loadlayout1();
                break;
            case R.id.lyt_profile:
                if(Commons.g_user.getAccount_type()==1)
                    SelectprofileDialog(this);
                break;
            case R.id.lyt_addproduct:
                activityAnimation(aScene,R.id.lyt_container);
                lyt_header.setVisibility(View.GONE);
                multitype = 2;
                loadLayout();
                break;
            case R.id.lyt_publish:
                getGroupid();
                break;
            case R.id.txv_add:
                activityAnimation(anotherScene,R.id.lyt_container);
                loadlayout1();
                break;
            case R.id.icon_back:
                activityAnimation(anotherScene,R.id.lyt_container);
                loadlayout1();
                break;
            case R.id.txv_discard:
                activityAnimation(anotherScene,R.id.lyt_container);
                loadlayout1();
                break;
            case R.id.imv_videothumnail:
                videovalue = "";
                selectVideo();
                break;
            case R.id.lyt_addvariation:
                AddVariationDialog addVariationDialog = new AddVariationDialog(hashMap,-1);
                addVariationDialog.setOnConfirmListener(new AddVariationDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String name ,ArrayList<String> arrayList) {
                        ArrayList<String> list = new ArrayList<>();
                        for(int i =0;i<arrayList.size();i++)
                            if(arrayList.get(i).length()!=0)list.add(arrayList.get(i));
                        hashMap.put(name,list);
                        stock_name.clear();
                        getStockname(hashMap,0,"");
                        initLayout();
                    }
                });
                addVariationDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.txv_post:
                postVerification();
                break;
            case R.id.txv_location:
                startActivityForResult(new Intent(this, SetPostRangeActivity.class),1);
                overridePendingTransition(0, 0);
                break;
            case R.id.lyt_selectall:
                imv_stocker_check.setEnabled(!imv_stocker_check.isEnabled());
                stockAdapter.setSelect(imv_stocker_check.isEnabled());
                break;
            case R.id.imv_variation_description:
                ConfirmVariationDialog confirmBookingDialog = new ConfirmVariationDialog(0);
                confirmBookingDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        setResult(RESULT_OK);
                        finish(NewSalePostActivity.this);
                    }
                });
                confirmBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
        }
    }

    void getGroupid(){
        showProgress();
        String apiLink = API.GET_MULTI_GROUP_ID;
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apiLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            int group_id = jsonObject.getInt("msg");
                            for (int i = 0; i < newsFeedEntities.size(); i++) {
                                    uploadSalePost(newsFeedEntities.get(i),newsFeedEntities.size()-(i+1),group_id);
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

    void postVerification(){
        if(media_type==1 && completedValue.size() ==0){
            showAlertDialog("Please add image for your service");
            return;
        }else if(media_type==2 && videovalue.length() ==0){
            showAlertDialog("Please add video for your service");
            return;
        }
        else if(edt_title.getText().toString().length()==0){
            showAlertDialog("Please add the service title ");
            return;
        }else if(edt_description.getText().toString().length()==0){
            showAlertDialog("Please input the description");
            return;
        }else if(edt_brand.getText().toString().length()==0){
            showAlertDialog("Please input the brand.");
            return;
        }else if(edt_price.getText().toString().length() ==0){
            showAlertDialog("Please input price.");
            return;
        }
//        else if(edt_tag.getText().toString().length()==0){
//            showAlertDialog("Please input tags.");
//            return;
//        }else if(edt_item.getText().toString().length()==0){
//            showAlertDialog("Please input the item.");
//            return;
//        }
        else if(!cash && !paypal){
            showAlertDialog("Please input payment option.");
            return;
        }else if(txv_location.getText().toString().length()==0){
            showAlertDialog("Please input the location");
            return;
        }else if(!postage && !collect && !!deliver){
            showAlertDialog("Please select a delivery option.");
            return;
        }else  if(deliver){
            if(edt_deliver_cost.getText().toString().length()==0){
                showAlertDialog("Please input the cost for delivery.");
                return;
            }
        }

        if(paypal && Commons.g_user.getBt_paypal_account().equals("")){
            GenralConfirmDialog confirmDialog = new GenralConfirmDialog();
            confirmDialog.setOnConfirmListener(new GenralConfirmDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    getPaymentToken();
                }
            },"Setup Paypal Account", "To be able to use the PayPal payment method and take payment for your item directly in the app you will need to add your PayPal.","Add Paypal", "Cancel");
            confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
        }else
            postSale();

    }
    void postSale(){

        NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
        int post_TYPE = 0;
        if(business_user || isPosting==0)
            post_TYPE = 1;
        newsFeedEntity.setPoster_profile_type(post_TYPE);
        newsFeedEntity.setMedia_type(media_type);
        newsFeedEntity.setTitle(edt_title.getText().toString());
        newsFeedEntity.setDescription(edt_description.getText().toString());
        newsFeedEntity.setPrice(edt_price.getText().toString());
        newsFeedEntity.setIs_deposit_required("0");
        newsFeedEntity.setCategory_title(spiner_category_type.getSelectedItem().toString());
        newsFeedEntity.setLocation_id(txv_location.getText().toString());
        newsFeedEntity.setLang(Commons.lng);
        newsFeedEntity.setLat(Commons.lat);
        if(cash && paypal)
            newsFeedEntity.setPayment_options(3);
        else if(cash)
            newsFeedEntity.setPayment_options(1);
        else
            newsFeedEntity.setPayment_options(2);
        newsFeedEntity.setPost_tags(edt_tag.getText().toString());
        newsFeedEntity.setBrand(edt_brand.getText().toString());
        int delivery_type =0;
        if(postage)delivery_type+=1;
        if(collect)delivery_type+=3;
        if(deliver)delivery_type+=5;
        newsFeedEntity.setDelivery_option(delivery_type);
        String deliver_cost = "";
        if(deliver)
            deliver_cost = edt_deliver_cost.getText().toString();
        newsFeedEntity.setDelivery_cost(deliver_cost);
        newsFeedEntity.setItem_title(edt_item.getText().toString());
        newsFeedEntity.setPost_condition(spiner_condition.getSelectedItem().toString());
        if(edt_stock.getText().toString().length()==0)
            newsFeedEntity.setStock_level(0);
        else
            newsFeedEntity.setStock_level(Integer.parseInt(edt_stock.getText().toString()));
        newsFeedEntity.setIs_multi(multitype);
        if(multitype>0)
            newsFeedEntity.setIs_multi(1);
        newsFeedEntity.setHashMap(hashMap);
        newsFeedEntity.setStockMap(stockMap);
        newsFeedEntity.setBooleans(stockAdapter.booleans);
        newsFeedEntity.setStock_levels(stockAdapter.stock_levels);
        newsFeedEntity.setPrices(stockAdapter.prices);
        newsFeedEntity.setCompletedValue(completedValue);
        newsFeedEntity.setVideovalue(videovalue);
         newsFeedEntity.setPost_type(2);
        if(multitype==0){
            uploadSalePost(newsFeedEntity,0,-1);
        }else {
            newsFeedEntities.add(newsFeedEntity);
            activityAnimation(anotherScene,R.id.lyt_container);
            loadlayout1();
        }

    }

    void uploadSalePost(NewsFeedEntity newsFeedEntity,int total_number,int groupId){
        showProgress();
        try {
            Map<String, String> params = new HashMap<>();
            params.put("token", Commons.token);
            params.put("poster_profile_type", String.valueOf(newsFeedEntity.getPoster_profile_type()));
            params.put("media_type", String.valueOf(newsFeedEntity.getMedia_type()));
            params.put("title", newsFeedEntity.getTitle());
            params.put("description", newsFeedEntity.getDescription());
            params.put("price", newsFeedEntity.getPrice());
            params.put("is_deposit_required", newsFeedEntity.getIs_deposit_required());
            params.put("category_title", newsFeedEntity.getCategory_title());
            params.put("location_id", newsFeedEntity.getLocation_id());
            params.put("lat", String.valueOf(newsFeedEntity.getLat()));
            params.put("lng", String.valueOf(newsFeedEntity.getLang()));
            // 0 - none selected, 1 - Cash on Colleciton, 2 - PayPal, 3 - both Cash and PayPal
            params.put("payment_options", String.valueOf(newsFeedEntity.getPayment_options()));
            params.put("post_tags", newsFeedEntity.getPost_tags());
            params.put("brand",  newsFeedEntity.getBrand());
            params.put("delivery_option", String.valueOf(newsFeedEntity.getDelivery_option()));
            params.put("delivery_cost", newsFeedEntity.getDelivery_cost());
            params.put("item_title", newsFeedEntity.getItem_title());
            params.put("post_condition", newsFeedEntity.getPost_condition());
            params.put("make_post", String.valueOf(isPosting));
            params.put("stock_level",String.valueOf(newsFeedEntity.getStock_level()));
            params.put("is_multi", String.valueOf(newsFeedEntity.getIs_multi()));
            if(groupId!=-1){
                params.put("multi_pos", String.valueOf(newsFeedEntities.size()-total_number-1));
                params.put("multi_group", String.valueOf(groupId));
            }
            JSONArray attributes = new JSONArray();
            for ( String key : newsFeedEntity.getHashMap().keySet() ) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("attribute_name",key);
                String str = newsFeedEntity.getHashMap().get(key).get(0);
                for(int i =1;i<newsFeedEntity.getHashMap().get(key).size();i++)
                {
                    str+="," + (newsFeedEntity.getHashMap().get(key).get(i));
                }
                jsonObject.put("values",str);
                attributes.put(jsonObject);
            }
            params.put("attributes",attributes.toString());
            //File part
            ArrayList<File> post = new ArrayList<>();
            if(newsFeedEntity.getMedia_type() ==1) {
                for (int i = 0; i < newsFeedEntity.getCompletedValue().size(); i++) {
                    File file = new File(newsFeedEntity.getCompletedValue().get(i));
                    post.add(file);
                }
            }else {
                File file = new File(newsFeedEntity.getVideovalue());
                post.add(file);
            }
            String API_LINK = API.ADD_PRODUCT,imageTitle = "post_imgs";
            //Log.d("aaaa",params.toString());
            Map<String, String> mHeaderPart= new HashMap<>();
            mHeaderPart.put("Content-type", "multipart/form-data; boundary=<calculated when request is sent>");
            mHeaderPart.put("Accept", "application/json");

            CustomMultipartRequest mCustomRequest = new CustomMultipartRequest(Request.Method.POST, this, API_LINK, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if(jsonObject.getBoolean("result")) {
//                            Log.d("bbbbb",jsonObject.toString());
                            if(newsFeedEntity.getHashMap().size()>0){
                                NewsFeedEntity newsFeedEntity1 = new NewsFeedEntity();
                                newsFeedEntity1.initDetailModel(jsonObject.getJSONObject("extra"));
                                updateProductVariants(newsFeedEntity,newsFeedEntity1,total_number);
                            }else if(total_number==0) {
                                closeProgress();
                                setResult(RESULT_OK);
                                finish(NewSalePostActivity.this);
                            }
                        }else {
                            showAlertDialog(jsonObject.getString("msg"));
                        }

                    }catch (Exception e){

                    }
                }
            }, new Response.ErrorListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    showToast("File upload failed");
                    Log.d("aaaaa",volleyError.toString());
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

    void updateProductVariants(NewsFeedEntity newsFeedEntity,NewsFeedEntity serverResponse,int total_number){
        for(int i =0;i<serverResponse.getVariationModels().size();i++){
                if(newsFeedEntity.getBooleans().get(i)){
                    uploadProductVaiants(serverResponse.getVariationModels().get(i),i,newsFeedEntity, total_number);
                }
        }

    }
    void uploadProductVaiants(VariationModel variationModel,int posstion,NewsFeedEntity newsFeedEntity,int total_number){
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.UPDATE_PRODUCT_VARIANT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        if(posstion == newsFeedEntity.getHashMap().size()-1 && total_number==0){
                            closeProgress();
                            setResult(RESULT_OK);
                            finish(NewSalePostActivity.this);
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
                params.put("id", String.valueOf(variationModel.getId()));
                params.put("stock_level", newsFeedEntity.getStock_levels().get(posstion));
                params.put("price", newsFeedEntity.getPrices().get(posstion));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    void getStockname(HashMap<String,ArrayList<String>>map,int level,String string){
        if(level == map.keySet().size()){
            stock_name.add(string);
            return;
        }else {
            int index = 0;
            for ( String key : map.keySet() ) {
                if (index == level) {
                    ArrayList<String> options = map.get(key);
                    for(int i =0;i<options.size();i++){
                        String space = ",";
                        if(level +1 == map.keySet().size())
                            space = "";
                        getStockname(map,level+1,string+ options.get(i) + space);
                    }
                }
                index++;
            }
        }
    }


    void selectImage(int posstion){
        if(posstion==maxImagecount){
            SelectMediaDialog selectMediaActionDialog = new SelectMediaDialog();
            selectMediaActionDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
                @Override
                public void OnCamera() {
                    if (Commons.g_user.getAccount_type() == 1) {
                        business_user = true;
                        maxImagecount = 9;
                        initLayout();
                    }else {
                        goTo(NewSalePostActivity.this, UpdateBusinessActivity.class,false);
                    }
                }

                @Override
                public void OnAlbum() {

                }
            },getResources().getString(R.string.upload3image),getResources().getString(R.string.yes),getResources().getString(R.string.no));
            selectMediaActionDialog.show(getSupportFragmentManager(), "action picker");
        }else {
            SelectMediaDialog selectMediaActionDialog = new SelectMediaDialog();
            selectMediaActionDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
                @Override
                public void OnCamera() {
                    if(completedValue.size()==maxImagecount)return;
                    Options options = Options.init()
                            .setRequestCode(100)                                           //Request code for activity results
                            .setCount(maxImagecount-completedValue.size())                                                   //Number of images to restict selection count
                            .setFrontfacing(false)                                         //Front Facing camera on start
                            .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                            .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                            .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                            .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                            .setPath("/pix/images");                                       //Custom Path For media Storage

                    Pix.start(NewSalePostActivity.this, options);
                }

                @Override
                public void OnAlbum() {
                    if(completedValue.size()>posstion)
                        completedValue.remove(posstion);
                    reloadImages();

                }
            },getResources().getString(R.string.what_wouldlike),getResources().getString(R.string.add_media),getResources().getString(R.string.remove_media));
            selectMediaActionDialog.show(getSupportFragmentManager(), "action picker");
        }
    }
    void selectVideo(){

        Options options = Options.init()
                .setRequestCode(200)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Video)                                     //Option to select only pictures or videos or both
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
            if(completedValue.size()>maxImagecount)return;

            completedValue.addAll(returnValue);
            reloadImages();
        }else  if(resultCode == Activity.RESULT_OK && requestCode == 200) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            videovalue = returnValue.get(0);
            reloadVideo();
        }else if(resultCode == Commons.subscription_code){
            business_user = true;
            maxImagecount = 9;
            initLayout();
        }else if(resultCode == Commons.location_code){
            txv_location.setText(Commons.location);
        }else if (requestCode == REQUEST_PAYMENT_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                Map<String, String> payment_params = new HashMap<>();
                payment_params.put("token",Commons.token);
                payment_params.put("paymentMethodNonce", Objects.requireNonNull(result.getPaymentMethodNonce()).getNonce());
                payment_params.put("customerId",Commons.g_user.getBt_customer_id());
//                if(result.getPaymentMethodType().name().equals("PAYPAL")){
//                    payment_params.put("paymentMethod","Paypal");
//                }else {
//                    payment_params.put("paymentMethod","Card");
//                }
//                paymentProcessing(payment_params,0);

                String deviceData = result.getDeviceData();
                if (result.getPaymentMethodType() == PaymentMethodType.PAY_WITH_VENMO) {
                    VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) result.getPaymentMethodNonce();
                    String venmoUsername = venmoAccountNonce.getUsername();
                }
                retrievePayPal(payment_params);
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("error:", error.toString());
            }
        }
    }

    void retrievePayPal(Map<String, String> payment_params){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_PP_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            Log.d("aaaa",json);
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                Commons.g_user.setBt_paypal_account(jsonObject.getString("msg"));
                                postSale();
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
                        showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return payment_params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void reloadImages(){
        imageViews.get(0).setImageResource(0);
        for(int i =1;i<imageViews.size();i++)imageViews.get(i).setImageResource(R.drawable.icon_image1);
        if(completedValue.size()>0)imv_imageicon.setVisibility(View.GONE);
        else imv_imageicon.setVisibility(View.VISIBLE);
        for(int i =0;i<completedValue.size();i++){
            Glide.with(this).load(completedValue.get(i)).placeholder(R.drawable.icon_image1).dontAnimate().into(imageViews.get(i));

        }
    }
    void reloadVideo(){
        //uploadThumbImage = Helper.getThumbnailPathForLocalFile(this, videovalue);

        Glide.with(this).load(videovalue).placeholder(R.drawable.image_thumnail).dontAnimate().into(imv_videothumnail);

        imv_videoicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_player));

    }
    @Override
    public boolean selectProfile(boolean flag){
        if(Commons.g_user.getBusinessModel().getPaid()==0){
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    Bundle bundle = new Bundle();
                    bundle.putInt("subScriptionType",2);
                    startActivityForResult(new Intent(NewSalePostActivity.this, UpgradeBusinessSplashActivity.class).putExtra("data",bundle),1);
                    overridePendingTransition(0, 0);
                }
            },getString(R.string.subscription_alert));
            confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
        }else {
            business_user = flag;
            if (flag) maxImagecount = 9;
            else {
                maxImagecount = 3;
                hashMap.clear();
                stockMap.clear();
            }
            initLayout();
        }

        return flag;
    }

    void Keyboard(){
        LinearLayout lytContainer = (LinearLayout) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txv_title.getWindowToken(), 0);
                return false;
            }
        });
    }

    void getPaymentToken(){
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
                                DropInRequest dropInRequest = new DropInRequest()
                                        .clientToken(clicent_token)
                                        .cardholderNameStatus(CardForm.FIELD_OPTIONAL)
                                        .collectDeviceData(true)
                                        .vaultManager(true);
                                startActivityForResult(dropInRequest.getIntent(NewSalePostActivity.this), REQUEST_PAYMENT_CODE);
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

    @Override
    public void onBackPressed() {
        if(multitype ==2){
            activityAnimation(anotherScene,R.id.lyt_container);
            loadlayout1();
        }
        else
            finish(this);
    }
}