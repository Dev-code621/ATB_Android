package com.atb.app.model;

import android.util.Log;

import com.android.volley.toolbox.JsonArrayRequest;
import com.atb.app.commons.Commons;
import com.atb.app.model.submodel.AttributeModel;
import com.atb.app.model.submodel.InsuranceModel;
import com.atb.app.model.submodel.PostImageModel;
import com.atb.app.model.submodel.VotingModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsFeedEntity {
    // postType:  "Advice", "Sales", "Service", "Poll"
    // "Text", "Image", "Video"
    // 0 - none selected, 1 - Cash on Colleciton, 2 - PayPal, 3 - both Cash and PayPal
    int id,user_id,post_type,poster_profile_type,media_type;
    boolean type;
    boolean select = false;
    ArrayList<NewsFeedEntity>  postEntities = new ArrayList();
    String title,description,brand,post_tags = "",post_condition,post_size,post_location,status_reason;
    String price = "0.00",deposit = "0.00",delivery_cost = "0.00",is_deposit_required,category_title,item_title,size_title,location_id,post_brand,post_item;
    int payment_options,delivery_option,is_active,is_sold;
    double lat = 0,lng=0;
    int is_multi,multi_pos,scheduled,likes,comments;
    String read_created,profile_name,profile_image,post_postage ;

    long updated_at,created_at;
    String multi_group,service_id,product_id,insurance_id,qualification_id,cancellations;

    ArrayList<PostImageModel>postImageModels = new ArrayList<>();
    ArrayList <VotingModel>poll_options = new ArrayList<>();
    ArrayList <CommentModel>commentModels = new ArrayList<>();
    boolean feedLike = false;
    boolean feedSave =false;
    UserModel userModel = new UserModel();
    ArrayList<VariationModel> variationModels = new ArrayList<>();
    ArrayList<String>attribute_titles = new ArrayList<>();
    ArrayList<InsuranceModel> insuranceModels = new ArrayList<>();
    ArrayList<InsuranceModel> qualifications = new ArrayList<>();


    int stock_level =0;
    HashMap<String, ArrayList<String>>hashMap = new HashMap<>();
    HashMap<String, VariationModel>stockMap = new HashMap<>();
    ArrayList<String>stock_levels = new ArrayList<>();
    public ArrayList<Boolean>booleans = new ArrayList<>();
    public ArrayList<String>prices = new ArrayList<>();
    ArrayList<String>completedValue = new ArrayList<>();
    String videovalue ="";


    public ArrayList<InsuranceModel> getInsuranceModels() {
        return insuranceModels;
    }

    public void setInsuranceModels(ArrayList<InsuranceModel> insuranceModels) {
        this.insuranceModels = insuranceModels;
    }

    public ArrayList<InsuranceModel> getQualifications() {
        return qualifications;
    }

    public void setQualifications(ArrayList<InsuranceModel> qualifications) {
        this.qualifications = qualifications;
    }

    public ArrayList<String> getCompletedValue() {
        return completedValue;
    }

    public void setCompletedValue(ArrayList<String> completedValue) {
        this.completedValue.clear();
        this.completedValue.addAll(completedValue);
    }

    public String getVideovalue() {
        return videovalue;
    }

    public void setVideovalue(String videovalue) {
        this.videovalue = videovalue;
    }

    public HashMap<String, ArrayList<String>> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, ArrayList<String>> hashMap) {
        this.hashMap.clear();
        this.hashMap.putAll(hashMap);
    }

    public HashMap<String, VariationModel> getStockMap() {
        return stockMap;
    }

    public void setStockMap(HashMap<String, VariationModel> stockMap) {
        this.stockMap.clear();
        this.stockMap.putAll(stockMap);
    }

    public ArrayList<String> getStock_levels() {
        return stock_levels;
    }

    public void setStock_levels(ArrayList<String> stock_levels) {
        this.stock_levels.clear();
        this.stock_levels.addAll(stock_levels);
    }

    public ArrayList<Boolean> getBooleans() {
        return booleans;
    }

    public void setBooleans(ArrayList<Boolean> booleans) {
        this.booleans.clear();
        this.booleans.addAll(booleans);
    }

    public ArrayList<String> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<String> prices) {
        this.prices.clear();
        this.prices.addAll(prices);
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    //group post require
    String attributes;

    public int getStock_level() {
        return stock_level;
    }

    public void setStock_level(int stock_level) {
        this.stock_level = stock_level;
    }

    public ArrayList<String> getAttribute_titles() {
        return attribute_titles;
    }

    public void setAttribute_titles(ArrayList<String> attribute_titles) {
        this.attribute_titles = attribute_titles;
    }

    public HashMap<String, ArrayList<AttributeModel>> getAttribute_map() {
        return attribute_map;
    }

    public void setAttribute_map(HashMap<String, ArrayList<AttributeModel>> attribute_map) {
        this.attribute_map = attribute_map;
    }

    HashMap<String, ArrayList<AttributeModel>> attribute_map =  new HashMap<>();
    public ArrayList<VariationModel> getVariationModels() {
        return variationModels;
    }

    public void setVariationModels(ArrayList<VariationModel> variationModels) {
        this.variationModels = variationModels;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public ArrayList<CommentModel> getCommentModels() {
        return commentModels;
    }

    public void setCommentModels(ArrayList<CommentModel> commentModels) {
        this.commentModels = commentModels;
    }

    public boolean isFeedLike() {
        return feedLike;
    }

    public void setFeedLike(boolean feedLike) {
        this.feedLike = feedLike;
    }

    public boolean isFeedSave() {
        return feedSave;
    }

    public void setFeedSave(boolean feedSave) {
        this.feedSave = feedSave;
    }

    public String getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(String delivery_cost) {
        this.delivery_cost = delivery_cost;
        if(delivery_cost.equals("") || delivery_cost.equals("null"))this.delivery_cost = "0.00";
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public ArrayList<VotingModel> getPoll_options() {
        return poll_options;
    }

    public void setPoll_options(ArrayList<VotingModel> poll_options) {
        this.poll_options = poll_options;
    }

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
        if(this.price.equals("null") || this.price.equals(""))
            this.price = "0.00";
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
            if(jsonObject.has("post_type"))
                post_type = jsonObject.getInt("post_type");
            poster_profile_type = jsonObject.getInt("poster_profile_type");
            media_type = jsonObject.getInt("media_type");
            title = jsonObject.getString("title");
            description = jsonObject.getString("description");
            brand = jsonObject.getString("brand");
            price = jsonObject.getString("price");
            if(jsonObject.has("deposit"))
                deposit = jsonObject.getString("deposit");
            if(jsonObject.has("deposit_amount"))
                deposit = jsonObject.getString("deposit_amount");
            if(jsonObject.has("is_deposit_required"))
                 is_deposit_required = jsonObject.getString("is_deposit_required");
            category_title = jsonObject.getString("category_title");
            item_title = jsonObject.getString("item_title");
            size_title = jsonObject.getString("size_title");
            payment_options = jsonObject.getInt("payment_options");
            location_id = jsonObject.getString("location_id");
            delivery_option = jsonObject.getInt("delivery_option");
            post_brand = jsonObject.getString("post_brand");
            post_item = jsonObject.getString("post_item");
            if(jsonObject.has("post_tags"))
                post_tags = jsonObject.getString("post_tags");
            post_condition = jsonObject.getString("post_condition");
            post_size = jsonObject.getString("post_size");
            post_location = jsonObject.getString("post_location");
            delivery_cost = jsonObject.getString("delivery_cost");
            is_active = jsonObject.getInt("is_active");
            if(jsonObject.has("status_reason"))
                status_reason = jsonObject.getString("status_reason");
            is_sold = jsonObject.getInt("is_sold");
            if(!jsonObject.getString("lat").equals("null") && ! jsonObject.getString("lat").equals(""))
                lat = jsonObject.getDouble("lat");
            if(!jsonObject.getString("lng").equals("null") && jsonObject.getString("lng").equals("null"))
                lng = jsonObject.getDouble("lng");
            if(jsonObject.has("is_multi"))
                is_multi = jsonObject.getInt("is_multi");
            if(jsonObject.has("multi_pos")){
                if(!jsonObject.getString("multi_pos").equals("null"))
                    multi_pos = jsonObject.getInt("multi_pos");
            }

            if(jsonObject.has("multi_group"))
                multi_group = jsonObject.getString("multi_group");
            if(jsonObject.has("service_id"))
                service_id = jsonObject.getString("service_id");
            if(jsonObject.has("product_id"))
                product_id = jsonObject.getString("product_id");
            if(jsonObject.has("insurance_id"))
                insurance_id = jsonObject.getString("insurance_id");
            if(jsonObject.has("qualification_id"))
                qualification_id = jsonObject.getString("qualification_id");
            if(jsonObject.has("cancellations"))
                cancellations = jsonObject.getString("cancellations");
            if(jsonObject.has("scheduled"))
                scheduled = jsonObject.getInt("scheduled");
            if(jsonObject.has("updated_at"))
                updated_at = jsonObject.getLong("updated_at");
            if(jsonObject.has("stock_level"))
                setStock_level(jsonObject.getInt("stock_level"));

            created_at = jsonObject.getLong("created_at");

            JSONArray arrayList = jsonObject.getJSONArray("post_imgs");
            postImageModels.clear();
            for(int i =0;i<arrayList.length();i++){
                JSONObject postImages = arrayList.getJSONObject(i);
                PostImageModel postImageModel = new PostImageModel();
                postImageModel.setId(postImages.getInt("id"));
                if(jsonObject.has("post_id"))
                    postImageModel.setPost_id(postImages.getInt("post_id"));
                postImageModel.setPath(postImages.getString("path"));
                postImageModel.setCreated_at(postImages.getLong("created_at"));
                postImageModels.add(postImageModel);
            }
            if(jsonObject.has("likes")) {

                likes = jsonObject.getInt("likes");
            }
            if(jsonObject.has("comments"))
                comments = jsonObject.getInt("comments");
            if(jsonObject.has("read_created"))
                read_created = jsonObject.getString("read_created");
            if(jsonObject.has("profile_img"))
                profile_image = jsonObject.getString("profile_img");
            if(jsonObject.has("profile_name"))
                profile_name = jsonObject.getString("profile_name");
            if(jsonObject.has("poll_options")){
                JSONArray poll_options =jsonObject.getJSONArray("poll_options");
                this.poll_options.clear();
                for(int i =0;i<poll_options.length();i++){
                    VotingModel votingModel = new VotingModel();
                    votingModel.initModel(poll_options.getJSONObject(i));
                    this.poll_options.add(votingModel);
                }
            }
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


    public void initDetailModel(JSONObject jsonObject){
        try {
            id = jsonObject.getInt("id");
            user_id = jsonObject.getInt("user_id");
            if(jsonObject.has("post_type"))
                post_type = jsonObject.getInt("post_type");
            poster_profile_type = jsonObject.getInt("poster_profile_type");
            media_type = jsonObject.getInt("media_type");
            title = jsonObject.getString("title");
            description = jsonObject.getString("description");
            brand = jsonObject.getString("brand");
            price = jsonObject.getString("price");
            if(jsonObject.has("deposit"))
                deposit = jsonObject.getString("deposit");
            if(jsonObject.has("deposit_amount"))
                deposit = jsonObject.getString("deposit_amount");
            is_deposit_required = jsonObject.getString("is_deposit_required");
            category_title = jsonObject.getString("category_title");
            item_title = jsonObject.getString("item_title");
            size_title = jsonObject.getString("size_title");
            payment_options = jsonObject.getInt("payment_options");
            location_id = jsonObject.getString("location_id");
            delivery_option = jsonObject.getInt("delivery_option");
            post_brand = jsonObject.getString("post_brand");
            post_item = jsonObject.getString("post_item");
            if(jsonObject.has("post_tags"))
                post_tags = jsonObject.getString("post_tags");
            post_condition = jsonObject.getString("post_condition");
            post_size = jsonObject.getString("post_size");
            post_location = jsonObject.getString("post_location");
            setDelivery_cost(jsonObject.getString("delivery_cost"));
            is_active = jsonObject.getInt("is_active");
            if(jsonObject.has("status_reason"))
                status_reason = jsonObject.getString("status_reason");
            is_sold = jsonObject.getInt("is_sold");
            if(!jsonObject.getString("lat").equals("null") && ! jsonObject.getString("lat").equals(""))
                lat = jsonObject.getDouble("lat");
            if(!jsonObject.getString("lng").equals("null") && jsonObject.getString("lng").equals("null"))
                lng = jsonObject.getDouble("lng");
            if(jsonObject.has("is_multi"))
                 is_multi = jsonObject.getInt("is_multi");
            if(jsonObject.has("multi_pos")){
                if(!jsonObject.getString("multi_pos").equals("null"))
                    multi_pos = jsonObject.getInt("multi_pos");
            }
            if(jsonObject.has("multi_group"))
                 multi_group = jsonObject.getString("multi_group");
            if(jsonObject.has("service_id"))
              service_id = jsonObject.getString("service_id");
            if(jsonObject.has("product_id"))
                 product_id = jsonObject.getString("product_id");
            if(jsonObject.has("insurance_id"))
                insurance_id = jsonObject.getString("insurance_id");
            if(jsonObject.has("qualification_id"))
                qualification_id = jsonObject.getString("qualification_id");
            if(jsonObject.has("cancellations"))
                cancellations = jsonObject.getString("cancellations");
            if(jsonObject.has("scheduled"))
                 scheduled = jsonObject.getInt("scheduled");
            if(jsonObject.has("updated_at"))
                updated_at = jsonObject.getLong("updated_at");
            created_at = jsonObject.getLong("created_at");



            JSONArray arrayList = jsonObject.getJSONArray("post_imgs");
            postImageModels.clear();
            for(int i =0;i<arrayList.length();i++){
                JSONObject postImages = arrayList.getJSONObject(i);
                PostImageModel postImageModel = new PostImageModel();
                postImageModel.setId(postImages.getInt("id"));
                if(jsonObject.has("post_id"))
                   postImageModel.setPost_id(postImages.getInt("post_id"));
                postImageModel.setPath(postImages.getString("path"));
                postImageModel.setCreated_at(postImages.getLong("created_at"));
                postImageModels.add(postImageModel);
            }
            if(jsonObject.has("read_created"))
                read_created = jsonObject.getString("read_created");
            if(jsonObject.has("poll_options")){
                JSONArray poll_options =jsonObject.getJSONArray("poll_options");
                this.poll_options.clear();
                for(int i =0;i<poll_options.length();i++){
                    VotingModel votingModel = new VotingModel();
                    votingModel.initModel(poll_options.getJSONObject(i));
                    this.poll_options.add(votingModel);
                }
            }
            if(jsonObject.has("group_posts")){
                JSONArray group_posts =jsonObject.getJSONArray("group_posts");
                postEntities.clear();
                for(int i =0;i<group_posts.length();i++){
                    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                    newsFeedEntity.initModel(group_posts.getJSONObject(i));
                    postEntities.add(newsFeedEntity);
                }
            }
            if(jsonObject.has("likes")){
                JSONArray array_like = jsonObject.getJSONArray("likes");
                likes = array_like.length();
                for(int i =0;i<array_like.length();i++){
                    if(array_like.getJSONObject(i).getInt("follower_user_id") == Commons.g_user.getId())
                        feedLike =true;
                }
            }
            if(jsonObject.has("comments")){
                JSONArray array_comment = jsonObject.getJSONArray("comments");
                comments = array_comment.length();
                commentModels.clear();
                for(int i =array_comment.length()-1;i>=0;i--){
                    JSONObject jsonObject_comment = array_comment.getJSONObject(i);
                    CommentModel commentModel = new CommentModel();
                    commentModel.initModel(jsonObject_comment,commentModels.size());
                    commentModels.add(commentModel);
                }
            }
            if(jsonObject.has("user")){
                JSONArray array_user = jsonObject.getJSONArray("user");
                JSONObject jsonObject_user = array_user.getJSONObject(0);
                userModel.initModel(jsonObject_user);
            }
            if(jsonObject.has("variations")){
                JSONArray jsonArray = jsonObject.getJSONArray("variations");
                variationModels.clear();
                ArrayList<AttributeModel> attributeModels = new ArrayList<>();
                for(int i =0;i<jsonArray.length();i++){
                    VariationModel variationModel = new VariationModel();
                    variationModel.initModel(jsonArray.getJSONObject(i));
                    for(int j =0;j<variationModel.getAttributeModels().size();j++){
                        AttributeModel attributeModel = variationModel.getAttributeModels().get(j);
                        if(!attribueContain(attributeModels,attributeModel))attributeModels.add(attributeModel);
                        if(i==0)
                            attribute_titles.add(attributeModel.getAttribute_title());
                    }
                    variationModels.add(variationModel);
                }
                for(int i=0;i<attribute_titles.size();i++){
                    ArrayList<AttributeModel> mapValue = new ArrayList<>();
                    for(int j =0;j<attributeModels.size();j++){
                        if(attributeModels.get(j).getAttribute_title().equals(attribute_titles.get(i))){
                            mapValue.add(attributeModels.get(j));
                        }
                    }
                    attribute_map.put(attribute_titles.get(i),mapValue);
                }
            }
            if(jsonObject.has("insurance")) {
                JSONArray jsonArray = jsonObject.getJSONArray("insurance");
                insuranceModels.clear();
                for(int i =0;i<jsonArray.length();i++){
                    InsuranceModel insuranceModel = new InsuranceModel();
                    insuranceModel.initModel(jsonArray.getJSONObject(i));
                    insuranceModels.add(insuranceModel);
                }
            }
            if(jsonObject.has("qualification")) {
                JSONArray jsonArray = jsonObject.getJSONArray("qualification");
                qualifications.clear();
                for(int i =0;i<jsonArray.length();i++){
                    InsuranceModel insuranceModel = new InsuranceModel();
                    insuranceModel.initModel(jsonArray.getJSONObject(i));
                    qualifications.add(insuranceModel);
                }
            }
        }catch (Exception e){
            Log.d("aaaa",e.toString());
        }
    }

    boolean attribueContain(ArrayList<AttributeModel> attributeModels,AttributeModel attributeModel){
        for(int i =0;i<attributeModels.size();i++){
            if(attributeModels.get(i).getAttribute_title().equals(attributeModel.getAttribute_title()) && attributeModels.get(i).getVariant_attirbute_value().equals(attributeModel.getVariant_attirbute_value()))return  true;
        }
        return false;
    }

    public VariationModel productHasStock(ArrayList<String>arrayList){
        for(int i =0;i<variationModels.size();i++){
            VariationModel variationModel = variationModels.get(i);
            boolean flag =false;
            for(int j =0;j<variationModel.attributeModels.size();j++){
                AttributeModel atr = variationModel.attributeModels.get(j);
                if(!atr.getVariant_attirbute_value().equals(arrayList.get(j))) {
                    flag = true;
                    break;
                }
            }
            if(!flag)return variationModel;
        }
        return  null;
    }
}
