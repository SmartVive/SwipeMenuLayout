package com.example.swipemenulayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import androidx.annotation.Nullable;

public class SwipeMenuLayout2 extends ViewGroup {
    public SwipeMenuLayout2(Context context) {
        super(context);
    }

    public SwipeMenuLayout2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeMenuLayout2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
