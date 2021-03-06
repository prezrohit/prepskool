package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.retrofit_model.SectionedHome;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.activities.StandardActivity;

public class HomeSectionRecyclerViewAdapter extends RecyclerView.Adapter<HomeSectionRecyclerViewAdapter.HomeSectionViewHolder> {

    private Context context;
    private ArrayList<SectionedHome> sectionHomeArrayList;

    private static final String TAG = "HomeSectionRecyclerView";

    public HomeSectionRecyclerViewAdapter(Context context, ArrayList<SectionedHome> sectionHomeArrayList) {
        this.context = context;
        this.sectionHomeArrayList = sectionHomeArrayList;
    }

    @NonNull
    @Override
    public HomeSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_section, parent, false);
        return new HomeSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeSectionViewHolder holder, int position) {
        final SectionedHome sectionHome = sectionHomeArrayList.get(position);
        holder.sectionLabel.setText(sectionHome.getSectionLabel());

        //recycler view for items
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);
        holder.itemRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));

        HomeItemRecyclerViewAdapter adapter = new HomeItemRecyclerViewAdapter(context, sectionHome.getHomeDataList());
        holder.itemRecyclerView.setAdapter(adapter);

        final int boardId = sectionHome.getSectionLabel().equals("Boards") ? 2 : -1;

        holder.itemRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, holder.itemRecyclerView,
                new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int homeItemId = sectionHome.getHomeDataList().get(position).getId();
                String homeItemName = sectionHome.getHomeDataList().get(position).getName();
                Intent intent = new Intent(context, StandardActivity.class);
                intent.putExtra("home_item_id", homeItemId);
                intent.putExtra("board_id", boardId);
                intent.putExtra("home_item_name", homeItemName);
                context.startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public int getItemCount() {
        return sectionHomeArrayList.size();
    }

    static class HomeSectionViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel;
        private RecyclerView itemRecyclerView;

        HomeSectionViewHolder(View itemView) {
            super(itemView);
            sectionLabel = (TextView) itemView.findViewById(R.id.home_section_label);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.home_item_recycler_view);
        }
    }
}

