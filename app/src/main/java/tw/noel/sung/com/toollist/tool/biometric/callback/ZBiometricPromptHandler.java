package tw.noel.sung.com.toollist.tool.biometric.callback;

import android.hardware.biometrics.BiometricPrompt;
import java.security.PublicKey;
import java.security.SignatureException;

public class ZBiometricPromptHandler extends BiometricPrompt.AuthenticationCallback{

    private PublicKey publicKey;

    public ZBiometricPromptHandler setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }
    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        try {
            BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();
            onSignFingerPrint(cryptoObject, cryptoObject.getSignature().sign(),publicKey);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }

    //----------------

    /***
     *  當註冊指紋 取得byte[] 用於解密
     * @param sign
     */
    public void onSignFingerPrint(BiometricPrompt.CryptoObject cryptoObject, byte[] sign, PublicKey publicKey) {

    }

    //-------------------
    /***
     * 當取消
     */
    public void onCancelScan(){

    }
}
