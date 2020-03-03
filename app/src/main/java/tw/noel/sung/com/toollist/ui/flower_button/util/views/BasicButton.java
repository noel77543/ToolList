package tw.noel.sung.com.toollist.ui.flower_button.util.views;

import android.content.Context;
/**
 * Created by noel on 2018/8/4.
 */
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.appcompat.widget.AppCompatButton;

public class BasicButton extends AppCompatButton {


    public BasicButton(Context context) {
        super(context);
        init();
    }


    //--------
    private void init() {
        int strokeWidth = 3;
        int strokeColor = Color.BLACK;
        int fillColor = Color.YELLOW;

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        setBackground(gradientDrawable);
    }
}
