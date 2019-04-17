package tw.noel.sung.com.toollist.tool.biometric;

import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.spec.ECGenParameterSpec;


/**
 * Created by noel on 2019/4/15.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class KeyHelper {

    private static final String KEYSTORE_NAME = "AndroidKeyStore";
    private final static String KEY_NAME = KeyHelper.class.getSimpleName();
    /***
     *  與後台交互需要進行非對稱加密
     *  即創建一處成雙成對的公鑰、私鑰
     *  其概念為 - > 只有對方能夠解密彼此
     */
    private KeyStore keyStore;
    private KeyPairGenerator keyPairGenerator;
    //成對的鑰匙 公鑰、私鑰
    private KeyPair keyPair;
    private Context context;
    private Signature signature;

    public KeyHelper(Context context) {
        this.context = context;
        try {
            signature = Signature.getInstance("SHA256withECDSA");
            keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, KEYSTORE_NAME);
            keyStore = KeyStore.getInstance(KEYSTORE_NAME);
            keyStore.load(null);
            //如定義的KeyName不存在則創建一個
//            if (!keyStore.isKeyEntry(KEY_NAME)) {
            createKey();
//            }
        } catch (IOException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    //---------------

    /***
     *  創建Key & KeyPair
     */
    private void createKey() {
        try {
            keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_SIGN)
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                    .setUserAuthenticationRequired(false)
                    .build());
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    //---------------

    /***
     * 取得加密對象
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public BiometricPrompt.CryptoObject getBiometricPromptCryptoObject() throws InvalidKeyException {
        PrivateKey privateKey = keyPair.getPrivate();
        signature.initSign(privateKey);
        return new BiometricPrompt.CryptoObject(signature);
    }


    //----------------

    /***
     *  取得加密對象
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public FingerprintManager.CryptoObject getFingerprintManagerCompatCryptoObject() throws InvalidKeyException {
        PrivateKey privateKey = keyPair.getPrivate();
        signature.initSign(privateKey);
        return new FingerprintManager.CryptoObject(signature);
    }

    //----------------

    /***
     * 取得公鑰
     * @return
     */
    public PublicKey getPublicKey(){
        return keyPair.getPublic();
    }
    //----------------

    /***
     * 取得私鑰
     * @return
     */
    public PrivateKey getPrivateKey(){
        return keyPair.getPrivate();
    }
}
