package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.atb.app.R;
import com.atb.app.adapter.NotificationAdapter;
import com.atb.app.base.CommonActivity;
import com.atb.app.model.NotiEntity;

import java.util.ArrayList;

public class NotificationActivity extends CommonActivity{

    ImageView imv_back;
    ListView list_notification;
    NotificationAdapter notificationAdapter ;
    ArrayList<NotiEntity>notiEntities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        imv_back = findViewById(R.id.imv_back);
        list_notification = findViewById(R.id.list_notification);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(NotificationActivity.this);
            }
        });

        notificationAdapter = new NotificationAdapter(this);
        list_notification.setAdapter(notificationAdapter);

        for(int i =0;i<10;i++){
            NotiEntity notiEntity = new NotiEntity();
            notiEntities.add(notiEntity);
        }
        notificationAdapter.setRoomData(notiEntities);

        list_notification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}