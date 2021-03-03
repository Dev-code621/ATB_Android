package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.activities.navigationItems.booking.CreateABookingActivity;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.NotiEntity;

import java.util.ArrayList;

public class BookingServiceAdapter extends BaseAdapter {

    private CreateABookingActivity _context;

    public ArrayList<BookingEntity> _roomDatas = new ArrayList<>();

    public BookingServiceAdapter(CreateABookingActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<BookingEntity> data) {
        _roomDatas = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return _roomDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return _roomDatas.get(position);
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
            convertView = inflater.inflate(R.layout.bookservice_item, parent, false);
            holder.txv_name = (TextView) convertView.findViewById(R.id.txv_name);
            holder.txv_price = (TextView) convertView.findViewById(R.id.txv_price);
            holder.imv_image = (ImageView) convertView.findViewById(R.id.imv_image);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final BookingEntity noti_item = _roomDatas.get(position);
//        holder.txv_title.setText(noti_item.getTitle());
//        holder.txv_content.setText(noti_item.getContent());
//        holder.txv_time.setText(noti_item.getTime());
        return convertView;
    }


    public class CustomHolder {
        TextView txv_name, txv_price;
        ImageView imv_image;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


