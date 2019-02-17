package tw.noel.sung.com.toollist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.ui.UIActivity;

public abstract class BasePageFragment extends Fragment {

    public UIActivity uiActivity;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UIActivity) {
            uiActivity = (UIActivity) context;
        }
    }


    //-----------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(getContentView(), container, false);
            ButterKnife.bind(this, view);
            init();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }
    //----------------------

    protected abstract int getContentView();

    //----------------------

    protected abstract void init();


    //----------------------

    /**
     * 無bundle
     */
    public void replacePageFragment(int layoutId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(layoutId, fragment);
        transaction.commit();
        getFragmentManager().executePendingTransactions();
    }
    //----------------------

    /**
     * 有 bundle
     */
    public void replacePageFragment(int layoutId, Fragment fragment, boolean addToBackStack, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(layoutId, fragment);
        transaction.commit();
        getFragmentManager().executePendingTransactions();
    }


}
