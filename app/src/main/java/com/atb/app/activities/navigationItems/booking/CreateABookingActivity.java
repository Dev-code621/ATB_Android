package com.atb.app.activities.navigationItems.booking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.adapter.BookingServiceAdapter;
import com.atb.app.base.CommonActivity;
import com.atb.app.model.BookingEntity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class CreateABookingActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_back;
    TextView txv_date,txv_time;
    MaterialSearchView search_view;
    CardView card_search;
    ListView list_booking;
    BookingServiceAdapter bookingServiceAdapter;
    ArrayList<BookingEntity>bookingEntities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_a_booking);
        search_view = findViewById(R.id.search_view);
        card_search = findViewById(R.id.card_search);
        imv_back = findViewById(R.id.imv_back);
        txv_date = findViewById(R.id.txv_date);
        list_booking = findViewById(R.id.list_booking);
        txv_time = findViewById(R.id.txv_time);

        card_search.setOnClickListener(this);
        imv_back.setOnClickListener(this);
        bookingServiceAdapter = new BookingServiceAdapter(this);
        list_booking.setAdapter(bookingServiceAdapter);
        list_booking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                startActivityForResult(new Intent(CreateABookingActivity.this, CreateBooking2Activity.class).putExtra("data",bundle),1);
                overridePendingTransition(0, 0);
            }
        });
        search_view.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        search_view.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                card_search.setVisibility(View.VISIBLE);
            }
        });
        loadLayout();
    }

    void loadLayout(){
        for(int i =0;i<10;i++){
            BookingEntity bookingEntity = new BookingEntity();
            bookingEntities.add(bookingEntity);
        }
        bookingServiceAdapter.setRoomData(bookingEntities);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_search:
                search_view.showSearch();
                card_search.setVisibility(View.GONE);
                break;
            case R.id.imv_back:
                finish(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            finish(this);
        }
    }
}