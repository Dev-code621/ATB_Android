package com.atb.app.view.zoom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.appcompat.widget.ContentFrameLayout;

import com.atb.app.R;
import com.atb.app.view.zoom.zoomableview.ZoomableDraweeView;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public final class ZoomAnimation {
    public Animator mCurrentAnimator;
    private ZoomableDraweeView mImageZoom;
    private Activity activity;
    private View zoomThumbVIew;
    private float startScale;
    private Rect startBounds;
    Context context;
    public ZoomAnimation(Activity activity) {
        this.activity = activity;
    }

    public void zoomReverse(View view, String url, long duration) {
        zoomImageFromThumbReverse(view,url, activity, duration);
    }

    public void zoom(View view, String uri, long duration, int ic_profile,Context context) {
        this.context = context;
        zoomImageFromThumb(view, activity,uri, duration,ic_profile);
    }

    public AnimatorSet set1;
    public AnimatorSet set2;

    private void zoomImageFromThumbReverse(final View thumbView,String url, Activity activity, long duration) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        ContentFrameLayout container = (ContentFrameLayout) activity.findViewById(android.R.id.content);
        mImageZoom = new ZoomableDraweeView(thumbView.getContext());
        mImageZoom.setBackgroundColor(Color.BLACK);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        mImageZoom.setLayoutParams(params);
        mImageZoom.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        mImageZoom.setImageURI(url);
        mImageZoom.getHierarchy().setPlaceholderImage(R.drawable.image_thumnail);
        mImageZoom.setVisibility(View.GONE);
        container.addView(mImageZoom);
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        Point globalOffset = new Point();
        thumbView.getGlobalVisibleRect(startBounds);
        container.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);
        final float startScale;
        float set;
        float startScaleFinal;
        if ((float) finalBounds.width() / (float) finalBounds.height() > (float) startBounds.width() / (float) startBounds.height()) {
            startScale = (float) startBounds.height() / (float) finalBounds.height();
            set = startScale * (float) finalBounds.width();
            startScaleFinal = (set - (float) startBounds.width()) / 2.0F;
            startBounds.left = (int) ((float) startBounds.left - startScaleFinal);
            startBounds.right = (int) ((float) startBounds.right + startScaleFinal);
        } else {
            startScale = (float) startBounds.width() / (float) finalBounds.width();
            set = startScale * (float) finalBounds.height();
            startScaleFinal = (set - (float) startBounds.height()) / 2.0F;
            startBounds.top = (int) ((float) startBounds.top - startScaleFinal);
            startBounds.bottom = (int) ((float) startBounds.bottom + startScaleFinal);
        }

        // thumbView.setAlpha(0.0F);
        // mImageZoom.setVisibility(View.VISIBLE);
        mImageZoom.setPivotX(0.0F);
        mImageZoom.setPivotY(0.0F);
        set2 = new AnimatorSet();
        set2.setDuration(duration);
        set2.play(ObjectAnimator.ofFloat(mImageZoom, View.X, new float[]{(float) startBounds.left, (float) finalBounds.left})).with(ObjectAnimator.ofFloat(mImageZoom, View.Y, new float[]{(float) startBounds.top, (float) finalBounds.top})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_X, new float[]{startScale, 1.0F})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_Y, new float[]{startScale, 1.0F}));
        set2.setInterpolator(new ReverseInterpolator());
        set2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mImageZoom.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mImageZoom.setVisibility(View.GONE);
            }
        });

        set1 = new AnimatorSet();
        set1.setDuration(duration);
        set1.play(ObjectAnimator.ofFloat(mImageZoom, View.X, new float[]{(float) startBounds.left, (float) finalBounds.left})).with(ObjectAnimator.ofFloat(mImageZoom, View.Y, new float[]{(float) startBounds.top, (float) finalBounds.top})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_X, new float[]{startScale, 1.0F})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_Y, new float[]{startScale, 1.0F}));
        set1.setInterpolator(new DecelerateInterpolator());
        set1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mImageZoom.setVisibility(View.VISIBLE);
            }

            public void onAnimationEnd(Animator animation) {
                //   thumbView.setAlpha(1.0F);
                //    mImageZoom.setVisibility(8);

                set2.start();
                mCurrentAnimator = null;
            }

            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
                mImageZoom.setVisibility(View.GONE);
            }
        });
        set1.start();

        mCurrentAnimator = set1;
        mImageZoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                mImageZoom.setBackgroundColor(0);
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(mImageZoom, View.X, new float[]{(float) startBounds.left})).with(ObjectAnimator.ofFloat(mImageZoom, View.Y, new float[]{(float) startBounds.top})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_X, new float[]{startScale})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_Y, new float[]{startScale}));
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1.0F);
                        mImageZoom.setVisibility(View.GONE);
                        if (mCurrentAnimator != null) {
                            mCurrentAnimator.cancel();
                        }
                        mCurrentAnimator = null;
                    }

                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1.0F);
                        mImageZoom.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    public View getZoomThumbVIew() {
        return zoomThumbVIew;
    }

    public float getStartScale() {
        return startScale;
    }

    public Rect getStartBounds() {
        return startBounds;
    }

    private void zoomImageFromThumb(final View thumbView, Activity activity, String uri, long duration, int ic_profile) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        zoomThumbVIew=thumbView;
        ContentFrameLayout container = (ContentFrameLayout) activity.findViewById(android.R.id.content);
        mImageZoom = new ZoomableDraweeView(thumbView.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        mImageZoom.setLayoutParams(params);
        mImageZoom.setActualImageResource(ic_profile);
        mImageZoom.setClickable(true);
        if (uri != null && !uri.equals("")) {
            Glide.with(context).load(uri).placeholder(R.drawable.image_thumnail).dontAnimate().into(mImageZoom);

//            if(uri.contains("http"))
//                setUri(Uri.parse(uri),mImageZoom);
//            else {
           //     setUri(((CommonActivity)context).getUri(uri)  , mImageZoom);
//            }
        }else {
            mImageZoom.setActualImageResource(ic_profile);
        }
        mImageZoom.setBackgroundColor(Color.BLACK);
        mImageZoom.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        mImageZoom.getHierarchy().setPlaceholderImage(R.drawable.image_thumnail, ScalingUtils.ScaleType.FIT_CENTER);
        mImageZoom.setVisibility(View.GONE);

        container.addView(mImageZoom);
          startBounds = new Rect();
        final Rect finalBounds = new Rect();
        Point globalOffset = new Point();
        thumbView.getGlobalVisibleRect(startBounds);
        container.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float set;
        float startScaleFinal;
        if ((float) finalBounds.width() / (float) finalBounds.height() > (float) startBounds.width() / (float) startBounds.height()) {
            startScale = (float) startBounds.height() / (float) finalBounds.height();
            set = startScale * (float) finalBounds.width();
            startScaleFinal = (set - (float) startBounds.width()) / 2.0F;
            startBounds.left = (int) ((float) startBounds.left - startScaleFinal);
            startBounds.right = (int) ((float) startBounds.right + startScaleFinal);
        } else {
            startScale = (float) startBounds.width() / (float) finalBounds.width();
            set = startScale * (float) finalBounds.height();
            startScaleFinal = (set - (float) startBounds.height()) / 2.0F;
            startBounds.top = (int) ((float) startBounds.top - startScaleFinal);
            startBounds.bottom = (int) ((float) startBounds.bottom + startScaleFinal);
        }

        // thumbView.setAlpha(0.0F);
        // mImageZoom.setVisibility(View.VISIBLE);
        mImageZoom.setPivotX(0.0F);
        mImageZoom.setPivotY(0.0F);

        set1 = new AnimatorSet();
        set1.setDuration(duration);
        set1.play(ObjectAnimator.ofFloat(mImageZoom, View.X, new float[]{(float) startBounds.left, (float) finalBounds.left})).with(ObjectAnimator.ofFloat(mImageZoom, View.Y, new float[]{(float) startBounds.top, (float) finalBounds.top})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_X, new float[]{startScale, 1.0F})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_Y, new float[]{startScale, 1.0F}));
        set1.setInterpolator(new DecelerateInterpolator());
        set1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mImageZoom.setVisibility(View.VISIBLE);
            }

            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
                mImageZoom.setVisibility(View.GONE);
            }
        });
        set1.start();

        mCurrentAnimator = set1;
        mImageZoom.setClickListner(new ZoomableDraweeView.OnClickListner() {
            @Override
            public void OnClick() {
                onImageClick(thumbView,startBounds,startScale);
            }
        });
    }

    public void setUri(Uri url, SimpleDraweeView sdv){

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(url)
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions( new ResizeOptions(1000,200))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(sdv.getController())
                .setTapToRetryEnabled(true)
                .build();
        sdv.setController(controller);
    }

    public void onImageClick(final View thumbView, Rect startBounds, float startScale) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        thumbView.setAlpha(1.0F);
        mImageZoom.setVisibility(View.GONE);
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        mCurrentAnimator = null;

     /*   mImageZoom.setBackgroundColor(0);
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(mImageZoom, View.X, new float[]{(float) startBounds.left})).with(ObjectAnimator.ofFloat(mImageZoom, View.Y, new float[]{(float) startBounds.top})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_X, new float[]{startScale})).with(ObjectAnimator.ofFloat(mImageZoom, View.SCALE_Y, new float[]{startScale}));
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                thumbView.setAlpha(1.0F);
                mImageZoom.setVisibility(View.GONE);
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }
                mCurrentAnimator = null;
            }

            public void onAnimationCancel(Animator animation) {
                thumbView.setAlpha(1.0F);
                mImageZoom.setVisibility(View.GONE);
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;*/
    }

}
