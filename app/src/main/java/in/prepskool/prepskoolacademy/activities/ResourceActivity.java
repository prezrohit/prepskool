package in.prepskool.prepskoolacademy.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.SectionRecyclerViewAdapter;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.Resource;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceResponse;
import in.prepskool.prepskoolacademy.retrofit_model.SectionedResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResourceActivity extends AppCompatActivity {
    @Inject
    Retrofit retrofit;

    private TextView lblNoData;
    private ProgressBar progressBar;
    private RecyclerView rvSectionedResource;

    private SectionRecyclerViewAdapter adapter;
    private ArrayList<SectionedResource> sectionedResourceList;

    private static final String TAG = "ResourceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        progressBar = findViewById(R.id.progress_bar);
        lblNoData = (TextView) findViewById(R.id.lbl_no_data_resource);
        lblNoData.setVisibility(View.GONE);
        setUpRecyclerView();

        ((PrepskoolApplication) getApplication()).getResourceComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        getResources(apiInterface);
    }

    private void setUpRecyclerView() {
        sectionedResourceList = new ArrayList<>();
        rvSectionedResource = (RecyclerView) findViewById(R.id.rv_sectioned_resource);
        rvSectionedResource.setHasFixedSize(true);
        rvSectionedResource.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getResources(ApiInterface apiInterface) {
        progressBar.setVisibility(View.VISIBLE);
        Call<ResourceResponse> call = apiInterface.getResources();
        call.enqueue(new Callback<ResourceResponse>() {
            @Override
            public void onResponse(@NonNull Call<ResourceResponse> call, @NonNull Response<ResourceResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.message());
                if (response.isSuccessful()) {
                    ArrayList<Resource> resourceList = response.body().getResourceList();
                    for (Resource resource : resourceList) {
                        sectionedResourceList.add(new SectionedResource(resource.getYear(), resourceList));
                        rvSectionedResource.setAdapter(new SectionRecyclerViewAdapter(ResourceActivity.this, sectionedResourceList));
                    }

                } else {
                    lblNoData.setVisibility(View.VISIBLE);
                    rvSectionedResource.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResourceResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
