package com.atb.app.util.model;

public class MediaModel {

    public enum MEDIA_TYPE {IMAGE, VIDEO};

    String id = "";
    MEDIA_TYPE mediaType;
    String mediaPath = "";
    String thumbPath = "";
    String videoDuration = "";
    long duration = 0; // seconds
    String userCaption = "";


    boolean selected = false;

    public MediaModel() {}

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public MEDIA_TYPE getMediaType() {
        return mediaType;
    }

    public void setMediaType(MEDIA_TYPE mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDurationString() {

        if (duration == 0)
            return "";

        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);

        String durationString = "";

        if (hours > 0) {
            durationString += hours + ":";
        }

        durationString += String.format("%02d:%02d", minutes, seconds);
        return durationString;
    }



    @Override
    public boolean equals(Object obj) {

        MediaModel one = (MediaModel) obj;
        return one.getId().equals(id);
    }

}
