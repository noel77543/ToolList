package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.graphics.Canvas;
/**
 * Created by noel on 2019/2/16.
 */
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.surfaceview.CustomSurfaceView;


public class CoverDrawable extends Drawable {
    private Paint paint;
    private Path path;

    private Drawable innerDrawable;


    public CoverDrawable(Drawable innerDrawable) {
        this.innerDrawable = innerDrawable;
        path = new Path();
        paint = new Paint();

        path.addRect(0, 0, CustomSurfaceView.RANGE, CustomSurfaceView.RANGE, Path.Direction.CW);
    }

    //-------

    public void setSrcPath(Path path) {
        this.path = path;
    }
    //-------

    @Override
    public void draw(@NonNull Canvas canvas) {
        innerDrawable.setBounds(getBounds());
        if (path == null || path.isEmpty()) {
            innerDrawable.draw(canvas);
        } else {
            //將繪製操作保存到新的圖層，因為圖像合成是很昂貴的操作，將用到硬件加速，這裡將圖像合成的處理放到離屏緩存中進行
            int saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), paint, Canvas.ALL_SAVE_FLAG);

            //dst 繪製目標圖
            innerDrawable.draw(canvas);

            //設置混合模式
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            //src 繪製源圖
            canvas.drawPath(path, paint);
            //清除混合模式
            paint.setXfermode(null);
            //還原畫布
            canvas.restoreToCount(saveCount);
        }
    }

    //---------

    @Override
    public void setAlpha(int alpha) {
        innerDrawable.setAlpha(alpha);
    }
    //---------

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        innerDrawable.setColorFilter(colorFilter);
    }
    //---------

    @Override
    public int getOpacity() {
        return innerDrawable.getOpacity();
    }
}
