package tw.noel.sung.com.toollist.ui.auto_text_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import tw.noel.sung.com.toollist.R;


public class AutoTextView extends android.support.v7.widget.AppCompatEditText implements View.OnTouchListener {

    private String text;

    //限定最大值
    private final int _DEFAULT_MAX_WIDTH = 100;
    private int maxWidth;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({_MODE_SCROLLABLE, _MODE_FIX, _MODE_NONE})
    public @interface AutoTextViewMode {
    }

    //可水平滾動
    public final static int _MODE_SCROLLABLE = 0;
    //限制範圍內顯示所有字串 字體自適應調整
    public final static int _MODE_FIX = 1;
    //無
    public final static int _MODE_NONE = 2;

    private @AutoTextViewMode
    int mode;

    private Context context;
    private float height;

    public AutoTextView(Context context) {
        this(context, null);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(attrs, defStyleAttr);
    }
    //------------

    private void initAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AutoTextView, defStyleAttr, 0);
        maxWidth = (int) typedArray.getDimension(R.styleable.AutoTextView_AutoTextViewMaxWidth, _DEFAULT_MAX_WIDTH);
        mode = typedArray.getInt(R.styleable.AutoTextView_AutoTextViewMode, _MODE_NONE);
        typedArray.recycle();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_MOVE;
    }

    //--------

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mode == _MODE_NONE) {
            setOnTouchListener(this);
        } else if (mode == _MODE_FIX) {
            text = getText().toString();
            getPXFromSP(getAutoTextSize());
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                params.height = (int) height;
                setLayoutParams(params);
            }
        }

        setSingleLine(true);
        setFocusable(false);
        setCursorVisible(false);
    }

    //--------

    /***
     *  sp 轉 px
     */
    private float getPXFromSP(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
    //--------

    /***
     * 使文字大小自適應
     */
    private float getAutoTextSize() {
        int avaiWidth = maxWidth - getPaddingLeft() - getPaddingRight() - 10;

        if (avaiWidth <= 0) {
            return getTextSize();
        }

        TextPaint textPaint = new TextPaint(getPaint());
        float trySize = textPaint.getTextSize();

        //不斷比對size是否會超出矩形直到不會為止
        while (textPaint.measureText(text) > avaiWidth) {
            trySize--;
            textPaint.setTextSize(trySize);
        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        height = trySize;
        return trySize;
    }
}
