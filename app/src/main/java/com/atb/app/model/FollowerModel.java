package com.atb.app.model;

import org.json.JSONObject;

public class FollowerModel {
    int id,follow_user_id,follow_business_id,follower_user_id,follower_business_id,post_notifications;
    long created_at;
    UserModel userModel = new UserModel();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollow_user_id() {
        return follow_user_id;
    }

    public void setFollow_user_id(int follow_user_id) {
        this.follow_user_id = follow_user_id;
    }

    public int getFollow_business_id() {
        return follow_business_id;
    }

    public void setFollow_business_id(int follow_business_id) {
        this.follow_business_id = follow_business_id;
    }

    public int getFollower_user_id() {
        return follower_user_id;
    }

    public void setFollower_user_id(int follower_user_id) {
        this.follower_user_id = follower_user_id;
    }

    public int getFollower_business_id() {
        return follower_business_id;
    }

    public void setFollower_business_id(int follower_business_id) {
        this.follower_business_id = follower_business_id;
    }

    public int getPost_notifications() {
        return post_notifications;
    }

    public void setPost_notifications(int post_notifications) {
        this.post_notifications = post_notifications;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
    public void initModel(JSONObject jsonObject){
        try {
            setId(jsonObject.getInt("id"));
            setFollow_user_id(jsonObject.getInt("follow_user_id"));
            setFollow_business_id(jsonObject.getInt("follow_business_id"));
            setFollower_user_id(jsonObject.getInt("follower_user_id"));
            setFollower_business_id(jsonObject.getInt("follower_business_id"));
            setPost_notifications(jsonObject.getInt("post_notifications"));
            setCreated_at(jsonObject.getLong("created_at"));
        }catch (Exception e){

        }
    }
}
