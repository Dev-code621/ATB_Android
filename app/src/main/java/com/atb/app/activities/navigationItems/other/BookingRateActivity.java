package com.atb.app.activities.navigationItems.other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
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
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.ConfirmRatingDialog;
import com.atb.app.dialog.ProblemRatingDialog;
import com.atb.app.dialog.RequestPaypalDialog;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookingRateActivity extends CommonActivity {
    BookingEntity bookingEntity = new BookingEntity();
    ImageView imv_back,imv_image;
    TextView txv_booking_name,txv_date,txv_time;
    SimpleRatingBar star;
    EditText edit_review;
    TextView txv_rate,txv_problem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_rate);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                String user= bundle.getString("BookModel");
                Gson gson = new Gson();
                bookingEntity = gson.fromJson(user, BookingEntity.class);
            }
        }
        imv_back = findViewById(R.id.imv_back);
        imv_image = findViewById(R.id.imv_image);
        txv_booking_name = findViewById(R.id.txv_booking_name);
        txv_date = findViewById(R.id.txv_date);
        txv_time = findViewById(R.id.txv_time);
        star = findViewById(R.id.star);
        edit_review = findViewById(R.id.edit_review);
        txv_rate = findViewById(R.id.txv_rate);
        txv_problem = findViewById(R.id.txv_problem);

        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(BookingRateActivity.this);
            }
        });

        txv_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRating();
            }
        });

        txv_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProblemRatingDialog problemRatingDialog = new ProblemRatingDialog();
                problemRatingDialog.setOnConfirmListener(new ProblemRatingDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String problem) {
                        sendProblem(problem);

                    }
                });
                problemRatingDialog.show(getSupportFragmentManager(), "DeleteMessage");
            }
        });
        loadLayout();
        Keyboard();

    }
    void loadLayout(){
        Glide.with(this).load(bookingEntity.getNewsFeedEntity().getPostImageModels().get(0).getPath()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_image);
        txv_booking_name.setText(bookingEntity.getNewsFeedEntity().getTitle());
        txv_date.setText(getDisplayDate(bookingEntity.getBooking_datetime()));
        txv_time.setText(Commons.gettimeFromMilionSecond(bookingEntity.getBooking_datetime()));

    }
    void Keyboard() {
        LinearLayout lytContainer = findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_review.getWindowToken(), 0);
                return false;
            }
        });
    }
    static String getDisplayDate(long milionsecond){
        String date = "";
        Date d = new Date(milionsecond*1000l);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMM");
        date = formatter.format(d);
        return date;
    }

    void sendProblem(String problem ){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.REPORT_BOOKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
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
                params.put("booking_id", String.valueOf(bookingEntity.getId()));
                params.put("problem", problem);
                params.put("service_id",String.valueOf(bookingEntity.getNewsFeedEntity().getId()));
                params.put("business_id",String.valueOf(bookingEntity.getNewsFeedEntity().getUser_id()));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    void sendRating(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.ADD_BUSINESS_REVIEWS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        ConfirmRatingDialog confirmRatingDialog = new ConfirmRatingDialog();
                        confirmRatingDialog.setOnConfirmListener(new ConfirmRatingDialog.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                finish(BookingRateActivity.this);
                            }
                        });
                        confirmRatingDialog.show(getSupportFragmentManager(), "DeleteMessage");

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
                params.put("business_id", String.valueOf(bookingEntity.getBusinessModel().getId()));
                params.put("rating", String.valueOf(star.getRating()));
                params.put("review",edit_review.getText().toString());
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