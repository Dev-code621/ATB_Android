package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.BookingActivity;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.TransactionEntity;
import com.atb.app.model.submodel.VotingModel;
import com.diffey.view.progressview.ProgressView;

import java.util.ArrayList;
import java.util.HashMap;

public class VotingListAdapter  extends BaseAdapter {

    private Context _context;

    public ArrayList<VotingModel> _roomDatas = new ArrayList<>();
    int totalUser =0;
    public VotingListAdapter(Context context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<VotingModel> data) {
        _roomDatas.clear();
        _roomDatas.addAll(data);
        totalUser = 0 ;
        for(int i =0;i<_roomDatas.size();i++){
            totalUser +=_roomDatas.get(i).getVotes().size();
        }
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
            convertView = inflater.inflate(R.layout.voting_item, parent, false);
            holder.lyt_container = (LinearLayout) convertView.findViewById(R.id.lyt_container);
            holder.txv_name = (TextView) convertView.findViewById(R.id.txv_name);
            holder.txv_percentage = (TextView) convertView.findViewById(R.id.txv_percentage);
            holder.imv_check = (ImageView) convertView.findViewById(R.id.imv_check);
            holder.main_progressview = (ProgressView) convertView.findViewById(R.id.main_progressview);
            holder.card_check = (CardView)convertView.findViewById(R.id.card_check);
            holder.card_progress = (CardView)convertView.findViewById(R.id.card_progress);

            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final VotingModel votingModel = _roomDatas.get(position);
        holder.txv_name.setText(votingModel.getPoll_value());
        int percentage = 0;
        if(totalUser!=0)
            percentage = 100*votingModel.getVotes().size()/totalUser;
        holder.txv_percentage.setText(String.valueOf(percentage) + "%");
        holder.txv_percentage.setTextColor(_context.getResources().getColor(R.color.txt_color));
        holder.txv_name.setTextColor(_context.getResources().getColor(R.color.txt_color));
        holder.main_progressview.setProgress(percentage);
        holder.main_progressview.setProgressColor(_context.getResources().getColor(R.color.progress_color));

        if(Commons.myVoting(votingModel)){
            holder.card_check.setVisibility(View.VISIBLE);
            holder.main_progressview.setProgressColor(_context.getResources().getColor(R.color.progress_color));
            holder.txv_percentage.setTextColor(_context.getResources().getColor(R.color.white));
            holder.txv_name.setTextColor(_context.getResources().getColor(R.color.white));
        }else{
            holder.card_check.setVisibility(View.GONE);
            if(percentage>0) {
                holder.main_progressview.setProgressColor(_context.getResources().getColor(R.color.boder_color));
            }
            else {
                holder.main_progressview.setProgressColor(_context.getResources().getColor(R.color.main_color1));

            }
        }

        return convertView;
    }



    public class CustomHolder {
        TextView txv_name, txv_percentage;
        ImageView imv_check;
        LinearLayout lyt_container;
        ProgressView main_progressview;
        CardView card_check,card_progress;

    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}




