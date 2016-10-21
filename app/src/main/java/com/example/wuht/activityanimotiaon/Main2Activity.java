package com.example.wuht.activityanimotiaon;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

public class Main2Activity extends AppCompatActivity implements RevealViewDemo.OnStateChangeListener {

    private RevealViewDemo revealBackgroundView;
    private LinearLayout linea;
    private Rect mRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        linea = (LinearLayout) findViewById(R.id.linea);
        revealBackgroundView = (RevealViewDemo) findViewById(R.id.RevealBackgroundView);
        mRect=getIntent().getSourceBounds();
        setupRevealBackground(savedInstanceState);
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        revealBackgroundView.setOnStateChangeListener(this);
        //初始化时，当前的savedInstanceState为空
        if (savedInstanceState == null) {
            final int[] startLocation = getIntent()
                    .getIntArrayExtra("location");
            // 初始化控件时的监听
            revealBackgroundView.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            revealBackgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                            // 设置单击坐标点的半径
                            revealBackgroundView.setCurrentRadius(50);
                            // 设置绘制填充画笔的颜色
                            revealBackgroundView.setFillPaintColor(0xff34A67B);
                            if (mRect != null) {
                                revealBackgroundView.setRect(mRect);
                            }
                            // 开启动画
                            revealBackgroundView.startFromLocation(startLocation);
                            return true;
                        }
                    });
        } else {
            // 完成动画
            revealBackgroundView.setToFinishedFrame();
        }
    }

    @Override
    public void onStateChange(int state) {
        // 如果当前状态完成，就显示底部布局，隐藏RevealBackgroundView
        if (RevealViewDemo.STATE_FINISHED == state) {
            linea.setVisibility(View.VISIBLE);
            revealBackgroundView.setVisibility(View.GONE);
        }
    }
}
