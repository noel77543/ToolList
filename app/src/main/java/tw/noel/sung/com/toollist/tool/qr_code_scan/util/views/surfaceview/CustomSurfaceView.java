package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.surfaceview;

import android.content.Context;
/**
 * Created by noel on 2019/2/16.
 */
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

import tw.noel.sung.com.toollist.tool.qr_code_scan.util.implement.OnQRCodeScanListener;


public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {


    //矩形長寬  px
    public static final int RANGE = 700;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Camera.PreviewCallback previewCallback;
    private Context context;
    private OnQRCodeScanListener onQRCodeScanListener;

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
                data, size.width, size.height, ((size.width - RANGE) / 2), ((size.height - RANGE) / 2), RANGE, RANGE, false);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        String content = getQRCodeResult(bitmap);
        if (content != null && onQRCodeScanListener != null) {
            onQRCodeScanListener.onQRCodeScanSuccess(content);
        }
    }

    //-------

    /***
     * 解析
     */
    private String getQRCodeResult(BinaryBitmap bitmap) {
        String text = null;
        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            text = result.getText();

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
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

}

