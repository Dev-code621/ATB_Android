package com.atb.app.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.database.MessageDatabaseService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicommons.ApplozicService;
import com.applozic.mobicommons.commons.core.utils.DateUtils;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.people.contact.Contact;
import com.atb.app.R;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.commons.Commons;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.applozic.mobicommons.commons.core.utils.DateUtils.isSameDay;

public class MessageAdapter extends BaseAdapter {

    private Context _context;

    public List<Message> _roomDatas = new ArrayList<>();
    private MessageDatabaseService messageDatabaseService;

    public MessageAdapter(Context context) {
        super();
        this._context = context;
        this.messageDatabaseService = new MessageDatabaseService(context);

    }


    public void setRoomData(List<Message> messageList) {
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
        final Message message = _roomDatas.get(position);
        final Contact contact = new AppContactService(_context).getContactById(message.getContactIds());
        holder.txv_name.setText(contact.getDisplayName());
        holder.txv_lastmessage.setText(message.getMessage());
        holder.txv_time.setText(getFormattedDateAndTime(_context,
                message.getCreatedAtTime(),
                R.string.JUST_NOW,
                R.plurals.MINUTES,
                R.plurals.HOURS));
        Glide.with(_context).load(contact.getImageURL()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_profile);
        if(contact.isOnline()){
            holder.imv_online.setImageDrawable(_context.getResources().getDrawable(R.drawable.online_circle));
        }else {
            holder.imv_online.setImageDrawable(_context.getResources().getDrawable(R.drawable.offline_circle));
        }
        if(messageDatabaseService.getUnreadMessageCountForContact(message.getContactIds()) != 0){
            holder.unreadSmsCount.setText(String.valueOf(messageDatabaseService.getUnreadMessageCountForContact(message.getContactIds())));
        }else {
            holder.unreadSmsCount.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static String getFormattedDateAndTime(Context context, Long timestamp, int justNow, int min, int hr) {
        boolean sameDay = isSameDay(timestamp);
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM");
        Date newDate = new Date();

        try {
            if (sameDay) {
                long currentTime = newDate.getTime() - date.getTime();
                long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime);
                long diffHours = TimeUnit.MILLISECONDS.toHours(currentTime);
                if (diffMinutes <= 1 && diffHours == 0) {
                    return Utils.getString(context, justNow);
                }
                if (diffMinutes <= 59 && diffHours == 0) {
                    return ApplozicService.getContext(context).getResources().getQuantityString(min, (int) diffMinutes, diffMinutes);
                }

                if (diffMinutes > 59 && diffHours <= 2) {
                    return ApplozicService.getContext(context).getResources().getQuantityString(hr, (int) diffHours, diffHours);
                }
                return simpleDateFormat.format(date);
            }
            return fullDateFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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


