package com.atb.app.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.atb.app.util.model.MediaModel;

import java.io.File;
import java.util.ArrayList;



public class MediaPicker {
    private OnMediaPickListener listener;
    private Context context;

    public MediaPicker(Context context) {
        //this.listener=listener;
        this.context = context;

    }


    public void chooseImage() {

    }

    public void chooseBoth() {

    }


    public interface OnMediaPickListener {
        void onMediaPicked(String path, boolean isVideo);
    }
    public ArrayList<String> getAllShownImagesPath(Activity activity) {
        ArrayList<String> list = new ArrayList<>();
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, column_id, thum;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media._ID, MediaStore.Images.Thumbnails.DATA};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " ASC");

        assert cursor != null;
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        /*column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        column_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        thum = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);*/

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            /*Log.e("Folder", cursor.getString(column_index_folder_name));
            Log.e("column_id", cursor.getString(column_id));
            Log.e("thum", cursor.getString(thum));*/

            list.add(absolutePathOfImage);

        }
        return list;
    }

    public ArrayList<MediaModel> getMediaFromDevice() {
        ArrayList<MediaModel> allMedias = new ArrayList<>();
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE,
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");

        Cursor cursor = context.getContentResolver().query( queryUri, projection, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " ASC" );

        if (cursor.moveToFirst()) {
            do {

                MediaModel mediaModel = new MediaModel();

                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                int type = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));

                if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    mediaModel.setMediaType(MediaModel.MEDIA_TYPE.IMAGE);
                } else {
                    mediaModel.setMediaType(MediaModel.MEDIA_TYPE.VIDEO);
                    int duration=0;
                    if (new File(path).exists()) {
                        try {
                            MediaPlayer mp = MediaPlayer.create(context, Uri.parse(path));
                            duration = mp.getDuration();
                            mp.release();


                        }catch (Exception e){
                            Log.e("ERRoR",e.getMessage());
                        }
                    }else {
                        Log.e("NO FILE",path);
                    }
                    mediaModel.setDuration(duration);
                }

                mediaModel.setId(String.valueOf(id));
                mediaModel.setMediaPath(path);

                if (!allMedias.contains(mediaModel))
                    allMedias.add(0, mediaModel);

            } while (cursor.moveToNext());
        }
        cursor.close();


        return allMedias;
    }

}

