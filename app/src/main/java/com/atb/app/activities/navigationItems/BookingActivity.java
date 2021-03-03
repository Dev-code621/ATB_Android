package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.navigationItems.booking.BookinViewActivity;
import com.atb.app.activities.navigationItems.booking.CreateABookingActivity;
import com.atb.app.activities.newsfeedpost.ExistSalesPostActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.adapter.BookingDateAdapter;
import com.atb.app.adapter.BookingListAdapter;
import com.atb.app.base.BaseActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.BookingEntity;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BookingActivity extends BaseActivity implements View.OnClickListener {
    ImageView imv_back,imv_selector;
    LinearLayout lyt_month,lyt_selector;
    TextView txt_month;
    RecyclerView recyclerView_date;
    ListView list_booking;
    int EndDate = 0,day,year,month;
    ArrayList<BookingEntity>bookingEntities = new ArrayList<>();
    ArrayList<Integer>timeSlot = new ArrayList<>();
    String[][]bookingSlot  = new String[40][100];
    String[] months;
    BookingListAdapter bookingListAdapter ;
    HashMap<String,BookingEntity>hashMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        imv_back = findViewById(R.id.imv_back);
        imv_selector = findViewById(R.id.imv_selector);
        lyt_month = findViewById(R.id.lyt_month);
        lyt_selector = findViewById(R.id.lyt_selector);
        txt_month = findViewById(R.id.txt_month);
        recyclerView_date = findViewById(R.id.recyclerView_date);
        list_booking = findViewById(R.id.list_booking);

        imv_back.setOnClickListener(this);
        lyt_selector.setOnClickListener(this);
        lyt_month.setOnClickListener(this);
        imv_selector.setEnabled(false);
        bookingListAdapter = new BookingListAdapter(this);
        list_booking.setAdapter(bookingListAdapter);
        list_booking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookingEntity bookingEntity = hashMap.get(bookingSlot[day][position]);
                if(bookingEntity == null) {
                    Bundle bundle = new Bundle();
                    goTo(BookingActivity.this, CreateABookingActivity.class, false, bundle);
                    Log.d("aaa","Create");
                }else {
                    Bundle bundle = new Bundle();
                    goTo(BookingActivity.this, BookinViewActivity.class, false, bundle);
                    Log.d("aaa","view");
                }
            }
        });

        loadLayout();
    }
    void loadLayout(){
        months = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();
        Calendar c = Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        txt_month.setText(months[month]+ ", " + String.valueOf(year));
        EndDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        recyclerView_date.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        loadBooking(year,month);

    }
    void loadBooking(int year,int month){
        this.year =year;
        this.month =month;
        timeSlot.clear();
        for(int i =0;i<EndDate;i++){
            timeSlot.add(i);
            for(int j =0;j<10;j++){
                bookingSlot[i][j] = String.valueOf(j)+":00 am";
            }
        }
        BookingDateAdapter bookingDateAdapter = new BookingDateAdapter(this, timeSlot,year,month, new BookingDateAdapter.OnSlotSelectListener() {
            @Override
            public void onSlotSelect(int posstion) {
                loadBookingByday(posstion+1);
            }
        });
        recyclerView_date.setAdapter(bookingDateAdapter);

    }

    void loadBookingByday(int day){
        hashMap.clear();
        this.day = day;
        for(int i =0;i<bookingSlot[day].length;i++){
            if(i%2==0) {
                BookingEntity bookingEntity = new BookingEntity();
                hashMap.put(bookingSlot[day][i], bookingEntity);
            }
        }
        bookingListAdapter.setRoomData(hashMap,bookingSlot[day]);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);

                break;
            case R.id.lyt_selector:
                imv_selector.setEnabled(!imv_selector.isEnabled());
                break;
            case R.id.lyt_month:
                new RackMonthPicker(this)
                        .setColorTheme(R.color.header_color1)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                txt_month.setText(monthLabel);
                                EndDate = endDate;
                                loadBooking(year,month);
                            }
                        })
                        .setNegativeButton(new OnCancelMonthDialogListener() {
                            @Override
                            public void onCancel(AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();

                break;
        }
    }

}