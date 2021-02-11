package com.atb.app.adapter;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<String> list;
    private OnImageSelectListener listener;
    public static String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA };
    public static String[] mediaColumns = { MediaStore.Video.Media._ID };
    public ImageAdapter(Context context, ArrayList<String> list, OnImageSelectListener listener) {
        this.list = list;
        this.listener = listener;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_row_item, parent, false);

        return new ViewHolder(itemView);
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        File file = null;
        if (isVideoFile(list.get(position))){
            Log.e("VIDEO",list.get(position));
            long id= ContentUris.parseId(Uri.fromFile(new File(list.get(position))));
            Uri uri= ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            holder.iv_image.setImageURI(uri);
            holder.card_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onImageSelect(list.get(position));
                }
            });
        }else{
            Log.e("Image",list.get(position));
            file=new File(list.get(position));
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(file))
                    .setResizeOptions(new ResizeOptions(300, 300)).disableDiskCache().disableMemoryCache().build();
            holder.iv_image.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController( holder.iv_image.getController())
                            .setImageRequest(request)
                            .build());
            holder.card_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onImageSelect(list.get(position));
                }
            });
        }

    }

    public static String getThumbnailPathForLocalFile(Activity context,
                                                      Uri fileUri) {

        long fileId = getFileId(context, fileUri);

        MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

        Cursor thumbCursor = null;
        try {

            thumbCursor = context.managedQuery(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + " = "
                            + fileId, null, null);

            if (thumbCursor.moveToFirst()) {
                String thumbPath = thumbCursor.getString(thumbCursor
                        .getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                return thumbPath;
            }

        } finally {
        }

        return null;
    }

    public static long getFileId(Activity context, Uri fileUri) {

        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null,
                null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int id = cursor.getInt(columnIndex);

            return id;
        }

        return 0;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_image;
        SimpleDraweeView iv_image;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_image=itemView.findViewById(R.id.iv_image);
            card_image=itemView.findViewById(R.id.card_image);
        }
    }

    public interface OnImageSelectListener{
        void onImageSelect(String path);
    }
}
