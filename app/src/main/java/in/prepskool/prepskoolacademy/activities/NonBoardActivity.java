package in.prepskool.prepskoolacademy.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class NonBoardActivity extends AppCompatActivity {
    @Inject
    Retrofit retrofit;

    private TextView lblNoData;
    private RecyclerView rvNonBoard;
    private ProgressBar progressBar;

    private static final String TAG = "NonBoardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_board);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_subject);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lblNoData = (TextView) findViewById(R.id.lbl_no_data_non_board);
        progressBar = findViewById(R.id.progress_bar);
        rvNonBoard = findViewById(R.id.rv_non_board);
        lblNoData.setVisibility(View.GONE);

        rvNonBoard.setLayoutManager(new LinearLayoutManager(this));

        ((PrepskoolApplication) getApplication()).getSubjectComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        getNonBoardSubjects(apiInterface);

        /*HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.bread_crumb_non_board);
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + SUBCATEGORY_HOME.replace(" BOARD", "")
                        + "</font></small> >> <small><font color=\"#12c48b\">" + standards.get(STANDARD)
                + "</font></small>", new HtmlResImageGetter(htmlTextView));*/
    }

    private void getNonBoardSubjects(ApiInterface apiInterface) {
        progressBar.setVisibility(View.VISIBLE);
        Call<SubjectResponse> call = apiInterface.getSubjects();
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(@NonNull Call<SubjectResponse> call, @NonNull Response<SubjectResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.message());
                if (response.isSuccessful()) {
                    ArrayList<Subject> subjectList = response.body().getSubjectList();
                    rvNonBoard.setAdapter(new SubjectAdapter(NonBoardActivity.this, subjectList));

                } else {
                    lblNoData.setVisibility(View.VISIBLE);
                    rvNonBoard.setVisibility(View.GONE);
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
