package com.yushilei.alipayring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private AlipayRingView alipayV;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    public void setScore(View view) {
        String num = et.getText().toString();
        if (num.trim().length() > 0) {
            int score = Integer.parseInt(num);
            if (score <= AlipayRingView.mScoreMax && score >= AlipayRingView.mScoreMin) {
                alipayV.setAnimScore(score);
            }
        }
    }
}
