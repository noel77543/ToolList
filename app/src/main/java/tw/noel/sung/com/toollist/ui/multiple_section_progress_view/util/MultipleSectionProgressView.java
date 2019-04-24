package tw.noel.sung.com.toollist.ui.multiple_section_progress_view.util;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

import tw.noel.sung.com.toollist.R;

public class MultipleSectionProgressView extends android.support.v7.widget.AppCompatImageView implements ValueAnimator.AnimatorUpdateListener {

    //預設最大值
    private final float DEFAULT_MAX_VALUE = 100;
    private float maxValue;
    //預設起始值
    private final float DEFAULT_TARGET_VALUE = 0;
    private float targetValue;

    //預設動畫時間
    private final int DEFAULT_ANIMATION_TIME = 3000;
    private int animationTime;
    //預設內間距
    private final float DEFAULT_INNER_MARGIN = 0;
    private float innerMargin;
    //線條寬度
    private final float DEFAULT_STROKE_WIDTH = 0;
    private float strokeWidth;

    //當前遞增的數值
    private float currentValue;
    //section的所有漸變色
    private int[] sectionColors;
    //section
    private float[] sections;
    //漸變色的起始位置
    private float[] positions;
    private int viewWidth;
    private int viewHeight;
    private Context context;
    private RectF rectF;
    //是否開始繪製
    private boolean isDrawing = false;
    //是否完成區間繪製
    private boolean isInitial = false;
    private Paint foregroundPaint;
    private Canvas foregroundCanvas;
    private Bitmap foregroundBitmap;
    private Paint backgroundPaint;
    private Canvas backgroundCanvas;
    private Bitmap backgroundBitmap;

    //色調調節器
    private ColorMatrix colorMatrix;
    private float margin;

    public MultipleSectionProgressView(Context context) {
        this(context, null);
    }

