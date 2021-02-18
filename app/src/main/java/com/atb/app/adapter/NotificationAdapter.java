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
import com.atb.app.model.NotiEntity;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    private NotificationActivity _context;

    public ArrayList<NotiEntity> _roomDatas = new ArrayList<>();

    public NotificationAdapter(NotificationActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<NotiEntity> data) {
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
            convertView = inflater.inflate(R.layout.noti_item, parent, false);
            holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title);
            holder.txv_content = (TextView) convertView.findViewById(R.id.txv_content);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final NotiEntity noti_item = _roomDatas.get(position);
//        holder.txv_title.setText(noti_item.getTitle());
//        holder.txv_content.setText(noti_item.getContent());
//        holder.txv_time.setText(noti_item.getTime());
        return convertView;
    }


    public class CustomHolder {
        TextView txv_title, txv_content, txv_time;
        ImageView imv_profile;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


