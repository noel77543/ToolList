package tw.noel.sung.com.toollist.tool.biometric;

import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.ECGenParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;


@RequiresApi(api = Build.VERSION_CODES.M)
public class KeyHelper {

    private static final String KEYSTORE_NAME = "AndroidKeyStore";
    private static final String KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES;
    private static final String BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC;
    private static final String ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7;
    private static final String TRANSFORMATION = KEY_ALGORITHM + "/" + BLOCK_MODE + "/" + ENCRYPTION_PADDING;

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

    public KeyHelper(Context context) {
        this.context = context;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, KEYSTORE_NAME);
            keyStore = KeyStore.getInstance(KEYSTORE_NAME);
            keyStore.load(null);
        } catch (IOException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    //------------

    /***
     *  取得key
     * @return
     */
    private Key getKey() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        Key secretKey;
        //如定義的KeyName不存在則創建一個
        if (!keyStore.isKeyEntry(KEY_NAME)) {
            createKey();
        }
        secretKey = keyStore.getKey(KEY_NAME, null);
        return secretKey;
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
                    .setUserAuthenticationRequired(true)
                    .build());
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }
    //-----------------

    /***
     *  創建加密對象
     * @param retry
     * @return
     * @throws Exception
     */
    private Cipher createCipher(boolean retry) throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException {

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        try {
            cipher.init(Cipher.ENCRYPT_MODE | Cipher.DECRYPT_MODE, getKey());
        } catch (KeyPermanentlyInvalidatedException e) {
            keyStore.deleteEntry(KEY_NAME);
            if (retry) {
                createCipher(false);
            } else {
                Log.e("無法建立","無法建立鑰匙對");
            }
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    //---------------

    /***
     * 取得加密對象
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public BiometricPrompt.CryptoObject getBiometricPromptCryptoObject() throws Exception {
        return new BiometricPrompt.CryptoObject(createCipher(true));
    }


    //----------------

    /***
     *  取得加密對象
     */
    public FingerprintManager.CryptoObject getFingerprintManagerCompatCryptoObject() throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException {
        return new FingerprintManager.CryptoObject(createCipher(true));
    }

    //-------------

    /***
     *   android api 23 ~ 27
     * 以私鑰進行加密CryptoObject物件
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public byte[] signCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        try {
            PrivateKey privateKey = keyPair.getPrivate();
            Signature signature = cryptoObject.getSignature();
            signature.initSign(privateKey);
            return signature.sign();
        } catch (InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    //--------------

    /***
     *   android api 23 ~ 27
     * 以公鑰進行解密
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean verifyCryptoObject(FingerprintManager.CryptoObject cryptoObject, byte[] signByteArray) {
        try {
            PublicKey publicKey = keyPair.getPublic();
            Signature signature = cryptoObject.getSignature();
            signature.initVerify(publicKey);
            return signature.verify(signByteArray);
        } catch (InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }
    //-------------

    /***
     *  android api 28 up
     * 以私鑰進行加密CryptoObject物件
     */
    public byte[] signCryptoObject(BiometricPrompt.CryptoObject cryptoObject) {
        try {
            PrivateKey privateKey = keyPair.getPrivate();
            Signature signature = cryptoObject.getSignature();
            signature.initSign(privateKey);
            return signature.sign();
        } catch (InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    //--------------

    /***
     *   android api 28 up
     * 以公鑰進行解密
     */
    public boolean verifyCryptoObject(BiometricPrompt.CryptoObject cryptoObject, byte[] signByteArray) {
        try {
            PublicKey publicKey = keyPair.getPublic();
            Signature signature = cryptoObject.getSignature();
            signature.initVerify(publicKey);
            return signature.verify(signByteArray);
        } catch (InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }
}
