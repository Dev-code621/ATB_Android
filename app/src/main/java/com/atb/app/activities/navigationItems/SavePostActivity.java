package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;

public class SavePostActivity extends CommonActivity {
    ImageView imv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_post);
        imv_back = findViewById(R.id.imv_back);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(SavePostActivity.this);
            }
        });
    }
}