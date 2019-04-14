package tw.noel.sung.com.toollist.tool.biometric;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import tw.noel.sung.com.toollist.R;

/**
 * Created by noel on 2019/1/21.
 */
public class BiometricHelper {


    private FingerprintManagerCompat fingerprintManagerCompat;
    private KeyguardManager keyguardManager;
    private Context context;
    //---------

    public BiometricHelper(Context context) {
        this.context = context;
        fingerprintManagerCompat = FingerprintManagerCompat.from(context);
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);


    }

    //--------

    /***
     * SDK版本是否28以上
     * @return
     */
    public boolean isSDKVersionAbove28() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }
    //----------

    /***
     * SDK 版本是否在23~27之間
     */
    public boolean isSDKVersionBetween23To27() {
        int version = Build.VERSION.SDK_INT;
        return version >= Build.VERSION_CODES.M && version <= Build.VERSION_CODES.O_MR1;
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
                //是
                .setPositiveButton(context.getString(R.string.go), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
                    }
                })
                .show();
    }

}
