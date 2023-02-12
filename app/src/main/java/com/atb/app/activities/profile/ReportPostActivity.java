package com.atb.app.activities.profile;

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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.ContactAdminActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportPostActivity extends CommonActivity {
    ImageView imv_back;
    TextView txv_post,txv_title;
    EditText edt_description,edt_question;
    int postId ;
    String reportType = "Post";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_post);
        imv_back = findViewById(R.id.imv_back);
        txv_post = findViewById(R.id.txv_post);
        edt_description = findViewById(R.id.edt_description);
        edt_question = findViewById(R.id.edt_question);
        txv_title = findViewById(R.id.txv_title);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(ReportPostActivity.this);
            }
        });

        txv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport();
            }
        });
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                reportType = bundle.getString("reportType");
                postId= bundle.getInt("postId");
                txv_title.setText("Report " + reportType);
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
                imm.hideSoftInputFromWindow(edt_question.getWindowToken(), 0);
                return false;
            }
        });
    }

    void sendReport(){
        if(edt_question.getText().toString().length()==0){
            showAlertDialog("Please input report reason.");
            return;
        }
        if(edt_description.getText().toString().length()==0){
            showAlertDialog("Please input report description.");
            return;
        }
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.REPORT_POST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                showAlertDialog("It's been reported Successfully!");
                                //finish(ReportPostActivity.this);
                            }else
                                showAlertDialog(jsonObject.getString("msg"));
                        }catch (Exception e){
                            Log.d("Exception ",e.toString());
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
                if(reportType == "Comment"){
                    params.put("comment_id", String.valueOf(postId));
                }else{
                    params.put("post_id", String.valueOf(postId));

                }
                params.put("user_id", String.valueOf(Commons.g_user.getId()));
                params.put("reason", edt_question.getText().toString());
                params.put("content", edt_description.getText().toString());
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