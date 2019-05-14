package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.content.Context;

public class CoverFlashButton extends android.support.v7.widget.AppCompatButton {


    private Context context;

    public CoverFlashButton(Context context) {
        super(context);


    }


    //---------
    public void setOnCoverFlashButtonClickListener(OnClickListener onCoverFlashButtonClickListener){
        setOnClickListener(onCoverFlashButtonClickListener);
    }
}
