package com.atb.app.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.model.CommentModel;
import com.atb.app.model.TransactionEntity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private NewsDetailActivity _context;

    public ArrayList<CommentModel> _roomDatas = new ArrayList<>();

    public CommentAdapter(NewsDetailActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<CommentModel> data) {
        _roomDatas.clear();
        for(int i =0;i<data.size();i++){
            _roomDatas.add(data.get(i));
            _roomDatas.addAll(data.get(i).getReplies());
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
            convertView = inflater.inflate(R.layout.comment_item, parent, false);
            holder.txv_comment = (TextView) convertView.findViewById(R.id.txv_comment);
            holder.txv_like = (TextView) convertView.findViewById(R.id.txv_like);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            holder.imageViews.add((ImageView)convertView.findViewById(R.id.imv_image1));
            holder.imageViews.add((ImageView)convertView.findViewById(R.id.imv_image2));
            holder.imageViews.add((ImageView)convertView.findViewById(R.id.imv_image3));
            holder.imv_like = (ImageView) convertView.findViewById(R.id.imv_like);
            holder.lyt_like = (LinearLayout) convertView.findViewById(R.id.lyt_like);
            holder.lyt_reply = (LinearLayout) convertView.findViewById(R.id.lyt_reply);
            holder.view_reply = (View) convertView.findViewById(R.id.view_reply);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final CommentModel commentModel = _roomDatas.get(position);
        String comment = commentModel.getComment();
        holder.view_reply.setVisibility(View.GONE);
        holder.txv_comment.setVisibility(View.GONE);
        for(int i =0;i<3;i++)holder.imageViews.get(i).setVisibility(View.GONE);
        if(commentModel.getLevel()==1)
            holder.view_reply.setVisibility(View.VISIBLE);
        if(comment.length()>0) {
            comment = comment.replace("\n", "<br/>");
            comment = comment.replace(" ", "&nbsp;");
            String username = "<font color='black'>" + "<b>" + commentModel.getUserName() + "</b>" + "</font>" + "  " + comment;
            //text
            holder.txv_comment.setText(Html.fromHtml(username), TextView.BufferType.SPANNABLE);
            holder.txv_comment.setVisibility(View.VISIBLE);
        }
        for(int i =0;i<commentModel.getImage_url().size();i++){
            Glide.with(_context).load(commentModel.getImage_url().get(i)).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.imageViews.get(i));
            holder.imageViews.get(i).setVisibility(View.VISIBLE);
        }
        holder.lyt_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _context.replyComment(commentModel.getParentPosstion());
            }
        });
        return convertView;
    }


    public class CustomHolder {
        ImageView imv_profile,imv_like;
        ArrayList<ImageView>imageViews = new ArrayList<>();
        TextView txv_comment,txv_time,txv_like;
        LinearLayout lyt_like,lyt_reply;
        View view_reply;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


