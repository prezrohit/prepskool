package in.prepskool.prepskoolacademy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.model.Home;

public class SavedFilesAdapter extends RecyclerView.Adapter<SavedFilesAdapter.ViewHolder> {

    private ArrayList<Home> list;

    public SavedFilesAdapter(ArrayList<Home> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SavedFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_downloadeditems, viewGroup, false);

        return new SavedFilesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedFilesAdapter.ViewHolder viewHolder, int i) {

        Home home = list.get(i);
        viewHolder.tvItems.setText(home.getLabel());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItems;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItems=(TextView)itemView.findViewById(R.id.tvItems);
        }
    }
}
