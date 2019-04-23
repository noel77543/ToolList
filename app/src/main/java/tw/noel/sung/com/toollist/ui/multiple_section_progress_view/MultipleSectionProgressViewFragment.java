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
        int startColor = R.color.multiple_section_progress_view_start;
        int endColor = R.color.multiple_section_progress_view_end;
        int[] sectionColors = new int[]{R.color.multiple_section_progress_view_section,R.color.multiple_section_progress_view_section2};
        float[] sections = new float[]{60,80};
        multipleSectionProgressView
                .setAnimationTime(1000)
                .setValue(100, 60)
                .setSections(startColor,endColor,sectionColors,sections)
                .draw(new LinearInterpolator());
    }
}
