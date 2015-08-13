package com.wordpress.smdaudhilbe.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by mohammed-2284 on 16/07/15.
 */
public class AudienceSpeedoMeter extends FrameLayout {

    private int radius, anotherRadius;
    private RimView rimView;
    private ArrowView arrowView;
    private FillView fillView;
    private ZeroArrowView zeroArrowView;
    private CentumArrowView centumArrowView;
    private FiftyArrowView fiftyArrowView;
    private LayoutParams layoutParams;
    private float count = 0, prevPercentage = -1, backWardPercentage = -1, forWardPercentage = -1;
    private boolean drawForward = true;
    private Paint arrowHeadPaint, rimPaint, fillPaint, insideArrowHeadPaint;
    private boolean isViewClicked = false, viewClickedNoNeedToMove = false;

    public AudienceSpeedoMeter(Context context) {
        super(context);
        init();
    }

    public AudienceSpeedoMeter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        rimView = new RimView(getContext());
        arrowView = new ArrowView(getContext());
        fillView = new FillView(getContext());
        zeroArrowView = new ZeroArrowView(getContext());
        centumArrowView = new CentumArrowView(getContext());
        fiftyArrowView = new FiftyArrowView(getContext());
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(fillView, layoutParams);
        this.addView(rimView, layoutParams);
        this.addView(arrowView, layoutParams);
        this.addView(zeroArrowView, layoutParams);
        this.addView(centumArrowView, layoutParams);
        this.addView(fiftyArrowView, layoutParams);
        fillView.setVisibility(INVISIBLE);
        arrowView.setVisibility(INVISIBLE);
        centumArrowView.setVisibility(INVISIBLE);
        fiftyArrowView.setVisibility(INVISIBLE);
    }

    //  View click performance
    public void performedClick() {
        if (!isViewClicked) {
            isViewClicked = true;
            arrowHeadPaint.setStyle(Paint.Style.STROKE);
            arrowHeadPaint.setStrokeWidth(8);
            insideArrowHeadPaint.setColor(getResources().getColor(android.R.color.white));
            fillView.setVisibility(VISIBLE);
        } else {
            isViewClicked = false;
            arrowHeadPaint.setStyle(Paint.Style.FILL);
            insideArrowHeadPaint.setColor(Color.parseColor("#00bbff"));
            fillView.setVisibility(INVISIBLE);
        }
        viewClickedNoNeedToMove = true;
        arrowView.invalidate();
        zeroArrowView.invalidate();
        centumArrowView.invalidate();
        fiftyArrowView.invalidate();
    }

    //  Arrow view
    public class ArrowView extends View {
        private Path arrowHeadPath, insideArrowPath;
        private Matrix arrowMatrix;
        private float xPoint, yPoint;
        Handler myHandler = new Handler();

        public ArrowView(Context context) {
            super(context);
            init();
        }

        public ArrowView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public void init() {
            arrowMatrix = new Matrix();
            arrowHeadPath = new Path();
            insideArrowPath = new Path();
            arrowHeadPaint = new Paint();
            arrowHeadPaint.setAntiAlias(true);
            arrowHeadPaint.setColor(Color.parseColor("#00bbff"));
            arrowHeadPaint.setStyle(Paint.Style.FILL);
            insideArrowHeadPaint = new Paint();
            insideArrowHeadPaint.setAntiAlias(true);
            insideArrowHeadPaint.setColor(Color.parseColor("#00bbff"));
            insideArrowHeadPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            xPoint = getWidth() / 2 - anotherRadius;
            yPoint = getHeight() / 2;
            arrowHeadPath.moveTo(xPoint, yPoint);
            arrowHeadPath.lineTo(xPoint, yPoint + 15);
            arrowHeadPath.lineTo(xPoint - 70, yPoint);
            arrowHeadPath.lineTo(xPoint, yPoint - 15);
            arrowHeadPath.close();
            insideArrowPath.moveTo(xPoint - 1, yPoint);
            insideArrowPath.lineTo(xPoint - 1, yPoint + 15 - 1);
            insideArrowPath.lineTo(xPoint - 70 - 1, yPoint - 1);
            insideArrowPath.lineTo(xPoint - 1, yPoint - 15 + 1);
            insideArrowPath.close();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!viewClickedNoNeedToMove) {
                if (drawForward) {
                    arrowMatrix.postRotate(1.0f, getWidth() / 2, getHeight() / 2);
                } else {
                    arrowMatrix.postRotate(-1.0f, getWidth() / 2, getHeight() / 2);
                }
            } else {
                viewClickedNoNeedToMove = false;
            }
            canvas.concat(arrowMatrix);
            canvas.drawPath(arrowHeadPath, arrowHeadPaint);
            canvas.drawPath(insideArrowPath, insideArrowHeadPaint);
        }

        //        invalidate view at regular intervals
        public void invalidateArrowViewForWard() {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    arrowView.setVisibility(VISIBLE);
                    centumArrowView.setVisibility(INVISIBLE);
                    zeroArrowView.setVisibility(INVISIBLE);
                    fiftyArrowView.setVisibility(INVISIBLE);
                    if (count < forWardPercentage) {
                        count++;
                        invalidate();
                        myHandler.postDelayed(this, 1);
                    } else {
                        count = 0;
                        if (prevPercentage == 180f || prevPercentage > 176f) {
                            arrowView.setVisibility(INVISIBLE);
                            centumArrowView.setVisibility(VISIBLE);
                        } else if (prevPercentage == 90f) {
                            arrowView.setVisibility(INVISIBLE);
                            fiftyArrowView.setVisibility(VISIBLE);
                        } else {
                            arrowView.setVisibility(VISIBLE);
                            centumArrowView.setVisibility(INVISIBLE);
                            fiftyArrowView.setVisibility(INVISIBLE);
                        }
                    }
                }
            }, 1);
        }

        public void invalidateArrowViewBackWard() {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    arrowView.setVisibility(VISIBLE);
                    centumArrowView.setVisibility(INVISIBLE);
                    zeroArrowView.setVisibility(INVISIBLE);
                    fiftyArrowView.setVisibility(INVISIBLE);
                    if (count < backWardPercentage) {
                        count++;
                        invalidate();
                        myHandler.postDelayed(this, 1);
                    } else {
                        count = 0;
                        if (prevPercentage == 0f) {
                            arrowView.setVisibility(INVISIBLE);
                            zeroArrowView.setVisibility(VISIBLE);
                        } else if (prevPercentage == 90f) {
                            arrowView.setVisibility(INVISIBLE);
                            fiftyArrowView.setVisibility(VISIBLE);
                        } else {
                            arrowView.setVisibility(VISIBLE);
                            zeroArrowView.setVisibility(INVISIBLE);
                            fiftyArrowView.setVisibility(INVISIBLE);
                        }
                    }
                }
            }, 1);
        }
    }

    //  view click
    public void invalidateArrowView(float presentPercentage) {
        if (prevPercentage < presentPercentage) {
            drawForward = true;
            forWardPercentage = presentPercentage - prevPercentage;
            prevPercentage = presentPercentage;
            arrowView.invalidateArrowViewForWard();
        } else if (prevPercentage > presentPercentage) {
            drawForward = false;
            backWardPercentage = prevPercentage - presentPercentage;
            prevPercentage = presentPercentage;
            arrowView.invalidateArrowViewBackWard();
        }
        System.out.println("percentageIs : " + prevPercentage);
    }

    //  background rim view
    public class RimView extends View {

        private RectF anotherBox, box;
        private Bitmap bitmap;
        private Canvas bitmapCanvas;

        public RimView(Context context) {
            super(context);
            init();
        }

        public RimView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            box = new RectF();
            anotherBox = new RectF();
            rimPaint = new Paint();
            rimPaint.setAntiAlias(true);
            rimPaint.setColor(Color.parseColor("#00bbff"));
            rimPaint.setStyle(Paint.Style.STROKE);
            rimPaint.setStrokeWidth(5);
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
            anotherRadius = radius - 25;
            box.set(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius, getHeight() / 2 + radius);
            anotherBox.set(getWidth() / 2 - anotherRadius, getHeight() / 2 - anotherRadius, getWidth() / 2 + anotherRadius, getHeight() / 2 + anotherRadius);
            float sweep = 180;
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
            bitmapCanvas.drawArc(box, 180, sweep, false, rimPaint);
            bitmapCanvas.drawArc(anotherBox, 180, sweep, false, rimPaint);
            bitmapCanvas.drawLine(getWidth() / 2 - radius, getHeight() / 2, getWidth() / 2 - anotherRadius, getHeight() / 2, rimPaint);
            bitmapCanvas.drawLine(getWidth() / 2 + radius, getHeight() / 2, getWidth() / 2 + anotherRadius, getHeight() / 2, rimPaint);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(bitmap, 0, 0, rimPaint);
        }
    }

    //  FillView
    public class FillView extends View {
        private RectF fillBox;
        private int radiusOfFillView;
        private float sweepAngle;

        public FillView(Context context) {
            super(context);
            initView();
        }

        public FillView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        private void initView() {
            fillBox = new RectF();
            fillPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
            fillPaint.setAntiAlias(true);
            fillPaint.setColor(Color.parseColor("#00bbff"));
            fillPaint.setStyle(Paint.Style.STROKE);
            fillPaint.setStrokeCap(Paint.Cap.SQUARE);
            fillPaint.setStrokeWidth(25);
            sweepAngle = 180;
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            int width = getWidth();
            int height = getHeight();
            if (width > height) {
                radiusOfFillView = height / 4 - 12;
            } else {
                radiusOfFillView = width / 4 - 12;
            }
            fillBox.set(getWidth() / 2 - radiusOfFillView, getHeight() / 2 - radiusOfFillView, getWidth() / 2 + radiusOfFillView, getHeight() / 2 + radiusOfFillView);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawArc(fillBox, 180, sweepAngle, false, fillPaint);
        }
    }

    //  place holder views
    public class ZeroArrowView extends View {

        private Path zeroArrowHeadPath, insideZeroArrowPath;
        private float xPoint, yPoint;

        public ZeroArrowView(Context context) {
            super(context);
            init();
        }

        public ZeroArrowView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public void init() {
            zeroArrowHeadPath = new Path();
            insideZeroArrowPath = new Path();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            xPoint = getWidth() / 2 - anotherRadius;
            yPoint = getHeight() / 2;
            zeroArrowHeadPath.moveTo(xPoint, yPoint);
            zeroArrowHeadPath.lineTo(xPoint, yPoint + 15);
            zeroArrowHeadPath.lineTo(xPoint - 70, yPoint);
            zeroArrowHeadPath.lineTo(xPoint, yPoint - 15);
            zeroArrowHeadPath.close();
            insideZeroArrowPath.moveTo(xPoint - 1, yPoint);
            insideZeroArrowPath.lineTo(xPoint - 1, yPoint + 15 - 1);
            insideZeroArrowPath.lineTo(xPoint - 70 - 1, yPoint - 1);
            insideZeroArrowPath.lineTo(xPoint, yPoint - 15 - 1);
            insideZeroArrowPath.close();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawPath(zeroArrowHeadPath, arrowHeadPaint);
            canvas.drawPath(insideZeroArrowPath, insideArrowHeadPaint);
        }
    }

    public class CentumArrowView extends View {

        private Path centumArrowHeadPath, insideCentumArrowPath;
        private float xPoint, yPoint;

        public CentumArrowView(Context context) {
            super(context);
            init();
        }

        public CentumArrowView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public void init() {
            centumArrowHeadPath = new Path();
            insideCentumArrowPath = new Path();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            xPoint = getWidth() / 2 + anotherRadius;
            yPoint = getHeight() / 2;
            centumArrowHeadPath.moveTo(xPoint, yPoint);
            centumArrowHeadPath.lineTo(xPoint, yPoint + 15);
            centumArrowHeadPath.lineTo(xPoint + 70, yPoint);
            centumArrowHeadPath.lineTo(xPoint, yPoint - 15);
            centumArrowHeadPath.close();
            insideCentumArrowPath.moveTo(xPoint - 1, yPoint);
            insideCentumArrowPath.lineTo(xPoint - 1, yPoint + 15 - 1);
            insideCentumArrowPath.lineTo(xPoint + 70 - 1, yPoint - 1);
            insideCentumArrowPath.lineTo(xPoint, yPoint - 15 - 1);
            insideCentumArrowPath.close();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawPath(centumArrowHeadPath, arrowHeadPaint);
            canvas.drawPath(insideCentumArrowPath, insideArrowHeadPaint);
        }
    }

    public class FiftyArrowView extends View {

        private Path fiftyArrowHeadPath, insideFiftyArrowPath;
        private float xPoint, yPoint;

        public FiftyArrowView(Context context) {
            super(context);
            init();
        }

        public FiftyArrowView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public void init() {
            fiftyArrowHeadPath = new Path();
            insideFiftyArrowPath = new Path();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            xPoint = getWidth() / 2;
            yPoint = getHeight() / 2 - anotherRadius;
            fiftyArrowHeadPath.moveTo(xPoint, yPoint);
            fiftyArrowHeadPath.lineTo(xPoint - 15, yPoint);
            fiftyArrowHeadPath.lineTo(xPoint, yPoint - 70);
            fiftyArrowHeadPath.lineTo(xPoint + 15, yPoint);
            fiftyArrowHeadPath.close();
            insideFiftyArrowPath.moveTo(xPoint, yPoint - 1);
            insideFiftyArrowPath.lineTo(xPoint - 15 - 1, yPoint);
            insideFiftyArrowPath.lineTo(xPoint, yPoint - 70 - 1);
            insideFiftyArrowPath.lineTo(xPoint + 15 - 1, yPoint);
            insideFiftyArrowPath.close();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawPath(fiftyArrowHeadPath, arrowHeadPaint);
            canvas.drawPath(insideFiftyArrowPath, insideArrowHeadPaint);
        }
    }
}