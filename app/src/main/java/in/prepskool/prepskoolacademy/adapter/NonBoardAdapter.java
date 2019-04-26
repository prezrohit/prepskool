package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import in.prepskool.prepskoolacademy.R;

public class NonBoardAdapter extends RecyclerView.Adapter<NonBoardAdapter.ViewHolder> {

    private ArrayList<String> arrayList;
    private Context context;

    public NonBoardAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNonBoard;

        ViewHolder(View view) {

            super(view);
            tvNonBoard = view.findViewById(R.id.tv_subject_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_subject, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String string = arrayList.get(i);
        viewHolder.tvNonBoard.setText(string);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
