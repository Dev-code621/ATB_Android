package com.atb.app.model;

import org.json.JSONObject;

public class NotiEntity {
    int id,user_id,type,related_id,read_status,visible;
    String text,name,profile_image;
    long updated_at,created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRelated_id() {
        return related_id;
    }

    public void setRelated_id(int related_id) {
        this.related_id = related_id;
    }

    public int getRead_status() {
        return read_status;
    }

    public void setRead_status(int read_status) {
        this.read_status = read_status;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public void initModel(JSONObject jsonObject){
        try {
            setId(jsonObject.getInt("id"));
            setUser_id(jsonObject.getInt("user_id"));
            setType(jsonObject.getInt("type"));
            setRelated_id(jsonObject.getInt("related_id"));
            setRead_status(jsonObject.getInt("read_status"));
            setVisible(jsonObject.getInt("visible"));
            setName(jsonObject.getString("name"));
            setText(jsonObject.getString("text"));
            setProfile_image(jsonObject.getString("profile_image"));
            setUpdated_at(jsonObject.getLong("updated_at"));
            setCreated_at(jsonObject.getLong("created_at"));
        }catch (Exception e){

        }
    }
}
