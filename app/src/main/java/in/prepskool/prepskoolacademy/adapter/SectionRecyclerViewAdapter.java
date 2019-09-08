package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.retrofit_model.SectionedResource;

public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionViewHolder> {

    private Context context;
    private ArrayList<SectionedResource> sectionedResourceList;

    public SectionRecyclerViewAdapter(Context context, ArrayList<SectionedResource> sectionedResourceList) {
        this.context = context;
        this.sectionedResourceList = sectionedResourceList;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_resource_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {

        SectionedResource sectionedResource = sectionedResourceList.get(position);

        holder.lblSectionLabel.setText(sectionedResource.getSectionLabel());

        holder.rvResourceItems.setHasFixedSize(true);
        holder.rvResourceItems.setNestedScrollingEnabled(false);
        holder.rvResourceItems.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.rvResourceItems.setAdapter(new ItemRecyclerViewAdapter(context, sectionedResource.getResourceList()));
    }

    @Override
    public int getItemCount() {
        return sectionedResourceList.size();
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView lblSectionLabel;
        private RecyclerView rvResourceItems;

        SectionViewHolder(View itemView) {
            super(itemView);

            lblSectionLabel = (TextView) itemView.findViewById(R.id.lbl_resource_label);
            rvResourceItems = (RecyclerView) itemView.findViewById(R.id.rv_resource_items);
        }
    }
}
