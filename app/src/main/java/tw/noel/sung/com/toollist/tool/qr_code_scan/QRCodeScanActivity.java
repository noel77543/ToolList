package tw.noel.sung.com.toollist.tool.qr_code_scan;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.implement.OnQRCodeScanListener;
import tw.noel.sung.com.toollist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.QRCodeScanView;


@RuntimePermissions
public class QRCodeScanActivity extends FragmentActivity implements OnQRCodeScanListener, View.OnClickListener {

    private final static int REQUEST_ALBUM = 11;

    @BindView(R.id.q_r_code_scan_view)
    QRCodeScanView qRCodeScanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        ButterKnife.bind(this);

        qRCodeScanView.setOnQRCodeScanListener(this);
        qRCodeScanView.setOnPhotoButtonClickListener(this);
    }


    //--------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qRCodeScanView.release();
    }

    //--------

    /***
     * 當成功掃描
     * @param content
     */
    @Override
    public void onQRCodeScanSuccess(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    //---------------

    @Override
    public void onClick(View v) {
        QRCodeScanActivityPermissionsDispatcher.OnPermissionAllowedWithPermissionCheck(this);
    }
    //---------------

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void OnPermissionAllowed() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    //---------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QRCodeScanActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    //---------------

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void OnShowRationale(final PermissionRequest request) {
    }
    //---------------

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void OnPermissionDenied() {
    }
    //---------------

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void OnNeverAskAgain() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //相簿
            if (requestCode == REQUEST_ALBUM) {

                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap photo = BitmapFactory.decodeStream(imageStream);

                    if (photo != null) {
                        qRCodeScanView.parseQRCodeResult(photo);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
