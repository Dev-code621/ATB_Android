package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.controls.actions.CommandAction;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.atb.app.R;
import com.atb.app.adapter.NotificationAdapter;
import com.atb.app.adapter.TransactionAdapter;
import com.atb.app.base.CommonActivity;
import com.atb.app.model.NotiEntity;
import com.atb.app.model.TransactionEntity;

import java.util.ArrayList;

public class TransactionHistoryActivity extends CommonActivity {
    ImageView imv_back;
    ListView list_transaction;
    ArrayList<TransactionEntity> transactionEntities = new ArrayList<>();
    TransactionAdapter transactionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        imv_back = findViewById(R.id.imv_back);
        list_transaction = findViewById(R.id.list_transaction);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(TransactionHistoryActivity.this);
            }
        });

        transactionAdapter = new TransactionAdapter(this);
        list_transaction.setAdapter(transactionAdapter);

        for(int i =0;i<10;i++){
            TransactionEntity transactionEntity = new TransactionEntity();
            transactionEntities.add(transactionEntity);
        }
        transactionAdapter.setRoomData(transactionEntities);

    }

}