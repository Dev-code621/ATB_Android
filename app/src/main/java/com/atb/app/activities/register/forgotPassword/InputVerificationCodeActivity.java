package com.atb.app.activities.register.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.tuo.customview.VerificationCodeView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InputVerificationCodeActivity extends CommonActivity implements View.OnClickListener {
    TextView txv_next;
    LinearLayout lyt_resend;
    ImageView imv_back;
    VerificationCodeView icv_code;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_verification_code);

        imv_back = findViewById(R.id.imv_back);
        icv_code = findViewById(R.id.icv_code);
        lyt_resend = findViewById(R.id.lyt_resend);
        txv_next = findViewById(R.id.txv_next);

        imv_back.setOnClickListener(this);
        txv_next.setOnClickListener(this);
        lyt_resend.setOnClickListener(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                email= bundle.getString("email");
            }
        }

        icv_code.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if(icv_code.getInputContent().length()==6) {
                    txv_next.setEnabled(true);
                    txv_next.setTextColor(_context.getResources().getColor(R.color.white));
                }

            }

            @Override
            public void deleteContent() {
                Log.i("icv_delete", icv_code.getInputContent());
                txv_next.setEnabled(false);
                txv_next.setTextColor(_context.getResources().getColor(R.color.line_white));
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
                verificationCode();
                break;
            case R.id.lyt_resend:
                sendResetEmail();
                break;
        }
    }
    void verificationCode(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.RESETCODE_VERIFY_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("aaaaa",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("email",email);
                                goTo(InputVerificationCodeActivity.this, ResetPasswordActivity.class,false,bundle);
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
                        //showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("verifycode", icv_code.getInputContent());
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
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
                params.put("email", email);
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