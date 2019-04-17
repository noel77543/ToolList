package tw.noel.sung.com.toollist.tool.biometric;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.widget.Toast;

import tw.noel.sung.com.toollist.R;

/**
 * Created by noel on 2019/4/7.
 */
public class BiometricHelper implements DialogInterface.OnClickListener {


    private FingerprintManagerCompat fingerprintManagerCompat;
    private KeyguardManager keyguardManager;
    private Context context;
    //---------

    public BiometricHelper(Context context) {
        this.context = context;
        fingerprintManagerCompat = FingerprintManagerCompat.from(context);
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    //----------

    /***
     * 裝置支援生物驗證
     * @return
     */
    public boolean isCanBioMetricAuthentication() {
        return fingerprintManagerCompat.isHardwareDetected() && isSetPinCode(keyguardManager.isKeyguardSecure()) &&
                isSetFingerPrint(fingerprintManagerCompat.hasEnrolledFingerprints());
    }


    //-------------

    /***
     * 裝置支援指紋辨識
     *
     * //是否具備指紋辨識硬體
     * //是否有屏幕鎖
     * //是否已經有設置指紋
     * @return
     */
    public boolean isCanFingerPrint() {
        return fingerprintManagerCompat.isHardwareDetected() && isSetPinCode(keyguardManager.isKeyguardSecure()) &&
                isSetFingerPrint(fingerprintManagerCompat.hasEnrolledFingerprints());
    }


    //-------------

    /***
     * 是否已經設置指紋
     */
    private boolean isSetFingerPrint(boolean isSetFingerPrint) {
        if (!isSetFingerPrint) {
            showAlert(context.getString(R.string.not_set_fingerprint));
        }
        return isSetFingerPrint;
    }

    //---------

    /***
     * 是否已經設置PIN碼
     */
    private boolean isSetPinCode(boolean isKeyguardSecure) {
        if (!isKeyguardSecure) {
            showAlert(context.getString(R.string.not_set_pin_code));
        }
        return isKeyguardSecure;
    }

    //----------

    /***
     * 要求 設置 PIN碼 或 指紋紀錄
     */
    private void showAlert(String title) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(context.getString(R.string.go_to_setting))
                //取消
                .setNegativeButton(context.getString(R.string.cancel), this)
                //是
                .setPositiveButton(context.getString(R.string.go), this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            //是
            case DialogInterface.BUTTON_POSITIVE:
                context.startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
                break;
            //取消
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(context, context.getString(R.string.not_finger_print), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
