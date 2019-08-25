package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.retrofit_model.SectionedHome;
import in.prepskool.prepskoolacademy.utils.IntentData;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.utils.RecyclerViewType;
import in.prepskool.prepskoolacademy.activities.StandardActivity;
import in.prepskool.prepskoolacademy.model.SectionHome;

public class HomeSectionRecyclerViewAdapter
        extends RecyclerView.Adapter<HomeSectionRecyclerViewAdapter.HomeSectionViewHolder> {

    static class HomeSectionViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel;
        private RecyclerView itemRecyclerView;

        HomeSectionViewHolder(View itemView) {
            super(itemView);
            sectionLabel = (TextView) itemView.findViewById(R.id.home_section_label);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.home_item_recycler_view);
        }
    }

    private Context context;
    private RecyclerViewType recyclerViewType;
    private ArrayList<SectionedHome> sectionHomeArrayList;

    public HomeSectionRecyclerViewAdapter(Context context, RecyclerViewType recyclerViewType,
                                          ArrayList<SectionedHome> sectionHomeArrayList) {
        this.context = context;
        this.recyclerViewType = recyclerViewType;
        this.sectionHomeArrayList = sectionHomeArrayList;
    }

    @NonNull
    @Override
    public HomeSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_section,
                parent, false);
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

        HomeItemRecyclerViewAdapter adapter = new HomeItemRecyclerViewAdapter(context,
                sectionHome.getHomeDataList());
        holder.itemRecyclerView.setAdapter(adapter);

        /*holder.itemRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(context,
                holder.itemRecyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                IntentData.CATEGORY_HOME = sectionHome.getSectionLabel();
                IntentData.SUBCATEGORY_HOME = sectionHome.getItemArrayList().get(position).getLabel();
                IntentData.SUBCATEGORY_HOME = IntentData.SUBCATEGORY_HOME.replace("NCERT ", "");

                switch (IntentData.CATEGORY_HOME) {

                    case "SCHOOL BOARDS": {

                        Intent intent = new Intent(context, StandardActivity.class);
                        intent.putExtra("CATEGORY_HOME", IntentData.CATEGORY_HOME);
                        intent.putExtra("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME + " BOARD");
                        intent.putExtra("type", "0");
                        context.startActivity(intent);
                        break;
                    }
                    case "NCERT": {

                        Intent intent = new Intent(context, StandardActivity.class);
                        intent.putExtra("CATEGORY_HOME", IntentData.CATEGORY_HOME);
                        intent.putExtra("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME);
                        intent.putExtra("type", "0");
                        context.startActivity(intent);
                        break;
                    }

                    case "CBSE PRACTICE PAPERS": {

                        Intent intent = new Intent(context, StandardActivity.class);
                        intent.putExtra("CATEGORY_HOME", IntentData.CATEGORY_HOME);
                        intent.putExtra("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME);
                        intent.putExtra("BOARD", "cbse board");
                        intent.putExtra("type", "1");
                        context.startActivity(intent);
                        break;
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/
    }

    @Override
    public int getItemCount() {
        return sectionHomeArrayList.size();
    }
}

