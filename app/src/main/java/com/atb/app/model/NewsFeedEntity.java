package com.atb.app.model;

import android.util.Log;

import com.atb.app.model.submodel.PostImageModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFeedEntity {
    int id,user_id,post_type,poster_profile_type,media_type;
    boolean type;
    boolean select = false;
    ArrayList<NewsFeedEntity>  postEntities = new ArrayList();
    String title,description,brand,post_tags,post_condition,post_size,post_location,status_reason;
    String price,deposit,is_deposit_required,category_title,item_title,size_title,location_id,post_brand,post_item;
    int payment_options,delivery_option,is_active,is_sold;
    double lat,lng;
    int is_multi,multi_pos,scheduled,likes,comments;
    String read_created,profile_name,profile_image,post_postage ;

    long updated_at,created_at;
    String multi_group,service_id,product_id,insurance_id,qualification_id,cancellations;

    ArrayList<PostImageModel>postImageModels = new ArrayList<>();

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }

    public int getPoster_profile_type() {
        return poster_profile_type;
    }

    public void setPoster_profile_type(int poster_profile_type) {
        this.poster_profile_type = poster_profile_type;
    }

    public int getMedia_type() {
        return media_type;
    }

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPost_tags() {
        return post_tags;
    }

    public void setPost_tags(String post_tags) {
        this.post_tags = post_tags;
    }

    public String getPost_condition() {
        return post_condition;
    }

    public void setPost_condition(String post_condition) {
        this.post_condition = post_condition;
    }

    public String getPost_size() {
        return post_size;
    }

    public void setPost_size(String post_size) {
        this.post_size = post_size;
    }

    public String getPost_location() {
        return post_location;
    }

    public void setPost_location(String post_location) {
        this.post_location = post_location;
    }

    public String getStatus_reason() {
        return status_reason;
    }

    public void setStatus_reason(String status_reason) {
        this.status_reason = status_reason;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getIs_deposit_required() {
        return is_deposit_required;
    }

    public void setIs_deposit_required(String is_deposit_required) {
        this.is_deposit_required = is_deposit_required;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getSize_title() {
        return size_title;
    }

    public void setSize_title(String size_title) {
        this.size_title = size_title;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getPost_brand() {
        return post_brand;
    }

    public void setPost_brand(String post_brand) {
        this.post_brand = post_brand;
    }

    public String getPost_item() {
        return post_item;
    }

    public void setPost_item(String post_item) {
        this.post_item = post_item;
    }

    public int getPayment_options() {
        return payment_options;
    }

    public void setPayment_options(int payment_options) {
        this.payment_options = payment_options;
    }

    public int getDelivery_option() {
        return delivery_option;
    }

    public void setDelivery_option(int delivery_option) {
        this.delivery_option = delivery_option;
    }

    public String getPost_postage() {
        return post_postage;
    }

    public void setPost_postage(String post_postage) {
        this.post_postage = post_postage;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIs_sold() {
        return is_sold;
    }

    public void setIs_sold(int is_sold) {
        this.is_sold = is_sold;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lng;
    }

    public void setLang(double lang) {
        this.lng = lang;
    }

    public int getIs_multi() {
        return is_multi;
    }

    public void setIs_multi(int is_multi) {
        this.is_multi = is_multi;
    }

    public int getMulti_pos() {
        return multi_pos;
    }

    public void setMulti_pos(int multi_pos) {
        this.multi_pos = multi_pos;
    }

    public int getScheduled() {
        return scheduled;
    }

    public void setScheduled(int scheduled) {
        this.scheduled = scheduled;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getRead_created() {
        return read_created;
    }

    public void setRead_created(String read_created) {
        this.read_created = read_created;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
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

    public String getMulti_group() {
        return multi_group;
    }

    public void setMulti_group(String multi_group) {
        this.multi_group = multi_group;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getInsurance_id() {
        return insurance_id;
    }

    public void setInsurance_id(String insurance_id) {
        this.insurance_id = insurance_id;
    }

    public String getQualification_id() {
        return qualification_id;
    }

    public void setQualification_id(String qualification_id) {
        this.qualification_id = qualification_id;
    }

    public String getCancellations() {
        return cancellations;
    }

    public void setCancellations(String cancellations) {
        this.cancellations = cancellations;
    }

    public ArrayList<PostImageModel> getPostImageModels() {
        return postImageModels;
    }

    public void setPostImageModels(ArrayList<PostImageModel> postImageModels) {
        this.postImageModels = postImageModels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public ArrayList<NewsFeedEntity> getPostEntities() {
        return postEntities;
    }

    public void setPostEntities(ArrayList<NewsFeedEntity> postEntities) {
        this.postEntities = postEntities;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }


    public void initModel(JSONObject jsonObject){
        try {
            id = jsonObject.getInt("id");
            user_id = jsonObject.getInt("user_id");
            post_type = jsonObject.getInt("post_type");
            poster_profile_type = jsonObject.getInt("poster_profile_type");
            media_type = jsonObject.getInt("media_type");
            title = jsonObject.getString("title");
            description = jsonObject.getString("description");
            brand = jsonObject.getString("brand");
            price = jsonObject.getString("price");
            deposit = jsonObject.getString("deposit");
            is_deposit_required = jsonObject.getString("is_deposit_required");
            category_title = jsonObject.getString("category_title");
            item_title = jsonObject.getString("item_title");
            size_title = jsonObject.getString("size_title");
            payment_options = jsonObject.getInt("payment_options");
            location_id = jsonObject.getString("location_id");
            delivery_option = jsonObject.getInt("delivery_option");
            post_brand = jsonObject.getString("post_brand");
            post_item = jsonObject.getString("post_item");
            post_tags = jsonObject.getString("post_tags");
            post_condition = jsonObject.getString("post_condition");
            post_size = jsonObject.getString("post_size");
            post_location = jsonObject.getString("post_location");
            post_postage = jsonObject.getString("post_postage");
            is_active = jsonObject.getInt("is_active");
            status_reason = jsonObject.getString("status_reason");
            is_sold = jsonObject.getInt("is_sold");
            if(!jsonObject.getString("lat").equals("null"))
                lat = jsonObject.getDouble("lat");
            if(!jsonObject.getString("lng").equals("null"))
                lng = jsonObject.getDouble("lng");
            is_multi = jsonObject.getInt("is_multi");
            multi_pos = jsonObject.getInt("multi_pos");
            multi_group = jsonObject.getString("multi_group");
            service_id = jsonObject.getString("service_id");
            product_id = jsonObject.getString("product_id");
            insurance_id = jsonObject.getString("insurance_id");
            qualification_id = jsonObject.getString("qualification_id");
            cancellations = jsonObject.getString("cancellations");
            scheduled = jsonObject.getInt("scheduled");
            updated_at = jsonObject.getLong("updated_at");
            created_at = jsonObject.getLong("created_at");

            JSONArray arrayList = jsonObject.getJSONArray("post_imgs");
            postImageModels.clear();
            for(int i =0;i<arrayList.length();i++){
                JSONObject postImages = arrayList.getJSONObject(i);
                PostImageModel postImageModel = new PostImageModel();
                postImageModel.setId(postImages.getInt("id"));
                postImageModel.setPost_id(postImages.getInt("post_id"));
                postImageModel.setPath(postImages.getString("path"));
                postImageModel.setCreated_at(postImages.getLong("created_at"));
                postImageModels.add(postImageModel);
            }
            likes = jsonObject.getInt("likes");
            comments = jsonObject.getInt("comments");
            read_created = jsonObject.getString("read_created");
            profile_image = jsonObject.getString("profile_img");
            profile_name = jsonObject.getString("profile_name");
            if(jsonObject.has("group_posts")){
                JSONArray group_posts =jsonObject.getJSONArray("group_posts");
                postEntities.clear();
                for(int i =0;i<group_posts.length();i++){
                    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                    newsFeedEntity.initModel(group_posts.getJSONObject(i));
                    postEntities.add(newsFeedEntity);
                }
            }

        }catch (Exception e){
            Log.d("aaaa",e.toString());
        }
    }
}
