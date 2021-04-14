package com.atb.app.activities.navigationItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.applikeysolutions.cosmocalendar.listeners.OnMonthChangeListener;
import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.model.Month;
import com.applikeysolutions.cosmocalendar.selection.BaseSelectionManager;
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager;
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays;
import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.navigationItems.booking.BookinViewActivity;
import com.atb.app.activities.navigationItems.booking.CreateABookingActivity;
import com.atb.app.activities.newsfeedpost.ExistSalesPostActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.adapter.BookingDateAdapter;
import com.atb.app.adapter.BookingListAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.BaseActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Helper;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.submodel.HolidayModel;
import com.atb.app.model.submodel.OpeningTimeModel;
import com.google.gson.JsonArray;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.security.AccessController.getContext;

public class BookingActivity extends CommonActivity implements View.OnClickListener, OnDaySelectedListener {
    ImageView imv_back,imv_selector;
    LinearLayout lyt_selector;
    ListView list_booking;
    int EndDate = 0,day,year,month;
    ArrayList<BookingEntity>bookingEntities = new ArrayList<>();
    ArrayList<ArrayList<String>> bookingSlot  = new ArrayList<>();
    String[] months;
    BookingListAdapter bookingListAdapter ;
    HashMap<String,BookingEntity>hashMap = new HashMap<>();
    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        imv_back = findViewById(R.id.imv_back);
        imv_selector = findViewById(R.id.imv_selector);
        lyt_selector = findViewById(R.id.lyt_selector);
        list_booking = findViewById(R.id.list_booking);
        calendarView = findViewById(R.id.calendar_view);

        imv_back.setOnClickListener(this);
        lyt_selector.setOnClickListener(this);
        imv_selector.setEnabled(false);
        bookingListAdapter = new BookingListAdapter(this);
        list_booking.setAdapter(bookingListAdapter);
        list_booking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookingEntity bookingEntity = hashMap.get(bookingSlot.get(day).get(position));
                if(bookingEntity.getType()  == 0) {
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

        calendarView.setSelectionType(SelectionType.SINGLE);
        calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Month month) {
                String mon = month.getMonthName().split(" ")[0];
                int year1 = Integer.parseInt(month.getMonthName().split(" ")[1]);
                Calendar c = Calendar.getInstance();
                int month_int = Commons.getMonthnumber(mon);
                loadBooking(year1, month_int);
            }
        });
        calendarView.setSelectionManager(new SingleSelectionManager(this));
        loadLayout();
    }



    void loadLayout(){
        months = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();
        Calendar c = Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        EndDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
;
        loadBooking(year,month);

    }
    void loadBooking(int year,int month){
        this.year =year;
        this.month =month;
        bookingListAdapter.init();
        Helper.getListViewSize(list_booking);
        Set<Long> disabledDaysSet = new HashSet<>();
        ArrayList<ArrayList<String>> slots  = Commons.g_user.getBusinessModel().getSlots();
        bookingSlot.clear();
        for(int i =1;i<=EndDate;i++){
            Calendar c = Calendar.getInstance();
            c.set(year,month,i);
            int weekDay = (c.get(Calendar.DAY_OF_WEEK)+7)%8;
            if(weekDay ==0)weekDay = 7;
            ArrayList<String>arrayList = new ArrayList<>();
            arrayList.clear();
            arrayList.addAll(slots.get(weekDay-1));
            bookingSlot.add(arrayList);
        }
        for(int i =0;i<Commons.g_user.getBusinessModel().getHolidayModels().size();i++){
            HolidayModel holidayModel = Commons.g_user.getBusinessModel().getHolidayModels().get(i);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(holidayModel.getDay_off()*1000);
            long start = holidayModel.getDay_off();
            for(int j =0;j<24*3600;j+=3600){

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((holidayModel.getDay_off()+j)*1000);
                int _day = calendar.get(Calendar.DAY_OF_MONTH);
                for(int ii=0;ii<bookingSlot.get(_day-1).size();ii++){
                    int milion = (int)start+j;
                    if(Commons.gettimeFromMilionSecond(milion).equals(bookingSlot.get(_day-1).get(ii))){
                        bookingSlot.get(_day-1).remove(ii);
                        break;
                    }
                }
            }
        }
        for(int i =1;i<=EndDate;i++){
            Calendar c = Calendar.getInstance();
            c.set(year,month,i);
            if(bookingSlot.get(i-1).size()==0)
                disabledDaysSet.add(c.getTimeInMillis());
        }

        calendarView.setDisabledDays(disabledDaysSet);
        getBooking();
    }

    void getBooking(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_BOOKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray arrayList = jsonObject.getJSONArray("extra");
                            bookingEntities.clear();
                            for(int i =0;i<arrayList.length();i++){
                                BookingEntity bookingEntity = new BookingEntity();
                                bookingEntity.initModel(arrayList.getJSONObject(i));
                                bookingEntities.add(bookingEntity);

                            }


                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("user_id", String.valueOf(Commons.g_user.getId()));
                params.put("is_business", "1");
                params.put("month", String.valueOf(year) +" " + String.valueOf(month+1));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void loadBookingByday(int day){
        hashMap.clear();
        this.day = day;
        for(int i =0;i<bookingSlot.get(day).size();i++){
            BookingEntity bookingEntity = new BookingEntity();
            int bookslot_id = slotBooked(bookingSlot.get(day).get(i));
            if(bookslot_id>=0)
                hashMap.put(bookingSlot.get(day).get(i), bookingEntities.get(bookslot_id));
            else
                hashMap.put(bookingSlot.get(day).get(i), bookingEntity);
        }
        bookingListAdapter.setRoomData(hashMap,bookingSlot.get(day));
        Helper.getListViewSize(list_booking);
    }
    int slotBooked(String str){
        for(int i =0;i<bookingEntities.size();i++){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(bookingEntities.get(i).getBooking_datetime()*1000l);
            if(str.equals(Commons.gettimeFromMilionSecond(bookingEntities.get(i).getBooking_datetime())) && calendar.get(Calendar.DAY_OF_MONTH)-1 == day)
                return i;
        }

        return -1;
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
        }
    }

    @Override
    public void onDaySelected() {
        loadBookingByday(calendarView.getSelectedDays().get(0).getDayNumber()-1);
    }
}