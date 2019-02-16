package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views;

import android.content.Context;
/**
 * Created by noel on 2019/2/16.
 */
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.implement.OnQRCodeScanListener;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover.CoverLayout;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.surfaceview.CustomSurfaceView;


public class QRCodeScanSurfaceView extends RelativeLayout implements View.OnClickListener {

    private Context context;
    private CustomSurfaceView customSurfaceView;
    private CoverLayout coverLayout;
    //手電筒是否開啟
    private boolean isFlushLightOn = false;
    //----------

    public QRCodeScanSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QRCodeScanSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        coverLayout.setOnCoverButtonClickListener(this);
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

    @Override
    public void onClick(View v) {
        Camera.Parameters parameters = customSurfaceView.getCamera().getParameters();

        String status;
        //開燈
        if (!isFlushLightOn) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            status = context.getString(R.string.open);
        }
        //關燈
        else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            status = context.getString(R.string.close);
        }
        coverLayout.setCoverButtonText(status);
        isFlushLightOn = !isFlushLightOn;
        customSurfaceView.getCamera().setParameters(parameters);

    }

    //------

    /***
     * 釋放資源
     */

    public void release(){
        customSurfaceView.release();
    }


    //-------

    /***
     * 掃描成功監聽
     */
    public void setOnQRCodeScanListener(OnQRCodeScanListener onQRCodeScanListener) {
        customSurfaceView.setOnQRCodeScanListener(onQRCodeScanListener);
    }
}
