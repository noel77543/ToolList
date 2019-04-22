package tw.noel.sung.com.toollist.ui.multiple_section_progress_view;

import android.view.animation.LinearInterpolator;

import butterknife.BindView;
import tw.noel.sung.com.toollist.BasePageFragment;
import tw.noel.sung.com.toollist.R;

public class MultipleSectionProgressViewFragment extends BasePageFragment {
    @BindView(R.id.multiple_section_progress_view)
    MultipleSectionProgressView multipleSectionProgressView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_multiple_section_progress_view;
    }

    @Override
    protected void init() {
        multipleSectionProgressView
                .setAnimationTime(1000)
                .setValue(100, 100)
                .setSections(R.color.multiple_section_progress_view_start,R.color.multiple_section_progress_view_end, new int[]{R.color.multiple_section_progress_view_section},new float[]{60})
                .draw(new LinearInterpolator());
    }
}
