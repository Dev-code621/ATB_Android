package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.controls.actions.CommandAction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.adapter.NotificationAdapter;
import com.atb.app.adapter.TransactionAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NotiEntity;
import com.atb.app.model.TransactionEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionHistoryActivity extends CommonActivity {
    ImageView imv_back;
    ListView list_transaction;
    ArrayList<TransactionEntity> transactionEntities = new ArrayList<>();
    TransactionAdapter transactionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        imv_back = findViewById(R.id.imv_back);
        list_transaction = findViewById(R.id.list_transaction);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(TransactionHistoryActivity.this);
            }
        });

        transactionAdapter = new TransactionAdapter(this);
        list_transaction.setAdapter(transactionAdapter);

        loadLayout();
    }
    void loadLayout(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_PP_TRANSACTIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try{
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                transactionEntities.clear();
                                for(int i =0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    TransactionEntity transactionEntity = new TransactionEntity();
                                    transactionEntity.setId(object.getInt("id"));
                                    transactionEntity.setUser_id(object.getInt("user_id"));
                                    transactionEntity.setIs_business(object.getInt("is_business"));
                                    transactionEntity.setTransaction_id(object.getString("transaction_id"));
                                    transactionEntity.setTransaction_type(object.getString("transaction_type"));
                                    transactionEntity.setTarget_id(object.getString("target_id"));
                                    transactionEntity.setAmount(object.getDouble("amount"));
                                    transactionEntity.setPayment_method(object.getString("payment_method"));
                                    transactionEntity.setPayment_source(object.getString("payment_source"));
                                    transactionEntity.setQuantity(object.getInt("quantity"));
                                    transactionEntity.setPurchase_type(object.getString("purchase_type"));
                                    if(!object.getString("delivery_option").equals("null"))
                                        transactionEntity.setDelivery_option(object.getInt("delivery_option"));
                                    transactionEntity.setCreated_at(object.getLong("created_at"));
                                    transactionEntities.add(transactionEntity);
                                }
                                transactionAdapter.setRoomData(transactionEntities);
                            }
                        }catch (Exception e){
                            Log.d("Exception", e.toString());
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
                params.put("user_id",String.valueOf(Commons.g_user.getId()));
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