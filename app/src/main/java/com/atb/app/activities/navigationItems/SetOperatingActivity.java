package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atb.app.R;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.adapter.HolidayAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.SelectCategoryDialog;
import com.atb.app.dialog.SelectHolidayDialog;
import com.atb.app.model.BusinessModel;
import com.atb.app.model.submodel.HolidayModel;
import com.atb.app.model.submodel.OpeningTimeModel;
import com.google.android.gms.common.internal.service.Common;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SetOperatingActivity extends CommonActivity implements View.OnClickListener{
    ImageView imv_back;
    TextView txv_regular_week,txv_holiday,txv_save;
    LinearLayout lyt_regular_week,lyt_add_calendar,lyt_description,lyt_holiday;
    ArrayList<LinearLayout>lyt_check = new ArrayList<>();
    ArrayList<ImageView>imv_selector= new ArrayList<>();
    ArrayList<TextView>txv_starttime= new ArrayList<>();
    ArrayList<TextView>txv_endtime= new ArrayList<>();
    ListView list_holiday;
    BusinessModel businessModel = new BusinessModel();
    HolidayAdapter holidayAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_operating);
        imv_back = findViewById(R.id.imv_back);
        txv_regular_week = findViewById(R.id.txv_regular_week);
        txv_holiday = findViewById(R.id.txv_holiday);
        txv_save = findViewById(R.id.txv_save);
        lyt_regular_week = findViewById(R.id.lyt_regular_week);
        lyt_add_calendar = findViewById(R.id.lyt_add_calendar);
        lyt_description = findViewById(R.id.lyt_description);
        lyt_holiday = findViewById(R.id.lyt_holiday);
        list_holiday = findViewById(R.id.list_holiday);
        lyt_check.add(findViewById(R.id.lyt_check1));
        lyt_check.add(findViewById(R.id.lyt_check2));
        lyt_check.add(findViewById(R.id.lyt_check3));
        lyt_check.add(findViewById(R.id.lyt_check4));
        lyt_check.add(findViewById(R.id.lyt_check5));
        lyt_check.add(findViewById(R.id.lyt_check6));
        lyt_check.add(findViewById(R.id.lyt_check7));
        imv_selector.add(findViewById(R.id.imv_selector1));
        imv_selector.add(findViewById(R.id.imv_selector2));
        imv_selector.add(findViewById(R.id.imv_selector3));
        imv_selector.add(findViewById(R.id.imv_selector4));
        imv_selector.add(findViewById(R.id.imv_selector5));
        imv_selector.add(findViewById(R.id.imv_selector6));
        imv_selector.add(findViewById(R.id.imv_selector7));
        txv_starttime.add(findViewById(R.id.txv_starttime1));
        txv_starttime.add(findViewById(R.id.txv_starttime2));
        txv_starttime.add(findViewById(R.id.txv_starttime3));
        txv_starttime.add(findViewById(R.id.txv_starttime4));
        txv_starttime.add(findViewById(R.id.txv_starttime5));
        txv_starttime.add(findViewById(R.id.txv_starttime6));
        txv_starttime.add(findViewById(R.id.txv_starttime7));
        txv_endtime.add(findViewById(R.id.txv_endtime1));
        txv_endtime.add(findViewById(R.id.txv_endtime2));
        txv_endtime.add(findViewById(R.id.txv_endtime3));
        txv_endtime.add(findViewById(R.id.txv_endtime4));
        txv_endtime.add(findViewById(R.id.txv_endtime5));
        txv_endtime.add(findViewById(R.id.txv_endtime6));
        txv_endtime.add(findViewById(R.id.txv_endtime7));

        imv_back.setOnClickListener(this);
        txv_save.setOnClickListener(this);
        txv_regular_week.setOnClickListener(this);
        txv_holiday.setOnClickListener(this);
        lyt_add_calendar.setOnClickListener(this);
        if(Commons.g_user.getAccount_type()==1) {
            businessModel = Commons.g_user.getBusinessModel();
            loadLayout();
        }

        holidayAdapter = new HolidayAdapter(this);
        list_holiday.setAdapter(holidayAdapter);
        holidayAdapter.setRoomData(businessModel.getHolidayModels());
        for(int i =0;i<7;i++){
            int finalI = i;
            lyt_check.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imv_selector.get(finalI).setEnabled(!imv_selector.get(finalI).isEnabled());
                    initLayout();
                }
            });
            txv_starttime.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imv_selector.get(finalI).isEnabled()){
                        Calendar now = Calendar.getInstance();
                        TimePickerDialog time = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                        DateFormat formart = new SimpleDateFormat("hh:mm a");
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);
                                        txv_starttime.get(finalI).setText(formart.format(calendar.getTime()));
                                    }
                                },
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                false
                        );
                        time.setTitle("Please choice time");
                        time.show(getSupportFragmentManager(), "Timepickerdialog");
                    }

                }
            });
            txv_endtime.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imv_selector.get(finalI).isEnabled()){
                        Calendar now = Calendar.getInstance();
                        TimePickerDialog time = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                        DateFormat formart = new SimpleDateFormat("hh:mm a");
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);
                                        txv_endtime.get(finalI).setText(formart.format(calendar.getTime()));
                                    }
                                },
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                false
                        );
                        time.setTitle("Please choice time");
                        time.show(getSupportFragmentManager(), "Timepickerdialog");
                    }
                }
            });
        }
    }

    void loadLayout(){
        for(int i =0;i<7;i++) {
            if(businessModel.getOpeningTimeModels().size()>i){
                OpeningTimeModel openingTimeModel = businessModel.getOpeningTimeModels().get(i);
                if(openingTimeModel.getIs_available()==1)
                    imv_selector.get(i).setEnabled(true);
                else
                    imv_selector.get(i).setEnabled(false);

                txv_starttime.get(i).setText(getLocaltime(openingTimeModel.getStart()));
                txv_endtime.get(i).setText(getLocaltime(openingTimeModel.getEnd()));
            }
            if(imv_selector.get(i).isEnabled()){
                txv_starttime.get(i).setBackground(_context.getResources().getDrawable(R.drawable.edit_rectangle_round));
                txv_endtime.get(i).setBackground(_context.getResources().getDrawable(R.drawable.edit_rectangle_round));
            }else {
                txv_starttime.get(i).setBackground(_context.getResources().getDrawable(R.drawable.edit_rectangle_round1));
                txv_endtime.get(i).setBackground(_context.getResources().getDrawable(R.drawable.edit_rectangle_round1));
            }
        }
        if(businessModel.getHolidayModels().size() ==0){
            lyt_description.setVisibility(View.GONE);
        }else {
            lyt_description.setVisibility(View.VISIBLE);
        }
    }

    void initLayout(){
        for(int i =0;i<7;i++) {
            if(imv_selector.get(i).isEnabled()){
                txv_starttime.get(i).setBackground(_context.getResources().getDrawable(R.drawable.edit_rectangle_round));
                txv_endtime.get(i).setBackground(_context.getResources().getDrawable(R.drawable.edit_rectangle_round));
            }else {
                txv_starttime.get(i).setBackground(_context.getResources().getDrawable(R.drawable.edit_rectangle_round1));
                txv_endtime.get(i).setBackground(_context.getResources().getDrawable(R.drawable.edit_rectangle_round1));
            }
        }
        if(businessModel.getHolidayModels().size() ==0){
            lyt_description.setVisibility(View.GONE);
        }else {
            lyt_description.setVisibility(View.VISIBLE);
        }
    }

    public void deleteHoliday(int posstion){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.DELETE_HOLIDAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                businessModel.getHolidayModels().remove(posstion);
                                holidayAdapter.setRoomData(businessModel.getHolidayModels());
                                initLayout();
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
                params.put("holiday_id", String.valueOf(businessModel.getHolidayModels().get(posstion).getId()));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    public void addHoliday(String title, long date){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.ADD_HOLIDAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject holiday = jsonObject.getJSONObject("extra");
                            HolidayModel holidayModel = new HolidayModel();
                            holidayModel.setId(holiday.getInt("id"));
                            holidayModel.setUser_id(holiday.getInt("user_id"));
                            holidayModel.setName(holiday.getString("name"));
                            holidayModel.setDay_off(holiday.getLong("day_off"));
                            holidayModel.setUpdated_at(holiday.getLong("updated_at"));
                            holidayModel.setCreated_at(holiday.getLong("created_at"));
                            businessModel.getHolidayModels().add(holidayModel);
                            holidayAdapter.setRoomData(businessModel.getHolidayModels());

                            initLayout();
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
                params.put("name", title);
                params.put("day_off", String.valueOf(date));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    void updateOperateTime(){
        showProgress();
        try {

            JSONArray weeks = new JSONArray();
            for(int i =0;i<7;i++){
                int is_available = 0;
                if(imv_selector.get(i).isEnabled())is_available = 1;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("start",getUTCDate(txv_starttime.get(i).getText().toString()));
                jsonObject.put("end",getUTCDate(txv_endtime.get(i).getText().toString()));
                jsonObject.put("day",String.valueOf(i));
                jsonObject.put("is_available",String.valueOf(is_available));
                weeks.put(jsonObject);
            }
            StringRequest myRequest = new StringRequest(
                    Request.Method.POST,
                    API.UPDTE_REGULAR_WEEK,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String json) {
                            try {
                                closeProgress();
                                JSONObject jsonObject = new JSONObject(json);
                                businessModel.getOpeningTimeModels().clear();
                                if(jsonObject.getBoolean("result")){
                                    for(int i =0;i<7;i++){
                                        OpeningTimeModel openingTimeModel = new OpeningTimeModel();
                                        int is_available = 0;
                                        if(imv_selector.get(i).isEnabled())is_available = 1;
                                        openingTimeModel.setDay(i);
                                        openingTimeModel.setIs_available(is_available);
                                        openingTimeModel.setStart(getUTCDate(txv_starttime.get(i).getText().toString()));
                                        openingTimeModel.setEnd(getUTCDate(txv_endtime.get(i).getText().toString()));
                                        businessModel.getOpeningTimeModels().add(openingTimeModel);
                                    }
                                    finish(SetOperatingActivity.this);
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
                    params.put("week", weeks.toString());
                    return params;
                }
            };
            myRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(myRequest, "tag");

        }catch (Exception e ){
            closeProgress();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.txv_save:
                updateOperateTime();
                break;
            case R.id.lyt_add_calendar:
                SelectHolidayDialog selectCategoryDialog = new SelectHolidayDialog();
                selectCategoryDialog.setOnConfirmListener(new SelectHolidayDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String title, long date) {
                        addHoliday(title,date/1000l);
                    }
                });
                selectCategoryDialog.show(getSupportFragmentManager(), "action picker");
                break;
            case R.id.txv_regular_week:
                lyt_regular_week.setVisibility(View.VISIBLE);
                lyt_holiday.setVisibility(View.GONE);
                txv_regular_week.setBackgroundColor(getResources().getColor(R.color.signup_popup_color));
                txv_regular_week.setTextColor(getResources().getColor(R.color.txt_color));
                txv_holiday.setBackgroundColor(getResources().getColor(R.color.btn_login_color));
                txv_holiday.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.txv_holiday:
                lyt_regular_week.setVisibility(View.GONE);
                lyt_holiday.setVisibility(View.VISIBLE);
                txv_holiday.setBackgroundColor(getResources().getColor(R.color.signup_popup_color));
                txv_holiday.setTextColor(getResources().getColor(R.color.txt_color));
                txv_regular_week.setBackgroundColor(getResources().getColor(R.color.btn_login_color));
                txv_regular_week.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

}