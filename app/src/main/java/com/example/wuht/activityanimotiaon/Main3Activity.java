package com.example.wuht.activityanimotiaon;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.Random;

public class Main3Activity extends BaseActivity {

    private int[] color;
    private RevealView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        activityStack.add(this);
        color = getResources().getIntArray(R.array.colors);

        Random r = new Random();
        switch (getIntent().getIntExtra("type", RevealView.REVEAL_TYPE_CIRCLE)) {
            case 1:
                t = new RevealView(this, getIntent().getSourceBounds(), RevealView.REVEAL_TYPE_CIRCLE, color[r.nextInt(9)]);
                break;
            case 2:
                t = new RevealView(this, getIntent().getSourceBounds(), RevealView.REVEAL_TYPE_RECT, color[r.nextInt(9)]);
                break;
            case 3:
                t = new RevealView(this, getIntent().getSourceBounds(), RevealView.REVEAL_TYPE_HEPTAGON, color[r.nextInt(9)]);
                break;
            case 4:
                t = new RevealView(this, getIntent().getSourceBounds(), RevealView.REVEAL_TYPE_PENTAGRAM, color[r.nextInt(9)]);
                break;
            default:
                t = new RevealView(this, getIntent().getSourceBounds(), RevealView.REVEAL_TYPE_RECT, color[r.nextInt(9)]);
        }
    }

    @Override
    protected void onDestroy() {
        overridePendingTransition(0, 0);

        ViewGroup rootView = (ViewGroup) activityStack.get(1).findViewById(Window.ID_ANDROID_CONTENT);
        rootView.getChildAt(0).setVisibility(View.INVISIBLE);
        t.outActivity();
        super.onDestroy();

    }
}
