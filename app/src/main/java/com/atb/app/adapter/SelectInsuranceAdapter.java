package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.model.TransactionEntity;
import com.atb.app.model.submodel.InsuranceModel;

import java.util.ArrayList;

public class SelectInsuranceAdapter extends BaseAdapter {

    private Context _context;

    public ArrayList<InsuranceModel> _roomDatas = new ArrayList<>();

    public SelectInsuranceAdapter(Context context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<InsuranceModel> data) {
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
            convertView = inflater.inflate(R.layout.select_spiner_item, parent, false);
            holder.txv_text = (TextView) convertView.findViewById(R.id.txv_text);
            holder.txv_type = (TextView) convertView.findViewById(R.id.txv_type);
            holder.txv_date = (TextView) convertView.findViewById(R.id.txv_date);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final InsuranceModel item = _roomDatas.get(position);
        holder.txv_date.setText(item.getExpiry());
        holder.txv_type.setText("Qualified Since");
        if(item.getType() ==0)
            holder.txv_type.setText("Expires");
        holder.txv_text.setText(item.getCompany() + " " + item.getReference());
//        holder.txv_time.setText(noti_item.getTime());
        return convertView;
    }


    public class CustomHolder {
        TextView txv_date, txv_type, txv_text;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


