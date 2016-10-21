package com.example.wuht.activityanimotiaon;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

public class BaseActivity extends AppCompatActivity {
    public static Stack<Activity> activityStack=new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        activityStack.add(0,this);

    }

    @Override
    protected void onDestroy() {
        activityStack.remove(this);
        super.onDestroy();
    }
}
