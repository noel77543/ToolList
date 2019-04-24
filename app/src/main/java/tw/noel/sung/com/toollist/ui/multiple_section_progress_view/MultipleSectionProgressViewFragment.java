package tw.noel.sung.com.toollist.ui.multiple_section_progress_view;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import tw.noel.sung.com.toollist.BasePageFragment;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.multiple_section_progress_view.util.MultipleSectionProgressView;

public class MultipleSectionProgressViewFragment extends BasePageFragment {
    @BindView(R.id.multiple_section_progress_view)
    MultipleSectionProgressView multipleSectionProgressView;
    @BindView(R.id.button)
    Button button;

    @Override
    protected int getContentView() {
        return R.layout.fragment_multiple_section_progress_view;
    }

    @Override
    protected void init() {


    }

    //-------------


    @OnClick(R.id.button)
    public void onClicked(View view){
        int startColor = R.color.multiple_section_progress_view_start;
        int endColor = R.color.multiple_section_progress_view_end;
        int[] sectionColors = new int[]{R.color.multiple_section_progress_view_section};
        float[] sections = new float[]{5000};

        multipleSectionProgressView
                .setAnimationTime(2000)
                .setValue(10000, 10000)
                .setSections(startColor, endColor, sectionColors, sections)
                .draw(new AccelerateInterpolator());
    }

}
