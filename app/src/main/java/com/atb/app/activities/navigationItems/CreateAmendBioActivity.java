package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.google.android.gms.common.internal.service.Common;

import java.util.HashMap;
import java.util.Map;

public class CreateAmendBioActivity extends CommonActivity {
    ImageView imv_back;
    TextView txv_post;
    EditText edt_bio;
    boolean userType ;
    TextView txv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_amend_bio);
        edt_bio = findViewById(R.id.edt_bio);
        imv_back = findViewById(R.id.imv_back);
        txv_post = findViewById(R.id.txv_post);
        txv_title = findViewById(R.id.txv_title);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(CreateAmendBioActivity.this);
            }
        });

        txv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_bio.getText().toString().length()==0){
                    if(userType)
                        showAlertDialog("Please input business bio.");
                    else
                        showAlertDialog("Please input user bio.");
                    return;
                }
                updateBio();

            }
        });
        Keyboard();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                userType= bundle.getBoolean("bussiness");
            }
        }
        if(userType) {
            edt_bio.setText(Commons.g_user.getBusinessModel().getBusiness_bio());
            txv_title.setText("Set Business Bio");
        }
        else {
            edt_bio.setText(Commons.g_user.getDescription());
            txv_title.setText("Set your Bio");
        }

    }

    void updateBio(){
        showProgress();
        String apilink = API.UPDATE_BIO_API;
        if(userType)
            apilink = API.UPDATE_BUSINESS_BIO;
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apilink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            if(userType)
                                Commons.g_user.getBusinessModel().setBusiness_bio(edt_bio.getText().toString());
                            else
                                Commons.g_user.setDescription(edt_bio.getText().toString());
                            finish(CreateAmendBioActivity.this);
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

                if(userType) {
                    params.put("id", String.valueOf(Commons.g_user.getBusinessModel().getId()));
                    params.put("business_bio", edt_bio.getText().toString());
                }
                else
                    params.put("bio", edt_bio.getText().toString());
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    void Keyboard(){
        LinearLayout lytContainer =  findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_bio.getWindowToken(), 0);
                return false;
            }
        });
    }
}