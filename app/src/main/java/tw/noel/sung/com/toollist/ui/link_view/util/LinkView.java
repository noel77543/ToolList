package tw.noel.sung.com.toollist.ui.link_view.util;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.link_view.util.implement.OnDrawLineFinishedListener;
import tw.noel.sung.com.toollist.ui.link_view.util.model.LinkPoint;


/**
 * Created by noel on 2018/4/24.
 */
public class LinkView extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    private int lineColor;
    private int pointOuterColor;
    private int pointInnerColor;

    //預設線條顏色
    private final String DEFAULT_LINE_COLOR = "#000000";
    //預設點 外圍顏色
    private final String DEFAULT_POINT_OUTER_COLOR = "#000000";
    //預設點 內圍顏色
    private final String DEFAULT_POINT_INNER_COLOR = "#000000";
    //預設  3 x 3
    private final int DEFAULT_POINT_COUNT = 3;
    //是否震動  預設不震動
    private boolean vibrateEnable;
    //所有點
    private ArrayList<LinkPoint> points;
    //觸發的點
    private ArrayList<LinkPoint> linkPoints;
    private int linked = 0;
    private Vibrator vibrator;
    //預設寬度100px
    private int viewWidth = 100;
    //預設高度100px
    private int viewHeight = 100;
    //預設總點數 3*3
    private int pointCount;
    //線條寬度
    private final int LINE_WIDTH = 10;
    //點的外圓半徑
    private final int OUTER_POINT_RADIUS = LINE_WIDTH * LINE_WIDTH / 3;
    //點的內圓半徑
    private final int INNER_POINT_RADIUS = OUTER_POINT_RADIUS / 3;

    private Bitmap bitmap;
    private Canvas canvasBackGround;
    private Canvas canvas;
    private Paint paint;
    private Path path;

    //用來裝設定的密碼
    private StringBuilder stringBuilder;
    private OnDrawLineFinishedListener onDrawLineFinishedListener;
    private Context context;


    public LinkView(Context context) {
        this(context, null);
    }

    public LinkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(attrs, defStyleAttr);
        init();
    }
    //----------

    /***
     *
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LinkView, defStyleAttr, 0);
        lineColor = typedArray.getColor(R.styleable.LinkView_linkViewLineColor, Color.parseColor(DEFAULT_LINE_COLOR));
        pointOuterColor = typedArray.getColor(R.styleable.LinkView_linkViewPointOuterColor, Color.parseColor(DEFAULT_POINT_OUTER_COLOR));
        pointInnerColor = typedArray.getColor(R.styleable.LinkView_linkViewPointInnerColor, Color.parseColor(DEFAULT_POINT_INNER_COLOR));
        pointCount = typedArray.getInteger(R.styleable.LinkView_linkViewPointCount, DEFAULT_POINT_COUNT);
        vibrateEnable = typedArray.getBoolean(R.styleable.LinkView_linkViewVibrate, false);
        typedArray.recycle();
    }

    //----------

    /***
     * init...
     */
    private void init() {
        points = new ArrayList<>();
        linkPoints = new ArrayList<>();
        stringBuilder = new StringBuilder();
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);

        initVibrator();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    //------------

    /***
     *  振動器初始化
     */
    private void initVibrator() {
        if (vibrateEnable) {
            vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        }
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
        bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        canvasBackGround = new Canvas(bitmap);
        setBackground(new BitmapDrawable(getResources(), bitmap));

        addPoints();

        bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint.setStyle(Paint.Style.STROKE);
        setImageBitmap(bitmap);

        setOnTouchListener(this);
    }

    //----------------

    /***
     *  加入點
     */
    private void addPoints() {

        paint.setStyle(Paint.Style.FILL);
        //線條寬度
        paint.setStrokeWidth(LINE_WIDTH);


        //畫點並且 將點位加入Arraylist中
        for (int y = (OUTER_POINT_RADIUS); y <= viewHeight - (OUTER_POINT_RADIUS); y += (viewHeight / (pointCount - 1)) - (OUTER_POINT_RADIUS)) {
            for (int x = (OUTER_POINT_RADIUS); x <= viewWidth - (OUTER_POINT_RADIUS); x += (viewWidth / (pointCount - 1)) - (OUTER_POINT_RADIUS)) {
                points.add(new LinkPoint(x, y));
                paint.setColor(pointOuterColor);
                canvasBackGround.drawCircle(x, y, (float) OUTER_POINT_RADIUS, paint);
                paint.setColor(pointInnerColor);
                canvasBackGround.drawCircle(x, y, (float) INNER_POINT_RADIUS, paint);
            }
        }
    }

    //-------------------

    /***
     *  檢查手指位置 是否觸及點位
     */
    private void checkPoint(int eventX, int eventY) {
        for (int i = 0; i < points.size(); i++) {
            int linkX = points.get(i).getPointX();
            int linkY = points.get(i).getPointY();


            //可允許的誤差偏移為50px 且 該點未加入連線儲備點位
            if (Math.sqrt((Math.pow(linkX - eventX, 2)) + (Math.pow(linkY - eventY, 2))) < 50 && linkPoints.indexOf(points.get(i)) == -1) {
                //將線條的項次加入string builder
                stringBuilder.append(i + "");
                //add進ArrayList<LinkPoint> 以便判斷是否已經滑過該點
                linkPoints.add(points.get(i));
                if (vibrateEnable && vibrator != null) {
                    vibrator.vibrate(50);
                }
            }
            //每一次都是兩個點彼此連線
            if (linkPoints.size() > 1 && linkPoints.size() > linked) {
                paint.setColor(lineColor);
                path.moveTo(linkPoints.get(linked).getPointX(), linkPoints.get(linked).getPointY());
                path.lineTo(linkPoints.get(linkPoints.size() - 1).getPointX(), linkPoints.get(linkPoints.size() - 1).getPointY());

                canvasBackGround.drawPath(path, paint);
                invalidate();

                //最後紀錄目前畫到第幾點了
                linked = linkPoints.size() - 1;
            }
        }

        if (linkPoints.size() > 0) {
            clearCanvas(canvas);
            paint.setColor(lineColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(linkPoints.get(linkPoints.size() - 1).getPointX(), linkPoints.get(linkPoints.size() - 1).getPointY(), eventX, eventY, paint);
            invalidate();
        }
    }
    //-------------------

    /***
     * 清空畫布
     */
    private void clearCanvas(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();
    }


    //-----------------

    /***
     *  清空路徑 並提出輸入值
     */
    private void drawFinished() {
        clearCanvas(canvas);
        if (onDrawLineFinishedListener != null && stringBuilder.length() > 0) {
            onDrawLineFinishedListener.onDrawLineFinished(stringBuilder.toString());
            stringBuilder.delete(0, stringBuilder.length());
        }
    }
    //--------------

    /***
     *  畫線
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            //手指下去時 如果是點位 則開始畫線 不是則return true 不讓事件下去下層
            case MotionEvent.ACTION_DOWN:
                if (linkPoints.size() > 0) {
                    clearCanvas(canvasBackGround);
                    //每次落下 先清空
                    linkPoints.clear();
                    points.clear();
                    linked = 0;
                    path.reset();

                    addPoints();
                    invalidate();
                }

                //滑動時 如果是點位 進行畫線
            case MotionEvent.ACTION_MOVE:
                checkPoint((int) event.getX(), (int) event.getY());
                break;
            //抬起時確認是否正確
            case MotionEvent.ACTION_UP:
                drawFinished();
                break;
        }
        return true;
    }


    //---------------

    /***
     *  對外接口
     * @param onDrawLineFinishedListener
     */
    public void setOnDrawLineFinishedListener(OnDrawLineFinishedListener onDrawLineFinishedListener) {
        this.onDrawLineFinishedListener = onDrawLineFinishedListener;
    }

    //----------

    /***
     *  啟動振動器
     */
    public void setVibrateEnable(boolean vibrateEnable) {
        this.vibrateEnable = vibrateEnable;
        initVibrator();
    }
}
