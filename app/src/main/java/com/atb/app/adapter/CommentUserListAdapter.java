package com.atb.app.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NotiEntity;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CommentUserListAdapter extends BaseAdapter {

    private Context _context;

    public ArrayList<UserModel> _roomDatas = new ArrayList<>();

    public CommentUserListAdapter(Context context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<UserModel> data) {
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
            convertView = inflater.inflate(R.layout.chat_user_item, parent, false);
            holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final UserModel noti_item = _roomDatas.get(position);
        Glide.with(_context).load(noti_item.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_profile);
        holder.txv_title.setText(Html.fromHtml("<b>@" + noti_item.getUserName()+"</b>"));
        return convertView;
    }


    public class CustomHolder {
        TextView txv_title;
        ImageView imv_profile;
    }
    private String secToTime(long old_time) {
        int sec = (int) (System.currentTimeMillis()/1000l - old_time);
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (seconds <= 0) {
            return "Just now";
        } else if (minutes == 0) {
            return String.format(_context.getString(R.string.second_ago), seconds);
        } else {
            if (minutes >= 60) {
                int hours = minutes / 60;
                minutes %= 60;
                if (hours >= 24) {
                    int days = hours / 24;
                    return String.format(_context.getString(R.string.days_ago), days);
                }
                return String.format(_context.getString(R.string.hours_ago), hours);
            }
            return String.format(_context.getString(R.string.min_ago), minutes);
        }
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


