package com.yushilei.alipayring;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author by  yushilei.
 * @time 2016/8/31 -09:42.
 * @Desc
 */
public class AlipayRingView extends View implements ValueAnimator.AnimatorUpdateListener {

    String TAG = "AliRing";

    public static int mScoreMax = 950;
    public static int mScoreMin = 350;

    public static int mAnimDuration = 2 * 1000;

    int mSweepAngle = 240;
    int mStartAngle = 150;

    int mScore = 750;

    Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int lineColor = Color.WHITE;
    int lineWidth = 4;

    Paint mPanPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    int panColor = Color.argb(100, 0xf0, 0xf0, 0xf0);
    int panWidth = 32;

    int mCenterX;
    int mCenterY;
    int mR;
    private RectF mRectF;
    int padding = 50;

    String tags[] = new String[]{"350", "较差", "550", "中等", "600", "良好", "650", "优秀", "700", "极好", "950"};

    String levelTags[] = new String[]{"信用较差", "信用一般", "信用良好", "信用优秀", "信用极好"};

    TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private float mRate;

    float tagsSize;
    float levelSize;
    float scoreSize;

    public AlipayRingView(Context context) {
        this(context, null);
    }

    public AlipayRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLinePaint.setColor(lineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(lineWidth);

        mPanPaint.setColor(panColor);
        mPanPaint.setStyle(Paint.Style.STROKE);
        mPanPaint.setStrokeWidth(panWidth);

        mRectF = new RectF();

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRate = (float) (w / 320);
        tagsSize = mRate * 10;
        levelSize = mRate * 20;
        scoreSize = mRate * 50;

        mCenterX = w / 2;
        mCenterY = h / 2;
        mR = w / 3;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int newHeightMs = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //外环
        mRectF.set(mCenterX - mR - padding, mCenterY - mR - padding, mCenterX + mR + padding, mCenterY + mR + padding);
        int sweepAngle = getSweepAngle();
        canvas.drawArc(mRectF, mStartAngle, sweepAngle, false, mLinePaint);

        //内盘
        mRectF.set(mCenterX - mR, mCenterY - mR, mCenterX + mR, mCenterY + mR);
        canvas.drawArc(mRectF, mStartAngle, mSweepAngle, false, mPanPaint);
        //分数
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(scoreSize);
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int textHeight = Math.abs(fontMetricsInt.top) + Math.abs(fontMetricsInt.bottom);
        int distance = (Math.abs(fontMetricsInt.ascent) - Math.abs(fontMetricsInt.descent)) / 2;
        canvas.drawText(mScore + "", mCenterX, mCenterY + distance, mTextPaint);

        //信用等级
        String levelStr = getLevelStr();
        mTextPaint.setTextSize(levelSize);
        Paint.FontMetricsInt levelMetricsInt = mTextPaint.getFontMetricsInt();
        int levelHeight = Math.abs(levelMetricsInt.top) + Math.abs(levelMetricsInt.bottom);
        int levelY = mCenterY + textHeight / 2 + padding + levelHeight / 2;
        canvas.drawText(levelStr, mCenterX, levelY, mTextPaint);
        //内盘 刻度
        for (int i = 0; i < 31; i++) {
            canvas.save();
            canvas.rotate(-120 + i * 8, mCenterX, mCenterY);
            float strokeWidth = mPanPaint.getStrokeWidth() / 2;
            if (i % 6 == 0) {
                mLinePaint.setColor(Color.WHITE);
                canvas.drawLine(mCenterX, mCenterY - mR - strokeWidth, mCenterX, mCenterY - mR + strokeWidth + 4, mLinePaint);
            } else {
                mLinePaint.setColor(Color.LTGRAY);
                canvas.drawLine(mCenterX, mCenterY - mR - strokeWidth, mCenterX, mCenterY - mR + strokeWidth, mLinePaint);
            }

            canvas.restore();
        }
        mTextPaint.setTextSize(tagsSize);
        mTextPaint.setColor(Color.WHITE);
        Paint.FontMetricsInt metricsInt = mTextPaint.getFontMetricsInt();
        int tagHeight = Math.abs(metricsInt.top) + Math.abs(metricsInt.bottom);

        for (int i = 0; i < tags.length; i++) {
            canvas.save();
            canvas.rotate(-120 + 24 * i, mCenterX, mCenterY);
            canvas.drawText(tags[i], mCenterX, mCenterY - mR + padding + tagHeight / 2, mTextPaint);
            canvas.restore();
        }

    }

    private String getLevelStr() {
        if (550 > mScore && mScore >= mScoreMin) {
            return levelTags[0];
        }
        if (mScore >= 550 && mScore < 600) {
            return levelTags[1];
        }
        if (mScore >= 600 && mScore < 650) {
            return levelTags[2];
        }
        if (mScore >= 650 && mScore < 700) {
            return levelTags[3];
        }

        return levelTags[4];
    }

    /**
     * 当前Score需要划过的角度
     *
     * @return
     */
    private int getSweepAngle() {
        int perAngle = mSweepAngle / 5;
        if (550 > mScore && mScore >= mScoreMin) {
            return (mScore - mScoreMin) * perAngle / (550 - mScoreMin);
        }
        if (mScore >= 550 && mScore < 600) {
            return (mScore - 550) * perAngle / (600 - 550) + perAngle;
        }
        if (mScore >= 600 && mScore < 650) {
            return (mScore - 600) * perAngle / (650 - 600) + 2 * perAngle;
        }
        if (mScore >= 650 && mScore < 700) {
            return (mScore - 650) * perAngle / (700 - 650) + 3 * perAngle;
        }

        return (mScore - 700) * perAngle / (950 - 700) + 4 * perAngle;

    }

    public void setMScore(int score) {
        mScore = score;
    }


    public void setAnimScore(int score) {
        if (score >= mScoreMin || score <= mScoreMax) {
            ObjectAnimator animator = ObjectAnimator.ofInt(this, "mScore", mScoreMin, score);
            animator.setDuration(mAnimDuration);
            animator.setInterpolator(new FastOutSlowInInterpolator());
            animator.addUpdateListener(this);
            animator.start();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        postInvalidate();
    }
}
