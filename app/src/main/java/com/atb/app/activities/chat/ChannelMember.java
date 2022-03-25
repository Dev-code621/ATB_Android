package com.atb.app.activities.chat;

public class ChannelMember {

    String id,name, imageUrl;
    public void initValue(String _id,String _name , String _imageUrl){
        setId(_id);
        setName(_name);
        setImageUrl(_imageUrl);

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
