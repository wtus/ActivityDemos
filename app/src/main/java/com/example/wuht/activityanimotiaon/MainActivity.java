package com.example.wuht.activityanimotiaon;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtn2;
    private Button mBtn1;
    private RelativeLayout mActivityMain;
    private Button btn3;
    private RelativeLayout activity_main;
    private Rect mRect;
    private Button button7;
    private Button button8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mRect = new Rect();
    }

    private void initView() {
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn1 = (Button) findViewById(R.id.btn1);
        mActivityMain = (RelativeLayout) findViewById(R.id.activity_main);

        mBtn2.setOnClickListener(this);
        mBtn1.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn2:
                v.getGlobalVisibleRect(mRect);
                Intent i = new Intent(this, Main3Activity.class);
                i.setSourceBounds(mRect);
                i.putExtra("type", 1);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.btn1:
                v.getGlobalVisibleRect(mRect);
                Intent i1 = new Intent(this, Main3Activity.class);
                i1.putExtra("type", 2);
                i1.setSourceBounds(mRect);
                startActivity(i1);
                overridePendingTransition(0, 0);
                break;
            case R.id.btn3:
                v.getGlobalVisibleRect(mRect);
                Intent i2 = new Intent(this, Main3Activity.class);
                i2.setSourceBounds(mRect);
                i2.putExtra("type", 3);
                startActivity(i2);
                overridePendingTransition(0, 0);
                break;
            case R.id.button7:
                v.getGlobalVisibleRect(mRect);
                Intent i3 = new Intent(this, Main3Activity.class);
                i3.setSourceBounds(mRect);
                i3.putExtra("type", 4);
                startActivity(i3);
                overridePendingTransition(0, 0);
                break;
            case R.id.button8:
                v.getGlobalVisibleRect(mRect);
                Intent i4 = new Intent(this, Main3Activity.class);
                i4.setSourceBounds(mRect);
                i4.putExtra("type", 1);
                startActivity(i4);
                overridePendingTransition(0, 0);
                break;
        }
    }
}
