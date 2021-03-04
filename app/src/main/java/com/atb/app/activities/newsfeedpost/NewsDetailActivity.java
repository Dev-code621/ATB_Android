package com.atb.app.activities.newsfeedpost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.adapter.CommentAdapter;
import com.atb.app.adapter.PollEmageAdapter;
import com.atb.app.adapter.SliderImageAdapter;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Helper;
import com.atb.app.model.CommentModel;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class NewsDetailActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_back,imv_profile,imv_navigation,imv_camera,imv_send,imv_close;
    LinearLayout lyt_reply;
    FrameLayout lyt_image;
    TextView txv_name,txv_id,txv_replyname;
    SliderView imageSlider;
    SliderImageAdapter setSliderAdapter;
    ListView list_comment;
    RecyclerView recyclerView_images;
    EditText edt_comment;
    PollEmageAdapter pollEmageAdapter;
    ArrayList<String>completedValue = new ArrayList<>();
    ArrayList<String>returnValue = new ArrayList<>();
    CommentAdapter commentAdapter ;
    ArrayList<CommentModel>commentModels = new ArrayList<>();
    int comment_level =0;
    NestedScrollView scrollView;
    int parentPosstion =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        imv_back = findViewById(R.id.imv_back);
        imv_profile = findViewById(R.id.imv_profile);
        imv_navigation = findViewById(R.id.imv_navigation);
        lyt_image = findViewById(R.id.lyt_image);
        txv_name = findViewById(R.id.txv_name);
        txv_id = findViewById(R.id.txv_id);
        imageSlider = findViewById(R.id.imageSlider);
        imv_camera = findViewById(R.id.imv_camera);
        imv_send = findViewById(R.id.imv_send);
        list_comment = findViewById(R.id.list_comment);
        recyclerView_images = findViewById(R.id.recyclerView_images);
        edt_comment = findViewById(R.id.edt_comment);
        scrollView = findViewById(R.id.scrollView);
        imv_close = findViewById(R.id.imv_close);
        lyt_reply = findViewById(R.id.lyt_reply);
        txv_replyname = findViewById(R.id.txv_replyname);


        setSliderAdapter = new SliderImageAdapter(this);
        imageSlider.setSliderAdapter(setSliderAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorSelectedColor(Color.WHITE);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        imageSlider.setScrollTimeInSec(3);
        imageSlider.setAutoCycle(true);
        imageSlider.startAutoCycle();

        loadLayout();

        imv_back.setOnClickListener(this);
        imv_profile.setOnClickListener(this);
        imv_navigation.setOnClickListener(this);
        imv_camera.setOnClickListener(this);
        imv_send.setOnClickListener(this);
        imv_close.setOnClickListener(this);
        edt_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                imv_send.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_send));
                if(edt_comment.getText().length()>0) {
                    imv_send.setColorFilter(_context.getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                }
                else {
                    imv_send.clearColorFilter();
                    imv_send.setColorFilter(_context.getResources().getColor(R.color.boder_color), PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pollEmageAdapter = new PollEmageAdapter(this, new PollEmageAdapter.SelectListener() {
            @Override
            public void onClose(int posstion) {
                completedValue.remove(posstion);
                reloadImages();
            }

            @Override
            public void addImage() {
                selectImage();
            }
        },3);
        recyclerView_images.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView_images.setAdapter(pollEmageAdapter);
        commentAdapter = new CommentAdapter(this);
        list_comment.setAdapter(commentAdapter);
        Keyboard();

    }
    void loadLayout(){
        ArrayList<String> sliderItems = new ArrayList<>();
        sliderItems.add("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        sliderItems.add("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
        sliderItems.add("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        setSliderAdapter.renewItems(sliderItems);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.imv_close:
                comment_level = 0;
                hideKeyboard();
                initComment();
                break;
            case R.id.imv_profile:
                Log.d("aaaa","ok");
                break;
            case R.id.imv_navigation:
                break;
            case R.id.imv_camera:
                selectImage();
                break;
            case R.id.imv_send:
                if(completedValue.size()>0|| edt_comment.getText().toString().length()>0)
                    sendComment();
                break;
        }
    }

    void sendComment(){
        CommentModel commentModel = new CommentModel();
        commentModel.setComment(edt_comment.getText().toString());
        commentModel.setLevel(comment_level);
        commentModel.setUserName("MingFang");
        commentModel.setImage_url(completedValue);
        if(comment_level==1)
            commentModel.setParentPosstion(parentPosstion);
        else
            commentModel.setParentPosstion(commentModels.size());

        if(comment_level==0) {
            commentModels.add(commentModel);

        }else {
            commentModels.get(parentPosstion).getReplies().add(commentModel);
        }
        commentAdapter.setRoomData(commentModels);
        Helper.getListViewSize(list_comment);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
                hideKeyboard();
            }
        });
        initComment();
    }


    public void replyComment(int posstion){
        comment_level = 1;
        parentPosstion = posstion;
        lyt_reply.setVisibility(View.VISIBLE);
        txv_replyname.setText(commentModels.get(posstion).getUserName());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(edt_comment.getWindowToken(), 0);
    }

    void  initComment(){
        edt_comment.setText("");
        completedValue.clear();
        reloadImages();
        comment_level =0;
        lyt_reply.setVisibility(View.GONE);
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_comment.getWindowToken(), 0);
    }
    void Keyboard(){
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_comment.getWindowToken(), 0);
                return false;
            }
        });


    }

    void selectImage(){
        Options options = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(3-completedValue.size())                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        Pix.start(this, options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            completedValue.addAll(returnValue);
            reloadImages();
        }
    }

    void reloadImages(){
        if(completedValue.size()>0){
            pollEmageAdapter.setData(completedValue);
            recyclerView_images.setVisibility(View.VISIBLE);
        }else {
            recyclerView_images.setVisibility(View.GONE);
        }
    }
}