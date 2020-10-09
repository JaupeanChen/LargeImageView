package com.example.largeimageview;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by Jarvan on 2020/9/7.
 */
public abstract class BaseGestureDetector {

    protected Context mContext;
    protected boolean isGesturing;
    protected MotionEvent mPreMotionEvent;
    protected MotionEvent mCurMotionEvent;

    public BaseGestureDetector(Context context) {
        this.mContext = context;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isGesturing) {
            handleTouchEventStart(event);
        } else {
            handleTouchEventGesturing(event);
        }
        return true;
    }

    protected abstract void handleTouchEventGesturing(MotionEvent event);

    protected abstract void handleTouchEventStart(MotionEvent event);

    protected abstract void updateStateByEvent(MotionEvent event);

    protected void resetState() {
        if (mPreMotionEvent != null) {
            mPreMotionEvent.recycle();
            mPreMotionEvent = null;
        }
        if (mCurMotionEvent != null) {
            mCurMotionEvent.recycle();
            mCurMotionEvent = null;
        }
        isGesturing = false;
    }
}
