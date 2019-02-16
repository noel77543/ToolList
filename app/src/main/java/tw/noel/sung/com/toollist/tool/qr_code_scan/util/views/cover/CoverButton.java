package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.content.Context;

/**
 * Created by noel on 2019/2/16.
 */
public class CoverButton extends android.support.v7.widget.AppCompatButton {


    private Context context;

    public CoverButton(Context context) {
        super(context);


    }


    //---------
    public void setOnCoverButtonClickListener(OnClickListener onCoverButtonClickListener){
        setOnClickListener(onCoverButtonClickListener);
    }
}
