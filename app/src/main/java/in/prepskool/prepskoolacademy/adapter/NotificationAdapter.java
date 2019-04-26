package in.prepskool.prepskoolacademy.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Activity context;
    private ArrayList<Notification> newsArrayList;
    private View v;

    public NotificationAdapter(Activity context, ArrayList<Notification> newsArrayList) {
        this.newsArrayList = newsArrayList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder  {      //storing each component views

        TextView tvChannelName;

        private ViewHolder(View itemView) {

            super(itemView);
            tvChannelName = (TextView) itemView.findViewById(R.id.tv_notification);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Notification news = this.newsArrayList.get(position);
        holder.tvChannelName.setText(news.getName());
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}


