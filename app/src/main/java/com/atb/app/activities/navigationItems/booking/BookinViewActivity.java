package com.atb.app.activities.navigationItems.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.base.CommonActivity;
import com.atb.app.dialog.CancelBookingDialog;
import com.atb.app.dialog.ConfirmBookingDialog;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.RequestPaypalDialog;
import com.atb.app.dialog.RequestRatingDialog;
import com.zcw.togglebutton.ToggleButton;

public class BookinViewActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back,lyt_add_calendar,lyt_message,lyt_request_change,lyt_request_rating,lyt_cancel_booking;
    ImageView imv_profile;
    TextView txv_username,txv_useremail,txv_booking_name,txv_date,txv_time,txv_booking_description,txv_booking_price,txv_deposit,txv_pending_funds;
    TextView txv_request_paypal;
    ToggleButton toggle_cash;
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
        toggle_cash = findViewById(R.id.toggle_cash);

        lyt_back.setOnClickListener(this);
        lyt_add_calendar.setOnClickListener(this);
        lyt_message.setOnClickListener(this);
        txv_request_paypal.setOnClickListener(this);
        lyt_request_change.setOnClickListener(this);
        lyt_request_rating.setOnClickListener(this);
        lyt_cancel_booking.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.lyt_add_calendar:
                break;
            case R.id.lyt_message:
                break;
            case R.id.txv_request_paypal:
                RequestPaypalDialog requestPaypalDialog = new RequestPaypalDialog();
                requestPaypalDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                    }
                });
                requestPaypalDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.lyt_request_change:
                break;
            case R.id.lyt_request_rating:
                RequestRatingDialog ratingDialog = new RequestRatingDialog();
                ratingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
            case R.id.lyt_cancel_booking:
                CancelBookingDialog cancelBookingDialog = new CancelBookingDialog();
                cancelBookingDialog.setOnConfirmListener(new CancelBookingDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onModifyBooking() {

                    }
                });
                cancelBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;


        }
    }
}