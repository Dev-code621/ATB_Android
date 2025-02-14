package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.profile.FollowerAndFollowingActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.FollowerModel;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowerAdapter extends BaseAdapter {

    private FollowerAndFollowingActivity _context;

    public ArrayList<FollowerModel> _roomDatas = new ArrayList<>();
    UserModel userModel = new UserModel();
    boolean isFollower ;
    public FollowerAdapter(FollowerAndFollowingActivity context, UserModel userModel) {

        super();
        this._context = context;
        this.userModel = userModel;
    }


    public void setRoomData(ArrayList<FollowerModel> data,boolean isFollower) {
        _roomDatas.clear();
        _roomDatas.addAll(data);
        this.isFollower = isFollower;

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
            convertView = inflater.inflate(R.layout.follower_item, parent, false);
            holder.txv_id = (TextView) convertView.findViewById(R.id.txv_id);
            holder.txv_username = (TextView) convertView.findViewById(R.id.txv_username);
            holder.lyt_delete = (LinearLayout) convertView.findViewById(R.id.lyt_delete);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            holder.txv_unfollow = (TextView) convertView.findViewById(R.id.txv_unfollow);

            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final FollowerModel followerModel = _roomDatas.get(position);
        Glide.with(_context).load(followerModel.getUserModel().getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().into(holder.imv_profile);

        holder.txv_username.setText(followerModel.getUserModel().getFirstname() + " " + followerModel.getUserModel().getLastname());
        holder.txv_id.setText(followerModel.getUserModel().getUserName());
        holder.txv_id.setTextColor(_context.getResources().getColor(R.color.txt_color));
        if(!isFollower && followerModel.getUserModel().getAccount_type()==1){
            Glide.with(_context).load(followerModel.getUserModel().getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_profile);
            holder.txv_username.setText(followerModel.getUserModel().getBusinessModel().getBusiness_profile_name());
            holder.txv_id.setText(followerModel.getUserModel().getBusinessModel().getBusiness_website());
            holder.txv_id.setTextColor(_context.getResources().getColor(R.color.head_color));
        }

        if(userModel.getId() == Commons.g_user.getId()) {
            if (isFollower) {
                holder.txv_unfollow.setText("DELETE");
                holder.txv_unfollow.setTextColor(_context.getResources().getColor(R.color.discard_color));
                holder.lyt_delete.setBackground(_context.getResources().getDrawable(R.drawable.deletebutton_border_round));
            } else {
                holder.txv_unfollow.setText("UNFOLLOW");
                holder.txv_unfollow.setTextColor(_context.getResources().getColor(R.color.head_color));
                holder.lyt_delete.setBackground(_context.getResources().getDrawable(R.drawable.followbutton_border_round));
            }
        }else {
            holder.lyt_delete.setVisibility(View.VISIBLE);
            holder.txv_unfollow.setText("FOLLOW");
            holder.txv_unfollow.setTextColor(_context.getResources().getColor(R.color.head_color));
            holder.lyt_delete.setBackground(_context.getResources().getDrawable(R.drawable.followbutton_border_round));
            if(followerModel.getFollow_user_id() == Commons.g_user.getId() || followerModel.getFollower_user_id()==Commons.g_user.getId())
                holder.lyt_delete.setVisibility(View.GONE);
            for(int i =0;i<Commons.g_user.getFollowerModels().size();i++){
                if( (isFollower && Commons.g_user.getFollowerModels().get(i).getFollower_user_id() == followerModel.getFollow_user_id()) || (!isFollower &&
                        Commons.g_user.getFollowerModels().get(i).getFollower_user_id() == followerModel.getFollower_user_id())){
                    holder.txv_unfollow.setText("FOLLOWING");
                    holder.txv_unfollow.setTextColor(_context.getResources().getColor(R.color.white));
                    holder.lyt_delete.setBackground(_context.getResources().getDrawable(R.drawable.button_rectangle_round));
                    break;
                }
            }

        }

        holder.lyt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel.getId() == Commons.g_user.getId())
                    _context.unFollower(followerModel.getUserModel().getId(),position,0);
                else {
                    int flag = -1;
                    for(int i =0;i<Commons.g_user.getFollowerModels().size();i++){
                        if( (isFollower && Commons.g_user.getFollowerModels().get(i).getFollower_user_id() == followerModel.getFollow_user_id()) || (!isFollower &&
                                Commons.g_user.getFollowerModels().get(i).getFollower_user_id() == followerModel.getFollower_user_id())){
                            flag =i;
                            break;
                        }
                    }

                    if(flag>=0)
                        if(isFollower)
                            _context.unFollower(followerModel.getFollow_user_id(),flag,1);
                        else
                            _context.unFollower(followerModel.getFollower_user_id(),flag,1);
                    else {
                        if(isFollower)
                            _context.addFollow(followerModel.getFollow_user_id());
                        else
                            _context.addFollow(followerModel.getFollower_user_id());
                    }
                }
            }
        });
        return convertView;
    }


    public class CustomHolder {
        TextView txv_username, txv_id,txv_unfollow;
        ImageView imv_profile;
        LinearLayout lyt_delete;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


