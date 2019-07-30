package com.wonbin.autoclick;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * Created by wilburnLee on 2019/4/22.
 */
public class AutoService extends AccessibilityService {

    public static final String ACTION = "action";
    public static final String SHOW = "show";
    public static final String HIDE = "hide";
    public static final String PLAY = "play";
    public static final String STOP = "stop";

    public static final String MODE = "mode";
    public static final String TAP = "tap";
    public static final String SWIPE = "swipe";
    private FloatingView mFloatingView;
    private int mInterval;
    private int mX;
    private int mY;
    private String mMode;

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = new FloatingView(this);
        HandlerThread handlerThread = new HandlerThread("auto-handler");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            if (SHOW.equals(action)) {
                mInterval = intent.getIntExtra("interval", 16) * 1000;
                mMode = intent.getStringExtra(MODE);
                mFloatingView.show();
            } else if (HIDE.equals(action)) {
                mFloatingView.hide();
                mHandler.removeCallbacksAndMessages(null);
            } else if (PLAY.equals(action)) {
                mX = intent.getIntExtra("x", 0);
                mY = intent.getIntExtra("y", 0);
                if (mRunnable == null) {
                    mRunnable = new IntervalRunnable();
                }
                mHandler.postDelayed(mRunnable, mInterval);
                Toast.makeText(getBaseContext(), "已开始", Toast.LENGTH_SHORT).show();
            } else if (STOP.equals(action)) {
                mHandler.removeCallbacksAndMessages(null);
                Toast.makeText(getBaseContext(), "已暂停", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void playTap(int x, int y) {
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 10L, 10L));
        GestureDescription gestureDescription = builder.build();
        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                mHandler.postDelayed(mRunnable, mInterval);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    private void playSwipe(int fromX, int fromY, int toX, int toY) {
        Path path = new Path();
        path.moveTo(fromX, fromY);
        path.lineTo(toX, toY);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 100L, 1000L));
        GestureDescription gestureDescription = builder.build();
        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                mHandler.postDelayed(mRunnable, mInterval);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }

    private IntervalRunnable mRunnable;

    private class IntervalRunnable implements Runnable {
        @Override
        public void run() {
            if (SWIPE.equals(mMode)) {
                playSwipe(mX, mY, mX, mY - 300);
            } else {
                playTap(mX, mY);
            }
        }
    }
}
