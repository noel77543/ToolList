package tw.noel.sung.com.toollist.ui.wave_progress_view.util;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import tw.noel.sung.com.toollist.R;

public class RoundProgressView extends android.support.v7.widget.AppCompatImageView implements ValueAnimator.AnimatorUpdateListener {

    //預設- 動畫時間
    private final int DEFAULT_ANIMATION_TIME = 3000;
    //預設- 外層線條顏色
    private final String DEFAULT_OUT_SIDE_LINE_COLOR = "#FF00BBFF";
    //預設- 外層線條寬度
    private final int DEFAULT_OUT_SIDE_LINE_WIDTH = 35;

    //預設- 內部顯示百分比的圓背景色
    private final String DEFAULT_INNER_CIRCLE_COLOR = "#8DE2E2E2";
    //預設- 內部圓顯示百分比的文字顏色
    private final String DEFAULT_INNER_CIRCLE_PROGRESS_TEXT_COLOR = "#FFFFFFFF";

    //預設- 進度圓的背景色
    private final String DEFAULT_PROGRESS_CIRCLE_COLOR = "#FFFF4081";


    private int animationTime;
    private int outsideLineColor;
    private int outsideLineWidth;

    private int innerCircleColor;
    private int innerCircleProgressTextColor;

    private int progressCircleColor;

    private Context context;
    private Paint backgroundPaint;
    private Paint textPaint;
    private Paint paint;

    //最外層圓的半徑
    private int outsideCircleRadius;
    //進度圓的半徑
    private int radius;
    //內部文字圓的半徑
    private int innerRadius;

    private Canvas canvas;
    private Canvas backgroundCanvas;
    private Bitmap bitmap;

    //View寬度
    private int viewWidth;
    //View高度
    private int viewHeight;
    //進度圓的圓心點
    private PointF pointF;
    //進度圓的框架
    private RectF rectF;
    //進度圓的繪製路徑
    private Path path;
    //總數
    private float total;
    //當前
    private float current;


    //波浪路徑
    private Path wavePath;
    //波浪長度
    private int waveWidth;
    //波浪數量
    private int mWaveCount;

    //-------------
    public RoundProgressView(Context context) {
        this(context, null);
    }

    public RoundProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(attrs, defStyleAttr);
        init();
    }


    //--------------

    private void initAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundProgressView, defStyleAttr, 0);

        animationTime = typedArray.getDimensionPixelSize(R.styleable.RoundProgressView_roundProgressViewAnimationTime, DEFAULT_ANIMATION_TIME);
        outsideLineColor = typedArray.getColor(R.styleable.RoundProgressView_roundProgressViewOutsideLineColor, Color.parseColor(DEFAULT_OUT_SIDE_LINE_COLOR));
        outsideLineWidth = typedArray.getDimensionPixelSize(R.styleable.RoundProgressView_roundProgressViewOutsideLineWidth, DEFAULT_OUT_SIDE_LINE_WIDTH);
        innerCircleColor = typedArray.getColor(R.styleable.RoundProgressView_roundProgressViewInnerCircleColor, Color.parseColor(DEFAULT_INNER_CIRCLE_COLOR));
        innerCircleProgressTextColor = typedArray.getColor(R.styleable.RoundProgressView_roundProgressViewInnerCircleProgressTextColor, Color.parseColor(DEFAULT_INNER_CIRCLE_PROGRESS_TEXT_COLOR));
        progressCircleColor = typedArray.getColor(R.styleable.RoundProgressView_roundProgressViewProgressCircleColor, Color.parseColor(DEFAULT_PROGRESS_CIRCLE_COLOR));
        typedArray.recycle();
    }


    //------------

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointF = new PointF();
        rectF = new RectF();
        path = new Path();
        wavePath = new Path();
    }

    //------------

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        //使維持正方形
        if (viewWidth > viewHeight) {
            viewWidth = viewHeight;
        } else {
            viewHeight = viewWidth;
        }
        setMeasuredDimension(viewWidth, viewHeight);

        outsideCircleRadius = (viewWidth - outsideLineWidth / 2) / 2;
        radius = outsideCircleRadius - outsideLineWidth / 2;
        innerRadius = radius / 2;

        bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        backgroundCanvas = new Canvas(bitmap);
        setBackground(new BitmapDrawable(getResources(), bitmap));
        canvas = new Canvas(bitmap);
        setImageBitmap(bitmap);

        drawOutsideCircle();
    }

    //----------------

    /***
     * 繪製外層圓
     */
    private void drawOutsideCircle() {
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setColor(outsideLineColor);
        backgroundPaint.setShader(null);
        backgroundCanvas.drawCircle(outsideCircleRadius, outsideCircleRadius, outsideCircleRadius, backgroundPaint);
    }


    //----------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawProgressCircle(current);
        drawProgressText(current);
        drawInnerCircle();
    }
    //----------------

    /***
     * 繪製內層圓
     */
    private void drawInnerCircle() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(innerCircleColor);
        canvas.drawCircle(outsideCircleRadius, outsideCircleRadius, innerRadius, paint);

    }

    //-----------------

    /***
     * 繪製當前進度圓
     */
    private void drawProgressCircle(float value) {
        pointF.x = outsideCircleRadius;
        pointF.y = outsideCircleRadius;
        rectF.set(pointF.x - radius, pointF.y - radius, pointF.x + radius, pointF.y + radius);

        float y = pointF.y + radius - (2 * radius * value / (total - 1));
        float x = pointF.x - (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(y - pointF.y, 2));

        float angle = (float) Math.toDegrees(Math.atan((pointF.y - y) / (x - pointF.x)));
        //起點 由下
        float startAngle = 180 - angle;
        //終點 至上
        float sweepAngle = 2 * angle - 180;

        path.rewind();
        path.addArc(rectF, startAngle, sweepAngle);
        path.close();


        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(progressCircleColor);
        backgroundCanvas.drawPath(path, backgroundPaint);
    }

    //-----------------

    /***
     * 寫出當前進度百分比
     */
    private void drawProgressText(float current) {
        //取小數第二位
        String percent = new BigDecimal((double) (current / total * 100)).setScale(2, RoundingMode.HALF_UP).toString() + "%";

        textPaint.setTextSize(innerRadius / 2);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(innerCircleProgressTextColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(percent, canvas.getWidth() / 2, (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)), textPaint);
    }
    //-----------------

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        current = (float) animation.getAnimatedValue();
        invalidate();
    }

    //-----------------

    /***
     * 設置進度
     */
    public void setProgress(float target, float total) {
        if (target <= total) {
            this.total = total;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, target);
            //設置時長
            valueAnimator.setDuration(animationTime);
            //設置加速器
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.addUpdateListener(this);
            valueAnimator.start();
        } else {
            Log.e(RoundProgressView.class.getSimpleName(), "total不得小於target");
        }
    }
}