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
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NotiEntity;
import com.atb.app.model.TransactionEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class TransactionAdapter extends BaseAdapter {

    private TransactionHistoryActivity _context;

    public ArrayList<TransactionEntity> _roomDatas = new ArrayList<>();

    public TransactionAdapter(TransactionHistoryActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<TransactionEntity> data) {
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
            convertView = inflater.inflate(R.layout.transaction_item, parent, false);
            holder.txv_price = (TextView) convertView.findViewById(R.id.txv_price);
            holder.txv_username = (TextView) convertView.findViewById(R.id.txv_username);
            holder.txv_category = (TextView) convertView.findViewById(R.id.txv_category);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final TransactionEntity transactionEntity = _roomDatas.get(position);
//        holder.txv_title.setText(noti_item.getTitle());
//        holder.txv_content.setText(noti_item.getContent());
//        holder.txv_time.setText(noti_item.getTime());
        String imv_url = Commons.g_user.getImvUrl();
        String username = Commons.g_user.getUserName();
        if(transactionEntity.getIs_business() == 1) {
            imv_url = Commons.g_user.getBusinessModel().getBusiness_logo();
            username = Commons.g_user.getBusinessModel().getBusiness_name();
        }
        Glide.with(_context).load(imv_url).placeholder(R.drawable.profile_pic).dontAnimate()
                .apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder)))
                .into(holder.imv_profile);

        holder.txv_price.setText("£" + String.valueOf(Math.abs(transactionEntity.getAmount())));
        holder.txv_username.setText(username);
        holder.txv_time.setText(Commons.getDisplayDate4(transactionEntity.getCreated_at()));
        holder.txv_category.setText(transactionEntity.getTransaction_type());
        return convertView;
    }


    public class CustomHolder {
        TextView txv_price, txv_username, txv_category,txv_time;
        ImageView imv_profile;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


