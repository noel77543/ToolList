package tw.noel.sung.com.toollist.tool.connect.util.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class ZLoadingView extends android.support.v7.widget.AppCompatImageView {

    private final String INNER_CIRCLE_COLOR = "#FFFFFF";
    private final String OUTTER_CIRCLE_COLOR = "#000000";

    private final int SIZE = 100;

    private Paint paint;
    private Canvas canvas;
    private Canvas backgroundCanvas;

    private int viewWidth;
    private int viewHeight;

    private int radius;
    private Bitmap bitmap;
    private Bitmap backgroundBitmap;
    private Context context;

    public ZLoadingView(Context context) {
        this(context, null);
    }

    public ZLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        init();
    }
    //------------

    private void init() {
        setLayoutParams(new ViewGroup.LayoutParams(SIZE, SIZE));
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

        radius = viewWidth / 2;

        bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        backgroundBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        backgroundCanvas = new Canvas(backgroundBitmap);
        setBackground(new BitmapDrawable(getResources(), backgroundBitmap));
        canvas = new Canvas(bitmap);
        setImageBitmap(bitmap);

        drawInnerCircle();
        drawOuterCircle();
        startRotateAnimation();
    }

    //-------------------

    /***
     * 繪製內部三個圓型
     */
    private void drawInnerCircle() {
        int innerRadius = radius / 5;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(INNER_CIRCLE_COLOR));
        canvas.drawCircle(radius, (int) (innerRadius * 1.5), innerRadius, paint);
        canvas.drawCircle(radius * 2 - innerRadius * 2, innerRadius * 7, innerRadius, paint);
        canvas.drawCircle(innerRadius * 2, innerRadius * 7, innerRadius, paint);
    }

    //-----------

    /***
     * 繪製外部圓型 及 圓環
     */
    private void drawOuterCircle() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(OUTTER_CIRCLE_COLOR));
        backgroundCanvas.drawCircle(radius, radius, radius, paint);
    }

    //-------------------

    /***
     * 設置旋轉動畫
     */
    private void startRotateAnimation() {

        Animation animation = new RotateAnimation(0, 360,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        startAnimation(animation);
    }
}
