package com.atb.app.activities.register.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.activities.register.Signup2Activity;
import com.atb.app.adapter.EmailAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_selector1,imv_back;
    EditText edt_email;
    RecyclerView recyclerView_images;
    TextView txv_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        txv_next = findViewById(R.id.txv_next);
        imv_selector1 = findViewById(R.id.imv_selector1);
        imv_back = findViewById(R.id.imv_back);
        edt_email = findViewById(R.id.edt_email);
        recyclerView_images = findViewById(R.id.recyclerView_images);

        imv_back.setOnClickListener(this);
        txv_next.setOnClickListener(this);
        imv_selector1.setOnClickListener(this);
        String[] list = getResources().getStringArray(R.array.email_list);
        recyclerView_images.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView_images.setAdapter(new EmailAdapter(this, list, new EmailAdapter.OnEmailSelectListener() {
            @Override
            public void onEmailSelect(String path) {
                edt_email.setText(edt_email.getText().toString() + path);
            }
        }));

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>0 && emailInvalied(edt_email.getText().toString())){
                    txv_next.setEnabled(true);
                    txv_next.setTextColor(_context.getResources().getColor(R.color.white));
                }else {
                    imv_selector1.setEnabled(true);
                    txv_next.setEnabled(false);
                    txv_next.setTextColor(_context.getResources().getColor(R.color.line_white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Keyboard();

    }

    void Keyboard() {
        LinearLayout lytContainer = (LinearLayout) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_email.getWindowToken(), 0);
                return false;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.txv_next:
                sendResetEmail();
                break;
            case R.id.imv_selector1:
                edt_email.setText("");
                break;
        }
    }

    void sendResetEmail(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.SEND_PWDRESETEMAIL_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("aaaaa",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                Bundle bundle = new Bundle();
                                bundle.putString("email",edt_email.getText().toString());
                                goTo(ForgotPasswordActivity.this, InputVerificationCodeActivity.class,false,bundle);
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
                Map<String, String> params = new HashMap<>();
                params.put("email", edt_email.getText().toString());
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