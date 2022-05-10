package com.atb.app.activities.register.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.register.ProfileSetActivity;
import com.atb.app.activities.register.Signup2Activity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_back, imv_selector1, imv_selector2;
    EditText edt_password, edt_confirm_password;
    TextView txv_next;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        imv_back = findViewById(R.id.imv_back);
        imv_selector1 = findViewById(R.id.imv_selector1);
        imv_selector2 = findViewById(R.id.imv_selector2);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        txv_next = findViewById(R.id.txv_next);


        imv_back.setOnClickListener(this);
        imv_selector1.setOnClickListener(this);
        imv_selector2.setOnClickListener(this);
        txv_next.setOnClickListener(this);
        edt_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edt_password.getText().toString().equals(edt_confirm_password.getText().toString()) && edt_password.getText().toString().length() > 0) {
                    txv_next.setEnabled(true);
                    txv_next.setTextColor(_context.getResources().getColor(R.color.white));
                    imv_selector1.setEnabled(false);
                    imv_selector2.setEnabled(false);
                } else {
                    txv_next.setEnabled(false);
                    imv_selector1.setEnabled(true);
                    imv_selector2.setEnabled(true);
                    txv_next.setTextColor(_context.getResources().getColor(R.color.line_white));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                email= bundle.getString("email");
            }
        }
        Keyboard();
    }

    void Keyboard() {
        LinearLayout lytContainer = (LinearLayout) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_password.getWindowToken(), 0);
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
            case R.id.imv_selector1:
                edt_password.setText("");
                break;
            case R.id.imv_selector2:
                edt_confirm_password.setText("");
                break;
            case R.id.txv_next:

                gotoSignUp();
                break;
        }
    }

    void gotoSignUp() {
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.PWDRESET_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            Commons.token = jsonObject.getString("msg");
                            if (jsonObject.getBoolean("result")) {
                                finishAffinity();
//                                goTo(ResetPasswordActivity.this, LoginActivity.class, true);
                                finish();
                            } else {
                                showAlertDialog(Commons.token);
                            }


                        } catch (Exception e) {

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
                params.put("email", email);
                params.put("pass", edt_password.getText().toString());

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