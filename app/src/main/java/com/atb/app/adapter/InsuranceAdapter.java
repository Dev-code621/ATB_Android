package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.business.UpdateBusinessActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.submodel.InsuranceModel;

import java.util.ArrayList;

public class InsuranceAdapter extends BaseAdapter {

    private UpdateBusinessActivity _context;

    public ArrayList<InsuranceModel> _roomDatas = new ArrayList<>();
    int type;
    public InsuranceAdapter(UpdateBusinessActivity context,int type ) {

        super();
        this._context = context;
        this.type = type;
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
            convertView = inflater.inflate(R.layout.insurance_item, parent, false);
            holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            holder.imv_delete = (ImageView) convertView.findViewById(R.id.imv_delete);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final InsuranceModel noti_item = _roomDatas.get(position);
        String str = "Qualified Since ";
        if(noti_item.getType()==0)
            str = "Insurance Until ";
        holder.txv_time.setText(str + Commons.getDisplayDate1(noti_item.getExpiry()));
        holder.txv_title.setText(noti_item.getCompany());
        holder.imv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _context.deleteInsurance(position,type);
            }
        });
        return convertView;
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


