package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
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

public class HomeItemRecyclerViewAdapter
        extends RecyclerView.Adapter<HomeItemRecyclerViewAdapter.HomeItemViewHolder> {

    class HomeItemViewHolder extends RecyclerView.ViewHolder {

        private TextView itemLabel;
        private ImageView imgHome;

        HomeItemViewHolder(View itemView) {
            super(itemView);
            itemLabel = (TextView) itemView.findViewById(R.id.home_item_label);
            imgHome = (ImageView) itemView.findViewById(R.id.gv_img_view);
        }
    }

    private Context context;
    private ArrayList<Home> arrayList;

    HomeItemRecyclerViewAdapter(Context context, ArrayList<Home> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_item,
                parent, false);
        return new HomeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemViewHolder holder, int position) {

        Home home = arrayList.get(position);
        holder.itemLabel.setText(home.getLabel());
        holder.imgHome.setImageResource(home.getImgId());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

