package com.atb.app.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
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
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.CommentModel;
import com.atb.app.model.TransactionEntity;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        String comment = Commons.g_user.getUserName()+" ";

        HashMap<Integer,Integer> mapStart = new HashMap<>();
        HashMap<Integer,Integer> mapEnd = new HashMap<>();
        ArrayList<Integer> indexArray = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(commentModel.getComment());

            for(int i =0;i<jsonArray.length();i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String str = jsonObject.getString("comment");
                if(jsonObject.has("user_id")){
                    mapStart.put(jsonObject.getInt("user_id"), comment.length());
                    mapEnd.put(jsonObject.getInt("user_id"), comment.length() + str.length());
                    indexArray.add(jsonObject.getInt("user_id"));
                }
                comment = comment +  str;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.view_reply.setVisibility(View.GONE);
        holder.txv_comment.setVisibility(View.GONE);
        for(int i =0;i<3;i++){
            holder.imageViews.get(i).setVisibility(View.GONE);
            int finalI = i;
            holder.imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _context.showPicture(v,commentModel.getImage_url().get(finalI));
                }
            });
        }
        if(commentModel.getLevel()==1)
            holder.view_reply.setVisibility(View.VISIBLE);

        if(comment.length()>0) {
            SpannableString ss = new SpannableString(comment);
            for(int i =0;i<indexArray.size() ;i++){
                int index =  indexArray.get(i);
                ss.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        for(UserModel userModel: Commons.AllUsers){
                            if(userModel.getId() == index){
                                Bundle bundle = new Bundle();
                                Gson gson = new Gson();
                                String usermodel = gson.toJson(userModel);
                                bundle.putString("userModel",usermodel);
                                bundle.putInt("userType",userModel.getAccount_type());
                                ((CommonActivity)_context).goTo(_context, OtherUserProfileActivity.class,false,bundle);
                                return;
                            }
                        }

                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(_context.getResources().getColor(R.color.black));
                        ds.setUnderlineText(false);
                    }
                }, mapStart.get(indexArray.get(i)), mapEnd.get(indexArray.get(i)), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            ss.setSpan(new StyleSpan(Typeface.BOLD), 0, Commons.g_user.getUserName().length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txv_comment.setText(ss);
            holder.txv_comment.setMovementMethod(LinkMovementMethod.getInstance());

            holder.txv_comment.setVisibility(View.VISIBLE);
        }
        for(int i =0;i<commentModel.getImage_url().size();i++){
            Glide.with(_context).load(commentModel.getImage_url().get(i)).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.imageViews.get(i));
            holder.imageViews.get(i).setVisibility(View.VISIBLE);
        }
        Glide.with(_context).load(commentModel.getUserImage()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_profile);
        holder.txv_time.setText(commentModel.getRead_created());
        holder.lyt_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _context.replyComment(commentModel.getParentPosstion(),_roomDatas.get(position));
            }
        });
        holder.lyt_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _context.likeComment(commentModel.getParentPosstion(),_roomDatas.get(position));
            }
        });

        if(commentModel.isLike()){
            holder.txv_like.setText("Liked");
            holder.imv_like.setImageDrawable(_context.getResources().getDrawable(R.drawable.icon_heart));
            holder.imv_like.setColorFilter(_context.getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);

        }else {
            holder.txv_like.setText("Like");
            holder.imv_like.setImageDrawable(_context.getResources().getDrawable(R.drawable.icon_health_unselect));

        }

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


