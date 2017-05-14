package com.zcdyy.personalparameter.ui.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.utils.imageprogress.ProgressImageView;
import com.zcdyy.personalparameter.utils.imageprogress.ProgressModelLoader;

import java.lang.ref.WeakReference;

public class ShowImageActivity extends BaseActivity {
    private ProgressImageView progressImageView;
    private String path;
    private ViewGroup container;
    @Override
    protected void findViewByIDS() {
        progressImageView = findViewsById(R.id.progress_imageview);
        container = findViewsById(R.id.container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_show_image);
//        setAnimation();
        initData();
    }


    @Override
    public void backClick(View view) {
        finish();
        this.overridePendingTransition(R.anim.anim_show,R.anim.anim_dismiss);
//        finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.anim_show,R.anim.anim_dismiss);
    }

    private void initData() {
        path = getIntent().getStringExtra("path");
        Glide.with(this).using(new ProgressModelLoader(new ProgressHandler(this,progressImageView)))
                .load(path).diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(progressImageView.getImageView());
    }
    private static class ProgressHandler extends Handler{
        private final WeakReference<Activity> mActivity;
        private final ProgressImageView mProgressImageView;
        private ProgressHandler(Activity activity,ProgressImageView progressImageView){
            super(Looper.getMainLooper());
            mActivity = new WeakReference<Activity>(activity);
            mProgressImageView = progressImageView;
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = mActivity.get();
            if (activity !=null){
                switch (msg.what){
                    case 1:
                        int percent = msg.arg1*100/msg.arg2;
                        mProgressImageView.setProgress(percent);
                        if (percent == 100){
                            mProgressImageView.dismissProgress();
                        }
                        break;
                }
            }
        }
    }
}
