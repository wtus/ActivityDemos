package com.example.wuht.activityanimotiaon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by wuht on 2016/10/20.
 */

public class RevealViewDemo extends View {
    public static final int STATE_NOT_STARTED = 0;// 没有开始状态
    public static final int STATE_FILL_STARTED = 1;// 填充满状态
    public static final int STATE_FINISHED = 2;// 填充完成状态

    // 在动画开始的时候速率比较慢，后面持续增加
    private static final Interpolator INTERPOLATOR = new AccelerateInterpolator();
    private static final int FILL_TIME = 400;// 动画时间600毫秒
    private int actionBarHeight;

    private int state = STATE_NOT_STARTED;// 默认状态

    private Paint fillPaint;// 画笔
    private int currentRadius;// 半径
    ObjectAnimator revealAnimator;

    private int startLocationX;// 控件的x坐标值
    private int startLocationY;// 控件的Y坐标值

    private float alpha;
    private Rect mRect;
    private Context mContext;

    private OnStateChangeListener onStateChangeListener;
    private int statusBarHeight;

    public RevealViewDemo(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public RevealViewDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RevealViewDemo(Context context, AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
        }
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
    }

    private void init() {
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.WHITE);
    }

    // 设置画笔的颜色
    public void setFillPaintColor(int color) {
        fillPaint.setColor(color);
    }

    //开启动画
    public void startFromLocation(int[] tapLocationOnScreen) {//参数就是位置
        changeState(STATE_FILL_STARTED);

        startLocationX = tapLocationOnScreen[0];//传递过来view的x的RELATIVE_TO_SELF坐标值
        startLocationY = tapLocationOnScreen[1];//传递过来view的y的RELATIVE_TO_SELF坐标值
        startLocationY -= actionBarHeight;
        // 动画标记为当前的半径currentRadius，值为RevealBackgroundView的0--width+height
        revealAnimator = ObjectAnimator.ofInt(this, "currentRadius", 0,
                getWidth() + getHeight()).setDuration(FILL_TIME);//逻辑比原来好，但是计算速度不一定有原来 getWidth()+getHeight() 快
        revealAnimator.setInterpolator(INTERPOLATOR);
        // 动画监听器
        revealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 在动画结束的时候调用该方法
                changeState(STATE_FINISHED);
            }
        });
        revealAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (int) animation.getAnimatedValue() / (float) Math.max(getWidth(), getHeight());
            }
        });
        revealAnimator.start();
    }

    // 当回调为true时，调用该方法，重新绘制当前界面
    public void setToFinishedFrame() {
        changeState(STATE_FINISHED);
        invalidate();
    }

    public void setRect(Rect rect) {
        mRect = rect;
        mRect = new Rect(mRect.left, mRect.top - actionBarHeight - statusBarHeight,
                mRect.right, mRect.bottom - actionBarHeight - statusBarHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 在动画完成后直接画整个界面，不在让他继续扩散
        if (state == STATE_FINISHED) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
        } else {
            // 绘制点击控件位置扩散的圆圈
            canvas.drawCircle(startLocationX, startLocationY, currentRadius, fillPaint);
            canvas.drawPoint(startLocationX, startLocationY, fillPaint);
            //canvas.drawRect(mRect, fillPaint);
            canvas.drawRect(mRect.left + (-alpha) * (mRect.left + 500f), mRect.top + (-alpha) * (mRect.top + 500f)
                    , mRect.right + (getWidth() - mRect.right + 500f) * alpha, mRect.bottom + (getHeight() - mRect.bottom + 500f) * alpha,fillPaint);
//            canvas.drawRect();

        }
    }

    // 判断当前的状态
    private void changeState(int state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        // 将当前状态不停的回调，回调时判断一下当前是否完成，是的话就显示底部布局
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChange(state);
        }
    }

    // 设置圆圈的半径时，重新调用onDraw，重新画圆
    public void setCurrentRadius(int radius) {
        this.currentRadius = radius;
        invalidate();
    }

    public void setOnStateChangeListener(
            OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public static interface OnStateChangeListener {
        void onStateChange(int state);
    }
}
