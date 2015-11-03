package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by santosh on 9/5/15.
 */
public class MyView extends View {
    final String LOG_TAG = MyView.class.getSimpleName();
    Paint mPaint;
    RectF rect;
    private float degrees;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rect = new RectF(20, 20, 100, 100);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mPaint.setShader(new LinearGradient(0.40f, 0.0f, 100.60f, 100.0f,
                Color.RED,
                Color.RED,
                Shader.TileMode.CLAMP));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int myHeight = hSpecSize;

        if (hSpecMode == MeasureSpec.EXACTLY)
            myHeight = hSpecSize;
        else if (hSpecMode == MeasureSpec.AT_MOST)
            myHeight = Math.min(100, hSpecSize);

        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myWidth = wSpecSize;

        if (wSpecMode == MeasureSpec.EXACTLY)
            myWidth = wSpecSize;
        else if (wSpecMode == MeasureSpec.AT_MOST || wSpecMode == MeasureSpec.UNSPECIFIED)
            myWidth = Math.min(100, wSpecSize);

        Log.v(LOG_TAG, "Custom view called in sunshine with Width: " + myWidth + " Height: " + myHeight);
        setMeasuredDimension(myWidth, myHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //  canvas.drawCircle(20,20,20, mPaint);
        canvas.drawLine(100, 30, 140, 160, mPaint);
        canvas.drawLine(60, 160, 100, 30, mPaint);
        canvas.drawLine(60, 160, 140, 160, mPaint);
        Log.v(LOG_TAG, "Set direction called. " + degrees);

    }

    public void setDirection(float direction) {
        degrees = direction;
    }
}
