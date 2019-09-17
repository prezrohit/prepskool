package in.prepskool.prepskoolacademy.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.SubjectAdapter;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.Subject;
import in.prepskool.prepskoolacademy.retrofit_model.SubjectResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScienceFragment extends Fragment {

    private ArrayList<Subject> subjectList;

    private static final String TAG = "ScienceFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_science, container, false);

        if (getArguments() != null) {
            subjectList = (ArrayList<Subject>) getArguments().getSerializable("list");
        }

        RecyclerView rvScience = view.findViewById(R.id.rv_science);
        TextView lblNoData = (TextView) view.findViewById(R.id.tv_no_data_science);
        lblNoData.setVisibility(View.GONE);

        if (subjectList == null || subjectList.isEmpty()) {
            lblNoData.setVisibility(View.VISIBLE);
            rvScience.setVisibility(View.GONE);
        }

        rvScience.setLayoutManager(new LinearLayoutManager(getActivity()));
        SubjectAdapter subjectAdapter = new SubjectAdapter(getActivity(), subjectList);
        rvScience.setAdapter(subjectAdapter);

        return view;
    }
}
