package tw.noel.sung.com.toollist.tool.web.util.toolbox;

import android.content.Context;
/**
 * Created by noel on 2018/6/19.
 */
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import butterknife.BindView;
import tw.noel.sung.com.toollist.BaseWindow;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.web.util.toolbox.adapter.ToolBoxAdapter;



public class ToolBoxPopupWindow extends BaseWindow implements ToolBoxAdapter.OnItemClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    private ToolBoxAdapter toolBoxAdapter;
    private OnToolBoxSelectListener onToolBoxSelectedListener;

    public ToolBoxPopupWindow(Context context) {
        super(context);
        setOutsideTouchable(true);
        setFocusable(true);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setAnimationStyle(R.style.style_main_tool_box_animation);
    }

    //---------

    @Override
    protected int getContentViewId() {
        return R.layout.window_tool_box;
    }

    //--------------
    /***
     *  初始化
     */
    @Override
    protected void init() {
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

    //---------
    public interface OnToolBoxSelectListener {
        void onToolBoxSelected(View view, int position);
    }

    //--------
    public void setOnToolBoxSelectedListener(OnToolBoxSelectListener onToolBoxSelectedListener) {
        this.onToolBoxSelectedListener = onToolBoxSelectedListener;
    }
}
