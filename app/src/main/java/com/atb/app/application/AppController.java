package com.atb.app.application;

import android.app.ActivityManager;
import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lky.toucheffectsmodule.TouchEffectsManager;
import com.lky.toucheffectsmodule.types.TouchEffectsViewType;
import com.lky.toucheffectsmodule.types.TouchEffectsWholeType;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.function.Supplier;

import io.branch.referral.Branch;

public class AppController extends Application {

    private static AppController instance;
    public static final String TAG = AppController.class.getSimpleName();

    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config.json";


    public RequestQueue _requestQueue;
    static {
        TouchEffectsManager.build(TouchEffectsWholeType.SCALE)
                .addViewType(TouchEffectsViewType.ALL)
                .setListWholeType(TouchEffectsWholeType.RIPPLE)
                .setAspectRatioType(1f,TouchEffectsWholeType.RIPPLE);//宽高比大于4时启动水波纹

    }
    @Override
    public void onCreate() {
        super.onCreate();
//        TerminalApplicationDelegate.onCreate(this);

        MultiDex.install(this);
//        Tiny.getInstance().init(this);
        // Branch logging for debugging
        Branch.enableLogging();
        // Branch object initialization
        Branch.getAutoInstance(this);
        instance = this;
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig
                .newBuilder(getApplicationContext())
                .setDiskCacheEnabled(false)
                .setDownsampleEnabled(true)
//                .setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier(activityManager))
                .build();

        Fresco.initialize(getApplicationContext(), imagePipelineConfig);




        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(1500000) // 1.5 Mb
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .enableLogging() // Not necessary in common
                .build();
        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);

        startCatcher(); // For catcher the crash
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public class LollipopBitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {

        private ActivityManager activityManager;

        public LollipopBitmapMemoryCacheParamsSupplier(ActivityManager activityManager) {
            this.activityManager = activityManager;
        }

        @Override
        public MemoryCacheParams get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return new MemoryCacheParams(getMaxCacheSize(), 1, 1, 1, 1);
            } else {
                return new MemoryCacheParams(
                        getMaxCacheSize(),
                        256,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE);
            }
        }

        private int getMaxCacheSize() {
            final int maxMemory =
                    Math.min(activityManager.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);

            if (maxMemory < 32 * ByteConstants.MB) {
                return 4 * ByteConstants.MB;
            } else if (maxMemory < 64 * ByteConstants.MB) {
                return 6 * ByteConstants.MB;
            } else {
                return maxMemory / 4;
            }
        }

    }

    public static synchronized AppController getInstance() {
        return instance;
    }


    public RequestQueue getRequestQueue() {

        if (_requestQueue == null) {
            _requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return _requestQueue;
    }

    public void cancelPendingRequests(Object tag) {
        if (_requestQueue != null) {
            _requestQueue.cancelAll(tag);
        }
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {

        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    //*************** Crash  ***************//
    private static final String LOG_TAG = "CrashCatch";
    private void startCatcher() {
        Thread.UncaughtExceptionHandler systemUncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
        // the following handler is used to catch exceptions thrown in background threads
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtHandler(new Handler()));

        while (true) {
            try {
                Log.v(LOG_TAG, "Starting crash catch Looper");
                Looper.loop();
                Thread.setDefaultUncaughtExceptionHandler(systemUncaughtHandler);
                throw new RuntimeException("Main thread loop unexpectedly exited");
            } catch (BackgroundException e) {
                Log.e(LOG_TAG, "Caught the exception in the background thread " + e.threadName + ", TID: " + e.tid, e.getCause());
                showCrashDisplayActivity(e.getCause());
            } catch (Throwable e) {
                Log.e(LOG_TAG, "Caught the exception in the UI thread, e:", e);
                showCrashDisplayActivity(e);
            }
        }
    }
    void showCrashDisplayActivity(Throwable e) {
//        Intent i = new Intent(this, CrashDisplayActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.putExtra("e", e);
//        startActivity(i);
    }
    /**
     * This handler catches exceptions in the background threads and propagates them to the UI thread
     */
    static class UncaughtHandler implements Thread.UncaughtExceptionHandler {
        private final Handler mHandler;
        UncaughtHandler(Handler handler) {
            mHandler = handler;
        }
        public void uncaughtException(Thread thread, final Throwable e) {
            Log.v(LOG_TAG, "Caught the exception in the background " + thread + " propagating it to the UI thread, e:", e);
            final int tid = Process.myTid();
            final String threadName = thread.getName();
            mHandler.post(new Runnable() {
                public void run() {
                    throw new BackgroundException(e, tid, threadName);
                }
            });
        }
    }
    /**
     * Wrapper class for exceptions caught in the background
     */
    static class BackgroundException extends RuntimeException {
        final int tid;
        final String threadName;
        /**
         * @param e original exception
         * @param tid id of the thread where exception occurred
         * @param threadName name of the thread where exception occurred
         */
        BackgroundException(Throwable e, int tid, String threadName) {
            super(e);
            this.tid = tid;
            this.threadName = threadName;
        }
    }
}

