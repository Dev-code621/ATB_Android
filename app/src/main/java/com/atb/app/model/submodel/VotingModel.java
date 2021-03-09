package com.atb.app.model.submodel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VotingModel {
    int id,post_id;
    String poll_value;
    long  expires,created_at,updated_at;
    ArrayList<Integer>votes = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPoll_value() {
        return poll_value;
    }

    public void setPoll_value(String poll_value) {
        this.poll_value = poll_value;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
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

    public ArrayList<Integer> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Integer> votes) {
        this.votes = votes;
    }

    public void initModel(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            post_id = jsonObject.getInt("post_id");
            poll_value = jsonObject.getString("poll_value");
            expires = jsonObject.getLong("expires");
            created_at = jsonObject.getLong("expires");
            updated_at = jsonObject.getLong("expires");
            JSONArray jsonArray = jsonObject.getJSONArray("votes");
            votes.clear();
            for(int i =0;i<jsonArray.length();i++){
                votes.add(jsonArray.getJSONObject(i).getInt("user_id"));
            }
        }catch (Exception e){
            Log.d("aaa",e.toString());
        }
    }
}
