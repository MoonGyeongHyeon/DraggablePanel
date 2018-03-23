package com.kakao.mycustomviewexample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by khan.moon on 2018. 3. 21..
 */

public class DraggablePanel extends RelativeLayout implements GestureDetector.OnGestureListener {
    public static final String TAG = "DraggablePanel";

    private float dX, dY;
    private int deviceWidth, deviceHeight;
    private int headerHeight;   // StatusBar + Toolbar
    private GestureDetectorCompat gestureDetector;

    public DraggablePanel(Context context) {
        this(context, null);
    }

    public DraggablePanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggablePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        deviceWidth = getResources().getDisplayMetrics().widthPixels;
        deviceHeight = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        calculateHeaderHeight();
    }

    private void calculateHeaderHeight() {
        int[] location = new int[2];
        getLocationOnScreen(location);

        headerHeight = location[1] - (int)getY();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isConsumed = false;

        isConsumed |= gestureDetector.onTouchEvent(event);
        isConsumed |= super.onTouchEvent(event);
        return isConsumed;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown");
        dX = e.getRawX() - getX();
        dY = e.getRawY() - getY();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "onScroll");
        float destX, destY;
        destX = e2.getRawX() - dX;
        destY = e2.getRawY() - dY;

        if (destX < 0) {
            destX = 0;
        } else if (destX + getWidth() > deviceWidth) {
            destX = deviceWidth - getWidth();
        }
        if (destY < 0) {
            destY = 0;
        } else if (destY + getHeight() > deviceHeight - headerHeight) {
            destY = deviceHeight - headerHeight - getHeight();
        }

        animate()
                .x(destX)
                .y(destY)
                .setDuration(0)
                .start();

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "onFling");

        return false;
    }
}
