package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.activities.ResourceTypeActivity;
import in.prepskool.prepskoolacademy.retrofit_model.Subject;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Subject> subjectArrayList;

    private static final String TAG = "SubjectAdapter";

    public SubjectAdapter(Context context, ArrayList<Subject> subjectArrayList) {
        this.subjectArrayList = subjectArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subject, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Subject subject = subjectArrayList.get(position);
        holder.lblSubjectName.setText(subject.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return subjectArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        private TextView lblSubjectName;

        private ViewHolder(View itemView) {

            super(itemView);
            lblSubjectName = (TextView) itemView.findViewById(R.id.lbl_subject_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ResourceTypeActivity.class));
                }
            });
        }
    }
}
