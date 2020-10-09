package com.example.largeimageview;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by Jarvan on 2020/9/7.
 */
public class MoveGestureDetector extends BaseGestureDetector {

    private PointF mCurPoint;
    private PointF mPrePoint;
    //保存最终结果，其实是手势移动的距离
    private PointF mFinalPoint = new PointF();

    public MoveGestureDetector(Context context) {
        super(context);
    }

    private OnMoveGestureListener onMoveGestureListener;

    public void setOnMoveGestureListener(OnMoveGestureListener onMoveGestureListener) {
        this.onMoveGestureListener = onMoveGestureListener;
    }

    public interface OnMoveGestureListener {
        boolean onMoveStart(MoveGestureDetector detector);

        boolean onMoving(MoveGestureDetector detector);

        void onMoveEnd();
    }


    @Override
    protected void handleTouchEventGesturing(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                onMoveGestureListener.onMoveEnd();
                resetState();
                break;
            case MotionEvent.ACTION_MOVE:
                updateStateByEvent(event);
                boolean moving = onMoveGestureListener.onMoving(this);
                if (moving) {
                    if (mPreMotionEvent != null) {
                        mPreMotionEvent.recycle();
                        mPreMotionEvent = MotionEvent.obtain(event);
                    }
                }
                break;
        }
    }

    @Override
    protected void handleTouchEventStart(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                resetState();
                mPreMotionEvent = MotionEvent.obtain(event);
//                onMoveGestureListener.onMoveStart(event);
                updateStateByEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //开始移动了才算开始，移动之后就在handleTouchEventGesturing()中处理
                isGesturing = onMoveGestureListener.onMoveStart(this);
                break;
        }

    }

    @Override
    protected void updateStateByEvent(MotionEvent event) {
        PointF pre = calculateCentrePoint(mPreMotionEvent);
        PointF cur = calculateCentrePoint(event);
        boolean isSkipThisMoveEvent = event.getPointerCount() != mPreMotionEvent.getPointerCount();
        mFinalPoint.x = isSkipThisMoveEvent ? 0 : cur.x - pre.x;
        mFinalPoint.y = isSkipThisMoveEvent ? 0 : cur.y - pre.y;
    }

    /**
     * 根据MotionEvent计算多指（如果是）中点
     *
     * @param event
     * @return
     */
    public PointF calculateCentrePoint(MotionEvent event) {
        int count = event.getPointerCount();
        float x = 0;
        float y = 0;
        for (int i = 0; i < count; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        return new PointF(x / count, y / count);
    }

    public float getMoveX() {
        return mFinalPoint.x;
    }

    public float getMoveY() {
        return mFinalPoint.y;
    }

}
