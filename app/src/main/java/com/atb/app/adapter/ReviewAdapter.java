package com.atb.app.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.activities.profile.ReviewActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Helper;
import com.atb.app.model.ReviewModel;
import com.atb.app.model.TransactionEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {

    private ReviewActivity _context;

    public ArrayList<ReviewModel> _roomDatas = new ArrayList<>();

    public ReviewAdapter(ReviewActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<ReviewModel> data) {
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
            convertView = inflater.inflate(R.layout.review_item, parent, false);
            holder.txv_name = (TextView) convertView.findViewById(R.id.txv_name);
            holder.txv_review = (TextView) convertView.findViewById(R.id.txv_review);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            holder.btShowmore = (TextView) convertView.findViewById(R.id.btShowmore);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            holder.imv_star = (SimpleRatingBar) convertView.findViewById(R.id.imv_star);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final ReviewModel reviewModel = _roomDatas.get(position);
        holder.txv_name.setText(reviewModel.getFirst_name()+" "+ reviewModel.getLast_name());
        holder.txv_review.setText(reviewModel.getReview());
        Glide.with(_context).load(reviewModel.getPic_url()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A6BFDE", Commons.glide_boder))).into(holder.imv_profile);
        holder.imv_star.setRating(reviewModel.getRating());
        holder.txv_time.setText(secToTime(reviewModel.getCreated_at()));
        String[] lines = reviewModel.getReview().split("\r\n|\r|\n");
        if(reviewModel.getReview().length()<30)
            holder.btShowmore.setVisibility(View.GONE);
        else {
            holder.btShowmore.setVisibility(View.VISIBLE);
            ObjectAnimator animation = ObjectAnimator.ofInt(holder.txv_review, "maxLines", 1);
            animation.setDuration(0).start();
        }
        holder.btShowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animation = ObjectAnimator.ofInt(holder.txv_review, "maxLines", lines.length);
                animation.setDuration(0).start();
                holder.btShowmore.setVisibility(View.GONE);

            }
        });
        return convertView;
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

    public class CustomHolder {
        TextView  txv_name, txv_review,txv_time;
        ImageView imv_profile;
        SimpleRatingBar imv_star;
        TextView btShowmore;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


