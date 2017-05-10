package com.zcdyy.personalparameter.views.showimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


import com.zcdyy.personalparameter.R;

import pl.droidsonroids.gif.GifImageView;

public class ShowImageActivity extends Activity implements OnClickListener {

    private View fanhuiView;
    private View fenxiangView;
    private View baocunView;
    private LinearLayout neirongView;

    private GifImageView network_gifimageview;
//	private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        fanhuiView = findViewById(R.id.activity_showimage_fanhui);
        fanhuiView.setOnClickListener(this);
        fenxiangView = findViewById(R.id.activity_showimage_fenxiang);
        fenxiangView.setOnClickListener(this);
        baocunView = findViewById(R.id.activity_showimage_baocun);
        baocunView.setOnClickListener(this);
        neirongView = (LinearLayout) findViewById(R.id.activity_showimage_neirong);
        Intent intent = getIntent();
        boolean isGif = intent.getBooleanExtra("isGif", false);
        String address = intent.getStringExtra("address");
        String width = intent.getStringExtra("width");
        String height = intent.getStringExtra("height");
        neirongView.addView(new MyImageView(this, false) {
            public void onClick(View v) {
            }

            ;
        }.setAddress(null, address, isGif, width, height));
        neirongView.postInvalidate();
    }

    @Override
    public void onClick(View v) {
        if (v == fanhuiView) {
            finish();
        }
        if (v == fenxiangView) {
            // TODO 分享
        }
        if (v == baocunView) {
            // TODO 保存
        }

    }
}
