package in.prepskool.prepskoolacademy.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.StandardAdapter;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.Standard;
import in.prepskool.prepskoolacademy.retrofit_model.StandardResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StandardActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private ProgressBar progressBar;

    private static final String TAG = "StandardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);

        MobileAds.initialize(this, getString(R.string.app_id));

        ((PrepskoolApplication) getApplication()).getStandardComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        progressBar = findViewById(R.id.progress_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_standard);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("658CE1DF8EB039135583BF17C48E41D8").build();
        mAdView.loadAd(adRequest);

        //HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.breadCrumbStandard);
        // loads html from string and displays cat_pic.png from the app's drawable folder
        //htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + SUBCATEGORY_HOME.replace(" BOARD", "") + "</font></small>", new HtmlResImageGetter(htmlTextView));

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

        getStandard(apiInterface);
    }

    private void getStandard(ApiInterface apiInterface) {
        progressBar.setVisibility(View.VISIBLE);
        Call<StandardResponse> call = apiInterface.getStandards();
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.message());

                if (response.isSuccessful()) {
                    ArrayList<Standard> standardsList = response.body().getStandardsList();
                    StandardAdapter standardAdapter = new StandardAdapter(StandardActivity.this, standardsList);
                    RecyclerView rvStandard = findViewById(R.id.rv_standard);
                    rvStandard.setLayoutManager(new GridLayoutManager(StandardActivity.this, 3));
                    rvStandard.setAdapter(standardAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}
