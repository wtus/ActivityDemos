package com.example.wuht.activityanimotiaon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;

import static com.example.wuht.activityanimotiaon.BaseActivity.activityStack;

/**
 * Created by wuht on 2016/10/20.
 */

public class RevealView extends View {

    private static final int STATE_NOT_STARTED = 0;
    private static final int STATE_FILL_STARTED = 1;
    private static final int STATE_FINISHED = 2;
    private int mState = STATE_NOT_STARTED;
    public final static int REVEAL_TYPE_CIRCLE = 1;
    public final static int REVEAL_TYPE_RECT = 2;
    public final static int REVEAL_TYPE_HEPTAGON = 3;
    public final static int REVEAL_TYPE_PENTAGRAM = 4;
    private int mType = REVEAL_TYPE_RECT;

    protected Paint mPaint;
    private ValueAnimator mRevealAnimator;
    private int actionBarHeight;
    private int mCx, mCy, mWidth, mHeight, mRadius;
    private float alpha, angle, x, y;
    private Rect mRect;
    private Context mContext;
    private int statusBarHeight;
    private ViewGroup mRootView;
    private RectF rectF;
    private Path mTmpPath;
    private ArrayList<PointF> mPoints;


    public RevealView(Context context) {
        this(context, null);
    }

    public RevealView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        attach2Activity((Activity) getContext(), this);

    }


    public RevealView(Context context, Rect rect, @RevealType int type, int revealColor) {
        super(context);
        mContext = context;
        mType = type;
        attach2Activity((Activity) getContext(), this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(revealColor);
        mRect = new Rect();
        mRect = rect;
        rectF = new RectF();
        mTmpPath = new Path();
        mPoints = new ArrayList<>();
        //attach2Activity(getTopActivity());
    }

    public RevealView(Context context, View anchorView, Intent intent) {
        super(context);
        mContext = context;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        mRect = new Rect();
        anchorView.getGlobalVisibleRect(mRect);
        context.startActivity(intent);
        //attach2Activity((Activity) getContext());
        //attach2Activity(getTopActivity(Main3Activity.class));

    }

    private void changeState(int started) {
        mState = started;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        //获取状态栏，actionBar的高度
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
        }
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;

        if (mRect != null) {
            mRect = new Rect(mRect.left, mRect.top - actionBarHeight - statusBarHeight, mRect.right,
                    mRect.bottom - actionBarHeight - statusBarHeight);
            mCx = mRect.left + mRect.width() / 2;
            mCy = mRect.top + mRect.height() / 2;
        }

        mRevealAnimator = ValueAnimator.ofFloat(0, 1);
        mRevealAnimator.setDuration(600);
        mRevealAnimator.setInterpolator(new AccelerateInterpolator());
        mRevealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FINISHED);
                mRootView.getChildAt(0).setVisibility(VISIBLE);
            }
        });
        mRevealAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (float) animation.getAnimatedValue();
                mRadius = (int) ((float) animation.getAnimatedValue() * (mWidth + mHeight));
                invalidate();
            }
        });
        mRevealAnimator.start();
        changeState(STATE_FILL_STARTED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mState == STATE_FILL_STARTED) {
            switch (mType) {
                case REVEAL_TYPE_CIRCLE:
                    canvas.drawCircle(mCx, mCy, mRadius, mPaint);
                   /* rectF.set(mCx + (-alpha) * (mCx + mHeight), mCy + (-alpha) * (mCy + mHeight)
                            , mCx + (getWidth() - mCx + mHeight) * alpha, mCy + (getHeight() - mCy + mHeight) * alpha);*/
    /*                int max=Math.min(mHeight,mWidth)+100;
                    rectF.set(mCx - max*alpha, mCy - max*alpha, mCx + max*alpha, mCy + max*alpha);
                    canvas.drawArc(rectF, 0, 359.9f * alpha, true, mPaint);*/
               /*
                    //失败，阿基米德螺旋线，不太对，以后搞
                    int r= (int) (10*(1+alpha));
                    int x= (int) (r*Math.cos(alpha * 360));
                    int y= (int) (r*Math.sin(alpha * 360));
                    mPaint.setStrokeWidth(20);
                    canvas.drawPoint(x,y,mPaint);*/
                    break;
                case REVEAL_TYPE_RECT:
//                    canvas.drawRect(mRect.left + (-alpha) * (mRect.left + (mWidth+mHeight)),
//                            mRect.top + (-alpha) * (mRect.top + (mWidth+mHeight))
//                            , mRect.right + (getWidth() - mRect.right + (mWidth+mHeight)) * alpha,
//                            mRect.bottom + (getHeight() - mRect.bottom + (mWidth+mHeight)) * alpha, mPaint);
                    canvas.drawRect(mCx + (-alpha) * (mCx + (mWidth + mHeight)), mCy + (-alpha) * (mCy + (mWidth + mHeight))
                            , mCx + (getWidth() - mCx + (mWidth + mHeight)) * alpha, mCy + (getHeight() - mCy + (mWidth + mHeight)) * alpha, mPaint);
                    break;
                case REVEAL_TYPE_PENTAGRAM:
                    canvas.drawRect(mRect.left + (-alpha) * (mRect.left + (mWidth + mHeight)),
                            mRect.top + (-alpha) * (mRect.top + (mWidth + mHeight))
                            , mRect.right + (getWidth() - mRect.right + (mWidth + mHeight)) * alpha,
                            mRect.bottom + (getHeight() - mRect.bottom + (mWidth + mHeight)) * alpha, mPaint);
                    //mPaint.setColor(Color.BLACK);
/*
   ❀月亮
   canvas.drawRGB(0, 0, 0);
                    mPaint.setColor(Color.YELLOW);
                    canvas.drawCircle(mCx, mCy, (mWidth + mHeight) * alpha, mPaint);
                    mPaint.setColor(Color.BLACK);
                    canvas.drawCircle(mCx - (mWidth + mHeight) * alpha * 0.4f, mCy, (mWidth + mHeight) * alpha, mPaint);*/
                    break;
                case REVEAL_TYPE_HEPTAGON:
                    canvas.save();
                    canvas.translate(mCx, mCy);
                    mTmpPath.reset();
                    angle = (float) (Math.PI * 2 / 7);
                    mRadius = (int) ((mWidth + mHeight) * alpha);
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
            }
        } else {
            //canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        }
    }

    private void attach2Activity(Activity activity, View child) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        mRootView = rootView;
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        rootView.getChildAt(0).setVisibility(INVISIBLE);
        rootView.addView(child, lp);
    }


    public <T extends Activity> T getTopActivity() {
        Activity act = null;
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            act = activityStack.get(i);
        }
        act = activityStack.get(1);
        return (T) act;
    }

    public void outActivity() {
        Activity activity = getTopActivity();
        RevealOutView revealOutView = new RevealOutView(activity, mRect, mPaint, mType);
    }

    @IntDef({REVEAL_TYPE_CIRCLE, REVEAL_TYPE_RECT, REVEAL_TYPE_HEPTAGON, REVEAL_TYPE_PENTAGRAM})
    public @interface RevealType {
    }
}
