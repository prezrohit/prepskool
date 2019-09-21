package in.prepskool.prepskoolacademy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class CommerceFragment extends Fragment {

    private static final String TAG = "CommerceFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_commerce, container, false);

        final ArrayList<Subject> subjectList = (ArrayList<Subject>) getArguments().getSerializable("list");
        final int boardId = getArguments().getInt("board_id", -1);
        final int standardId = getArguments().getInt("standard_id", -1);
        final String sectionName = getArguments().getString("section_name");
        final String standardName = getArguments().getString("standard_name");

        RecyclerView rvCommerce = view.findViewById(R.id.rv_commerce);
        TextView lblNoData = (TextView) view.findViewById(R.id.tv_no_data_commerce);
        lblNoData.setVisibility(View.GONE);

        if (subjectList == null || subjectList.isEmpty()) {
            lblNoData.setVisibility(View.VISIBLE);
            rvCommerce.setVisibility(View.GONE);

        } else {

            rvCommerce.setLayoutManager(new LinearLayoutManager(getActivity()));
            SubjectAdapter subjectAdapter = new SubjectAdapter(getActivity(), subjectList);
            rvCommerce.setAdapter(subjectAdapter);

            rvCommerce.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvCommerce, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    int subjectId = subjectList.get(position).getId();
                    Intent intent;
                    if (boardId == 2) {           // for board Category
                        intent = new Intent(getActivity(), ResourceTypeActivity.class);

                    } else {
                        intent = new Intent(getActivity(), ResourceActivity.class);
                    }
                    intent.putExtra("section_name", sectionName);
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
