package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.content.Context;

import androidx.appcompat.widget.AppCompatButton;

public class CoverFlashButton extends AppCompatButton {


    private Context context;

    public CoverFlashButton(Context context) {
        super(context);


    }


    //---------
    public void setOnCoverFlashButtonClickListener(OnClickListener onCoverFlashButtonClickListener){
        setOnClickListener(onCoverFlashButtonClickListener);
    }
}
