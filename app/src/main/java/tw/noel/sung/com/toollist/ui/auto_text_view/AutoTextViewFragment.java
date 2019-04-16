package tw.noel.sung.com.toollist.ui.auto_text_view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tw.noel.sung.com.toollist.BasePageFragment;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.auto_text_view.util.AutoTextView;

public class AutoTextViewFragment extends BasePageFragment {


    @BindView(R.id.auto_text_view_fix)
    AutoTextView autoTextViewFix;
    @BindView(R.id.auto_text_view_scrollable)
    AutoTextView autoTextViewScrollable;
    @BindView(R.id.auto_text_view_none)
    AutoTextView autoTextViewNone;


    @Override
    protected int getContentView() {
        return R.layout.fragment_auto_text_view;
    }

    @Override
    protected void init() {
        String text = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTEEEEEEEEEEEEEEEEEEEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTTTTTTTTT";
        autoTextViewFix.setText(text);
        autoTextViewScrollable.setText(text);
        autoTextViewNone.setText(text);
    }

}
