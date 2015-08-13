package com.wordpress.smdaudhilbe.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mohammed-2284 on 15/07/15.
 */
public class SemiCircleView extends View {

    private final Paint mPaint;
    private final RectF box, anotherBox;
    private int percent = 100;
    private int anotherRadius = 0, radius = 0;
    private Canvas canvas;
    private Bitmap bitmap;

    public SemiCircleView(Context context) {
        super(context);
        mPaint = new Paint();
        box = new RectF();
        anotherBox = new RectF();
    }

    public SemiCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        box = new RectF();
        anotherBox = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = getWidth();
        int height = getHeight();
        if (width > height) {
            radius = height / 4;
        } else {
            radius = width / 4;
        }
        anotherRadius = radius - 15;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#00bbff"));
        mPaint.setStrokeWidth(5);
        box.set(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius, getHeight() / 2 + radius);
        anotherBox.set(getWidth() / 2 - anotherRadius, getHeight() / 2 - anotherRadius, getWidth() / 2 + anotherRadius, getHeight() / 2 + anotherRadius);


        float sweep = 180;
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawArc(box, 180, sweep, false, mPaint);
        canvas.drawArc(anotherBox, 180, sweep, false, mPaint);
        canvas.drawLine(getWidth() / 2 - radius, getHeight() / 2, getWidth() / 2 - anotherRadius, getHeight() / 2, mPaint);
        canvas.drawLine(getWidth() / 2 + radius, getHeight() / 2, getWidth() / 2 + anotherRadius, getHeight() / 2, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, mPaint);

//        working fine
//        float sweep = 180 * percent * 0.01f;
//        canvas.drawArc(box, 180, sweep, false, mPaint);
//        canvas.drawArc(anotherBox, 180, sweep, false, mPaint);
//        canvas.drawLine(getWidth() / 2 - radius, getHeight() / 2, getWidth() / 2 - anotherRadius, getHeight() / 2, mPaint);
//        if (percent == 100) {
//            canvas.drawLine(getWidth() / 2 + radius, getHeight() / 2, getWidth() / 2 + anotherRadius, getHeight() / 2, mPaint);
//        }
    }

    public void setPercent(int percentage) {
        this.percent = percentage;
    }
}