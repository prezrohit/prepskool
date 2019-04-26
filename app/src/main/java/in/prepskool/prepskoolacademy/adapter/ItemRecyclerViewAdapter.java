package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.activities.DownloadActivity;
import in.prepskool.prepskoolacademy.activities.PdfLoaderActivity;
import in.prepskool.prepskoolacademy.model.Pdf;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView itemLabel;
        private TextView itemVieww;
        private TextView itemDown;
        private ImageView imgPdfView;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemLabel = (TextView) itemView.findViewById(R.id.item_label);

            itemVieww = (TextView) itemView.findViewById(R.id.item_view);

            itemDown = (TextView) itemView.findViewById(R.id.item_down);

            imgPdfView = (ImageView) itemView.findViewById(R.id.pdf_img_view);
        }
    }

    private Context context;
    private ArrayList<Pdf> arrayList;

    ItemRecyclerViewAdapter(Context context, ArrayList<Pdf> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_custom_item,
                parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.itemLabel.setText(arrayList.get(position).getName());

        holder.itemVieww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, PdfLoaderActivity.class);
                i.putExtra("link", arrayList.get(position).getLink());
                i.putExtra("slug", arrayList.get(position).getSlug());
                context.startActivity(i);
            }
        });

        holder.itemDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, DownloadActivity.class);
                i.putExtra("link", arrayList.get(position).getLink());
                i.putExtra("slug", arrayList.get(position).getSlug());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
