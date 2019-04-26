package in.prepskool.prepskoolacademy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.model.Home;

public class DownloadedItemsAdapter extends RecyclerView.Adapter<DownloadedItemsAdapter.ViewHolder> {

    private ArrayList<Home> list;

    public DownloadedItemsAdapter(ArrayList<Home> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DownloadedItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_downloadeditems, viewGroup, false);

        return new DownloadedItemsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadedItemsAdapter.ViewHolder viewHolder, int i) {

        Home home = list.get(i);
        viewHolder.tvItems.setText(home.getLabel());
        viewHolder.imgPdfView.setImageResource(R.mipmap.ic_pdf);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgPdfView;
        public TextView tvItems;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItems=(TextView)itemView.findViewById(R.id.tvItems);
            imgPdfView = (ImageView) itemView.findViewById(R.id.img_pdf_view);
        }
    }
}
