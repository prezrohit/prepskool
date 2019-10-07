package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.button.MaterialButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Objects;

import in.prepskool.prepskoolacademy.PaymentActivity;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.activities.DownloadActivity;
import in.prepskool.prepskoolacademy.activities.PdfLoaderActivity;
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
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Resource resource = arrayList.get(position);

        holder.lblResourceName.setText(resource.getName());

        final String price = resource.getPrice();

        holder.btnViewResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PdfLoaderActivity.class);
                intent.putExtra("price", price);
                intent.putExtra("name", resource.getName());
                intent.putExtra("link", resource.getUrl());
                intent.putExtra("slug", resource.getSlug());
                context.startActivity(intent);
            }
        });

        holder.btnDownloadResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (price == null) {
                    intent = new Intent(context, DownloadActivity.class);
                    intent.putExtra("name", resource.getName());
                    intent.putExtra("link", resource.getUrl());
                    intent.putExtra("slug", resource.getSlug());

                } else if (Objects.equals(price, "")) {
                    intent = new Intent(context, DownloadActivity.class);
                    intent.putExtra("name", resource.getName());
                    intent.putExtra("link", resource.getUrl());
                    intent.putExtra("slug", resource.getSlug());

                } else {
                    intent = new Intent(context, PaymentActivity.class);
                    intent.putExtra("price", price);
                    intent.putExtra("name", resource.getName());
                    intent.putExtra("link", resource.getUrl());
                    intent.putExtra("slug", resource.getSlug());
                }

                context.startActivity(intent);
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