package com.atb.app.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.base.BaseActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.submodel.InsuranceModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.barteksc.pdfviewer.PDFView;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.watermark.androidwm.WatermarkBuilder;
import com.watermark.androidwm.bean.WatermarkText;
import com.watermark.androidwm_light.bean.WatermarkImage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.atb.app.base.BaseActivity.closeProgress;
import static com.atb.app.base.BaseActivity.showProgress;

public class InsuranceViewDialog extends DialogFragment {

    private InsuranceViewDialog.OnConfirmListener listener;
    ArrayList<InsuranceModel> qualifications = new ArrayList<>();
    PDFView pdfView;
    ImageView imv_content,imageView;
    int type ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.insurance_view_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(InsuranceViewDialog.OnConfirmListener listener,ArrayList<InsuranceModel> qualifications,int type) {
        this.listener = listener;
        this.qualifications = qualifications;
        this.type = type;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView txv_title = view.findViewById(R.id.txv_title);
        TextView txv_time = view.findViewById(R.id.txv_time);
        imv_content = view.findViewById(R.id.imv_content);
        TextView txv_download = view.findViewById(R.id.txv_download);
        TextView txv_ok = view.findViewById(R.id.txv_ok);
        pdfView = view.findViewById(R.id.pdfView);
        imageView = view.findViewById(R.id.imageView);
        if(type ==0) {
            txv_title.setText("Electrical Insurance");
            txv_time.setText("Insurance Until " + Commons.getDisplayDate1(qualifications.get(0).getExpiry()));
        }else {
            txv_title.setText("Electrical Support");
            txv_time.setText("Qualified Since " + Commons.getDisplayDate1(qualifications.get(0).getExpiry()));
        }

        String file_name = qualifications.get(0).getFile();
        if(qualifications.get(0).getFile().contains(".pdf")){
            new DownloadFileFromURL().execute(file_name);

        }
        else {
            Glide.with(getActivity()).load(file_name).placeholder(R.drawable.image_thumnail).into(imv_content);

        }
        txv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onDownload();
//                Toast.makeText(getActivity(), "Repost Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        txv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        imageView.setAlpha(127);
        com.watermark.androidwm.bean.WatermarkText watermarkText = new WatermarkText("ATB Approved")
                .setPositionX(0.5)
                .setPositionY(0.5)
                .setTextAlpha(255)
                .setRotation(30)
                .setTextSize(15)
                .setTextColor(Color.WHITE)
                .setTextShadow(0.1f, 5, 5, getContext().getColor(R.color.grey));

        WatermarkBuilder.create(getContext(), imageView)
                .setTileMode(true)
                .loadWatermarkText(watermarkText)
                .getWatermark()
                .setToImageView(imageView);

    }




    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public interface OnConfirmListener {
        void onDownload();
    }

    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            ((CommonActivity)getContext()).showProgress();
            super.onPreExecute();

        }

        /**
         * Downloading file in background thread
         * */
        File file;
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String[] str = f_url[0].split("/");
                file = new File(path, str[str.length-1]);
                if(file.exists()) {
                    Log.d("bbbbbbbb","File exist");
                    return null;
                }

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream

                OutputStream output = new FileOutputStream(file);

                byte[] data = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                ((CommonActivity)getContext()).closeProgress();
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            ((CommonActivity)getContext()).closeProgress();
            Bitmap bitmap = ((CommonActivity)getContext()).generateImageFromPdf(Uri.fromFile(file));
            Glide.with(getActivity()).load(bitmap).placeholder(R.drawable.image_thumnail).into(imv_content);
        }

    }
}

