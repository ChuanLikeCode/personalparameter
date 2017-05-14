package com.zcdyy.personalparameter.listener;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * Created by yyuand on 2017.4.22.
 */

public interface OnItemHealthCircleClick {
    void onPraiseClick(ImageView praise, TextView count,int position);
    void onItemClick(int position);
    void onImgClick(ImageView img, int position);
}
