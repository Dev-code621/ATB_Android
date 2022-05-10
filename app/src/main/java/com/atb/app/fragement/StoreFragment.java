package com.atb.app.fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.booking.BookFromPostActivity;
import com.atb.app.activities.navigationItems.business.UpdateBusinessActivity;
import com.atb.app.activities.navigationItems.business.UpgradeBusinessSplashActivity;
import com.atb.app.activities.newsfeedpost.EditSalesPostActivity;
import com.atb.app.activities.newsfeedpost.NewServiceOfferActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.adapter.StoreItemAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.PaymentBookingDialog;
import com.atb.app.dialog.ProductVariationSelectDialog;
import com.atb.app.dialog.SelectDeliveryoptionDialog;
import com.atb.app.model.NewsFeedEntity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.atb.app.base.BaseActivity.closeProgress;
import static com.atb.app.base.BaseActivity.showProgress;
import static com.atb.app.base.BaseActivity.showToast;

public class StoreFragment extends Fragment {
    View view;
    Context context;
    ArrayList<NewsFeedEntity>newsFeedEntities = new ArrayList<>();
    RecyclerView recyclerView;
    StoreItemAdapter storeItemAdapter ;
    ArrayList<String> selected_Variation = new ArrayList<>();
    int deliveryOption = 0;
    int REQUEST_EDITFEED =23232;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store, container, false);
        return  view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        loadStoreItems();

    }
    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
    }
    void addAdapter(){
        storeItemAdapter = new StoreItemAdapter(context, 1, 0, new StoreItemAdapter.SelectListener() {
            @Override
            public void OnItemSelect(int posstion) {
                Bundle bundle = new Bundle();
                bundle.putInt("postId",newsFeedEntities.get(posstion).getId());
                bundle.putBoolean("CommentVisible",false);
                Gson gson = new Gson();
                String usermodel = gson.toJson(newsFeedEntities.get(posstion));
                bundle.putString("newfeedEntity",usermodel);
                startActivityForResult(new Intent(context, NewsDetailActivity.class).putExtra("data",bundle),1);
            }

            @Override
            public void OnEditSelect(int posstion) {
                NewsFeedEntity newsFeedEntity = newsFeedEntities.get(posstion);
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String string = gson.toJson(newsFeedEntity);
                bundle.putBoolean("edit",true);
                bundle.putString("newsFeedEntity",string);
                if(newsFeedEntity.getPost_type()==2){
                    startActivityForResult(new Intent(getActivity()
                            , EditSalesPostActivity.class).putExtra("data",bundle),REQUEST_EDITFEED);
                    getActivity().overridePendingTransition(0, 0);
                }else if(newsFeedEntity.getPost_type() ==3){
                    startActivityForResult(new Intent(getActivity()
                            , NewServiceOfferActivity.class).putExtra("data",bundle),REQUEST_EDITFEED);
                    getActivity().overridePendingTransition(0, 0);
                }
            }

            @Override
            public void OnPostSelect(int posstion) {
                if(newsFeedEntities.get(posstion).getIs_active() !=1){
                    ((CommonActivity)(context)).showAlertDialog("The service is currently pending for admin approval, please wait until it's get approved");
                    return;
                }
                if(Commons.selected_user.getId() == Commons.g_user.getId())
                    makePost(newsFeedEntities.get(posstion));
                else {
                    if(newsFeedEntities.get(posstion).getPost_type() == 2) {
                        ProductVariationSelectDialog productVariationSelectDialog = new ProductVariationSelectDialog();
                        productVariationSelectDialog.setOnConfirmListener(new ProductVariationSelectDialog.OnConfirmListener() {
                            @Override
                            public void onPurchase(ArrayList<String> Variation) {
                                selected_Variation = Variation;
                                selectDeliveryDialog(posstion);
                            }
                        }, newsFeedEntities.get(posstion), selected_Variation);
                        productVariationSelectDialog.show(getActivity().getSupportFragmentManager(), "DeleteMessage");
                    }else if(newsFeedEntities.get(posstion).getPost_type()==3){
                        if(newsFeedEntities.get(posstion).getIs_active() !=1){
                            ((CommonActivity)context).showAlertDialog("The service is currently pending for admin approval, please wait until it's get approved");
                            return;
                        }
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        String newfeedentity = gson.toJson(newsFeedEntities.get(posstion));
                        bundle.putString("newsFeedEntity",newfeedentity);
                        startActivityForResult(new Intent(context, BookFromPostActivity.class).putExtra("data",bundle),1);
                    }
                }
            }
        });
        recyclerView.setAdapter(storeItemAdapter);
        storeItemAdapter.setDate(newsFeedEntities);
    }

    void makePost(NewsFeedEntity newsFeedEntity){
        if(!((CommonActivity)(context)).isValidMakePost())return;
        String apiLink = API.CREATE_POST_API;
       showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apiLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                closeProgress();
                                if(newsFeedEntity.getPost_type() == 3){
                                    ConfirmDialog confirmDialog = new ConfirmDialog();
                                    confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            ((CommonActivity)context).setResult(RESULT_OK);
                                            ((CommonActivity)context).finish(context);
                                        }
                                    },"ATB admin is currently reviewing your post, the review process can take up to 24 hours so please be patient.","Thanks");
                                    confirmDialog.show(getChildFragmentManager(), "DeleteMessage");

                                }else{
                                    ((CommonActivity)context).setResult(RESULT_OK);
                                    ((CommonActivity)context).finish(context);
                                }


                            }else {
                                closeProgress();
                                ((CommonActivity)(context)).showAlertDialog(jsonObject.getString("msg"));
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
                params.put("type", String.valueOf(newsFeedEntity.getPost_type()));
                params.put("media_type",String.valueOf(newsFeedEntity.getMedia_type()));
                params.put("profile_type", "1");
                params.put("title", newsFeedEntity.getTitle());
                params.put("description", newsFeedEntity.getDescription());
                params.put("brand", newsFeedEntity.getBrand());
                params.put("price", newsFeedEntity.getPrice());
                params.put("category_title", newsFeedEntity.getCategory_title());
                params.put("post_condition", newsFeedEntity.getPost_condition());
                if(newsFeedEntity.getPost_tags().equals("null"))
                    params.put("post_tags", "");
                else
                    params.put("post_tags", newsFeedEntity.getPost_tags());
                params.put("item_title", newsFeedEntity.getItem_title());
                params.put("payment_options", String.valueOf(newsFeedEntity.getPayment_options()));
                params.put("location_id", newsFeedEntity.getLocation_id());
                params.put("delivery_option", String.valueOf(newsFeedEntity.getDelivery_option()));
                params.put("delivery_cost", newsFeedEntity.getDelivery_cost());
                params.put("deposit", newsFeedEntity.getDeposit());
                params.put("lat", String.valueOf(newsFeedEntity.getLat()));
                params.put("lng", String.valueOf(newsFeedEntity.getLng()));
                params.put("is_multi", "0");

                if(newsFeedEntity.getPostImageModels().size()>0) {
                    String url = newsFeedEntity.getPostImageModels().get(0).getPath();
                    for (int i = 1; i < newsFeedEntity.getPostImageModels().size(); i++) {
                        url = url + " ," + newsFeedEntity.getPostImageModels().get(i).getPath();
                    }
                    params.put("post_img_uris", url);

                }
                if(newsFeedEntity.getPost_type() ==2){
                    params.put("product_id", String.valueOf(newsFeedEntity.getId()));
                    params.put("stock_level", String.valueOf(newsFeedEntity.getStock_level()));
                }else {
                    params.put("is_deposit_required", newsFeedEntity.getIs_deposit_required());
                    params.put("cancellations", newsFeedEntity.getCancellations());
                    params.put("insurance_id", newsFeedEntity.getInsurance_id());
                    params.put("qualification_id", newsFeedEntity.getQualification_id());
                    params.put("service_id",String.valueOf(newsFeedEntity.getId()));
                    params.put("duration", String.valueOf(newsFeedEntity.getDuration()));

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

    public void loadStoreItems(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_BUSINESS_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONObject("extra").getJSONArray("items");
                            newsFeedEntities.clear();
                            for(int i =0;i<jsonArray.length();i++){
                                NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                                newsFeedEntity.initDetailModel(jsonArray.getJSONObject(i));
                                newsFeedEntity.setUserModel(Commons.selected_user);
                                newsFeedEntity.setService_id(String.valueOf(newsFeedEntity.getId()));
//                                if(newsFeedEntity.getIs_active()==1)
                                    newsFeedEntities.add(newsFeedEntity);
                            }
                            addAdapter();

                        }catch (Exception e){
                            Log.d("aaaaa",e.toString());
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
                params.put("user_id",String.valueOf(Commons.selected_user.getId()));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void selectDeliveryDialog(int posstion) {
        SelectDeliveryoptionDialog deliveryoptionDialog = new SelectDeliveryoptionDialog();
        deliveryoptionDialog.setOnConfirmListener(new SelectDeliveryoptionDialog.OnConfirmListener() {
            @Override
            public void onConfirm(int type) {
                confirmPaymentDialog(type,posstion);
                deliveryOption = type;
            }
        },newsFeedEntities.get(posstion),selected_Variation);
        deliveryoptionDialog.show(getActivity().getSupportFragmentManager(), "DeleteMessage");
    }

    void confirmPaymentDialog(int type,int posstion){
        PaymentBookingDialog paymentBookingDialog = new PaymentBookingDialog();
        paymentBookingDialog.setOnConfirmListener(new PaymentBookingDialog.OnConfirmListener() {
            @Override
            public void onConfirm(int payment_type,double price) {
                if(payment_type ==1){
                    ((CommonActivity)context).gotochat(context,newsFeedEntities.get(posstion).getPoster_profile_type(),newsFeedEntities.get(posstion).getUserModel());

                }else {
                    ((CommonActivity)context).getPaymentToken(String.valueOf(price),newsFeedEntities.get(posstion),deliveryOption,selected_Variation);
                }
            }
        },newsFeedEntities.get(posstion),type,selected_Variation);
        paymentBookingDialog.show(getActivity().getSupportFragmentManager(), "DeleteMessage");
    }

}