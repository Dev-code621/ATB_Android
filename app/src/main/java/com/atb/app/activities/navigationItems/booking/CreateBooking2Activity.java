package com.atb.app.activities.navigationItems.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.base.CommonActivity;
import com.atb.app.dialog.ConfirmBookingDialog;
import com.atb.app.dialog.ConfirmDialog;

public class CreateBooking2Activity extends CommonActivity implements View.OnClickListener {
    ImageView imv_back,imv_image;
    TextView txv_date,txv_time,txv_name,txv_price,txv_create;
    EditText edt_email,edt_name,edt_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking2);
        imv_back = findViewById(R.id.imv_back);
        imv_image = findViewById(R.id.imv_image);
        txv_date = findViewById(R.id.txv_date);
        txv_time = findViewById(R.id.txv_time);
        txv_name = findViewById(R.id.txv_name);
        txv_price = findViewById(R.id.txv_price);
        txv_create = findViewById(R.id.txv_create);
        edt_email = findViewById(R.id.edt_email);
        edt_name = findViewById(R.id.edt_name);
        edt_phone = findViewById(R.id.edt_phone);
        imv_back.setOnClickListener(this);
        txv_create.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);

                break;
            case R.id.txv_create:
                ConfirmBookingDialog confirmBookingDialog = new ConfirmBookingDialog();
                confirmBookingDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        setResult(RESULT_OK);
                        finish(CreateBooking2Activity.this);
                    }
                });
                confirmBookingDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
                break;
        }
    }
}