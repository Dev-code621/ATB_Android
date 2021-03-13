package com.atb.app.model.submodel;

import android.util.Log;

import org.json.JSONObject;

public class InsuranceModel {

    // type = 0,1 ; insrance , qualitfication
    int id,user_id,type;
    String company, reference, expiry, file;
    long created_at,modified_at;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getModified_at() {
        return modified_at;
    }

    public void setModified_at(long modified_at) {
        this.modified_at = modified_at;
    }

    public void initModel(JSONObject jsonObject){
        try {
            setId(jsonObject.getInt("id"));
            setUser_id(jsonObject.getInt("user_id"));
            setType(jsonObject.getInt("type"));
            setCompany(jsonObject.getString("company"));
            setReference(jsonObject.getString("reference"));
            setExpiry(jsonObject.getString("expiry"));
            setFile(jsonObject.getString("file"));
            setCreated_at(jsonObject.getLong("created_at"));
            setModified_at(jsonObject.getLong("modified_at"));
        }catch (Exception e ){
            Log.d("aaaaaa",e.toString());

        }
    }
}
