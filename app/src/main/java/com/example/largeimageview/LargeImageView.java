package com.example.largeimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jarvan on 2020/9/4.
 */
public class LargeImageView extends View implements MoveGestureDetector.OnMoveGestureListener {


    int mImageWidth;
    int mImageHeight;

    private Context mContext;
    private volatile Rect mRect = new Rect();
    private BitmapRegionDecoder mRegionDecoder;
    private MoveGestureDetector gestureDetector;
    private BitmapFactory.Options options = new BitmapFactory.Options();

    public LargeImageView(Context context) {
        this(context, null, 0);
    }

    public LargeImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LargeImageView(Context context, AttributeSet attributeSet, int defAttr) {
        super(context, attributeSet, defAttr);
        mContext = context;
        init();
    }

    public void init() {
        gestureDetector = new MoveGestureDetector(mContext);
        gestureDetector.setOnMoveGestureListener(this);
    }

    public void setImageResource(InputStream inputStream) {
        try {
            mRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            mImageWidth = options.outWidth;
            mImageHeight = options.outHeight;
            requestLayout();
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onMoveStart(MoveGestureDetector detector) {
        return true;
    }

    @Override
    public boolean onMoving(MoveGestureDetector detector) {
        int moveX = (int) detector.getMoveX();
        int moveY = (int) detector.getMoveY();
        if (mImageWidth > getWidth()) {
            mRect.offset(-moveX, 0);
            checkWidth();
            invalidate();
        }
        if (mImageHeight > getHeight()) {
            mRect.offset(0, -moveY);
            checkHeight();
            invalidate();
        }

        return true;
    }

    @Override
    public void onMoveEnd() {

    }

    private void checkWidth() {
        if (mRect.right > mImageWidth) {
            mRect.right = mImageWidth;
            mRect.left = mImageWidth - getWidth();
        }
        if (mRect.left < 0) {
            mRect.left = 0;
            mRect.right = getWidth();
        }
    }

    private void checkHeight() {
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight;
            mRect.top = mImageHeight - getHeight();
        }
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = getHeight();
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.performClick();
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRegionDecoder != null) {
            Bitmap bitmap = mRegionDecoder.decodeRegion(mRect, options);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //绘制完成先以图片中心显示
        mRect.left = mImageWidth / 2 - getMeasuredWidth() / 2;
        mRect.top = mImageHeight / 2 - getMinimumHeight() / 2;
        mRect.right = mRect.left + getMinimumWidth();
        mRect.bottom = mRect.top + getMinimumHeight();
    }
}
