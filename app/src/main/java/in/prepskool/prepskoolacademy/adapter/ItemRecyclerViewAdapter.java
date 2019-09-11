package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.retrofit_model.Resource;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<Resource> arrayList;

    ItemRecyclerViewAdapter(Context context, ArrayList<Resource> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_resource_item,
                parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        holder.lblResourceName.setText(arrayList.get(position).getName());

        holder.btnViewResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "view", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDownloadResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "download", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView lblResourceName;
        private Button btnViewResource;
        private Button btnDownloadResource;

        ItemViewHolder(View itemView) {
            super(itemView);

            lblResourceName = (TextView) itemView.findViewById(R.id.lbl_resource_name);
            btnViewResource = (MaterialButton) itemView.findViewById(R.id.btn_view_resource);
            btnDownloadResource = (MaterialButton) itemView.findViewById(R.id.btn_download_resource);
        }
    }
}