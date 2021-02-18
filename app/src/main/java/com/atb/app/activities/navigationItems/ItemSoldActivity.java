package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.adapter.SoldHeaderAdapter;
import com.atb.app.base.CommonActivity;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

public class ItemSoldActivity extends CommonActivity {
    FrameLayout lyt_profile;
    ImageView imv_business,imv_profile;
    TextView txv_ok;
    RecyclerView recyclerView;
    public static final boolean SHOW_ADAPTER_POSITIONS = true;
    SoldHeaderAdapter soldHeaderAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sold);
        lyt_profile = findViewById(R.id.lyt_profile);
        imv_business = findViewById(R.id.imv_business);
        imv_profile = findViewById(R.id.imv_profile);
        txv_ok = findViewById(R.id.txv_ok);
        recyclerView = findViewById(R.id.recyclerView);

        txv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(ItemSoldActivity.this);
            }
        });

        lyt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectprofileDialog(ItemSoldActivity.this);
            }
        });

        StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        recyclerView.setLayoutManager(stickyHeaderLayoutManager);
        // set a header position callback to set elevation on sticky headers, because why not
        stickyHeaderLayoutManager.setHeaderPositionChangedCallback(new StickyHeaderLayoutManager.HeaderPositionChangedCallback() {
            @Override
            public void onHeaderPositionChanged(int sectionIndex, View header, StickyHeaderLayoutManager.HeaderPosition oldPosition, StickyHeaderLayoutManager.HeaderPosition newPosition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
                    header.setElevation(elevated ? 8 : 0);
                }
            }
        });

        soldHeaderAdapter = new SoldHeaderAdapter(2, 5, true, false, false, SHOW_ADAPTER_POSITIONS);
        recyclerView.setAdapter(soldHeaderAdapter);
    }
}