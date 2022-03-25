package com.atb.app.activities.navigationItems.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.BookingActivity;
import com.atb.app.base.CommonActivity;

public class BookingSplashActivity extends CommonActivity implements View.OnClickListener {

    LinearLayout lyt_business_diary,lyt_future_booking;
    ImageView imv_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_splash);

        imv_close = findViewById(R.id.imv_close);
        lyt_business_diary = findViewById(R.id.lyt_business_diary);
        lyt_future_booking = findViewById(R.id.lyt_future_booking);

        imv_close.setOnClickListener(this);
        lyt_business_diary.setOnClickListener(this);
        lyt_future_booking.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_close:
                finish(this);
                break;
            case R.id.lyt_business_diary:
                goTo(this, BookingActivity.class,true);
                break;
            case R.id.lyt_future_booking:
                goTo(this, FutureBookingActivity.class,true);
                break;
        }
    }
}