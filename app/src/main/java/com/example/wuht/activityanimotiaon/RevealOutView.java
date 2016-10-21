package com.example.wuht.activityanimotiaon;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;

import static com.example.wuht.activityanimotiaon.RevealView.REVEAL_TYPE_CIRCLE;
import static com.example.wuht.activityanimotiaon.RevealView.REVEAL_TYPE_HEPTAGON;
import static com.example.wuht.activityanimotiaon.RevealView.REVEAL_TYPE_PENTAGRAM;
import static com.example.wuht.activityanimotiaon.RevealView.REVEAL_TYPE_RECT;

/**
 * Created by wuht on 2016/10/21.
 */

public class RevealOutView extends View {
    private final ValueAnimator valueAnimator;
    Paint mPaint;
    Rect mRect;
    private int mWidth, mHeight;
    private int mType;
    private float alpha, angle, x, y;
    private float mCx, mCy, mRadius;
    private Context mContext;
    private Path mTmpPath;

    public RevealOutView(Context context, Rect mRect, Paint mPaint, int type) {
        super(context);
        mContext = context;
        attach2Activity((Activity) context);
        this.mPaint = mPaint;
        mTmpPath = new Path();
        this.mRect = mRect;
        this.mType = type;
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mType) {
            case REVEAL_TYPE_RECT:
                int m = mRect.left + mRect.width() / 2;
                int n = mRect.top + mRect.height() / 2;
                canvas.drawRect(-100 + (m + 100) * alpha, -100 + (n + 100) * alpha,
                        (mWidth + 100) + (-mWidth - 100 + m) * alpha, (mHeight + 100) + (-mHeight - 100 + n) * alpha, mPaint);
                //canvas.drawRect(mRect,mPaint);
                break;
            case REVEAL_TYPE_CIRCLE:
                canvas.drawCircle(mCx, mCy, (20 + Math.max(mWidth, mHeight)) * (-alpha + 1), mPaint);
                break;
            case REVEAL_TYPE_HEPTAGON:
                canvas.save();
                canvas.translate(mCx, mCy);
                mTmpPath.reset();
                angle = (float) (Math.PI * 2 / 7);
                mRadius = (int) (Math.max(mWidth, mHeight) + 20) * (-alpha + 1);
                for (int i = 1; i <= 7; i++) {
                    x = (float) (Math.cos(i * angle) * mRadius);
                    y = (float) (Math.sin(i * angle) * mRadius);
                    if (i == 1) {
                        mTmpPath.moveTo(x, y);
                    } else {
                        mTmpPath.lineTo(x, y);
                    }
                }
                mTmpPath.close();
                canvas.drawPath(mTmpPath, mPaint);
                canvas.restore();
                break;
            case REVEAL_TYPE_PENTAGRAM:
                canvas.drawRect(-100 + (mRect.left + 100) * alpha, -100 + (mRect.top + 100) * alpha,
                        (mWidth + 100) + (-mWidth - 100 + mRect.right) * alpha, (mHeight + 100) + (-mHeight - 100 + mRect.bottom) * alpha, mPaint);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

/*        //获取状态栏，actionBar的高度
        TypedValue tv = new TypedValue();
        int actionBarHeight=0;
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
        }
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        if (mRect != null) {
            mRect = new Rect(mRect.left, mRect.top - actionBarHeight - statusBarHeight, mRect.right,
                    mRect.bottom - actionBarHeight - statusBarHeight);
            mCx = mRect.left + mRect.width() / 2;
            mCy = mRect.top + mRect.height() / 2;
        }*/
        mCx = mRect.left + mRect.width() / 2;
        mCy = mRect.top + mRect.height() / 2;
        valueAnimator.start();
    }

    private void attach2Activity(Activity activity) {
        final ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        rootView.getChildAt(0).setVisibility(INVISIBLE);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                rootView.getChildAt(0).setVisibility(VISIBLE);
            }
        }, 400);
        rootView.addView(this, lp);
    }
}
