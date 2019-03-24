package tw.noel.sung.com.toollist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private ArrayList<String> data;
    private LayoutInflater layoutInflater;

    public TestAdapter(Context context) {
        data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("1");
        data.add("2");
        data.add("3");
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.list_test, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //--------

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView textView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
