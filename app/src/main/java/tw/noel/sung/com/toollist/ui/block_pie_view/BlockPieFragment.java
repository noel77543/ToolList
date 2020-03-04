package tw.noel.sung.com.toollist.ui.block_pie_view;

import butterknife.BindView;
import tw.noel.sung.com.library_ui_blockpieview.block_pie_view.BlockPieView;
import tw.noel.sung.com.toollist.BasePageFragment;
/**
 * Created by noel on 2019/2/16.
 */
import tw.noel.sung.com.toollist.R;

public class BlockPieFragment extends BasePageFragment {
    @BindView(R.id.block_pie_view)
    BlockPieView blockPieView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_block_pie;
    }

    @Override
    protected void init() {
        int[] colors = new int[]{android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_purple};
        float[] values = new float[]{50f, 30f, 20f};
        blockPieView.setDuration(2000).setColors(colors).setValues(values).start();
    }
}