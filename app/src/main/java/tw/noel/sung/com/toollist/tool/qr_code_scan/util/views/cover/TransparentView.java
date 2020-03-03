package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.content.Context;
/**
 * Created by noel on 2019/2/16.
 */
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.appcompat.widget.AppCompatImageView;

import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.surfaceview.CustomSurfaceView;

public class TransparentView extends AppCompatImageView {


    private int lineLength;
    private Paint paint;
    private Path path;
    private final String ANGLE_COLOR = "#3d88ed";
    private final int STROKE_WIDTH = 14;
    private final float PATH_RADIUS = 30;

    public TransparentView(Context context) {
        super(context);
        init();
    }

    //-----------
    private void init() {

        path = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setPathEffect(new CornerPathEffect(PATH_RADIUS));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(ANGLE_COLOR));
    }

    //-----------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        lineLength = (int) (CustomSurfaceView.range * 0.09);
        drawAngle(canvas);
    }


    //-------------

    /***
     * 左上角 右上角 左下角 右下角 繪製
     * @param canvas
     */
    private void drawAngle(Canvas canvas) {


        int offset = (int) (STROKE_WIDTH * 0.45);


        //左上角
        path.moveTo(lineLength, offset);
        path.lineTo(offset, offset);
        path.lineTo(offset, lineLength);


        //左下角
        path.moveTo(offset, CustomSurfaceView.range - lineLength);
        path.lineTo(offset, CustomSurfaceView.range - offset);
        path.lineTo(lineLength, CustomSurfaceView.range - offset);

        //右下角
        path.moveTo(CustomSurfaceView.range - lineLength, CustomSurfaceView.range - offset);
        path.lineTo(CustomSurfaceView.range - offset, CustomSurfaceView.range - offset);
        path.lineTo(CustomSurfaceView.range - offset, CustomSurfaceView.range - lineLength);

        //右上角
        path.moveTo(CustomSurfaceView.range - offset, lineLength);
        path.lineTo(CustomSurfaceView.range - offset, offset);
        path.lineTo(CustomSurfaceView.range - lineLength, offset);

        canvas.drawPath(path, paint);
    }
}
