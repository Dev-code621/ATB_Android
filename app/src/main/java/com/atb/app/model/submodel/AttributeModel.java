package com.atb.app.model.submodel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class AttributeModel {
    int attribute_id;
    String attribute_title,variant_attirbute_value;

    public int getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(int attribute_id) {
        this.attribute_id = attribute_id;
    }

    public String getAttribute_title() {
        return attribute_title;
    }

    public void setAttribute_title(String attribute_title) {
        this.attribute_title = attribute_title;
    }

    public String getVariant_attirbute_value() {
        return variant_attirbute_value;
    }

    public void setVariant_attirbute_value(String variant_attirbute_value) {
        this.variant_attirbute_value = variant_attirbute_value;
    }

    public void initModel(JSONObject jsonObject){
        try {
            setAttribute_id(jsonObject.getInt("attribute_id"));
            setAttribute_title(jsonObject.getString("attribute_title"));
            setVariant_attirbute_value(jsonObject.getString("variant_attirbute_value"));
        }catch (Exception e){
            Log.d("aaa",e.toString());
        }
    }
}
