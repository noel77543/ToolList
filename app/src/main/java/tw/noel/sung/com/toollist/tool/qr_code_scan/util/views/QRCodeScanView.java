package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.implement.OnQRCodeScanListener;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover.CoverLayout;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.surfaceview.CustomSurfaceView;


/**
 * Created by noel on 2019/2/18.
 */
public class QRCodeScanView extends RelativeLayout implements View.OnClickListener {

    private Context context;
    private CustomSurfaceView customSurfaceView;
    private CoverLayout coverLayout;
    //手電筒是否開啟
    private boolean isFlushLightOn = false;

    //----------

    public QRCodeScanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QRCodeScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    //------

    private void init() {
        initSurfaceView();
        initCoverLayout();
        addView(customSurfaceView);
        addView(coverLayout);

        coverLayout.setOnCoverFlashButtonClickListener(this);
    }

    //--------

    private void initSurfaceView() {
        customSurfaceView = new CustomSurfaceView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(ALIGN_PARENT_TOP);
        params.addRule(ALIGN_PARENT_START);
        customSurfaceView.setLayoutParams(params);
    }

    //------
    private void initCoverLayout() {
        coverLayout = new CoverLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(ALIGN_PARENT_TOP);
        params.addRule(ALIGN_PARENT_START);
        coverLayout.setLayoutParams(params);
    }

    //-------

    /***
     * 設置標題
     * @param title
     */
    public void setTitle(String title) {
        coverLayout.setTitle(title);
    }

    //-------

    /***
     * 設置內文
     */
    public void setContent(String content) {
        coverLayout.setContent(content);
    }


    //-------

    /***
     * 顯示 / 隱藏 右上角『照片』
     */
    public void setPhotoButtonVisibility(int visibility) {
        coverLayout.setPhotoButtonVisibility(visibility);
    }


    //-------

    @Override
    public void onClick(View v) {
        Camera.Parameters parameters = customSurfaceView.getCamera().getParameters();

        String text;
        //開燈
        if (!isFlushLightOn) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            text = context.getString(R.string.open);
        }
        //關燈
        else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            text = context.getString(R.string.close);
        }
        coverLayout.setCoverFlashButtonText(text);
        isFlushLightOn = !isFlushLightOn;
        customSurfaceView.getCamera().setParameters(parameters);

    }

    //------

    /***
     * 釋放資源
     */

    public void release() {
        customSurfaceView.release();
    }


    //-------

    /***
     * 掃描成功監聽
     */
    public void setOnQRCodeScanListener(OnQRCodeScanListener onQRCodeScanListener) {
        customSurfaceView.setOnQRCodeScanListener(onQRCodeScanListener);
    }


    //------

    /***
     * 右上角照片按鈕監聽
     */
    public void setOnPhotoButtonClickListener(OnClickListener onClickListener) {
        coverLayout.setOnPhotoButtonClickListener(onClickListener);
    }

    //------

    /***
     * 停止preview
     */
    public void startPreView() {
        customSurfaceView.startPreView();
    }


    //------

    /***
     * 繼續preview
     */
    public void stopPreView() {
        customSurfaceView.stopPreView();
    }

    //------

    /***
     * 解析bitmap
     * @param bitmap
     */
    public void parseQRCodeResult(Bitmap bitmap) {
        customSurfaceView.parseQRCodeResult(bitmap);
    }
}