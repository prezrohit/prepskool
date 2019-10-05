package in.prepskool.prepskoolacademy.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.activities.ResourceActivity;
import in.prepskool.prepskoolacademy.activities.ResourceTypeActivity;
import in.prepskool.prepskoolacademy.adapter.SubjectAdapter;
import in.prepskool.prepskoolacademy.retrofit_model.Subject;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;

public class ArtFragment extends Fragment {

    private static final String TAG = "ArtFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_arts, container, false);

        final ArrayList<Subject> subjectList = (ArrayList<Subject>) getArguments().getSerializable("list");
        final int homeItemId = getArguments().getInt("home_item_id", -1);
        final int boardId = getArguments().getInt("board_id", -1);
        final int standardId = getArguments().getInt("standard_id", -1);
        final String homeItemName = getArguments().getString("home_item_name");
        final String standardName = getArguments().getString("standard_name");

        RecyclerView rvArts = (RecyclerView) view.findViewById(R.id.rv_arts);
        TextView lblNoData = (TextView) view.findViewById(R.id.tv_no_data_arts);
        lblNoData.setVisibility(View.GONE);

        if (subjectList == null || subjectList.isEmpty()) {
            lblNoData.setVisibility(View.VISIBLE);
            rvArts.setVisibility(View.GONE);

        } else {

            rvArts.setLayoutManager(new LinearLayoutManager(getActivity()));
            SubjectAdapter subjectAdapter = new SubjectAdapter(getActivity(), subjectList);
            rvArts.setAdapter(subjectAdapter);

            rvArts.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvArts, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    int subjectId = subjectList.get(position).getId();
                    Intent intent;
                    if (boardId == 2) {           // for board Category
                        intent = new Intent(getActivity(), ResourceTypeActivity.class);

                    } else {
                        intent = new Intent(getActivity(), ResourceActivity.class);
                    }
                    intent.putExtra("home_item_id", homeItemId);
                    intent.putExtra("home_item_name", homeItemName);
                    intent.putExtra("standard_name", standardName);
                    intent.putExtra("subject_name", subjectList.get(position).getName());
                    intent.putExtra("board_id", boardId);
                    intent.putExtra("standard_id", standardId);
                    intent.putExtra("subject_id", subjectId);
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }

        return view;
    }
}
