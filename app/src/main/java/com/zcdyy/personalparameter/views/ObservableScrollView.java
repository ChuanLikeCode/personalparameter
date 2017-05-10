package com.zcdyy.personalparameter.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by zhouchuan on 2017/3/6.
 */

public class ObservableScrollView extends ScrollView {
    private int mTouchSlop;
    private int downX;
    private int downY;
        private ScrollViewListener scrollViewListener = null;

        public ObservableScrollView(Context context) {
            super(context);
            mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }

        public ObservableScrollView(Context context, AttributeSet attrs,
                                    int defStyle) {
            super(context, attrs, defStyle);
            mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }

        public ObservableScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }

        public void setScrollViewListener(ScrollViewListener scrollViewListener) {
            this.scrollViewListener = scrollViewListener;
        }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
        @Override
        protected void onScrollChanged(int x, int y, int oldx, int oldy) {
            super.onScrollChanged(x, y, oldx, oldy);
            if (scrollViewListener != null) {
                scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
            }
        }

    public interface ScrollViewListener {

        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    }
}
