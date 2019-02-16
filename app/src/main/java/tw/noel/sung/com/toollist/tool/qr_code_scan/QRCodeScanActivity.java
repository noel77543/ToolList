package tw.noel.sung.com.toollist.tool.qr_code_scan;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.implement.OnQRCodeScanListener;
import tw.noel.sung.com.toollist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.QRCodeScanSurfaceView;


public class QRCodeScanActivity extends FragmentActivity implements OnQRCodeScanListener {


    @BindView(R.id.q_r_code_scan_surface_view)
    QRCodeScanSurfaceView qRCodeScanSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        ButterKnife.bind(this);

        qRCodeScanSurfaceView.setOnQRCodeScanListener(this);
    }


    //--------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qRCodeScanSurfaceView.release();
    }

    //--------

    /***
     * 當成功掃描
     * @param content
     */
    @Override
    public void onQRCodeScanSuccess(String content) {
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }
}
