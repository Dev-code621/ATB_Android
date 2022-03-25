package com.atb.app.activities.navigationItems.booking;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;

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
import com.atb.app.dialog.CancelBookingConfirmDialog;
import com.atb.app.dialog.CancelBookingDialog;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.RequestPaypalDialog;
import com.atb.app.dialog.RequestRatingDialog;
import com.atb.app.model.BookingEntity;
import com.atb.app.preference.PrefConst;
import com.atb.app.preference.Preference;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.zcw.togglebutton.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingViewActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back,lyt_add_calendar,lyt_message,lyt_request_change,lyt_request_rating,lyt_cancel_booking;
    ImageView imv_profile;
    TextView txv_username,txv_useremail,txv_booking_name,txv_date,txv_time,txv_booking_description,txv_booking_price,txv_deposit,txv_pending_funds;
    TextView txv_request_paypal,txv_finish;
    ToggleButton toggle_cash;
    BookingEntity bookingEntity = new BookingEntity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookin_view);
        lyt_back = findViewById(R.id.lyt_back);
        lyt_add_calendar = findViewById(R.id.lyt_add_calendar);
        lyt_message = findViewById(R.id.lyt_message);
        lyt_request_change = findViewById(R.id.lyt_request_change);
        lyt_request_rating = findViewById(R.id.lyt_request_rating);
        lyt_cancel_booking = findViewById(R.id.lyt_cancel_booking);
        imv_profile = findViewById(R.id.imv_profile);
        txv_username = findViewById(R.id.txv_username);
        txv_useremail = findViewById(R.id.txv_useremail);
        txv_booking_name = findViewById(R.id.txv_booking_name);
        txv_time = findViewById(R.id.txv_time);
        txv_date = findViewById(R.id.txv_date);
        txv_booking_description = findViewById(R.id.txv_booking_description);
        txv_booking_price = findViewById(R.id.txv_booking_price);
        txv_deposit = findViewById(R.id.txv_deposit);
        txv_pending_funds = findViewById(R.id.txv_pending_funds);
        txv_request_paypal = findViewById(R.id.txv_request_paypal);
        txv_finish = findViewById(R.id.txv_finish);
        txv_finish.setOnClickListener(this);
        toggle_cash = findViewById(R.id.toggle_cash);

        lyt_back.setOnClickListener(this);
        lyt_add_calendar.setOnClickListener(this);
        lyt_message.setOnClickListener(this);
        txv_request_paypal.setOnClickListener(this);
        lyt_request_change.setOnClickListener(this);
        lyt_request_rating.setOnClickListener(this);
        lyt_cancel_booking.setOnClickListener(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                String books= bundle.getString("bookModel");
                Gson gson = new Gson();
                bookingEntity = gson.fromJson(books, BookingEntity.class);
            }
        }

        loadLayout();
        toggle_cash.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    if(bookingEntity.getNewsFeedEntity().getPayment_options() %2 ==1){
                        txv_request_paypal.setVisibility(View.GONE);
                        txv_finish.setVisibility(View.VISIBLE);
                    }else {
                        toggle_cash.setToggleOff();
                    }
                }else {
                    txv_request_paypal.setVisibility(View.VISIBLE);
                    txv_finish.setVisibility(View.GONE);
                }
            }
        });

    }

    void loadLayout(){
        String imv_url = "";
        if(bookingEntity.getUser_id()>=0) {
            imv_url = bookingEntity.getUserModel().getImvUrl();
        }
        Glide.with(_context).load(imv_url).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A6BFDE", Commons.glide_boder))).into(imv_profile);
        txv_username.setText( bookingEntity.getUserModel().getUserName());
        txv_useremail.setText(bookingEntity.getUserModel().getEmail());
        txv_booking_name.setText(bookingEntity.getNewsFeedEntity().getTitle());
        txv_date.setText(getDisplayDate(bookingEntity.getBooking_datetime()));
        txv_time.setText(Commons.gettimeFromMilionSecond(bookingEntity.getBooking_datetime()));
        txv_booking_price.setText("£" + bookingEntity.getNewsFeedEntity().getPrice());
        txv_deposit.setText("£" + bookingEntity.getNewsFeedEntity().getDeposit());
        txv_pending_funds.setText("£" + String.valueOf(Double.parseDouble(bookingEntity.getNewsFeedEntity().getPrice()) - Double.parseDouble(bookingEntity.getNewsFeedEntity().getDeposit())));
        getEvent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.lyt_add_calendar:
                addEvent();
                break;
            case R.id.lyt_message:
                if(bookingEntity.getUser_id()>0)
                    gotochat(this,0,bookingEntity.getUserModel());
                else {
                    showAlertDialog("You can't chat this user because this user didn't created ATB");
                }
                break;
            case R.id.txv_request_paypal:
                RequestPaypalDialog requestPaypalDialog = new RequestPaypalDialog(bookingEntity,0);
                requestPaypalDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        requestPayment(0);
                    }
                });
                requestPaypalDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.lyt_request_change:
                Gson gson = new Gson();
                String booking = gson.toJson(bookingEntity);
                Bundle bundle = new Bundle();
                bundle.putString("bookingEntity",booking);
                startActivityForResult(new Intent(this, ChangeRequestBookingActivity.class).putExtra("data",bundle),1);
                break;
            case R.id.lyt_request_rating:
                requestPayment(1);

                break;
            case R.id.lyt_cancel_booking:
                CancelBookingDialog cancelBookingDialog = new CancelBookingDialog();
                cancelBookingDialog.setOnConfirmListener(new CancelBookingDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        requestPayment(2);
                    }
                    @Override
                    public void onModifyBooking() {

                    }
                });
                cancelBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.txv_finish:
                finishBooking();
                break;


        }
    }

    void finishBooking(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.FINISHBOOKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        setResult(RESULT_OK);
                        finish(BookingViewActivity.this);
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
                params.put("booking_id", String.valueOf(bookingEntity.getId()));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void requestPayment(int type){
        showProgress();
        String api_link =   API.REQEST_PAYMENT;
        if(type == 1)
            api_link =   API.REQUEST_RATING;
        else if(type==2){
            api_link =API.CANCEL_BOOKING;
        }
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                api_link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        if(type ==0)
                            showAlertDialog("The request has been sent. We will inform you when the payment is done by the user.");
                        else if(type==1) {
                            RequestRatingDialog ratingDialog = new RequestRatingDialog();
                            ratingDialog.show(getSupportFragmentManager(), "DeleteMessage");
                        }else {
                            CancelBookingConfirmDialog cancelBookingConfirmDialog = new CancelBookingConfirmDialog();
                            cancelBookingConfirmDialog.setOnConfirmListener(new CancelBookingConfirmDialog.OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    setResult(RESULT_OK);
                                    finish(BookingViewActivity.this);
                                }

                            });
                            cancelBookingConfirmDialog.show(getSupportFragmentManager(), "DeleteMessage");
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
                params.put("booking_id", String.valueOf(bookingEntity.getId()));
                if(type<2)
                    params.put("booked_user_id", String.valueOf(bookingEntity.getUserModel().getId()));
                else
                    params.put("is_requested_by", "1");
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    static String getDisplayDate(long milionsecond){
        String date = "";
        Date d = new Date(milionsecond*1000l);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMM");
        date = formatter.format(d);
        return date;
    }

    public void getEvent(){
        String str  = Preference.getInstance().getValue(this, PrefConst.PREFKEY_CALENDERLIST, "[]");
        Long start_time = bookingEntity.getBooking_datetime()*1000l;

        try {
            JSONArray jsonArray = new JSONArray(str);
            for(int i = 0 ; i <jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("Title").equals(bookingEntity.getNewsFeedEntity().getTitle()) && jsonObject.getLong("Start_time") == start_time){
                    lyt_add_calendar.setClickable(false);
                    showAlertDialog("This booking has been ");
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lyt_add_calendar.setClickable(true);


    }
    public void addEvent() {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, bookingEntity.getNewsFeedEntity().getTitle());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, bookingEntity.getNewsFeedEntity().getPost_location());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Booking");

        GregorianCalendar calDate = new GregorianCalendar(2012, 10, 02);
        Long start_time = bookingEntity.getBooking_datetime()*1000l;
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                start_time);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                start_time+ 3600000l);

        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);


        intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        startActivity(intent);

        String str  = Preference.getInstance().getValue(this, PrefConst.PREFKEY_CALENDERLIST, "[]");
        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Title" , bookingEntity.getNewsFeedEntity().getTitle());
            jsonObject.put("Start_time" , start_time);
            jsonArray.put(jsonObject);
            Preference.getInstance().put(this, PrefConst.PREFKEY_CALENDERLIST, jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onResume();
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            showAlertDialog("The request has been sent. We will inform you when it's confirmed by the user");
            int time=data.getIntExtra("Time",0);
            bookingEntity.setBooking_datetime(time);
            loadLayout();

        }
    }
}