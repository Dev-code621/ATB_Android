package com.atb.app.activities.register;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.adapter.CategoryAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.preference.PrefConst;
import com.atb.app.preference.Preference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateFeedActivity extends CommonActivity {

    TextView txv_next;
    ImageView imv_selector;
    LinearLayout lyt_selectall;
    RecyclerView recycleview_categoires;
    ArrayList<Boolean>categories  = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed);

        txv_next = findViewById(R.id.txv_next);
        imv_selector = findViewById(R.id.imv_selector);
        lyt_selectall = findViewById(R.id.lyt_selectall);
        recycleview_categoires = findViewById(R.id.recyclerView_categories);
        for(int i=0;i<10;i++)categories.add(false);
        categoryAdapter = new CategoryAdapter(this);
        categoryAdapter.setHasStableIds(true);
        recycleview_categoires.setLayoutManager(new GridLayoutManager(this,2));
        recycleview_categoires.setAdapter(categoryAdapter);
        categoryAdapter.setData(categories);
        imv_selector.setEnabled(false);
        lyt_selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<10;i++)categories.set(i,!imv_selector.isEnabled());
                imv_selector.setEnabled(!imv_selector.isEnabled());
                categoryAdapter.setData(categories);
            }
        });
        txv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedUpdate();
            }
        });

    }
    void feedUpdate(){
        showProgress();
        String feedString = "";
        for(int i =0;i<categories.size();i++){
            if(categories.get(i)) {
                if (feedString == "") {
                    feedString = Constants.category_word[i];
                } else {
                    feedString = feedString + "," + Constants.category_word[i];
                }
            }
        }
        String finalFeedString = feedString;
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.UPDATE_FEED_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("bbbbb",finalFeedString);
                        Log.d("aaaaa",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                Preference.getInstance().put(CreateFeedActivity.this, PrefConst.PREFKEY_USEREMAIL, Commons.g_user.getEmail());
                                Preference.getInstance().put(CreateFeedActivity.this, PrefConst.PREFKEY_USERPWD, Commons.g_user.getPassword());
                                finishAffinity();
                                goTo(CreateFeedActivity.this, MainActivity.class,true);
                            }else {
                                showAlertDialog(Commons.token);
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
                params.put("feeds", finalFeedString);
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }

    public void selectItem(int posstion){
        categories.set(posstion,!categories.get(posstion));
        //recycleview_categoires.setAdapter(categoryAdapter);
        categoryAdapter.setData(categories);

    }
}