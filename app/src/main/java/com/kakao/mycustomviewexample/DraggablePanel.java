package com.kakao.mycustomviewexample;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.kakao.mycustomviewexample.model.Item;

import java.util.List;

/**
 * Created by khan.moon on 2018. 3. 21..
 */

public class DraggablePanel extends ViewPager implements GestureDetector.OnGestureListener {
    public static final String TAG = "DraggablePanel";
    private final int REVERT_ANIMATION_DURATION_IN_MILLIS = 500;
    private final int SWIPE_MIN_DISTANCE = 50;

    private float dX, dY;
    private int maxWidth, maxHeight;
    private GestureDetectorCompat gestureDetector;
    private boolean isLocationReverted;
    private float revertX, revertY;
    private DragState currentState;
    private boolean isCalculated;
    private boolean isApproachingBottom;

    private ItemPagerAdapter adapter;
    private List<Item> itemList;

    public enum DragState {
        VERIFING,
        VIEW_PAGER,
        PANEL
    }

    public DraggablePanel(Context context) {
        super(context, null);
    }

    public DraggablePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        isCalculated = false;
        isApproachingBottom = false;

        gestureDetector = new GestureDetectorCompat(getContext(), this);
        adapter = new ItemPagerAdapter();
        currentState = DragState.VERIFING;

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.d(TAG, "Current State: " + state);
//                currentState = state;
            }
        });
    }

    public void setLocationReverted(boolean locationReverted) {
        isLocationReverted = locationReverted;
    }

    private void revert() {
        animate().y(revertY)
                .setDuration(REVERT_ANIMATION_DURATION_IN_MILLIS)
                .start();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout");

        if (!isCalculated) {
            View parent = (View) getParent();

            if (parent != null) {
                maxWidth = parent.getWidth();
                maxHeight = parent.getHeight();
                Log.d(TAG, "hei: " + maxHeight);
            } else {
                Log.d(TAG, "here");
                maxWidth = getWidth();
                maxHeight = getHeight();
            }

            revertX = getX();
            revertY = getY();

            isCalculated = true;
        }
    }

//    private void calculateHeaderHeight() {
//        Window window = ((Activity) getContext()).getWindow();
//        int contentViewHeight =
//                window.findViewById(Window.ID_ANDROID_CONTENT).getHeight();
//        int deviceHeight = getResources().getDisplayMetrics().heightPixels;
//
//        headerHeight = deviceHeight - contentViewHeight;
//
//        Log.d(TAG, "headerHeight: " + headerHeight);
//    }

    public void render(List<Item> itemList) {
        this.itemList = itemList;
        adapter.setItemList(itemList);

        setAdapter(adapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isConsumed = false;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "onUp");

            if (isLocationReverted) {
                revert();
            }

            if (isApproachingBottom) {
                isApproachingBottom = false;

                changeSomething();
            }

            return gestureDetector.onTouchEvent(event);
        }

        if (currentState == DragState.VERIFING) {
            Log.d(TAG, "검증 중");
            isConsumed |= gestureDetector.onTouchEvent(event);
        } else if (currentState == DragState.PANEL) {
            Log.d(TAG, "드래깅 중");
            isConsumed |= gestureDetector.onTouchEvent(event);
        } else if (currentState == DragState.VIEW_PAGER) {
            Log.d(TAG, "뷰페이저가 처리 중");
            isConsumed |= super.onTouchEvent(event);
        }

        return isConsumed;
    }

    private void changeSomething() {
        Log.d(TAG, "changeSomething");
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown");
        dX = e.getRawX() - getX();
        dY = e.getRawY() - getY();

        currentState = DragState.VERIFING;

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
        float destX, destY;
        destX = e2.getRawX() - dX;
        destY = e2.getRawY() - dY;

        Log.d(TAG, "destX: " + destX + ", revertX: " + revertX);

        if ((destX - revertX > SWIPE_MIN_DISTANCE || revertX - destX > SWIPE_MIN_DISTANCE)
                && currentState != DragState.PANEL) {
            if (currentState != DragState.VIEW_PAGER) {
                Log.d(TAG, "X 검증 완료");
                currentState = DragState.VIEW_PAGER;
            }
        }

        if (destY - revertY > SWIPE_MIN_DISTANCE
                && currentState != DragState.VIEW_PAGER) {
            if (currentState != DragState.PANEL) {
                Log.d(TAG, "Y 검증 완료");
                currentState = DragState.PANEL;
            }
        }

        if (currentState == DragState.PANEL) {
            isApproachingBottom = false;

            destY -= SWIPE_MIN_DISTANCE;

            if (destY < revertY) {
                destY = revertY;
            } else if (destY + getHeight() > maxHeight) {
                destY = maxHeight - getHeight();
                isApproachingBottom = true;
            }

            animate()
                    .y(destY)
                    .setDuration(0)
                    .start();
        }

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
