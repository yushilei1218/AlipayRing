package com.yushilei.alipayring;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private AlipayRingView alipayV;
    private EditText et;
    private LinearLayout back;

    int mStartColor = Color.rgb(0xff, 0x6f, 0x00);
    int mEndColor = Color.rgb(0x00, 0xa2, 0xff);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back = (LinearLayout) findViewById(R.id.back);
        alipayV = (AlipayRingView) findViewById(R.id.ali);
        et = (EditText) findViewById(R.id.et);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Random random = new Random();
        int i = random.nextInt(950 - 350);
        int score = 350 + i;
        alipayV.setAnimScore(score);
        ObjectAnimator animator = ObjectAnimator.ofInt(back, "backgroundColor", mStartColor, mEndColor);
        animator.setDuration(AlipayRingView.mAnimDuration);
        //animator.setInterpolator(new LinearInterpolator());
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }

    public void setScore(View view) {
        String num = et.getText().toString();
        if (num.trim().length() > 0) {
            int score = Integer.parseInt(num);
            if (score <= AlipayRingView.mScoreMax && score >= AlipayRingView.mScoreMin) {
                alipayV.setAnimScore(score);
                ObjectAnimator animator = ObjectAnimator.ofInt(back, "backgroundColor", mStartColor, mEndColor);
                animator.setDuration(AlipayRingView.mAnimDuration);
                animator.setEvaluator(new ArgbEvaluator());
                animator.start();//
            }
        }
    }
}
