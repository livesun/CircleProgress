package livesun.circleprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by 29028 on 2017/6/6.
 */

public class CircleProgress extends View {

    private int mInnerColor = Color.BLUE;
    private int mOterColor = Color.YELLOW;
    private int mOuterColor = Color.RED;
    private float mRoundWidth;
    private int mTextSize;
    private int mTextColor = Color.BLACK;
    private Paint mInnerPaint;
    private Paint mOuterPaint;
    private Paint mTextPaint;
    private float mMax = 100;
    private float mCurrent;
    private Paint mOterPaint;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray td = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);
        mInnerColor = td.getColor(R.styleable.CircleProgress_inner_color, mInnerColor);
        mOuterColor = td.getColor(R.styleable.CircleProgress_outer_color, mOuterColor);
        mOterColor = td.getColor(R.styleable.CircleProgress_other_color, mOterColor);
        mRoundWidth = td.getDimension(R.styleable.CircleProgress_progress_width, px2dip(5));
        mTextSize = td.getDimensionPixelSize(R.styleable.CircleProgress_progress_textSize, px2sp(15));
        mTextColor = td.getColor(R.styleable.CircleProgress_progress_textColor, mTextColor);
        mMax = td.getFloat(R.styleable.CircleProgress_progress_max, mMax);
        mCurrent = td.getFloat(R.styleable.CircleProgress_progress_current, mCurrent);
        td.recycle();

        mInnerPaint = getPaint(mInnerColor);
        mOuterPaint = getPaint(mOuterColor);
        mOterPaint = getPaint(mOterColor);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    private Paint getPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mRoundWidth);
        return paint;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heigtMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            width = (int) px2dip(80);
        }
        if (heigtMode == MeasureSpec.AT_MOST) {
            height = (int) px2dip(80);
        }

        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = Math.min(getWidth(), getHeight()) / 2;
        //内圆
        canvas.drawCircle(center, center, center - mRoundWidth / 2, mInnerPaint);
        //外圆
        float percent = 0;
        if (mMax != 0) {
            percent = (mCurrent / mMax);
            RectF rect = new RectF(mRoundWidth / 2, mRoundWidth / 2, getWidth() - mRoundWidth / 2, getHeight() - mRoundWidth / 2);

            if (percent <= 1) {
                canvas.drawArc(rect, 0, percent * 360, false, mOuterPaint);
            } else if (percent <= 2) {
                canvas.drawArc(rect, 0, 360, false, mOuterPaint);
                canvas.drawArc(rect, 0, (percent - 1) * 360, false, mOterPaint);
            }

        } else {
            throw new IllegalArgumentException("请设置进度条的最大值");
        }
        //画字
        int v = (int) (percent * 100);
        String content;
        if (v <= 200) {
            content = v + "%";
        } else {
            content = "200%";
        }

        Rect textBound = new Rect();
        mTextPaint.getTextBounds(content, 0, content.length(), textBound);
        int dx = center - textBound.width() / 2;
        Paint.FontMetricsInt anInt = mTextPaint.getFontMetricsInt();
        //字体基线
        int dy = (anInt.bottom - anInt.top) / 2 - anInt.bottom;
        canvas.drawText(content, dx, center + dy, mTextPaint);
    }

    public void setCurrent(float current) {
        mCurrent = current;
        invalidate();
    }

    public void setMax(float max) {
        mMax = max;
    }

    private int px2sp(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, getResources().getDisplayMetrics());
    }


    private float px2dip(int px) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }


    public void show(float max, float current, long time) {
        setMax(max);
        ValueAnimator animator = ValueAnimator.ofFloat(0, current);
        animator.setDuration(time);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setCurrent(value);
            }
        });

        animator.start();
    }
}