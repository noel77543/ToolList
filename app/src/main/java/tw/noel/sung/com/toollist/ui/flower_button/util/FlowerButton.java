package tw.noel.sung.com.toollist.ui.flower_button.util;


import android.animation.Animator;
import android.content.Context;
/**
 * Created by noel on 2018/8/4.
 */
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import tw.noel.sung.com.toollist.ui.flower_button.util.views.BasicButton;

public class FlowerButton extends FrameLayout implements Runnable, View.OnClickListener {
    private final int _ANIMATION_DURENT = 100;
    private int layoutWidth;
    private int layoutHeight;

    private OnMainButtonClickListener onMainButtonClickListener;
    private FrameLayout.LayoutParams mainButtonParams;
    private BasicButton mainButton;
    private int mainButtonSize;

    private Map<Integer, Button> childButtons;
    private OnChildButtonClickListener onChildButtonClickListener;
    private int childButtonSize;
    private FrameLayout.LayoutParams childButtonParams;
    private int childCount = 1;
    private Context context;
    private boolean isShow;
    private boolean isDisplay;

    private final int MAIN_BUTTON_ID = 100;
    //動畫中的項目索引
    private int index = -1;
    //----------

    public FlowerButton(@NonNull Context context) {
        super(context);
        this.context = context;
        post(this);
    }
    //----------

    public FlowerButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mainButton = new BasicButton(context);
        post(this);

    }
    //----------

    @Override
    public void run() {
        init();
    }

    //----------
    private void init() {
        addMainButton();
    }

    //-------

    /***
     *加入主button
     */
    private void addMainButton() {
        layoutWidth = getWidth();
        layoutHeight = getHeight();
        mainButtonSize = layoutWidth < layoutHeight ? layoutWidth : layoutHeight;
        mainButtonSize = (int) (mainButtonSize * 0.5);
        childButtonSize = (int) (mainButtonSize * 0.5);
        mainButtonParams = new FrameLayout.LayoutParams(mainButtonSize, mainButtonSize);
        mainButtonParams.gravity = Gravity.CENTER;
        mainButton.setId(MAIN_BUTTON_ID);
        mainButton.setLayoutParams(mainButtonParams);
        mainButton.setOnClickListener(this);
        addView(mainButton);
        addChildren();
    }

    //----------

    /***
     * add 所有子按鈕
     */
    private void addChildren() {
        childButtons = new HashMap<>();
        childButtonParams = new FrameLayout.LayoutParams(childButtonSize, childButtonSize);
        childButtonParams.gravity = Gravity.CENTER;
        for (int i = 0; i < childCount; i++) {
            BasicButton button = new BasicButton(context);
            childButtons.put(i, button);
            button.setId(i);
            button.setOnClickListener(this);
            button.setLayoutParams(childButtonParams);
            addView(button, 0);
        }
    }
    //----------


    @Override
    public void onClick(View v) {
        //點選主按鈕
        if (mainButton.getId() == v.getId()) {
            if(!isDisplay){
                if (!isShow) {
                    index++;
                    showChildButtons();
                } else {
                    index--;
                    hideChildButtons();
                }
            }

            if (onMainButtonClickListener != null) {
                onMainButtonClickListener.onMainButtonCLicked();
            }
        }
        //點選子按鈕
        else if (onChildButtonClickListener != null) {
            onChildButtonClickListener.onChildButtonCLicked(v.getId());
        }
    }

    //--------

    /***
     * 顯示所有子按鈕
     */
    private void showChildButtons() {
        if (index < childCount) {
            isDisplay = true;
            double angle = Math.PI * 2 / childCount * index;
            float cosX = (float) Math.cos(angle);
            float sinY = (float) Math.sin(angle);
            float transX = cosX * ((mainButtonSize + childButtonSize) / 2);
            float transY = sinY * ((mainButtonSize + childButtonSize) / 2);

            childButtons.get(index)
                    .animate()
                    .translationX(transX)
                    .translationY(transY)
                    .setDuration(_ANIMATION_DURENT)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            index++;
                            showChildButtons();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        } else {
            isShow = true;
            isDisplay = false;
        }
    }

    //------------

    /***
     *  隱藏所有子按鈕
     */
    private void hideChildButtons() {
        if (index > -1) {
            isDisplay = true;
            childButtons.get(index)
                    .animate()
                    .translationX(0)
                    .translationY(0)
                    .setDuration(_ANIMATION_DURENT)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            index--;
                            hideChildButtons();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        } else {
            isShow = false;
            isDisplay = false;
        }
    }


    //----------

    /***
     * 設置子按鈕數
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount > 8 ? 8 : childCount < 0 ? 1 : childCount;
    }

    //--------

    /***
     * 主紐設置文字
     */
    public void setText(String text) {
        mainButton.setText(text);
    }

    /***
     * 主紐設置文字  子紐設置文字
     */
    public void setText(String text, String[] textArray) {
        mainButton.setText(text);
    }
    //-----

    /***
     * 主紐設置文字顏色
     */
    public void setTextColor(int colorRes) {
        mainButton.setTextColor(colorRes);
    }
    //--------

    /***
     * 主紐設置顏色
     */
    public void setBackgroundColor(int colorRes) {
        mainButton.setBackgroundColor(colorRes);
    }

    //--------

    /***
     * 主紐設置背景 for drawable
     */
    public void setBackground(Drawable drawable) {
        mainButton.setBackground(drawable);
    }

    //-----

    /***
     * 主紐設置背景 for resource
     */
    public void setBackgroundResource(int imgRes) {
        mainButton.setBackgroundResource(imgRes);
    }

    //-------
    public interface OnMainButtonClickListener {
        void onMainButtonCLicked();
    }

    public void setOnMainButtonClickListener(OnMainButtonClickListener onMainButtonClickListener) {
        this.onMainButtonClickListener = onMainButtonClickListener;
    }

    //-------
    public interface OnChildButtonClickListener {
        void onChildButtonCLicked(int position);
    }

    public void setOnChildButtonClickListener(OnChildButtonClickListener onChildButtonClickListener) {
        this.onChildButtonClickListener = onChildButtonClickListener;
    }
}
