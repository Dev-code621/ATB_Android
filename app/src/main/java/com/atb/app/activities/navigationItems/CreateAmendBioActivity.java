package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;

public class CreateAmendBioActivity extends CommonActivity {
    ImageView imv_back;
    TextView txv_post;
    EditText edt_bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_amend_bio);
        edt_bio = findViewById(R.id.edt_bio);
        imv_back = findViewById(R.id.imv_back);
        txv_post = findViewById(R.id.txv_post);

        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(CreateAmendBioActivity.this);
            }
        });

        txv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(CreateAmendBioActivity.this);
            }
        });
        Keyboard();

    }

    void Keyboard(){
        LinearLayout lytContainer =  findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_bio.getWindowToken(), 0);
                return false;
            }
        });
    }
}