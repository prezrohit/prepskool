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
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.SectionRecyclerViewAdapter;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceList;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceResponse;
import in.prepskool.prepskoolacademy.retrofit_model.SectionedResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResourceActivity extends AppCompatActivity {
    @Inject
    Retrofit retrofit;

    private String resourceTypeName = "!";
    private int resourceTypeId;

    private TextView lblNoData;
    private ProgressBar progressBar;
    private RecyclerView rvSectionedResource;

    private SectionRecyclerViewAdapter sectionRecyclerViewAdapter;
    private ArrayList<Object> sectionedResourceList;

    // The number of native ads to load and display.
    public static final int NUMBER_OF_ADS = 5;

    // The AdLoader used to load ads.
    private AdLoader adLoader;

    // List of native ads that have been successfully loaded.
    private ArrayList<UnifiedNativeAd> mNativeAds = new ArrayList<>();

    private static final String TAG = "ResourceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        final String sectionName = getIntent().getStringExtra("section_name");
        final String standardName = getIntent().getStringExtra("standard_name");
        final String subjectName = getIntent().getStringExtra("subject_name");
        int boardId = getIntent().getIntExtra("board_id", -1);
        int standardId = getIntent().getIntExtra("standard_id", -1);
        int subjectId = getIntent().getIntExtra("subject_id", -1);

        if (boardId == 2) {
            resourceTypeId = getIntent().getIntExtra("resource_type_id", -1);
            resourceTypeName = getIntent().getStringExtra("resource_type_name");
        }

        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.lbl_breadcrumb_resource);
        if (boardId == 2) {
            htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + sectionName + "</font></small> >> <small><font color=\"#12c48b\">"
                    + standardName + "</font></small> >> <small><font color='#ff6347'>" + subjectName
                    + "</font></small> >> <small><font color='#ffca28'>" + resourceTypeName + "</font></small>",
                    new HtmlResImageGetter(htmlTextView));
        } else {
            htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + sectionName + "</font></small> >> <small><font color=\"#12c48b\">"
                    + standardName + "</font></small> >> <small><font color='#ffca28'>" + subjectName + "</font></small>",
                    new HtmlResImageGetter(htmlTextView));
        }

        MobileAds.initialize(this, getString(R.string.app_id));

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

        loadNativeAds();

        ((PrepskoolApplication) getApplication()).getResourceComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        getResources(apiInterface, boardId, standardId, subjectId);
    }

    private void setUpRecyclerView() {
        sectionedResourceList = new ArrayList<>();
        sectionRecyclerViewAdapter = new SectionRecyclerViewAdapter(ResourceActivity.this, sectionedResourceList);
        rvSectionedResource = (RecyclerView) findViewById(R.id.rv_sectioned_resource);
        rvSectionedResource.setHasFixedSize(true);
        rvSectionedResource.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getResources(ApiInterface apiInterface, int boardId, int standardId, int subjectId) {
        progressBar.setVisibility(View.VISIBLE);
        Call<ResourceResponse> call;
        if (boardId == 2) {

            Log.d(TAG, "resourceTypeID: " + resourceTypeId);
            Log.d(TAG, "boardId: " + boardId);
            call = apiInterface.getBoardResources(boardId, standardId, subjectId, resourceTypeId);
            call.enqueue(new Callback<ResourceResponse>() {
                @Override
                public void onResponse(@NonNull Call<ResourceResponse> call, @NonNull Response<ResourceResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: " + response.message());
                    if (response.isSuccessful()) {
                        ArrayList<ResourceList> responseList = response.body().getResourceList();
                        for (ResourceList resourceList : responseList) {
                            sectionedResourceList.add(new SectionedResource(resourceList.getYear(), resourceList.getResourceList()));
                        }
                        sectionRecyclerViewAdapter = new SectionRecyclerViewAdapter(ResourceActivity.this, sectionedResourceList);
                        rvSectionedResource.setAdapter(sectionRecyclerViewAdapter);

                    } else {
                        lblNoData.setVisibility(View.VISIBLE);
                        rvSectionedResource.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResourceResponse> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Toast.makeText(ResourceActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            call = apiInterface.getOtherResources(standardId, subjectId);
            call.enqueue(new Callback<ResourceResponse>() {
                @Override
                public void onResponse(@NonNull Call<ResourceResponse> call, @NonNull Response<ResourceResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: " + response.message());
                    if (response.isSuccessful()) {
                        ArrayList<ResourceList> responseList = response.body().getResourceList();
                        for (ResourceList resourceList : responseList) {
                            sectionedResourceList.add(new SectionedResource(resourceList.getYear(), resourceList.getResourceList()));
                        }
                        sectionRecyclerViewAdapter = new SectionRecyclerViewAdapter(ResourceActivity.this, sectionedResourceList);
                        rvSectionedResource.setAdapter(sectionRecyclerViewAdapter);

                    } else {
                        lblNoData.setVisibility(View.VISIBLE);
                        rvSectionedResource.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResourceResponse> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Toast.makeText(ResourceActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNativeAds() {

        progressBar.setVisibility(View.VISIBLE);
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_ad_unit_id));
        adLoader = builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // A native ad loaded successfully, check if the ad loader has finished loading
                // and if so, insert the ads into the list.
                Log.d(TAG, "onUnifiedNativeAdLoaded: loaded");
                progressBar.setVisibility(View.GONE);
                mNativeAds.add(unifiedNativeAd);
                if (!adLoader.isLoading()) {
                    insertAdsInMenuItems();
                }

            }
        }).withAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d(TAG, "onAdLoaded: loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // A native ad failed to load, check if the ad loader has finished loading
                // and if so, insert the ads into the list.
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "AD Failed error code: " + errorCode);
                if (!adLoader.isLoading()) {
                    insertAdsInMenuItems();
                }
                Log.e(TAG, "onAdFailedToLoad: The previous native ad failed to load. Attempting to load another.");
            }
        }).build();

        // Load the Native Express ad.
        adLoader.loadAds(new AdRequest.Builder().addTestDevice("658CE1DF8EB039135583BF17C48E41D8").build(), NUMBER_OF_ADS);
    }

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            Log.d(TAG, "insertAdsInMenuItems: adList empty");
            return;
        }

        int offset = (sectionedResourceList.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad : mNativeAds) {
            sectionedResourceList.add(index, ad);
            index = index + offset;
        }
        sectionRecyclerViewAdapter.notifyDataSetChanged();
    }
}
