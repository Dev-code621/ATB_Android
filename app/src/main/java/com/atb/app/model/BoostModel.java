package com.atb.app.model;

import org.json.JSONObject;

public class BoostModel {
    int id,user_id,type,position,bidon;
    String category,tags,paid,price,country,county,region;
    UserModel userModel = new UserModel();
    boolean emptyModel = false;
    long created_at,updated_at;
    int total_bids;

    public int getTotal_bids() {
        return total_bids;
    }

    public void setTotal_bids(int total_bids) {
        this.total_bids = total_bids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }


    public boolean isEmptyModel() {
        return emptyModel;
    }

    public void setEmptyModel(boolean emptyModel) {
        this.emptyModel = emptyModel;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getBidon() {
        return bidon;
    }

    public void setBidon(int bidon) {
        this.bidon = bidon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public void initModel(JSONObject jsonObject){
        try {
            setId(jsonObject.getInt("id"));
            setUser_id(jsonObject.getInt("user_id"));
            setType(jsonObject.getInt("type"));
            setPrice(jsonObject.getString("price"));
            setCategory(jsonObject.getString("category"));
            setTags(jsonObject.getString("tags"));
            setPosition(jsonObject.getInt("position"));
            setPaid(jsonObject.getString("paid"));
            setCountry(jsonObject.getString("country"));
            setCounty(jsonObject.getString("county"));
            setRegion(jsonObject.getString("region"));
            setCreated_at(jsonObject.getLong("created_at"));
            setUpdated_at(jsonObject.getLong("updated_at"));
            if(jsonObject.has("bidon"))
                setBidon(jsonObject.getInt("bidon"));
            if(jsonObject.has("total_bids"))
                setTotal_bids(jsonObject.getInt("total_bids"));
            JSONObject user_object = jsonObject.getJSONObject("user");
            userModel.initModel(user_object);
            setEmptyModel(true);
        }catch (Exception e){

        }
    }
}
