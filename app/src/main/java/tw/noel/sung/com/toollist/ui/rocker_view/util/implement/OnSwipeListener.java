package tw.noel.sung.com.toollist.ui.rocker_view.util.implement;

import tw.noel.sung.com.toollist.ui.rocker_view.util.RockerView;

public interface OnSwipeListener {
    void onStartSwipe();
    void onSwiping(@RockerView.SwipeEvent int event, double angle);
    void onSwiped();
}
