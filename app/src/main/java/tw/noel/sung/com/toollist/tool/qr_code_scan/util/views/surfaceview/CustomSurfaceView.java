package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

import tw.noel.sung.com.toollist.tool.qr_code_scan.util.implement.OnQRCodeScanListener;


public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback, ViewTreeObserver.OnGlobalLayoutListener {


    //矩形長寬  px
    public static int range;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Camera.PreviewCallback previewCallback;
    private Context context;
    private OnQRCodeScanListener onQRCodeScanListener;

    private final int MAX_PIXEL = 160000;
    private final int MAX_MAGNIFICATION = 50;

    public CustomSurfaceView(Context context) {
        super(context);
        this.context = context;

        init();
    }


    //---------

    /**
     * 開啟相機
     * ps 注意權限 camera
     */
    public void init() {

        previewCallback = this;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    //---------

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        camera.setDisplayOrientation(getDisplayOrientation());
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //--------

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        camera.setPreviewCallback(previewCallback);
        Camera.Parameters parameters = camera.getParameters();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        camera.setParameters(parameters);
        camera.startPreview();
    }


    //--------

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    //--------

    /***
     * start preview callback
     * @param data
     * @param camera
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        Camera.Size size = camera.getParameters().getPreviewSize();
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                data, size.width, size.height, ((size.width - range) / 2), ((size.height - range) / 2), range, range, false);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        String content = getQRCodeResult(bitmap);
        if (content != null && onQRCodeScanListener != null) {
            onQRCodeScanListener.onQRCodeScanSuccess(content);
        }
    }

    //------

    /***
     * 解析Bitmap
     */
    public void parseQRCodeResult(Bitmap bitmap) {

        bitmap = getSmallerBitmap(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];


        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width, height, pixels);

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));

        String content = getQRCodeResult(binaryBitmap);

        if (content != null && onQRCodeScanListener != null) {
            onQRCodeScanListener.onQRCodeScanSuccess(content);
        }
    }

    //---------

    /***
     * 壓縮圖片 否則在大圖會有OOM的可能
     * @param bitmap
     * @return
     */
    private Bitmap getSmallerBitmap(Bitmap bitmap) {


        //如果畫素低於MAX_PIXEL 則直接使用原bitmap
        if (bitmap.getWidth() * bitmap.getHeight() <= MAX_PIXEL) {
            return bitmap;
        }

        int size = bitmap.getWidth() * bitmap.getHeight() / MAX_PIXEL;
        size = size > MAX_MAGNIFICATION ? MAX_MAGNIFICATION : size;

        //否則等比例縮小後回傳
        Matrix matrix = new Matrix();
        matrix.postScale((float) (1 / Math.sqrt(size)), (float) (1 / Math.sqrt(size)));
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //------------

    /***
     * 解析
     */
    private String getQRCodeResult(BinaryBitmap bitmap) {
        String text = null;

        try {

            Result result = new MultiFormatReader().decode(bitmap);
            text = result.getText();

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        Log.e("AAA", text + "");
        return text;
    }


    //-------

    /***
     * 相機預覽的旋轉角度需要根據相機預覽目前的旋轉角度，以及屏幕的旋轉角度計算得到
     * @return
     */
    private int getDisplayOrientation() {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        Camera.CameraInfo camInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        return (camInfo.orientation - degrees + 360) % 360;
    }


    //----------

    /***
     * 取得相機實例
     * @return
     */
    public Camera getCamera() {
        return camera;
    }

    //------

    /***
     * 釋放資源
     */

    public void release() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    //-------

    /***
     * 掃描成功監聽
     */
    public void setOnQRCodeScanListener(OnQRCodeScanListener onQRCodeScanListener) {
        this.onQRCodeScanListener = onQRCodeScanListener;
    }

    //-------

    /***
     * 開始preview
     */
    public void startPreView() {
        if (camera != null) {
            camera.startPreview();
        }
    }

    //------

    /***
     * 停止preview
     */
    public void stopPreView() {
        if (camera != null) {
            camera.stopPreview();
        }
    }

    //------


    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        range = (int) (getWidth() * 0.6);
    }
}

