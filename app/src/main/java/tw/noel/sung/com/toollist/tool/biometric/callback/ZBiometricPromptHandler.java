package tw.noel.sung.com.toollist.tool.biometric.callback;

import android.hardware.biometrics.BiometricPrompt;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;

import tw.noel.sung.com.toollist.tool.biometric.KeyHelper;
import tw.noel.sung.com.toollist.tool.biometric.VerifyHelper;

/**
 * Created by noel on 2019/4/16.
 */
public class ZBiometricPromptHandler extends BiometricPrompt.AuthenticationCallback {

    private KeyHelper keyHelper;
    private String keyString;
    private String lockString;

    public ZBiometricPromptHandler(@Nullable String lockString,@Nullable String keyString) {
        this.lockString = lockString;
        this.keyString = keyString;
    }
    //----------

    public ZBiometricPromptHandler setKeyHelper(KeyHelper keyHelper) {
        this.keyHelper = keyHelper;
        return this;
    }


    //-------------
    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        try {
            BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();

            if (lockString!=null && keyString!=null) {
                try {

                    byte[] byteKey = Base64.decode(keyString, Base64.DEFAULT);
                    X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
                    KeyFactory keyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_EC);
                    PublicKey key = keyFactory.generatePublic(X509publicKey);
                    VerifyHelper verifyHelper = new VerifyHelper();
                    onVerifiedFingerPrint(verifyHelper.verifyCryptoObject(cryptoObject, Base64.decode(lockString,Base64.DEFAULT), key));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //未建立走向創建與註冊流程
            else {

                onSignedFingerPrint( Base64.encodeToString(cryptoObject.getSignature().sign(),Base64.DEFAULT), new String(Base64.encode(keyHelper.getPublicKey().getEncoded(), Base64.DEFAULT)));
            }
        } catch (SignatureException  e) {
            e.printStackTrace();
        }
    }

    //----------------

    /***
     *  當註冊指紋 取得keyString 用於解密
     */
    public void onSignedFingerPrint(String lockString, String keyString) {

    }

    //----------------------

    /***
     * 解密指紋
     */
    public void onVerifiedFingerPrint(boolean isSuccess) {

    }


    //-------------------

    /***
     * 當取消
     */
    public void onCancelScan() {

    }
    //-------------------

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        //取消掃描
        if (errorCode == BiometricPrompt.BIOMETRIC_ERROR_USER_CANCELED) {
            onCancelScan();
        }
    }
}
