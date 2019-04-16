package tw.noel.sung.com.toollist.tool.biometric;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.biometric.callback.ZBiometricPromptHandler;
import tw.noel.sung.com.toollist.tool.biometric.callback.ZFingerprintManagerHandler;

import java.security.InvalidKeyException;
import java.util.concurrent.Executor;


/**
 * Created by noel on 2019/1/21.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class BiometricTool {
    /***
     * 指紋辨識
     * 在api 23 時 出現
     * 在api 28 時 歸類於BiometricPrompt類 之生物辨識應用
     */
    //生物驗證類 android api 28 以上
    private BiometricPrompt biometricPrompt;
    //指紋辨識類 android api 23 - 27
    private FingerprintManager fingerprintManager;

    private Context context;
    private Executor executor;
    private CancellationSignal cancellationSignal;
    private BiometricHelper biometricHelper;
    private KeyHelper keyHelper;

    public BiometricTool(Context context) {
        this.context = context;

        keyHelper = new KeyHelper(context);
        biometricHelper = new BiometricHelper(context);
        cancellationSignal = new CancellationSignal();
    }

    //--------

    /***
     * 進行辨識
     *  android api 23 - 27 之間
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startScanFinger(final ZFingerprintManagerHandler zFingerprintManagerHandler) {

        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                zFingerprintManagerHandler.onCancelScan();
            }
        });
        try {
            if (biometricHelper.isCanFingerPrint()) {
                fingerprintManager = (FingerprintManager) context.getSystemService(Activity.FINGERPRINT_SERVICE);
                fingerprintManager.authenticate(keyHelper.getFingerprintManagerCompatCryptoObject(), cancellationSignal, 0, zFingerprintManagerHandler.setPublicKey(keyHelper.getPublicKey()), null);
            } else {
                Toast.makeText(context, context.getString(R.string.not_finger_print), Toast.LENGTH_SHORT).show();
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    //----------

    /***
     * 進行辨識
     *  android api 28 以上
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void startScanFinger(final ZBiometricPromptHandler zBiometricPromptHandler) {

        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                Log.e("TTT","AAA");
                zBiometricPromptHandler.onCancelScan();
            }
        });
        try {
            if (biometricHelper.isCanBioMetricAuthentication()) {
                executor = context.getMainExecutor();
                biometricPrompt = new BiometricPrompt.Builder(context)
                        .setTitle(context.getString(R.string.finger_print))
                        .setDescription(context.getString(R.string.finger_print_description))
                        .setNegativeButton(context.getString(R.string.cancel), executor, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                zBiometricPromptHandler.onCancelScan();
                            }
                        })
                        .build();
                biometricPrompt.authenticate(keyHelper.getBiometricPromptCryptoObject(), cancellationSignal, executor, zBiometricPromptHandler.setPublicKey(keyHelper.getPublicKey()));


            } else {
                Toast.makeText(context, context.getString(R.string.not_finger_print), Toast.LENGTH_SHORT).show();
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    //-------------------

    /***
     * 停止掃描
     */
    public void stopScan(){
        cancellationSignal.cancel();
    }
}
