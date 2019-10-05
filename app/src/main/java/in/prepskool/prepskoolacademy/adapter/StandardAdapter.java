package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.retrofit_model.Standard;

public class StandardAdapter extends RecyclerView.Adapter<StandardAdapter.StandardViewHolder> {

    private Context context;
    private ArrayList<Standard> standardList;

    public StandardAdapter(Context context, ArrayList<Standard> standardList) {
        this.context = context;
        this.standardList = standardList;
    }

    @NonNull
    @Override
    public StandardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_gridview_box, viewGroup, false);
        return new StandardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StandardViewHolder standardViewHolder, int i) {
        Standard standard = standardList.get(i);
        standardViewHolder.lblStandard.setText(standard.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return standardList.isEmpty() ? 0 : standardList.size();
    }

    static class StandardViewHolder extends RecyclerView.ViewHolder {

        private TextView lblStandard;

        StandardViewHolder(@NonNull View itemView) {
            super(itemView);

            lblStandard = itemView.findViewById(R.id.lbl_standard);
        }
    }
}
