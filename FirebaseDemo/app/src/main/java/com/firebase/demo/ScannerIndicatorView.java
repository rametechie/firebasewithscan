package com.firebase.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ScannerIndicatorView extends View {
    private static final float PATH_INSET_DP = 20f;
    private static final float LINE_WIDTH_DP = 4f;
    private static final float LINE_SPACE_DP = 1f;

    private final Paint mCutoutPaint;
    private final Paint mLinePaint;
    private Path mTopPath;
    private Path mBottomPath;
    private float mPathBorderInset;
    private float mPathWidth;
    private float mPathSpace;
    private RectF mCutoutRect;
    private int mMaxWidth;
    private int mMaxHeight;

    public ScannerIndicatorView(Context context) {
        this(context, null, 0);
    }

    public ScannerIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScannerIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCutoutPaint = new Paint();
        mCutoutPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCutoutRect = new RectF();
        mPathWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LINE_WIDTH_DP,
                getResources().getDisplayMetrics());
        mPathSpace = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LINE_SPACE_DP,
                getResources().getDisplayMetrics());
        mPathBorderInset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PATH_INSET_DP,
                getResources().getDisplayMetrics());

        mLinePaint = new Paint();
        mLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mLinePaint.setStrokeWidth(mPathWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mTopPath = new Path();
        mBottomPath = new Path();

        readAttrs(attrs);
    }

    private void readAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext()
                    .obtainStyledAttributes(attrs, R.styleable.ScannerIndicatorView, 0, 0);
            try {
                mMaxWidth = (int) array.getDimension(R.styleable.ScannerIndicatorView_maxWidth, Integer.MAX_VALUE);
                mMaxHeight = (int) array.getDimension(R.styleable.ScannerIndicatorView_maxHeight, Integer.MAX_VALUE);
            } finally {
                array.recycle();
            }
        } else {
            mMaxWidth = Integer.MAX_VALUE;
            mMaxHeight = Integer.MAX_VALUE;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            calculatePathsAndRects();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        if (width > mMaxWidth) {
            width = mMaxWidth;
        }
        if (height > mMaxHeight) {
            height = mMaxHeight;
        }
        setMeasuredDimension(width, height);
    }

    private void calculatePathsAndRects() {
        mBottomPath.reset();
        mTopPath.reset();

        mTopPath.moveTo(0, mPathBorderInset);
        mTopPath.lineTo(0, 0);
        mTopPath.lineTo(getWidth(), 0);
        mTopPath.lineTo(getWidth(), mPathBorderInset);

        mBottomPath.moveTo(0, getHeight() - mPathBorderInset);
        mBottomPath.lineTo(0, getHeight());
        mBottomPath.lineTo(getWidth(), getHeight());
        mBottomPath.lineTo(getWidth(), getHeight() - mPathBorderInset);
        float inset = mPathSpace + mPathWidth;
        mCutoutRect.set(inset, inset, getWidth() - inset, getHeight() - inset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(mCutoutRect, mCutoutPaint);
        canvas.drawPath(mTopPath, mLinePaint);
        canvas.drawPath(mBottomPath, mLinePaint);
    }
}
