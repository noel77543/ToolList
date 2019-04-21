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
                .setSections(new float[]{60}, new int[]{R.color.colorAccent,android.R.color.holo_red_dark})
                .draw(new LinearInterpolator());
    }
}
