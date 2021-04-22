package com.atb.app.model;

import org.json.JSONObject;

public class ReviewModel {
    int id,userid,business_id,rating;
    String review;
    long created_at;
    String first_name,last_name,pic_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(int business_id) {
        this.business_id = business_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
    public void initModel(JSONObject jsonObject){
        try {
            setId(jsonObject.getInt("id"));
            setUserid(jsonObject.getInt("user_id"));
            setRating(jsonObject.getInt("rating"));
            if(getRating()>5)setRating(5);
            setReview(jsonObject.getString("review"));
            setCreated_at(jsonObject.getLong("created_at"));
            JSONObject rater = jsonObject.getJSONObject("rater");
            setFirst_name(rater.getString("first_name"));
            setLast_name(rater.getString("last_name"));
            setPic_url(rater.getString("pic_url"));

        }catch (Exception e){

        }
    }
}
