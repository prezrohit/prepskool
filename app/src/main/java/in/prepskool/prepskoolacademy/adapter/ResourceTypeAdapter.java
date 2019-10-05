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
import in.prepskool.prepskoolacademy.retrofit_model.ResourceType;

public class ResourceTypeAdapter extends RecyclerView.Adapter<ResourceTypeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ResourceType> resourceTypeList;

    public ResourceTypeAdapter(Context context, ArrayList<ResourceType> resourceTypeList) {
        this.context = context;
        this.resourceTypeList = resourceTypeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_resource_type, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ResourceType resourceType = resourceTypeList.get(i);
        viewHolder.lblResourceType.setText(resourceType.getName());
    }

    @Override
    public int getItemCount() {
        return resourceTypeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView lblResourceType;

        ViewHolder(View itemView) {
            super(itemView);

            lblResourceType = itemView.findViewById(R.id.lbl_resource_type);
        }
    }
}