    public MultipleSectionProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleSectionProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(attrs, defStyleAttr);
        init();
    }
    //--------

    private void initAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MultipleSectionProgressView, defStyleAttr, 0);
        maxValue = typedArray.getFloat(R.styleable.MultipleSectionProgressView_multipleSectionProgressViewMaxValue, DEFAULT_MAX_VALUE);
        targetValue = typedArray.getFloat(R.styleable.MultipleSectionProgressView_multipleSectionProgressViewTargetValue, DEFAULT_TARGET_VALUE);
        animationTime = typedArray.getInteger(R.styleable.MultipleSectionProgressView_multipleSectionProgressViewAnimationTime, DEFAULT_ANIMATION_TIME);
        innerMargin = typedArray.getDimension(R.styleable.MultipleSectionProgressView_multipleSectionProgressViewInnerMargin, DEFAULT_INNER_MARGIN);
        strokeWidth = typedArray.getDimension(R.styleable.MultipleSectionProgressView_multipleSectionProgressViewStrokeWidth, DEFAULT_STROKE_WIDTH);
        typedArray.recycle();
    }
    //--------

    private void init() {
        colorMatrix = new ColorMatrix();

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    //--------

    /***
     *  動畫時間
     */
    public MultipleSectionProgressView setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
        return this;
    }

    //--------

    /***
     *  設置值
     */
    public MultipleSectionProgressView setValue(float maxValue, float targetValue) {
        this.maxValue = maxValue;
        this.targetValue = targetValue > maxValue ? maxValue : targetValue;
        return this;
    }

    //--------

    /***
     * 設置sections
     * @param sections
     */
    public MultipleSectionProgressView setSections(int startColor, int endColor, int[] sectionColors, float[] sections) {
        if (sectionColors.length == sections.length) {
            this.sections = sections;
            //陣列長度為 所有section+ 起始 + 最終
            this.sectionColors = new int[sections.length + 2];
            //陣列長度為
            this.positions = new float[sections.length + 2];

            //設置起始色的漸變起始點為0
            this.positions[0] = 0f;
            //加入漸變起始色
            this.sectionColors[0] = getResources().getColor(startColor);

            //加入所有section的漸變色
            for (int i = 0; i < sectionColors.length; i++) {
                //設置漸變色
                this.sectionColors[i + 1] = getResources().getColor(sectionColors[i]);
                //設置漸變起始點
                this.positions[i + 1] = (sections[i] / maxValue);
            }

            //設置最終色的漸變起始點為1
            this.positions[this.positions.length - 1] = this.positions[this.positions.length - 2] + 0.1f;
            //加入漸變最終色
            this.sectionColors[this.sectionColors.length - 1] = getResources().getColor(endColor);
            isDrawing = true;
            invalidate();
        }
        return this;
    }

    //-----------

    /***
     *  設置填滿色 單一色彩
     */
    public MultipleSectionProgressView setColor(int color) {
        backgroundPaint.setColor(getResources().getColor(color));
        return this;
    }


    //--------------

    /***
     *  設置內間距
     * @param innerMargin
     * @return
     */
    public MultipleSectionProgressView setInnerMargin(float innerMargin) {
        this.innerMargin = innerMargin;
        return this;
    }


    //-----------

    /***
     * 帶入加速器 並開始繪製
     */
    public void draw(TimeInterpolator timeInterpolator) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, targetValue);
        //設置時長
        valueAnimator.setDuration(animationTime);
        //設置加速器
        valueAnimator.setInterpolator(timeInterpolator);
        valueAnimator.addUpdateListener(this);
        valueAnimator.start();
    }
    //-----------

    /***
     * 當數值刷新
     * @param animation
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        currentValue = (((float) animation.getAnimatedValue() / maxValue) * viewWidth);
        invalidate();
    }
    //-----------

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getWidth();
        viewHeight = getHeight();
        if (strokeWidth == DEFAULT_STROKE_WIDTH) {
            strokeWidth = viewHeight * 0.03f;
        }
        margin = (strokeWidth / 2);

        //繪製外部
        if (isDrawing) {

            foregroundPaint.setStrokeWidth(strokeWidth);
            foregroundBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            foregroundCanvas = new Canvas(foregroundBitmap);
            backgroundBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            backgroundCanvas = new Canvas(backgroundBitmap);
            setImageBitmap(foregroundBitmap);
            setBackground(new BitmapDrawable(backgroundBitmap));


            if (!isInitial) {
                if (sections != null && sectionColors != null) {
                    //漸層設置
                    backgroundPaint.setShader(new LinearGradient(0, 0, viewWidth, 0, sectionColors, positions, LinearGradient.TileMode.CLAMP));
                    foregroundPaint.setShader(new LinearGradient(0, 0, viewWidth, 0, sectionColors, null, LinearGradient.TileMode.CLAMP));
                    colorMatrix.setRotate(0, 25);

                    foregroundPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
                    //畫出所有section
                    for (int i = 0; i < sections.length; i++) {
                        //section 不得超過最大值
                        if (sections[i] < maxValue) {
                            float part = (sections[i] / maxValue) * viewWidth;
                            rectF = new RectF(margin, margin, part - margin, viewHeight - margin);

                            foregroundCanvas.drawRoundRect(rectF, viewHeight, viewHeight, foregroundPaint);
                        }
                    }
                }

                rectF = new RectF(margin, margin, viewWidth - margin, viewHeight - margin);
                foregroundCanvas.drawRoundRect(rectF, viewHeight, viewHeight, foregroundPaint);
                //完成區間繪製
                isInitial = true;
            }
        }
    }

    //--------

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //繪製內部
        float innerMargin = this.innerMargin + margin;
        backgroundCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        rectF.set(innerMargin, innerMargin, currentValue - innerMargin, viewHeight - innerMargin);
        backgroundCanvas.drawRoundRect(rectF, viewHeight, viewHeight, backgroundPaint);
    }
}
