package com.atb.app.model;

import com.atb.app.model.submodel.InsuranceModel;
import com.atb.app.model.submodel.PostImageModel;

import java.util.ArrayList;

public class ServiceModel {
    int id,user_id,poster_profile_type,media_type;
    String title,brand,image,description,category_title,item_title,size_title,location_id,post_brand,post_item,post_condition,post_size;
    String post_location,approval_reason;
    int is_deposit_required,deposit_amount,insurance_id,qualification_id,cancellations,payment_options,delivery_option,post_postage;
    int is_active,is_sold,approved;
    double price,lat,lng ;
    long created_at;
    ArrayList<InsuranceModel>insuranceModels = new ArrayList<>();
    ArrayList<PostImageModel>postImageModels = new ArrayList<>();

}
