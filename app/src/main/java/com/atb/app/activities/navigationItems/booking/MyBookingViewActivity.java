package com.atb.app.activities.navigationItems.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import com.atb.app.activities.navigationItems.PurchasesActivity;
import com.atb.app.activities.navigationItems.other.BookingRateActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.activities.profile.ReviewActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.CancelBookingConfirmDialog;
import com.atb.app.dialog.CancelBookingDialog;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.PaidBookingConfirmDialog;
import com.atb.app.dialog.PaymentSuccessDialog;
import com.atb.app.dialog.RequestPaypalDialog;
import com.atb.app.dialog.RequestRatingDialog;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.UserModel;
import com.atb.app.model.VariationModel;
import com.atb.app.preference.PrefConst;
import com.atb.app.preference.Preference;
import com.atb.app.util.RoundedCornersTransformation;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.models.VenmoAccountNonce;
import com.braintreepayments.cardform.view.CardForm;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fxn.pix.Pix;
import com.google.gson.Gson;
import com.zcw.togglebutton.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.atb.app.commons.Commons.REQUEST_PAYMENT_CODE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyBookingViewActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back,lyt_add_calendar,lyt_message,lyt_request_change,lyt_cancel_booking,lyt_request_paypal;
    ImageView imv_profile,imv_image;
    TextView txv_booking_name,txv_date,txv_time,txv_booking_description,txv_booking_price,txv_deposit,txv_pending_funds;
    TextView txv_finish;
    BookingEntity bookingEntity = new BookingEntity();
    Map<String, String> payment_params = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking_view);
        lyt_back = findViewById(R.id.lyt_back);
        lyt_add_calendar = findViewById(R.id.lyt_add_calendar);
        lyt_message = findViewById(R.id.lyt_message);
        lyt_request_change = findViewById(R.id.lyt_request_change);
        lyt_cancel_booking = findViewById(R.id.lyt_cancel_booking);
        imv_profile = findViewById(R.id.imv_profile);
        txv_booking_name = findViewById(R.id.txv_booking_name);
        txv_time = findViewById(R.id.txv_time);
        txv_date = findViewById(R.id.txv_date);
        txv_booking_description = findViewById(R.id.txv_booking_description);
        txv_booking_price = findViewById(R.id.txv_booking_price);
        txv_deposit = findViewById(R.id.txv_deposit);
        txv_pending_funds = findViewById(R.id.txv_pending_funds);
        lyt_request_paypal = findViewById(R.id.lyt_request_paypal);
        txv_finish = findViewById(R.id.txv_finish);
        imv_image = findViewById(R.id.imv_image);

        txv_finish.setOnClickListener(this);
        lyt_back.setOnClickListener(this);
        lyt_add_calendar.setOnClickListener(this);
        lyt_message.setOnClickListener(this);
        lyt_request_paypal.setOnClickListener(this);
        lyt_request_change.setOnClickListener(this);
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


    }

    void loadLayout(){
        String imv_url = "";
        if(bookingEntity.getUser_id()>=0) {
            imv_url = bookingEntity.getBusinessModel().getBusiness_logo();
        }
        Glide.with(_context).load(imv_url).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A6BFDE", Commons.glide_boder))).into(imv_profile);
        Glide.with(_context).load(bookingEntity.getNewsFeedEntity().getPostImageModels().get(0).getPath()).placeholder(R.drawable.image_thumnail).into(imv_image);
        txv_booking_name.setText(bookingEntity.getNewsFeedEntity().getTitle());
        txv_date.setText(getDisplayDate(bookingEntity.getBooking_datetime()));
        txv_time.setText(Commons.gettimeFromMilionSecond(bookingEntity.getBooking_datetime()));
        txv_booking_price.setText("£" + bookingEntity.getNewsFeedEntity().getPrice());
        txv_deposit.setText("£" + String.valueOf(bookingEntity.getPaid_amount()));
        txv_pending_funds.setText("£" + String.valueOf(Double.parseDouble(bookingEntity.getNewsFeedEntity().getPrice()) - bookingEntity.getPaid_amount()));
        txv_booking_description.setText(bookingEntity.getNewsFeedEntity().getDescription());
        if(Double.parseDouble(bookingEntity.getNewsFeedEntity().getPrice()) - bookingEntity.getPaid_amount() ==0)
            lyt_request_paypal.setVisibility(View.GONE);
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
                if(bookingEntity.getUser_id()>0) {
                    UserModel userModel = new UserModel();
                    userModel.setBusinessModel(bookingEntity.getBusinessModel());
                    userModel.setId(bookingEntity.getBusiness_user_id());
                    gotochat(this, 1, userModel);
                }
                else {
                    showAlertDialog("You can't chat this user because this user didn't created ATB");
                }
                break;
            case R.id.lyt_request_paypal:
                RequestPaypalDialog requestPaypalDialog = new RequestPaypalDialog(bookingEntity,1);
                requestPaypalDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        getPaymentToken( String.valueOf(Double.parseDouble(bookingEntity.getNewsFeedEntity().getPrice()) - bookingEntity.getPaid_amount()),bookingEntity.getNewsFeedEntity(),0, new ArrayList<>());
                    }
                });
                requestPaypalDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.lyt_request_change:
                Gson gson = new Gson();
                String booking = gson.toJson(bookingEntity);
                Bundle bundle = new Bundle();
                bundle.putString("bookingEntity",booking);
                startActivityForResult(new Intent(this, ChangeBookingActivity.class).putExtra("data",bundle),1);
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

    @Override
    public void processPayment(String price, String client_id, String clicnet_token, NewsFeedEntity newsFeedEntity1, int deliveryOption1, ArrayList<String> selected_Variation1){
        payment_params.clear();
        payment_params.put("token",Commons.token);
        payment_params.put("customerId",Commons.g_user.getBt_customer_id());
        payment_params.put("amount",price);
        payment_params.put("toUserId", String.valueOf(newsFeedEntity1.getUser_id()));
        payment_params.put("booking_id", String.valueOf(bookingEntity.getId()));
        payment_params.put("is_business",String.valueOf(newsFeedEntity1.getPoster_profile_type() ));
        payment_params.put("quantity","1");

        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clicnet_token)
                .cardholderNameStatus(CardForm.FIELD_OPTIONAL)
                .collectDeviceData(true)
                .vaultManager(true);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_PAYMENT_CODE);
    }

    @Override
    public void finishPayment(String transaction_id) {
        PaidBookingConfirmDialog paidBookingConfirmDialog = new PaidBookingConfirmDialog();
        paidBookingConfirmDialog.setOnConfirmListener(new PaidBookingConfirmDialog.OnConfirmListener() {
            @Override
            public void onRate() {
                setResult(RESULT_OK);
                Gson gson = new Gson();
                String book = gson.toJson(bookingEntity);
                Bundle bundle = new Bundle();
                bundle.putString("BookModel",book);
                goTo(MyBookingViewActivity.this, BookingRateActivity.class,true,bundle);

            }

            @Override
            public void goBack() {
                setResult(RESULT_OK);
                finish(MyBookingViewActivity.this);
            }
        });
        paidBookingConfirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == REQUEST_PAYMENT_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                payment_params.put("paymentNonce", Objects.requireNonNull(result.getPaymentMethodNonce()).getNonce());
                if(result.getPaymentMethodType().name().equals("PAYPAL")){
                    payment_params.put("paymentMethod","Paypal");
                }else {
                    payment_params.put("paymentMethod","Card");
                }
                paymentProcessing(payment_params,0);

                String deviceData = result.getDeviceData();
                if (result.getPaymentMethodType() == PaymentMethodType.PAY_WITH_VENMO) {
                    VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) result.getPaymentMethodNonce();
                    String venmoUsername = venmoAccountNonce.getUsername();
                }
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("error:", error.toString());
            }
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
                        finish(MyBookingViewActivity.this);
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
                                    finish(MyBookingViewActivity.this);
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
                    params.put("is_requested_by", "0");
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

    public boolean getEvent(){
        String str  = Preference.getInstance().getValue(this, PrefConst.PREFKEY_CALENDERLIST, "[]");
        Long start_time = bookingEntity.getBooking_datetime()*1000l;

        try {
            JSONArray jsonArray = new JSONArray(str);
            for(int i = 0 ; i <jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("Title").equals(bookingEntity.getNewsFeedEntity().getTitle()) && jsonObject.getLong("Start_time") == start_time){
                    showAlertDialog("This booking has already been added to your Calendar");
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;


    }
    public void addEvent() {
        if(getEvent()) return;
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
    }
}