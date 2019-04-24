package tw.noel.sung.com.toollist.ui;

import android.content.Intent;
/**
 * Created by noel on 2019/2/16.
 */
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.auto_text_view.AutoTextViewFragment;
import tw.noel.sung.com.toollist.ui.block_pie_view.BlockPieFragment;
import tw.noel.sung.com.toollist.ui.flower_button.FlowerButtonFragment;
import tw.noel.sung.com.toollist.ui.link_view.LinkViewFragment;
import tw.noel.sung.com.toollist.ui.loterry_view.LotteryViewFragment;
import tw.noel.sung.com.toollist.ui.multiple_section_progress_view.MultipleSectionProgressViewFragment;
import tw.noel.sung.com.toollist.ui.simple_pie_view.SimplePieViewFragment;
import tw.noel.sung.com.toollist.ui.round_progress_view.RoundProgressViewFragment;

public class UIActivity extends FragmentActivity {

    @BindView(R.id.text_view_title)
    TextView textViewTitle;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;


    public static final int PAGE_FLOWER_BUTTON = 101;
    public static final int PAGE_LINK_VIEW = 102;
    public static final int PAGE_SIMPLE_PIE_VIEW = 103;
    public static final int PAGE_BLOCK_PIE_VIEW = 104;
    public static final int PAGE_ROUND_PROGRESS_VIEW = 105;
    public static final int PAGE_LOTTERY_VIEW = 106;
    public static final int PAGE_AUTO_TEXT_VIEW = 107;
    public static final int PAGE_MULTIPLES_SECTION_PROGRESS_VIEW = 108;

    @IntDef({PAGE_FLOWER_BUTTON, PAGE_LINK_VIEW, PAGE_SIMPLE_PIE_VIEW, PAGE_BLOCK_PIE_VIEW, PAGE_ROUND_PROGRESS_VIEW, PAGE_LOTTERY_VIEW, PAGE_AUTO_TEXT_VIEW,PAGE_MULTIPLES_SECTION_PROGRESS_VIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Page {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        textViewTitle.setText(intent.getStringExtra("title"));
        goToNextPage(intent.getIntExtra("page", 0));
    }

    //---------

    /***
     * 依照nextPage替換Fragment
     * @param nextPage
     */
    private void goToNextPage(int nextPage) {
        Fragment fragment = null;
        switch (nextPage) {
            case PAGE_FLOWER_BUTTON:
                fragment = new FlowerButtonFragment();
                break;
            case PAGE_LINK_VIEW:
                fragment = new LinkViewFragment();
                break;
            case PAGE_SIMPLE_PIE_VIEW:
                fragment = new SimplePieViewFragment();
                break;
            case PAGE_BLOCK_PIE_VIEW:
                fragment = new BlockPieFragment();
                break;
            case PAGE_ROUND_PROGRESS_VIEW:
                fragment = new RoundProgressViewFragment();
                break;
            case PAGE_LOTTERY_VIEW:
                fragment = new LotteryViewFragment();
                break;
            case PAGE_AUTO_TEXT_VIEW:
                fragment = new AutoTextViewFragment();
                break;
            case PAGE_MULTIPLES_SECTION_PROGRESS_VIEW:
                fragment = new MultipleSectionProgressViewFragment();
                break;
        }
        if (fragment != null) {
            replaceFragment(fragment, null);
        }
    }

    //---------

    /***
     *  替換Fragment
     * @param fragment
     * @param bundle
     */
    private void replaceFragment(Fragment fragment, Bundle bundle) {
        if (null != bundle) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment).commit();
    }
}
