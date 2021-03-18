package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.SetOperatingActivity;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.model.TransactionEntity;
import com.atb.app.model.submodel.HolidayModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HolidayAdapter extends BaseAdapter {

    private SetOperatingActivity _context;

    public ArrayList<HolidayModel> _roomDatas = new ArrayList<>();

    public HolidayAdapter(SetOperatingActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<HolidayModel> data) {
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
            convertView = inflater.inflate(R.layout.holiday_item, parent, false);
            holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            holder.imv_delete = (ImageView) convertView.findViewById(R.id.imv_delete);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final HolidayModel noti_item = _roomDatas.get(position);
        holder.txv_time.setText(getDisplayDate(noti_item.getDay_off()));
        holder.txv_title.setText(noti_item.getName());
        holder.imv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _context.deleteHoliday(position);
            }
        });
        return convertView;
    }

    String  getDisplayDate(long milionsecond){
        String date = "";
        Date d = new Date(milionsecond*1000l);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        date = formatter.format(d);
        return date;
    }

    public class CustomHolder {
        TextView txv_time, txv_title;
        ImageView imv_delete;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


