package com.atb.app.activities.newsfeedpost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.transition.Scene;

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
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.SetPostRangeActivity;
import com.atb.app.activities.navigationItems.business.BusinessProfilePaymentActivity;
import com.atb.app.activities.navigationItems.business.UpdateBusinessActivity;
import com.atb.app.activities.navigationItems.business.UpgradeBusinessSplashActivity;
import com.atb.app.adapter.MultiPostFeedAdapter;
import com.atb.app.adapter.StockAdapter;
import com.atb.app.adapter.VariationAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Helper;
import com.atb.app.dialog.AddVariationDialog;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.ConfirmVariationDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.VariationModel;
import com.atb.app.model.submodel.AttributeModel;
import com.atb.app.util.CustomMultipartRequest;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.gson.Gson;
import com.zcw.togglebutton.ToggleButton;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditSalesPostActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back,lyt_header;
    FrameLayout lyt_profile;
    TextView txv_title;
    ImageView imv_profile;
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
    HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
    HashMap<String, VariationModel>stockMap = new HashMap<>();
    ArrayList<String>stock_name = new ArrayList<>();
    VariationAdapter variationAdapter;
    StockAdapter stockAdapter;
    int isPosting;
    int media_type = 1;
    boolean cash = false, paypal = false,postage=false,collect=false,deliver =false;
    ImageView imv_variation_description;
    boolean editable =false;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sales_post);
        lyt_back = findViewById(R.id.lyt_back);
        txv_title = findViewById(R.id.txv_title);
        imv_profile = findViewById(R.id.imv_profile);
        lyt_profile = findViewById(R.id.lyt_profile);
        lyt_header = findViewById(R.id.lyt_header);


        lyt_back.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        variationAdapter = new VariationAdapter(this);
        stockAdapter = new StockAdapter(this);
        loadLayout();
        Keyboard();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                editable= bundle.getBoolean("edit");
                String string = bundle.getString("newsFeedEntity");
                Gson gson = new Gson();
                newsFeedEntity = gson.fromJson(string, NewsFeedEntity.class);
                if(editable) {
                    business_user = false;
                    if (newsFeedEntity.getPoster_profile_type() == 1) business_user = true;
                    for (int i = 0; i < newsFeedEntity.getPostImageModels().size(); i++) {
                        if (Commons.mediaVideoType(newsFeedEntity.getPostImageModels().get(i).getPath())) {
                            videovalue = newsFeedEntity.getPostImageModels().get(i).getPath();
                        } else {
                            completedValue.add(newsFeedEntity.getPostImageModels().get(i).getPath());
                        }
                    }
                    media_type = newsFeedEntity.getMedia_type();
                    spiner_media_type.setSelectedIndex(media_type-1);
                    if (media_type == 1)
                        reloadImages();
                    else if (media_type == 2)
                        reloadVideo();

                    edt_title.setText(newsFeedEntity.getTitle());
                    edt_description.setText(newsFeedEntity.getDescription());
                    String[] array = getResources().getStringArray(R.array.category_type);
                    for (int i = 0; i < array.length; i++){
                        if (array[i].equals(newsFeedEntity.getCategory_title())) {
                            spiner_category_type.setSelectedIndex(i);
                            txv_post.setText("Edit in " +  spiner_category_type.getSelectedItem().toString());
                            break;
                        }
                    }
                    edt_brand.setText(newsFeedEntity.getPost_brand());
                    edt_price.setText(newsFeedEntity.getPrice());
                    array = getResources().getStringArray(R.array.condition_type);
                    for (int i = 0; i < array.length; i++){
                        if (array[i].equals(newsFeedEntity.getPost_condition())) {
                            spiner_condition.setSelectedIndex(i);
                            break;
                        }
                    }
                    edt_tag.setText(newsFeedEntity.getPost_tags());
                    edt_item.setText(newsFeedEntity.getPost_item());
                    edt_stock.setText(String.valueOf(newsFeedEntity.getStock_level()));
                    for(int i =0;i<newsFeedEntity.getAttribute_map().size();i++){
                        ArrayList<AttributeModel> attributeModels = newsFeedEntity.getAttribute_map().get(newsFeedEntity.getAttribute_titles().get(i));
                        ArrayList<String>variation = new ArrayList<>();
                        for(int j =0;j<attributeModels.size();j++){
                            variation.add(attributeModels.get(j).getVariant_attirbute_value());
                        }
                        hashMap.put(newsFeedEntity.getAttribute_titles().get(i),variation);
                    }
                    getStockname(hashMap,0,"");
                    if(newsFeedEntity.getPayment_options()== 3){
                        toggle_cash.setToggleOn();
                        toggle_paypal.setToggleOn();
                        cash = true; paypal =true;
                    }else  if(newsFeedEntity.getPayment_options() ==2){
                        paypal =true;
                        toggle_paypal.setToggleOn();
                    }else if(newsFeedEntity.getPayment_options() ==1) {
                        cash = true;
                        toggle_cash.setToggleOn();
                    }
                    txv_location.setText(newsFeedEntity.getPost_location());

                    int delivery_option = newsFeedEntity.getDelivery_option();
                    if(delivery_option-5>=0){
                        deliver = true;
                        toggle_will_deliver.setToggleOn();
                        delivery_option -=5;
                        edt_deliver_cost.setText(newsFeedEntity.getDelivery_cost());
                        lyt_deliver.setVisibility(View.VISIBLE);
                    }
                    if(delivery_option -3>=0){
                        collect = true;
                        toggle_buyer_collects.setToggleOn();
                        delivery_option -=3;
                    }
                    if(delivery_option -1>=0) {
                        postage= true;
                        toggle_free_postage.setToggleOn();
                    }

                    initLayout();
                }
            }
        }
    }
    void loadLayout(){
        imageViews.clear();
        completedValue.clear();
        returnValue.clear();
        hashMap.clear();
        stockMap.clear();
        stock_name.clear();
        media_type = 1;
        RelativeLayout lyt_addtitle = findViewById(R.id.lyt_addtitle);

        imv_videothumnail = findViewById(R.id.imv_videothumnail);
        imv_videoicon = findViewById(R.id.imv_videoicon);
        FrameLayout lyt_video = findViewById(R.id.lyt_video);
        imv_imageicon = findViewById(R.id.imv_imageicon);
        imageViews.add(findViewById(R.id.imv_image));
        imageViews.add(findViewById(R.id.imv_image1));
        imageViews.add(findViewById(R.id.imv_image2));
        imageViews.add(findViewById(R.id.imv_image3));
        imageViews.add(findViewById(R.id.imv_image4));
        imageViews.add(findViewById(R.id.imv_image5));
        imageViews.add(findViewById(R.id.imv_image6));
        imageViews.add(findViewById(R.id.imv_image7));
        imageViews.add(findViewById(R.id.imv_image8));
        LinearLayout lyt_image = findViewById(R.id.lyt_image);
        lyt_business_stock = findViewById(R.id.lyt_business_stock);
        lyt_stock = findViewById(R.id.lyt_stock);
        edt_stock = findViewById(R.id.edt_stock);
        imv_stocker_check = findViewById(R.id.imv_stocker_check);
        lyt_addvariation = findViewById(R.id.lyt_addvariation);
        lyt_selectall = findViewById(R.id.lyt_selectall);
        lyt_unselect_stock = findViewById(R.id.lyt_unselect_stock);
        edt_item = findViewById(R.id.edt_item);
        edt_tag = findViewById(R.id.edt_tag);
        edt_price = findViewById(R.id.edt_price);
        edt_brand = findViewById(R.id.edt_brand);
        edt_description = findViewById(R.id.edt_description);
        edt_title = findViewById(R.id.edt_title);
        txv_location = findViewById(R.id.txv_location);
        txv_post = findViewById(R.id.txv_post);
        spiner_condition = findViewById(R.id.spiner_condition);
        spiner_category_type = findViewById(R.id.spiner_category_type);
        spiner_media_type = findViewById(R.id.spiner_media_type);
        list_variation = findViewById(R.id.list_variation);
        list_stock = findViewById(R.id.list_stock);
        toggle_cash = findViewById(R.id.toggle_cash);
        toggle_paypal = findViewById(R.id.toggle_paypal);
        toggle_free_postage = findViewById(R.id.toggle_free_postage);
        toggle_buyer_collects = findViewById(R.id.toggle_buyer_collects);
        toggle_will_deliver = findViewById(R.id.toggle_will_deliver);
        toggle_free_postage = findViewById(R.id.toggle_free_postage);
        edt_deliver_cost = findViewById(R.id.edt_deliver_cost);
        lyt_deliver = findViewById(R.id.lyt_deliver);
        imv_variation_description = findViewById(R.id.imv_variation_description);
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
        spiner_category_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String text ="Edit in " +  spiner_category_type.getSelectedItem().toString();
                SpannableString ss = new SpannableString(text);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                ss.setSpan(boldSpan, 0, "Edit in ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                addVariationDialog.show(EditSalesPostActivity.this.getSupportFragmentManager(), "DeleteMessage");
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

            for(int j = 0;j<newsFeedEntity.getVariationModels().size();j++){
                if(newsFeedEntity.getVariationModels().get(j).getAttributeModels().size() != attributeModels.size())break;
                int count = 0;
                for(int k = 0;k<attributeModels.size();k++){
                    if(newsFeedEntity.getVariationModels().get(j).getAttributeModels().get(k).getAttribute_title().equals(attributeModels.get(k).getAttribute_title())){
                        count ++;
                    }
                }
                if(count == attributeModels.size()){
                    variationModel.setPrice(newsFeedEntity.getVariationModels().get(j).getPrice());
                    variationModel.setStock_level(newsFeedEntity.getVariationModels().get(j).getStock_level());
                }

            }
            variationModel.setAttributeModels(attributeModels);
            stockMap.put(stock_name.get(i),variationModel);
        }
        list_stock.setAdapter(stockAdapter);
        stockAdapter.setEditRoomData(stockMap);
        Helper.getListViewSize(list_stock);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.lyt_profile:
                if(Commons.g_user.getAccount_type()==1)
                    SelectprofileDialog(this);
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
                updateSale();
                break;
            case R.id.txv_location:
                Bundle bundle = new Bundle();
                bundle.putBoolean("flag", true);
                startActivityForResult(new Intent(this, SetPostRangeActivity.class).putExtra("data",bundle),1);
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
                        finish(EditSalesPostActivity.this);
                    }
                });
                confirmBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
        }
    }

    void updateSale(){

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
//        }else if(edt_tag.getText().toString().length()==0){
//            showAlertDialog("Please input tags.");
//            return;
//        }else if(edt_item.getText().toString().length()==0){
//            showAlertDialog("Please input the item.");
//            return;
        }else if(!cash && !paypal){
            showAlertDialog("Please input payment option.");
            return;
        }else if(txv_location.getText().toString().length()==0){
            showAlertDialog("Please input the location");
            return;
        }else if( !collect){
            showAlertDialog("Please select a delivery option.");
            return;
        }else  if(deliver){
            if(edt_deliver_cost.getText().toString().length()==0){
                showAlertDialog("Please input the cost for delivery.");
                return;
            }

        }

        int post_TYPE = 0;
        if(business_user)
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
        uploadSalePost();
    }
    void uploadSalePost(){

        showProgress();
        try {
            Map<String, String> params = new HashMap<>();
            params.put("token", Commons.token);
            params.put("id", newsFeedEntity.getProduct_id());
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
            params.put("stock_level",String.valueOf(newsFeedEntity.getStock_level()));

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
            String post_image_uris = "";
            if(media_type ==1) {
                for (int i = 0; i < completedValue.size(); i++) {
                    if(URLUtil.isNetworkUrl(completedValue.get(i)))
                        post_image_uris += completedValue.get(i) + ",";
                    else {
                        post_image_uris  +="data" +",";
                        File file = new File(completedValue.get(i));
                        post.add(file);
                    }
                }
                post_image_uris = post_image_uris.substring(0,post_image_uris.length()-1);
            }else {
                if(URLUtil.isNetworkUrl(videovalue)){
                    post_image_uris = videovalue;
                }else {
                    post_image_uris = "data";
                    File file = new File(videovalue);
                    post.add(file);
                }
            }
            params.put("post_img_uris", post_image_uris);


            String API_LINK = API.UPDATE_PRODUCT,imageTitle = "post_imgs";
            //Log.d("aaaa",params.toString());
            Map<String, String> mHeaderPart= new HashMap<>();
            mHeaderPart.put("Content-type", "multipart/form-data; boundary=<calculated when request is sent>");
            mHeaderPart.put("Accept", "application/json");
            Log.d("aaaaa",params.toString());
            CustomMultipartRequest mCustomRequest = new CustomMultipartRequest(Request.Method.POST, this, API_LINK, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if(jsonObject.getBoolean("result")) {
//                            Log.d("bbbbb",jsonObject.toString());
                            if(newsFeedEntity.getHashMap().size()>0){
                                NewsFeedEntity newsFeedEntity1 = new NewsFeedEntity();
                                newsFeedEntity1.initDetailModel(jsonObject.getJSONObject("extra"));
                                updateProductVariants(newsFeedEntity1);
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

    void updateProductVariants(NewsFeedEntity serverResponse){
        for(int i =0;i<serverResponse.getVariationModels().size();i++){
            if(newsFeedEntity.getBooleans().get(i)){
                uploadProductVaiants(serverResponse.getVariationModels().get(i),i);
            }
        }

    }
    void uploadProductVaiants(VariationModel variationModel,int posstion){
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.UPDATE_PRODUCT_VARIANT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        if(posstion == newsFeedEntity.getHashMap().size()-1 ){
                            closeProgress();
                            setResult(RESULT_OK);
                            finish(EditSalesPostActivity.this);
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
                        goTo(EditSalesPostActivity.this, UpdateBusinessActivity.class,false);
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

                    Pix.start(EditSalesPostActivity.this, options);
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
        }
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
                    startActivityForResult(new Intent(EditSalesPostActivity.this, BusinessProfilePaymentActivity.class).putExtra("data",bundle),1);
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

}