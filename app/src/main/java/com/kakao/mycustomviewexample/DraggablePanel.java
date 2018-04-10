package com.kakao.mycustomviewexample;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.kakao.mycustomviewexample.model.Item;

import java.util.List;

/**
 * Created by khan.moon on 2018. 3. 21..
 */

public class DraggablePanel extends ViewPager implements GestureDetector.OnGestureListener {
    public static final String TAG = "DraggablePanel";
    private final int REVERT_ANIMATION_DURATION_IN_MILLIS = 1000;

    private float dX, dY;
    private int maxWidth, maxHeight;
    private int headerHeight;   // StatusBar + Toolbar
    private GestureDetectorCompat gestureDetector;
    private boolean isLocationReverted;
    private float revertX, revertY;

    private ItemPagerAdapter adapter;
    private List<Item> itemList;

    public DraggablePanel(Context context) {
        super(context, null);
    }

    public DraggablePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        adapter = new ItemPagerAdapter();
    }

    public void setLocationReverted(boolean locationReverted) {
        isLocationReverted = locationReverted;
    }

    private void revert() {
        animate().x(revertX)
                .y(revertY)
                .setDuration(REVERT_ANIMATION_DURATION_IN_MILLIS)
                .start();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View parent = (View) getParent();

        if (parent != null) {
            maxWidth = parent.getWidth();
            maxHeight = parent.getHeight();
            Log.d(TAG, "wi: " + maxWidth + ", hei: " + maxHeight);
        } else {
            Log.d(TAG, "here");
            maxWidth = getWidth();
            maxHeight = getHeight();
        }

        revertX = getX();
        revertY = getY();

        calculateHeaderHeight();
    }

    private void calculateHeaderHeight() {
        Window window = ((Activity) getContext()).getWindow();
        int contentViewHeight =
                window.findViewById(Window.ID_ANDROID_CONTENT).getHeight();
        int deviceHeight = getResources().getDisplayMetrics().heightPixels;

        headerHeight = deviceHeight - contentViewHeight;

        Log.d(TAG, "headerHeight: " + headerHeight);
    }

    public void render(List<Item> itemList) {
        this.itemList = itemList;
        adapter.setItemList(itemList);

        setAdapter(adapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isConsumed = false;

        if (isLocationReverted &&
                event.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "onUp");
            revert();
        }

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
        } else if (destX + getWidth() > maxWidth) {
            destX = maxWidth - getWidth();
        }
        if (destY < 0) {
            destY = 0;
        } else if (destY + getHeight() > maxHeight) {
            destY = maxHeight - getHeight();
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
