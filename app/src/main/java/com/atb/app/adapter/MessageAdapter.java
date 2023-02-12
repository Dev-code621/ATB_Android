package com.atb.app.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.RoomModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class MessageAdapter extends BaseAdapter {

    private Context _context;

    public List<RoomModel> _roomDatas = new ArrayList<>();

    public MessageAdapter(Context context) {
        super();
        this._context = context;

    }


    public void setRoomData(List<RoomModel> messageList) {
        _roomDatas = messageList;
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
            convertView = inflater.inflate(R.layout.message_item, parent, false);
            holder.txv_name = (TextView) convertView.findViewById(R.id.txv_name);
            holder.txv_lastmessage = (TextView) convertView.findViewById(R.id.txv_lastmessage);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            holder.imv_online = (ImageView)convertView.findViewById(R.id.imv_online);
            holder.unreadSmsCount = (TextView)convertView.findViewById(R.id.unreadSmsCount);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final RoomModel roomModel = _roomDatas.get(position);
        holder.txv_name.setText(roomModel.getName());
        holder.txv_lastmessage.setText(roomModel.getLast_message());

        holder.txv_time.setText(getFormattedDateAndTime(_context,
                roomModel.getLastMessageTime()/10000));
        holder.unreadSmsCount.setVisibility(View.GONE);

        if(roomModel.getUnReadCount()>0){
            holder.unreadSmsCount.setVisibility(View.VISIBLE);
            holder.unreadSmsCount.setText(String.valueOf(roomModel.getUnReadCount()));
        }
        Glide.with(_context).load(roomModel.getImage()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_profile);
        if(roomModel.isOnline()){
            holder.imv_online.setImageDrawable(_context.getResources().getDrawable(R.drawable.green_circle));
        }else {
            holder.imv_online.setImageDrawable(_context.getResources().getDrawable(R.drawable.red_circle));
        }

        return convertView;
    }

    public static String getFormattedDateAndTime(Context context, Long timestamp) {

        Date date = new Date(timestamp);
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM hh:mm aa");
        return fullDateFormat.format(date);
    }

    public class CustomHolder {
        TextView txv_name,  txv_lastmessage,txv_time,unreadSmsCount;
        ImageView imv_profile,imv_online;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


