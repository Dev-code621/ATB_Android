package com.atb.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.BookingActivity;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.NotiEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BookingListAdapter extends BaseAdapter {

    private BookingActivity _context;

    HashMap<String,BookingEntity> _roomDatas = new HashMap<>();
    String[]bookingSlot = new String[100];
    public BookingListAdapter(BookingActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(HashMap<String,BookingEntity> data,String[] bookingSlot) {
        _roomDatas = data;
        this.bookingSlot = bookingSlot;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return _roomDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final CustomHolder holder;
        if (convertView == null) {
            holder = new CustomHolder();
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.futurebooking_item, parent, false);
            holder.lyt_addbooking = (LinearLayout) convertView.findViewById(R.id.lyt_addbooking);
            holder.lyt_booking = (LinearLayout) convertView.findViewById(R.id.lyt_booking);
            holder.txv_name = (TextView) convertView.findViewById(R.id.txv_name);
            holder.txv_id = (TextView) convertView.findViewById(R.id.txv_id);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            holder.txv_timeslot = (TextView) convertView.findViewById(R.id.txv_timeslot);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }

        final BookingEntity bookingEntity =  _roomDatas.get(bookingSlot[position]);
        holder.txv_timeslot.setText(bookingSlot[position]);
        if(bookingEntity==null){
            holder.lyt_addbooking.setVisibility(View.VISIBLE);
            holder.lyt_booking.setVisibility(View.GONE);
        }else {
            holder.lyt_addbooking.setVisibility(View.GONE);
            holder.lyt_booking.setVisibility(View.VISIBLE);
        }
//        holder.txv_title.setText(noti_item.getTitle());
//        holder.txv_content.setText(noti_item.getContent());
//        holder.txv_time.setText(noti_item.getTime());
        return convertView;
    }


    public class CustomHolder {
        LinearLayout lyt_addbooking,lyt_booking;
        ImageView imv_profile;
        TextView txv_name,txv_id,txv_timeslot;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


