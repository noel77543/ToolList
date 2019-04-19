package tw.noel.sung.com.toollist.ui.multiple_section_progress_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import tw.noel.sung.com.toollist.R;

public class MultipleSectionProgressView extends android.support.v7.widget.AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener, ValueAnimator.AnimatorUpdateListener {

    //預設最大值
    private final float DEFAULT_MAX_VALUE = 100;
    private float maxValue;
    //預設線條顏色
    private final String DEFAULT_STROKE_COLOR = "#000000";
    private int strokeColor;
    //預設動畫時間
    private final int DEFAULT_ANIMATION_TIME = 3000;
    private int animationTime;
    //當前遞增的數值
    private float currentValue;
    private float strokeWidth;
    private int[] sectionColors;
    private float[] sections;
    private int viewWidth;
    private int viewHeight;
    private Context context;
    private Path path;
    private RectF rectF;
    //進度圓的圓心點
    private PointF pointF;

    //是否繪製完成區間
    private boolean isInitial = false;
    private Paint foregroundPaint;
    private Canvas foregroundCanvas;
    private Bitmap foregroundBitmap;
    private Paint backgroundPaint;
    private Canvas backgroundCanvas;
    private Bitmap backgroundBitmap;


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
        animationTime = typedArray.getInteger(R.styleable.MultipleSectionProgressView_multipleSectionProgressViewAnimationTime, DEFAULT_ANIMATION_TIME);
        strokeColor = typedArray.getColor(R.styleable.MultipleSectionProgressView_multipleSectionProgressViewStrokeColor, Color.parseColor(DEFAULT_STROKE_COLOR));
        typedArray.recycle();
    }
    //--------

    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setColor(strokeColor);
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        path = new Path();
        pointF = new PointF();

    }

    //----------

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= 16) {
            getViewTreeObserver()
                    .removeOnGlobalLayoutListener(this);
        } else {
            getViewTreeObserver()
                    .removeGlobalOnLayoutListener(this);
        }
        viewWidth = getWidth();
        viewHeight = getHeight();
        strokeWidth = viewHeight * 0.03f;
        foregroundPaint.setStrokeWidth(strokeWidth);
        foregroundBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        foregroundCanvas = new Canvas(foregroundBitmap);
        backgroundBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        backgroundCanvas = new Canvas(backgroundBitmap);
        setImageBitmap(foregroundBitmap);
        setBackground(new BitmapDrawable(backgroundBitmap));
    }

    //--------

    /***
     * 設置sections
     * @param sections
     */
    public MultipleSectionProgressView setSections(float[] sections, int[] sectionColors, int[] gradientColors, float maxValue) {
        this.maxValue = maxValue;
        this.sections = sections;
        this.sectionColors = sectionColors;
        LinearGradient linearGradient = new LinearGradient(0, 0, viewWidth, 0, gradientColors, null, LinearGradient.TileMode.CLAMP);
        backgroundPaint.setShader(linearGradient);
        return this;
    }

    //-----------

    /***
     * 開始繪製
     */
    public void draw() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, maxValue);
        //設置時長
        valueAnimator.setDuration(animationTime);
        //設置加速器
        valueAnimator.setInterpolator(new AccelerateInterpolator());
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

    //--------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float margin = strokeWidth / 2;
        if (!isInitial) {
            if (sections != null) {
                //畫出所有section
                for (int i = 0; i < sections.length; i++) {
                    //section 不得超過最大值
                    if (sections[i] < maxValue) {
                        if (sectionColors != null && sectionColors[i] != 0) {
                            foregroundPaint.setColor(getResources().getColor(sectionColors[i]));
                        }
                        float part = (sections[i] / maxValue) * viewWidth;
                        rectF = new RectF(margin, margin, part - margin, viewHeight - margin);
                        foregroundCanvas.drawRoundRect(rectF, viewHeight / 2, viewHeight / 2, foregroundPaint);
                    }
                }
            }

            //畫出最後尾部
            foregroundPaint.setColor(strokeColor);
            rectF = new RectF(margin, margin, viewWidth - margin, viewHeight - margin);
            foregroundCanvas.drawRoundRect(rectF, viewHeight / 2, viewHeight / 2, foregroundPaint);
            isInitial = true;
        } else {


            rectF.set(margin, margin, currentValue, viewHeight - margin);
            backgroundCanvas.drawRoundRect(rectF, viewHeight / 2, viewHeight / 2, backgroundPaint);
        }
    }


}
