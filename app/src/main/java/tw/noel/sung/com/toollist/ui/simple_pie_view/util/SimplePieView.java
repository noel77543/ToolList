package tw.noel.sung.com.toollist.ui.simple_pie_view.util;

import android.animation.ValueAnimator;
/**
 * Created by noel on 2018/12/15.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;

import tw.noel.sung.com.toollist.R;

public class SimplePieView extends android.support.v7.widget.AppCompatImageView implements ValueAnimator.AnimatorUpdateListener {

    //預設進度條寬度
    private final int DEFAULT_PROGRESS_WIDTH = 100;
    //預設左下起始值 右下最大值文字SIZE
    private final int DEFAULT_START_AND_END_TEXT_SIZE = 15;
    //預設中間當前值文字SIZE
    private final int DEFAULT_CURRENT_PROGRESS_TEXT_SIZE = 80;
    //預設進度條 漸層起始色
    private final String DEFAULT_START_COLOR = "#FF4397F7";
    //預設進度條 漸層最終色
    private final String DEFAULT_END_COLOR = "#FF4397F7";
    //預設進度條 未填滿色
    private final String DEFAULT_EMPTY_COLOR = "#ddedff";
    //預設 左下起始值 與 右下最終值 文字顏色
    private final String DEFAULT_START_AND_END_TEXT_COLOR = "#000000";
    //預設 中間 當前值 文字顏色
    private final String DEFAULT_CURRENT_PROGRESS_TEXT_COLOR = "#000000";
    //動畫時間
    private final int ANIMATION_TIME_MILLIS = 1000;
    //最小
    private String minValue = "0%";
    //最大
    private String maxValue = "100%";
    //進度條 paint
    private Paint paint;
    //文字 paint
    private Paint paintText;
    //顏色漸層 起始
    private int startColor;
    //顏色漸層 結束
    private int endColor;
    //未填滿處顏色
    private int emptyColor;
    //View寬度
    private int viewWidth;
    //View高度
    private int viewHeight;
    private int marginBottom;
    //進度條寬度
    private int progressWidth;
    //半圓的半徑
    private int radius;
    //設置的目標值 progress
    private float progressTarget;
    //每次update的progress
    private float progressCurrent;
    //左右兩邊 最小值與最大值文字size
    private int startAndEndTextSize;
    //左右兩邊 最小值與最大值文字顏色
    private int startAndEndTextColor;
    //中間當前數值文字SIZE
    private int currentProgressTextSize;
    //中間當前數值文字顏色
    private int currentProgressTextColor;
    private Context context;

    private Canvas canvas;
    private Bitmap bitmap;

    //------------


    public SimplePieView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplePieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(attrs, defStyleAttr);
        init();
    }

    //------------

    /***
     *  初始化設定檔
     */
    private void initAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimplePieView, defStyleAttr, 0);

        startColor = typedArray.getColor(R.styleable.SimplePieView_simplePieViewStartColor, Color.parseColor(DEFAULT_START_COLOR));
        endColor = typedArray.getColor(R.styleable.SimplePieView_simplePieViewEndColor, Color.parseColor(DEFAULT_END_COLOR));
        emptyColor = typedArray.getColor(R.styleable.SimplePieView_simplePieViewEmptyColor, Color.parseColor(DEFAULT_EMPTY_COLOR));
        startAndEndTextSize = typedArray.getDimensionPixelSize(R.styleable.SimplePieView_simplePieViewStartAndEndTextSize, DEFAULT_START_AND_END_TEXT_SIZE);
        startAndEndTextColor = typedArray.getColor(R.styleable.SimplePieView_simplePieViewStartAndEndTextColor, Color.parseColor(DEFAULT_START_AND_END_TEXT_COLOR));
        progressWidth = typedArray.getDimensionPixelSize(R.styleable.SimplePieView_simplePieViewProgressWidth, DEFAULT_PROGRESS_WIDTH);
        currentProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.SimplePieView_simplePieViewCurrentProgressTextSize, DEFAULT_CURRENT_PROGRESS_TEXT_SIZE);
        currentProgressTextColor = typedArray.getColor(R.styleable.SimplePieView_simplePieViewCurrentProgressTextColor, Color.parseColor(DEFAULT_CURRENT_PROGRESS_TEXT_COLOR));
        typedArray.recycle();
    }

    //------------

    /**
     * 初始化邏輯
     */
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(progressWidth);
        paint.setStyle(Paint.Style.STROKE);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(emptyColor);
        paintText.setTextSize(startAndEndTextSize);

        marginBottom = getTextHeight(paintText);
    }
    //------------

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 以元件高度計算弧形半徑
        radius = viewHeight - marginBottom - progressWidth / 2;
        viewWidth = radius * 2 + progressWidth + getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(viewWidth, viewHeight);


        bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        setBackground(new BitmapDrawable(getResources(), bitmap));
        drawBackground();
        drawText(canvas);
    }

    //-----------

    /***
     * 繪製背景
     */
    private void drawBackground() {
        //設置繪圖框架
        int left = progressWidth / 2 + getPaddingLeft();
        int right = left + 2 * radius;
        int top = viewHeight - marginBottom - radius;
        int bottom = viewHeight - marginBottom + radius;

        //容器 及 最大值設定
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setColor(emptyColor);
        paint.setShader(null);
        //將容器從180度的位置 開始繪製180度
        canvas.drawArc(new RectF(left, top, right, bottom), 180, 180, false, paint);
    }
    //------------

    /**
     * 繪製文字  左下起始與右下最大值
     */
    private void drawText(Canvas canvas) {
        paintText.setColor(startAndEndTextColor);
        paintText.setTextSize(startAndEndTextSize);
        Paint.FontMetrics fontMetrics = paintText.getFontMetrics();
        float baseline = viewHeight - fontMetrics.descent;
        float stringWidth = paintText.measureText(minValue);
        //繪製文字  左下起始值
        canvas.drawText(minValue, getPaddingLeft() - stringWidth / 2 + progressWidth / 2, baseline, paintText);
        stringWidth = paintText.measureText(maxValue);
        //繪製文字  右下最大值
        canvas.drawText(maxValue, viewWidth - getPaddingRight() - stringWidth + stringWidth / 2 - progressWidth / 2, baseline, paintText);
    }

    //------------

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        drawCurrent(canvas);
        drawProgressText(canvas, progressCurrent);
    }
    //------------

    /***
     * 繪製當前進度
     */
    private void drawCurrent(Canvas canvas){
        //設置繪圖框架
        int left = progressWidth / 2 + getPaddingLeft();
        int right = left + 2 * radius;
        int top = viewHeight - marginBottom - radius;
        int bottom = viewHeight - marginBottom + radius;
        //漸層設定
        int[] colors = new int[]{startColor, endColor};
        LinearGradient linearGradient = new LinearGradient(0, 0, viewWidth, 0, colors, null, LinearGradient.TileMode.CLAMP);
        paint.setShader(linearGradient);
        //從180度 繪製到 當前數值
        canvas.drawArc(new RectF(left, top, right, bottom), 180, progressCurrent, false, paint);
    }
    //------------

    /**
     * 位置文字 中間當前數值
     */
    private void drawProgressText(Canvas canvas, float progress) {
        paintText.setColor(currentProgressTextColor);
        paintText.setTextSize(currentProgressTextSize);
        //取得浮點數小數點後1位數
        String text = String.format("%.1f", progress / 180.f * 100) + "%";
        Paint.FontMetrics fontMetrics = paintText.getFontMetrics();
        float baseline = viewHeight - fontMetrics.descent;
        float stringWidth = paintText.measureText(text);
        canvas.drawText(text, viewWidth / 2 - stringWidth / 2, baseline, paintText);
    }

    //------------

    private int getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //取最小值
        return (int) (Math.ceil(fontMetrics.descent - fontMetrics.ascent) + 2);
    }

    //------------

    /**
     * 動畫效果 取值
     */
    private void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, progressTarget / 100f * 180);
        //設置時長
        animator.setDuration(ANIMATION_TIME_MILLIS);
        //設置加速器
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(this);
        animator.start();
    }
    //------------

    /***
     * 當動畫進度更新
     * @param animation
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        progressCurrent = (float) animation.getAnimatedValue();
        invalidate();
    }
    //------------

    /***
     *  設定目標值
     * @param progress
     */
    public void setProgress(float progress) {
        progressTarget = progress;
        startAnimation();
    }
}
