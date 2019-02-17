package tw.noel.sung.com.toollist.tool.web.util.toolbox;

import android.content.Context;
/**
 * Created by noel on 2018/6/19.
 */
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.web.util.BasicPopupWindow;
import tw.noel.sung.com.toollist.tool.web.util.toolbox.adapter.ToolBoxAdapter;



public class ToolBoxPopupWindow extends BasicPopupWindow implements ToolBoxAdapter.OnItemClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @IntDef({TOOL_BOX_TYPE_MAIN, TOOL_BOX_TYPE_WEB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ToolBoxType {
    }

    public static final int TOOL_BOX_TYPE_MAIN = 666;
    public static final int TOOL_BOX_TYPE_WEB = 667;
    private int toolBoxType;

    private Context context;
    private ToolBoxAdapter toolBoxAdapter;
    private OnToolBoxSelectListener onToolBoxSelectedListener;

    public ToolBoxPopupWindow(Context context, @ToolBoxType int toolBoxType) {
        super(context);
        this.toolBoxType = toolBoxType;
        this.context = context;
        init();
    }

    //---------

    /***
     *  初始化
     */
    private void init() {
        setAnimationStyle(R.style.style_main_tool_box_animation);
        toolBoxAdapter = new ToolBoxAdapter(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(toolBoxAdapter);
        toolBoxAdapter.setData(Arrays.asList(context.getResources().getStringArray(R.array.web_tool_box_items)));
        toolBoxAdapter.setOnItemClickListener(this);
    }

    //------------

    /***
     *  當點擊項目
     * @param view
     * @param position
     */
    @Override
    public void onItemClicked(View view, int position) {
        dismiss();
        onToolBoxSelectedListener.onToolBoxSelected(view, position);
    }

    //-----------

    @Override
    protected int getContentLayoutId() {
        return R.layout.popup_window_tool_box;
    }
    //-----------

    @Override
    protected void initContentView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected Drawable getBackgroundDrawable() {
        return new ColorDrawable(Color.WHITE);
    }

    //---------
    public interface OnToolBoxSelectListener {
        void onToolBoxSelected(View view, int position);
    }

    //--------
    public void setOnToolBoxSelectedListener(OnToolBoxSelectListener onToolBoxSelectedListener) {
        this.onToolBoxSelectedListener = onToolBoxSelectedListener;
    }
}
