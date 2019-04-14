package tw.noel.sung.com.toollist.tool.biometric.callback;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ZFingerprintManagerHandler extends FingerprintManager.AuthenticationCallback{

}
