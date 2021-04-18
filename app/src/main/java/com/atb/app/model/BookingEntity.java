package com.atb.app.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class BookingEntity {
    int id,service_id,user_id = -1,business_user_id,booking_datetime,is_reminder_enabled;
    String state,email,full_name,phone,total_cost,remaining;
    long created_at,updated_at;
    UserModel userModel = new UserModel();
    BusinessModel businessModel = new BusinessModel();
    TransactionEntity transactionEntity = new TransactionEntity();
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    //0 free slot, 1: booked slot : -1: disalbe solot
    int type = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBusiness_user_id() {
        return business_user_id;
    }

    public void setBusiness_user_id(int business_user_id) {
        this.business_user_id = business_user_id;
    }

    public int getBooking_datetime() {
        return booking_datetime;
    }

    public void setBooking_datetime(int booking_datetime) {
        this.booking_datetime = booking_datetime;
    }

    public int getIs_reminder_enabled() {
        return is_reminder_enabled;
    }

    public void setIs_reminder_enabled(int is_reminder_enabled) {
        this.is_reminder_enabled = is_reminder_enabled;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
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

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public BusinessModel getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(BusinessModel businessModel) {
        this.businessModel = businessModel;
    }

    public TransactionEntity getTransactionEntity() {
        return transactionEntity;
    }

    public void setTransactionEntity(TransactionEntity transactionEntity) {
        this.transactionEntity = transactionEntity;
    }

    public NewsFeedEntity getNewsFeedEntity() {
        return newsFeedEntity;
    }

    public void setNewsFeedEntity(NewsFeedEntity newsFeedEntity) {
        this.newsFeedEntity = newsFeedEntity;
    }

    public void initModel(JSONObject jsonObject){
        try{
            setId(jsonObject.getInt("id"));
            setService_id(jsonObject.getInt("service_id"));
            setState(jsonObject.getString("state"));
            if(!jsonObject.getString("user_id").equals("null"))
              setUser_id(jsonObject.getInt("user_id"));
            setBusiness_user_id(jsonObject.getInt("business_user_id"));
            setBooking_datetime(jsonObject.getInt("booking_datetime"));
            setIs_reminder_enabled(jsonObject.getInt("is_reminder_enabled"));
            setEmail(jsonObject.getString("email"));
            setPhone(jsonObject.getString("phone"));
            setFull_name(jsonObject.getString("full_name"));
            setTotal_cost(jsonObject.getString("total_cost"));
            setRemaining(jsonObject.getString("remaining"));
            setCreated_at(jsonObject.getLong("created_at"));
            setUpdated_at(jsonObject.getLong("updated_at"));
            JSONArray transactions = jsonObject.getJSONArray("transactions");
            if(transactions.length()>0){
                JSONObject object = transactions.getJSONObject(0);
                transactionEntity = new TransactionEntity();
                transactionEntity.setId(object.getInt("id"));
                transactionEntity.setUser_id(object.getInt("user_id"));
                transactionEntity.setIs_business(object.getInt("is_business"));
                transactionEntity.setTransaction_id(object.getString("transaction_id"));
                transactionEntity.setTransaction_type(object.getString("transaction_type"));
                transactionEntity.setTarget_id(object.getString("target_id"));
                transactionEntity.setAmount(object.getDouble("amount"));
                transactionEntity.setPayment_method(object.getString("payment_method"));
                transactionEntity.setPayment_source(object.getString("payment_source"));
                transactionEntity.setQuantity(object.getInt("quantity"));
                transactionEntity.setPurchase_type(object.getString("purchase_type"));
                if(!object.getString("delivery_option").equals("null"))
                    transactionEntity.setDelivery_option(object.getInt("delivery_option"));
                transactionEntity.setCreated_at(object.getLong("created_at"));
            }
            if(jsonObject.has("user")){
                JSONArray users = jsonObject.getJSONArray("user");
                if(users.length()>0)
                    userModel.initModel(users.getJSONObject(0));
            }else {
                userModel.id =-1;
                userModel.setUserName(getFull_name());
                userModel.setEmail(getEmail());
            }
            if(jsonObject.has("business")){
                JSONArray users = jsonObject.getJSONArray("business");
                if(users.length()>0)
                    businessModel.initModel(users.getJSONObject(0));
            }
            if(jsonObject.has("service")){
                JSONArray services = jsonObject.getJSONArray("service");
                if(services.length()>0)
                    newsFeedEntity.initModel(services.getJSONObject(0));
            }
            setType(1);

        }catch (Exception e){
            Log.d("booking issue",e.toString());

        }
    }
}
