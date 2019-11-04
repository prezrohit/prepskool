package in.prepskool.prepskoolacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.app.AppSharedPreferences;
import in.prepskool.prepskoolacademy.app.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.ResourceTypeAdapter;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceType;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceTypeResponse;
import in.prepskool.prepskoolacademy.utils.ApiHeaders;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResourceTypeActivity extends AppCompatActivity {
    @Inject
    Retrofit retrofit;

    private TextView lblNoData;
    private RecyclerView rvResourceType;
    private ProgressBar progressBar;
    private ArrayList<ResourceType> resourceTypeList;

    private static final String TAG = "ResourceTypeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_type);

        final int homeItemId = getIntent().getIntExtra("home_item_id", -1);
        final String homeItemName = getIntent().getStringExtra("home_item_name");
        final String standardName = getIntent().getStringExtra("standard_name");
        final String subjectName = getIntent().getStringExtra("subject_name");
        final int boardId = getIntent().getIntExtra("board_id", -1);
        final int standardId = getIntent().getIntExtra("standard_id", -1);
        final int subjectId = getIntent().getIntExtra("subject_id", -1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Choose a Resource Type");
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

        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.lbl_breadcrumb_resource_type);
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + homeItemName
                + "</font></small> >> <small><font color=\"#12c48b\">" + standardName + "</font></small> >> <small><font color='red'>"
                + subjectName + "</font></small>", new HtmlResImageGetter(htmlTextView));

        rvResourceType = findViewById(R.id.rv_resource_type);
        lblNoData = findViewById(R.id.lbl_no_data_type);
        progressBar = findViewById(R.id.progress_bar);
        lblNoData.setVisibility(View.GONE);

        ((PrepskoolApplication) getApplication()).getResourceTypeComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        getResourceTypes(apiInterface, standardId, subjectId);

        rvResourceType.addOnItemTouchListener(new RecyclerTouchListener(this, rvResourceType, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int resourceTypeId = resourceTypeList.get(position).getId();

                Intent intent = new Intent(ResourceTypeActivity.this, ResourceActivity.class);
                intent.putExtra("home_item_id", homeItemId);
                intent.putExtra("home_item_name", homeItemName);
                intent.putExtra("standard_name", standardName);
                intent.putExtra("subject_name", subjectName);
                intent.putExtra("resource_type_name", resourceTypeList.get(position).getName());
                intent.putExtra("board_id", boardId);
                intent.putExtra("standard_id", standardId);
                intent.putExtra("subject_id", subjectId);
                intent.putExtra("resource_type_id", resourceTypeId);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    /**
     * @param apiInterface
     * @param standardId
     * @param subjectId
     */
    private void getResourceTypes(ApiInterface apiInterface, int standardId, int subjectId) {
        AppSharedPreferences appSharedPreferences = new AppSharedPreferences(this);
        progressBar.setVisibility(View.VISIBLE);
        Call<ResourceTypeResponse> call = apiInterface.getResourceTypes(standardId, subjectId);
        call.enqueue(new Callback<ResourceTypeResponse>() {
            @Override
            public void onResponse(@NonNull Call<ResourceTypeResponse> call, @NonNull Response<ResourceTypeResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.message());
                if (response.isSuccessful()) {
                    resourceTypeList = response.body().getResourceTypeList();
                    rvResourceType.setLayoutManager(new LinearLayoutManager(ResourceTypeActivity.this));
                    rvResourceType.setAdapter(new ResourceTypeAdapter(ResourceTypeActivity.this, resourceTypeList));

                } else {
                    lblNoData.setVisibility(View.VISIBLE);
                    rvResourceType.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResourceTypeResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}
