package com.kakao.mycustomviewexample;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

/**
 * Created by khan.moon on 2018. 3. 21..
 */

public class DraggablePanel extends RelativeLayout implements GestureDetector.OnGestureListener {
    public static final String TAG = "DraggablePanel";
    private final int REVERT_ANIMATION_DURATION_IN_MILLIS = 1000;
    private final int THRESHOLD = 30;
    private final int SWITCHER_THRESHOLD = 200;
    private final int MAX_OPACITY = 255;

    private GestureDetectorCompat gestureDetector;
    private OnSwitchListener listener;
    private View parent;
    private float dX, dY;
    private int maxWidth, maxHeight; // 가로, 세로(상태바, 툴바 높이 제외)
    private int headerHeight;   // 상태바 + 툴바
    private boolean isLocationReverted;
    private boolean isAvailableBottomSwitcher;
    private boolean isLocked;
    private boolean isOverBoundary;
    private boolean isSwitchOn;
    private float revertX, revertY;
    private float approachingX;
    private float maxX, maxY;
    private int switchOnBackgroundResId, switchOffBackgroundResId;
    private int opacity;


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
    }

    public void setLocationReverted(boolean locationReverted) {
        isLocationReverted = locationReverted;
    }

    public void setOnSwitchListener(OnSwitchListener listener) {
        this.listener = listener;
    }

    public int getSwitchOnBackgroundResId() {
        return switchOnBackgroundResId;
    }

    public void setSwitchOnBackgroundResId(int switchOnBackgroundResId) {
        this.switchOnBackgroundResId = switchOnBackgroundResId;
    }

    public int getSwitchOffBackgroundResId() {
        return switchOffBackgroundResId;
    }

    public void setSwitchOffBackgroundResId(int switchOffBackgroundResId) {
        this.switchOffBackgroundResId = switchOffBackgroundResId;
    }

    public boolean isSwitchOn() {
        return isSwitchOn;
    }

    private void revert() {
        startTranslation(revertX, revertY, REVERT_ANIMATION_DURATION_IN_MILLIS);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        parent = (View) getParent();

        if (parent == null) {
            parent = this;
        }

        maxWidth = parent.getWidth();
        maxHeight = parent.getHeight();
        Log.d(TAG, "wi: " + maxWidth + ", hei: " + maxHeight);

        revertX = getX();
        revertY = getY();

        maxX = maxWidth - getWidth();
        maxY = maxHeight - getHeight();

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isConsumed = false;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "onUp");
            if (isLocked) {
                Log.d(TAG, "isLocked");
                isLocked = false;
                if (isOverBoundary) {   // 스위칭
                    switching();
                    startTranslation(approachingX, maxHeight - getHeight(), 400);
                } else {
                    startTranslation(approachingX, maxHeight - getHeight(), 400, new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            opacity = (int) ((1 - animation.getAnimatedFraction()) * opacity);
                            applyOpacityToBackground();
                        }
                    });
                }
            }
            if (isLocationReverted) {
                revert();
            }
        }

        isConsumed |= gestureDetector.onTouchEvent(event);
        isConsumed |= super.onTouchEvent(event);

        return isConsumed;
    }

    private void switching() {
        if (listener != null) {
            isSwitchOn = !isSwitchOn;
            listener.onSwitch(this);
        }
        changeBackground();
    }

    private void changeBackground() {
        if (isSwitchOn) {
            parent.setBackgroundResource(switchOnBackgroundResId);
            parent.getBackground().setAlpha(MAX_OPACITY);
        } else {
            parent.setBackgroundResource(switchOffBackgroundResId);
            parent.getBackground().setAlpha(MAX_OPACITY);
        }
    }

    public void notifyChangedBackground() {
        if (parent != null) {
            changeBackground();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown");
        isAvailableBottomSwitcher = isApproachedToBottom(e);

        dX = e.getRawX() - getX();
        dY = e.getRawY() - getY();

        return true;
    }

    private boolean isApproachedToBottom(MotionEvent e) {
        int currentY = (int) (e.getRawY() - e.getY() + getHeight() - headerHeight);
        return (currentY + THRESHOLD >= maxHeight);
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

        if (isAvailableBottomSwitcher) {
            Log.d(TAG, "Switch on");
            if (!isLocked) {
                isLocked = true;
                approachingX = destX;
            }

            if (destY > maxY + SWITCHER_THRESHOLD) { // 스위치 동작 범위까지 내려갔는지.
                destY = maxY + SWITCHER_THRESHOLD;
                opacity = MAX_OPACITY * 2;
                isOverBoundary = true;
            } else if (destY < maxY) {   // 잠금 해제 범위까지 올라갔는지.
                isAvailableBottomSwitcher = false;
                isOverBoundary = false;
                isLocked = false;
                opacity = 0;
            } else {
                isOverBoundary = false;
                opacity = (int) (((destY - maxY) / SWITCHER_THRESHOLD) * MAX_OPACITY) * 2;
            }

            applyOpacityToBackground();

            startTranslation(approachingX, destY, 0);

            return true;
        }

        if (!isLocked) {
            if (destX < 0) {
                destX = 0;
            } else if (destX > maxX) {
                destX = maxX;
            }
            if (destY < 0) {
                destY = 0;
            } else if (destY > maxY) {
                destY = maxY;
            }

            startTranslation(destX, destY, 0);
        }

        return true;
    }

    private void applyOpacityToBackground() {
        if (isSwitchOn) {
            if (opacity <= MAX_OPACITY) {
                parent.setBackgroundResource(switchOnBackgroundResId);
                parent.getBackground().setAlpha(MAX_OPACITY - opacity);
            } else {
                parent.setBackgroundResource(switchOffBackgroundResId);
                parent.getBackground().setAlpha(opacity - MAX_OPACITY);
            }
        } else {
            if (opacity <= MAX_OPACITY) {
                parent.setBackgroundResource(switchOffBackgroundResId);
                parent.getBackground().setAlpha(MAX_OPACITY - opacity);
            } else {
                parent.setBackgroundResource(switchOnBackgroundResId);
                parent.getBackground().setAlpha(opacity - MAX_OPACITY);
            }
        }
    }

    private void startTranslation(float x, float y, int duration) {
        animate()
                .x(x)
                .y(y)
                .setDuration(duration)
                .setUpdateListener(null)
                .start();
    }

    private void startTranslation(float x, float y, int duration, ValueAnimator.AnimatorUpdateListener listener) {
        animate()
                .x(x)
                .y(y)
                .setDuration(duration)
                .setUpdateListener(listener)
                .start();
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
