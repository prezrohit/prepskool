package in.prepskool.prepskoolacademy.fragments;

import android.content.Context;
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

public class ArtFragment extends Fragment {
    @Inject
    Retrofit retrofit;

    private ProgressBar progressBar;
    private RecyclerView rvArts;
    private TextView lblNoData;

    private static final String TAG = "ArtFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_arts, container, false);

        progressBar = (ProgressBar)  view.findViewById(R.id.progress_bar);
        rvArts = (RecyclerView) view.findViewById(R.id.rv_arts);
        lblNoData = (TextView) view.findViewById(R.id.tv_no_data_arts);
        lblNoData.setVisibility(View.GONE);

        ((PrepskoolApplication) getActivity().getApplication()).getSubjectComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        getArtsSubjects(view, apiInterface);

        return view;
    }

    private void getArtsSubjects(final View view, ApiInterface apiInterface) {
        progressBar.setVisibility(View.VISIBLE);
        Call<SubjectResponse> call = apiInterface.getSubjects();
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(@NonNull Call<SubjectResponse> call, @NonNull Response<SubjectResponse> response) {

                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.message());
                if (response.isSuccessful()) {
                    ArrayList<Subject> subjectsList = response.body().getSubjectList();
                    rvArts.setLayoutManager(new LinearLayoutManager(getContext()));
                    SubjectAdapter subjectAdapter = new SubjectAdapter(getContext(), subjectsList);
                    rvArts.setAdapter(subjectAdapter);

                } else {
                    rvArts.setVisibility(View.GONE);
                    lblNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SubjectResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}






















/*rvArts.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvArts,
                new RecyclerTouchListener.ClickListener() {

                    @Override
                    public void onClick(View view, int position) {

                        SUBJECT = arrayList.get(position).getName();

                        Intent intent = new Intent(getActivity(), ResourceActivity.class);
                        intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                        intent.putExtra("SUBJECT", SUBJECT);
                        intent.putExtra("STANDARD", STANDARD);
                        intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);

                        if (CATEGORY_HOME.equals("SCHOOL BOARDS")) {

                            if (sourceId == 1) {
                                intent.putExtra("TYPE", TYPE);
                                intent.putExtra("source", 1);
                            }

                            else {
                                intent = new Intent(getActivity(), ResourceTypeActivity.class);
                                intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                                intent.putExtra("SUBJECT", SUBJECT);
                                intent.putExtra("STANDARD", STANDARD);
                                intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                            }
                        }
                        else if (CATEGORY_HOME.equals("CBSE PRACTICE PAPERS"))
                            intent.putExtra("BOARD", BOARD);

                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));*/


