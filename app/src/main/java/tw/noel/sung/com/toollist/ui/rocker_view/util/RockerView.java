package tw.noel.sung.com.toollist.ui.rocker_view.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.rocker_view.util.implement.OnSwipeListener;

public class RockerView extends android.support.v7.widget.AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener, View.OnTouchListener {

    //東邊
    public static final int SWIPE_EVENT_EAST = 30;
    //東北
    public static final int SWIPE_EVENT_EAST_NORTH = 31;
    //北
    public static final int SWIPE_EVENT_NORTH = 32;
    //西北
    public static final int SWIPE_EVENT_WEST_NORTH = 33;
    //西
    public static final int SWIPE_EVENT_WEST = 34;
    //西南
    public static final int SWIPE_EVENT_WEST_SOUTH = 35;
    //南
    public static final int SWIPE_EVENT_SOUTH = 36;
    //東南
    public static final int SWIPE_EVENT_EAST_SOUTH = 37;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SWIPE_EVENT_EAST, SWIPE_EVENT_EAST_NORTH, SWIPE_EVENT_NORTH, SWIPE_EVENT_WEST_NORTH,
            SWIPE_EVENT_WEST, SWIPE_EVENT_WEST_SOUTH, SWIPE_EVENT_SOUTH, SWIPE_EVENT_EAST_SOUTH})
    public @interface SwipeEvent {
    }


    private final int[] _SWIPE_EVENTS = {SWIPE_EVENT_SOUTH, SWIPE_EVENT_EAST_SOUTH, SWIPE_EVENT_EAST,
            SWIPE_EVENT_EAST_NORTH, SWIPE_EVENT_NORTH, SWIPE_EVENT_WEST_NORTH, SWIPE_EVENT_WEST, SWIPE_EVENT_WEST_SOUTH};

    private final int LINE_WIDTH = 5;
    //一個圓的角度
    private final double _CIRCLE = 360;
    //切分八個相位
    private final double _INTERVAL_ANGLE = _CIRCLE / 8;

    private OnSwipeListener onSwipeListener;
    private Context context;
    // 外圓
    private Canvas canvasOuter;
    private Bitmap bitmapOuter;
    private float radiusOuter;
    private Paint paintOuter;
    //預設外圓線條顏色
    private final String DEFAULT_CIRCLE_OUTER_LINE_COLOR = "#000000";
    //預設外圓顏色
    private final String DEFAULT_CIRCLE_OUTER_COLOR = "#FFFFFF";
    private int outerCircleLineColor;
    private int outerCircleColor;

    // 內圓
    private Canvas canvasInner;
    private Bitmap bitmapInner;
    private float radiusInner;
    private Paint paintInner;
    //預設內圓線條顏色
    private final String DEFAULT_CIRCLE_INNER_LINE_COLOR = "#000000";
    //預設內圓顏色
    private final String DEFAULT_CIRCLE_INNER_COLOR = "#FFFFFF";
    private int innerCircleLineColor;
    private int innerCircleColor;


    /***
     *  概念：
     *                180
     *
     *                 |
     *                 |
     *                 |
     *                 |
     *  270  ----------|----------  90
     *                 |
     *                 |
     *                 |
     *                 |
     *
     *                 0
     */


    public RockerView(Context context) {
        this(context, null);
    }

    public RockerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(attrs, defStyleAttr);
        init();
    }

    //-----------------

    private void initAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RockerView, defStyleAttr, 0);
        radiusInner = typedArray.getDimension(R.styleable.RockerView_rockerViewInnerCircleRadius, 0);
        outerCircleLineColor = typedArray.getColor(R.styleable.RockerView_rockerViewOuterCircleLineColor, Color.parseColor(DEFAULT_CIRCLE_OUTER_LINE_COLOR));
        outerCircleColor = typedArray.getColor(R.styleable.RockerView_rockerViewOuterCircleColor, Color.parseColor(DEFAULT_CIRCLE_OUTER_COLOR));
        innerCircleLineColor = typedArray.getColor(R.styleable.RockerView_rockerViewInnerCircleLineColor, Color.parseColor(DEFAULT_CIRCLE_INNER_LINE_COLOR));
        innerCircleColor = typedArray.getColor(R.styleable.RockerView_rockerViewInnerCircleColor, Color.parseColor(DEFAULT_CIRCLE_INNER_COLOR));
        typedArray.recycle();
    }


    //-----------------

    private void init() {
        paintOuter = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintInner = new Paint(Paint.ANTI_ALIAS_FLAG);

        getViewTreeObserver().addOnGlobalLayoutListener(this);
        setOnTouchListener(this);
    }

    //-----------------

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= 16) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }

        int viewWidth = getWidth();
        int viewHeight = getHeight();
        //布局寬高較小者的一半 為外圓半徑
        radiusOuter = viewWidth < viewHeight ? viewWidth / 2 : viewHeight / 2;
        radiusInner = radiusInner <= 0 || radiusInner > radiusOuter ? radiusOuter / 3 : radiusOuter;


        bitmapOuter = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        canvasOuter = new Canvas(bitmapOuter);
        setBackground(new BitmapDrawable(getResources(), bitmapOuter));
        drawOuterCircle();


        bitmapInner = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        canvasInner = new Canvas(bitmapInner);
        setImageBitmap(bitmapInner);
        drawInnerCircle(radiusOuter, radiusOuter);
    }
    //-------------

    /***
     *  繪製外圓
     */
    private void drawOuterCircle() {
        paintOuter.setStyle(Paint.Style.STROKE);
        paintOuter.setStrokeWidth(LINE_WIDTH / 2);
        paintOuter.setColor(outerCircleLineColor);
        canvasOuter.drawCircle(radiusOuter, radiusOuter, radiusOuter, paintOuter);
        paintOuter.setStyle(Paint.Style.FILL);
        paintOuter.setColor(outerCircleColor);
        canvasOuter.drawCircle(radiusOuter, radiusOuter, radiusOuter - LINE_WIDTH, paintOuter);
    }
    //-------------

    /***
     *  繪製內圓
     */
    private void drawInnerCircle(float centerX, float centerY) {
        canvasInner.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paintInner.setStyle(Paint.Style.STROKE);
        paintInner.setStrokeWidth(LINE_WIDTH / 2);
        paintInner.setColor(innerCircleLineColor);
        canvasInner.drawCircle(centerX, centerY, radiusInner, paintInner);
        paintInner.setStyle(Paint.Style.FILL);
        paintInner.setColor(innerCircleColor);
        canvasInner.drawCircle(centerX, centerY, radiusInner - LINE_WIDTH, paintInner);
        invalidate();
    }


    //------------

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (onSwipeListener != null) {
                    onSwipeListener.onStartSwipe();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //觸摸點為內圓圓心 該點距離應小於外圓半徑-內圓半徑
                if (getDistanceOfCenter(x, y) < radiusOuter - radiusInner) {
                    drawInnerCircle(x, y);


                    if (onSwipeListener != null) {
                        double angle = getAngleOfCenter(x, y);
                        onSwipeListener.onSwiping(getSwipeEvent(angle), angle);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                drawInnerCircle(radiusOuter, radiusOuter);
                if (onSwipeListener != null) {
                    onSwipeListener.onSwiped();
                }
                break;
        }
        return true;
    }

    //------------

    /***
     * 取得與圓心的距離
     * @return
     */
    private double getDistanceOfCenter(float x, float y) {
        return Math.sqrt(Math.pow((y - radiusOuter), 2) + Math.pow((x - radiusOuter), 2));
    }

    //-------------

    /***
     * 取得觸摸點與圓心的夾角
     */
    private double getAngleOfCenter(float x, float y) {

        double angle = (Math.atan2(x - radiusOuter, y - radiusOuter) * _CIRCLE / 2 / Math.PI);
        return angle < 0 ? angle + _CIRCLE : angle;
    }

    //------------

    /***
     * 取得事件
     */
    private @SwipeEvent
    int getSwipeEvent(double angle) {

        for (int i = 0; i < _SWIPE_EVENTS.length; i++) {
            if (angle < i * _INTERVAL_ANGLE) {
                return _SWIPE_EVENTS[i];
            }
        }
        return _SWIPE_EVENTS[0];
    }


    //------------

    /***
     *  拖動方向監聽
     * @param onSwipeListener
     */
    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

}
