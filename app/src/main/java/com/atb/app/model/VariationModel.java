package com.atb.app.model;

import android.util.Log;

import com.atb.app.model.submodel.AttributeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VariationModel {
    int id,product_id,stock_level;
    String title,price;
    long updated_at,created_at;
    ArrayList<AttributeModel> attributeModels = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getStock_level() {
        return stock_level;
    }

    public void setStock_level(int stock_level) {
        this.stock_level = stock_level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public ArrayList<AttributeModel> getAttributeModels() {
        return attributeModels;
    }

    public void setAttributeModels(ArrayList<AttributeModel> attributeModels) {
        this.attributeModels = attributeModels;
    }

    public void initModel(JSONObject jsonObject){
        try {
            setId(jsonObject.getInt("id"));
            setProduct_id(jsonObject.getInt("product_id"));
            setStock_level(jsonObject.getInt("stock_level"));
            setTitle(jsonObject.getString("title"));
            setPrice(jsonObject.getString("price"));
            setUpdated_at(jsonObject.getLong("updated_at"));
            setCreated_at(jsonObject.getLong("created_at"));
            attributeModels.clear();
            if(jsonObject.has("attributes")){
                JSONArray jsonArray = jsonObject.getJSONArray("attributes");
                for(int i =0;i<jsonArray.length();i++){
                    AttributeModel attributeModel = new AttributeModel();
                    attributeModel.initModel(jsonArray.getJSONObject(i));
                    attributeModels.add(attributeModel);
                }
            }
        }catch (Exception e){
            Log.d("aaa",e.toString());
        }
    }
}
