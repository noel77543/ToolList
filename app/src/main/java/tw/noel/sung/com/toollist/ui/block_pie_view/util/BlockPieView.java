package tw.noel.sung.com.toollist.ui.block_pie_view.util;

import android.animation.Animator;
/**
 * Created by noel on 2018/12/27.
 */
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.List;

import tw.noel.sung.com.toollist.R;


public class BlockPieView extends AppCompatImageView implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    //圓形
    private final int _CIRCLE = 360;
    //預設進度條寬度
    private final int DEFAULT_PROGRESS_WIDTH = 100;
    //動畫時間  預設一秒
    private int duration = 1000;
    //進度條 paint
    private Paint paint;
    //View寬度
    private int viewWidth;
    //View高度
    private int viewHeight;
    private int marginBottom;
    //進度條寬度
    private int progressWidth;
    //半圓的半徑
    private int radius;
    //每次update的progress
    private float progressCurrent;

    private Context context;
    //各個區塊值
    private float[] values = new float[]{};
    //各個區塊對應顏色
    private int[] colors = new int[]{};
    //總值
    private float totalValue;
    //當前索引
    private int index = 0;
    //當前區塊的起始度數
    private float startAngle = 180;
    private ValueAnimator animator;
    private Bitmap bitmap;
    //當前進度條用
    private Canvas canvas;
    //圖形框架
    private RectF rect;
    private boolean isShouldRunOnHWLayer;


    //------------


    public BlockPieView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockPieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BlockPieView, defStyleAttr, 0);

        progressWidth = typedArray.getDimensionPixelSize(R.styleable.BlockPieView_blockPieViewProgressWidth, DEFAULT_PROGRESS_WIDTH);
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
        marginBottom = getTextHeight(paint);
    }
    //------------

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 以元件高度計算弧形半徑
        radius = viewHeight - marginBottom - progressWidth / 2;
        //使高度等同寬度
        viewWidth = radius * 2 + progressWidth + getPaddingLeft() + getPaddingRight();
        viewHeight = viewWidth;

        setMeasuredDimension(viewWidth, viewHeight);

        initRect();
        bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.translate(0, -radius + getPaddingTop() - progressWidth / 3);
        setBackground(new BitmapDrawable(getResources(), bitmap));
        setImageBitmap(bitmap);
    }

    //------------

    /***
     * 設置繪圖框架
     */
    private void initRect() {
        int left = progressWidth / 2 + getPaddingLeft();
        int right = left + 2 * radius;
        int top = viewHeight - radius - marginBottom;
        int bottom = viewHeight + radius - marginBottom;

        rect = new RectF(left, top, right, bottom);
    }


    //------------

    private int getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //取進位值
        return (int) (Math.ceil(fontMetrics.descent - fontMetrics.ascent) + 2);
    }
    //------------

    /***
     * 當遞增到區塊極值時 call這method進行區塊繪製
     */
    private void drawPart() {
        canvas.drawArc(rect, startAngle, progressCurrent, false, paint);
    }


    //------------

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (colors.length > 0) {
            canvas.translate(0, -radius + getPaddingTop() - progressWidth / 3);
            paint.setStrokeCap(Paint.Cap.BUTT);
            paint.setColor(getResources().getColor(colors[index]));
            //從180度 繪製到 當前數值
            canvas.drawArc(rect, startAngle, progressCurrent, false, paint);
        }

    }

    //---------

    /**
     * 開始動畫
     */
    private void startAnimation() {

        float percent = values[index] / totalValue;
        animator = ValueAnimator.ofFloat(0, percent * _CIRCLE);
        //設置時長
        animator.setDuration((int) (duration * percent));
        //設置加速器 等加速度
//        animator.setInterpolator(new AccelerateInterpolator());
        //設置加速器 等速度
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(this);
        animator.addListener(this);
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

    /***
     * 動畫開始
     * @param animation
     */
    @Override
    public void onAnimationStart(Animator animation) {
        isShouldRunOnHWLayer = shouldRunOnHWLayer(this, animator);
        if (isShouldRunOnHWLayer) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    /***
     * 動畫結束
     * @param animation
     */
    @Override
    public void onAnimationEnd(Animator animation) {
        //繼續下一階段動畫
        if (index < values.length - 1) {
            drawPart();
            index++;
            startAngle += progressCurrent;
            startAnimation();
        }
        //跑完所有動畫
        else {
            if (isShouldRunOnHWLayer) {
                setLayerType(View.LAYER_TYPE_NONE, null);
            }
            animator.removeUpdateListener(this);
            animator.removeListener(this);
        }
    }

    /***
     * 取消動畫
     * @param animation
     */
    @Override
    public void onAnimationCancel(Animator animation) {

    }

    /***
     * 動畫重啟
     * @param animation
     */
    @Override
    public void onAnimationRepeat(Animator animation) {

    }


    //--------

    private boolean shouldRunOnHWLayer(View view, Animator animator) {
        if (view == null || animator == null) {
            return false;
        }
        return view.getLayerType() == View.LAYER_TYPE_NONE
                && view.hasOverlappingRendering()
                && modifiesAlpha(animator);

    }


    //--------


    private boolean modifiesAlpha(Animator animator) {
        if (animator == null) {
            return false;
        }
        if (animator instanceof ValueAnimator) {
            ValueAnimator valueAnim = (ValueAnimator) animator;
            PropertyValuesHolder[] values = valueAnim.getValues();
            for (PropertyValuesHolder value : values) {
                if (("alpha").equals(value.getPropertyName())) {
                    return true;
                }
            }
        } else if (animator instanceof AnimatorSet) {
            List<Animator> animList = ((AnimatorSet) animator).getChildAnimations();
            for (int i = 0; i < animList.size(); i++) {
                if (modifiesAlpha(animList.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }


    //-------------

    /***
     * 設定各個區塊值
     */
    public BlockPieView setValues(float[] values) {
        this.values = values;
        return this;
    }


    //-------------

    /***
     * 設定各區塊對應顏色
     */
    public BlockPieView setColors(int[] colors) {
        this.colors = colors;
        return this;
    }
    //-----------

    /***
     * 動畫總時間 毫秒   預設一千豪秒
     * @param duration
     * @return
     */
    public BlockPieView setDuration(int duration) {
        this.duration = duration;
        return this;
    }


    //------------

    /***
     * 啟動
     */
    public void start() {
        if (colors.length > 0 && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                totalValue += values[i];
            }
            startAnimation();
        } else {
            Log.e(BlockPieView.class.getSimpleName(), "未設值或顏色");
        }
    }
}

