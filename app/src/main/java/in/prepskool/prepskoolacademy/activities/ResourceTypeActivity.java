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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.ResourceTypeAdapter;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceType;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceTypeResponse;
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

    private static final String TAG = "ResourceTypeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        Toolbar toolbar = findViewById(R.id.toolbar);
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

        rvResourceType = findViewById(R.id.rv_resource_type);
        lblNoData = findViewById(R.id.lbl_no_data_type);
        progressBar = findViewById(R.id.progress_bar);
        lblNoData.setVisibility(View.GONE);

        ((PrepskoolApplication) getApplication()).getResourceTypeComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        getResourceTypes(apiInterface);

        /*HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.tvBreadCrumbType);
        // loads html from string and displays cat_pic.png from the app's drawable folder
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + SUBCATEGORY_HOME.replace(" BOARD", "")
                + "</font></small> >> <small><font color=\"#12c48b\">" + standards.get(STANDARD) + "</font></small> >> <small><font color='red'>"
                + SUBJECT + "</font></small>", new HtmlResImageGetter(htmlTextView));*/
    }

    private void getResourceTypes(ApiInterface apiInterface) {
        progressBar.setVisibility(View.VISIBLE);
        Call<ResourceTypeResponse> call = apiInterface.getResourceTypes();
        call.enqueue(new Callback<ResourceTypeResponse>() {
            @Override
            public void onResponse(@NonNull Call<ResourceTypeResponse> call, @NonNull Response<ResourceTypeResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.message());
                if (response.isSuccessful()) {
                    ArrayList<ResourceType> resourceTypeList = response.body().getResourceTypeList();
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
