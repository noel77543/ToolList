package tw.noel.sung.com.toollist.ui.loterry_view.util;

import android.content.Context;
/**
 * Created by noel on 2019/3/24.
 */
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.loterry_view.util.implement.OnScratchListener;

public class LotteryView extends android.support.v7.widget.AppCompatImageView {

    //預設刮刮樂線條寬度
    private final int DEFAULT_SCRATCH_SIZE = 3;
    private int scratchSize = DEFAULT_SCRATCH_SIZE;

    //前景畫布
    private Canvas foregroundCanvas;
    private Bitmap foregroundBitmap;

    private Path path;
    private Paint paint;
    private Context context;
    private boolean isSetCoverFinish = false;
    private Bitmap bitmap;

    private int[] pixels;
    //目前刮除的pixel
    private float drawArea = 0;
    //整個LotteryView的的Pixel
    private float totalArea = 0;
    private int viewHeight;
    private int viewWidth;
    private OnScratchListener onScratchListener;
    //預設完全刮除才觸發完成刮刮樂接口
    private int scratchedPercent = 100;
    //當finish時不允許繼續刮刮樂
    private boolean isScratch = true;

    public LotteryView(Context context) {
        this(context, null);
    }

    public LotteryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LotteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(attrs, defStyleAttr);
        init();
    }

    //-----------

    private void initAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LotteryView, defStyleAttr, 0);
        scratchSize = typedArray.getDimensionPixelSize(R.styleable.LotteryView_lotteryViewScratchSize, DEFAULT_SCRATCH_SIZE);
    }

    //-----------

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(scratchSize);
        paint.setAlpha(0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }


    //------------

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        foregroundBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        foregroundCanvas = new Canvas(foregroundBitmap);
    }

    //------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isSetCoverFinish) {
            bitmap = Bitmap.createScaledBitmap(bitmap, foregroundBitmap.getWidth(), foregroundBitmap.getHeight(), false);
            foregroundCanvas.drawBitmap(bitmap, 0, 0, null);
            isSetCoverFinish = true;
        }
        canvas.drawBitmap(foregroundBitmap, 0, 0, null);
        foregroundCanvas.drawPath(path, paint);


        viewWidth = getWidth();
        viewHeight = getHeight();
        if (onScratchListener != null) {
            if (scratchedPercent == getDrawAreaPercent()) {
                isScratch = false;
                onScratchListener.OnScratchFinish();
            }
        }
    }

    //--------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (foregroundCanvas != null && isScratch) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.reset();
                    path.moveTo(x, y);
                    invalidate();
                    break;

                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x, y);
                    if (onScratchListener != null) {
                        onScratchListener.OnScratching();
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    foregroundCanvas.drawPath(path, paint);
                    invalidate();
                    break;
            }
        }
        return true;
    }

    //--------------

    /***
     *  清空上層圖片
     *  自行決定是否當finish時使用
     */
    public void clear() {
        foregroundCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }


    //--------------

    /***
     *  取得繪製的百分比
     */
    private int getDrawAreaPercent() {
        drawArea = 0;
        totalArea = viewWidth * viewHeight;

        pixels = new int[viewWidth * viewHeight];

        // 獲取所有像素的資訊
        foregroundBitmap.getPixels(pixels, 0, viewWidth, 0, 0, viewWidth, viewHeight);
        // 統計所有刮開的區域
        for (int i = 0; i < viewWidth; i++) {
            for (int j = 0; j < viewHeight; j++) {
                int index = i + j * viewWidth;
                if (pixels[index] == 0) {
                    drawArea++;
                }
            }
        }
        Log.e("AA", ((int) (drawArea * 100 / totalArea)) + "");
        return (int) (drawArea * 100 / totalArea);
    }

    //------------

    /***
     * 設置遮擋於獎品前的刮刮view
     */
    public void setScratchImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }
    //-----------

    /***
     * 設置被遮擋的獎品圖示
     */
    public void setRewardImage(Bitmap bitmap) {
        setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    //-----------

    /***
     *  對外接口
     *  @param onScratchListener
     *  @param scratchedPercent  刮除的百分比
     */
    public void setOnScratchListener(int scratchedPercent, OnScratchListener onScratchListener) {
        this.scratchedPercent = scratchedPercent;
        this.onScratchListener = onScratchListener;
    }
}
