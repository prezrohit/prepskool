package in.prepskool.prepskoolacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.app.AppSharedPreferences;
import in.prepskool.prepskoolacademy.app.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.StandardAdapter;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.Standard;
import in.prepskool.prepskoolacademy.retrofit_model.StandardResponse;
import in.prepskool.prepskoolacademy.utils.ApiHeaders;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StandardActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private ProgressBar progressBar;
    private RecyclerView rvStandard;
    private ArrayList<Standard> standardsList;

    private ArrayList<Standard> practicePapersList;

    private int boardId;
    private int homeItemId;

    private static final String TAG = "StandardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);

        final String homeItemName = getIntent().getStringExtra("home_item_name");
        boardId = getIntent().getIntExtra("board_id", -1);
        homeItemId = getIntent().getIntExtra("home_item_id", -1);

        practicePapersList = new ArrayList<>();

        MobileAds.initialize(this, getString(R.string.app_id));

        mAdView = findViewById(R.id.ad_banner_standard);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_ad));

        ((PrepskoolApplication) getApplication()).getStandardComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        rvStandard = findViewById(R.id.rv_standard);
        progressBar = findViewById(R.id.progress_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_standard);

        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.bread_crumb_standard);
         //loads html from string and displays cat_pic.png from the app's drawable folder
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + homeItemName + "</font></small>",
                new HtmlResImageGetter(htmlTextView));

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

        rvStandard.addOnItemTouchListener(new RecyclerTouchListener(this, rvStandard, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mInterstitialAd.isLoaded()) {
                    Log.d(TAG, "onClick: interstitial loaded");
                    Toast.makeText(StandardActivity.this, "interstitial loaded", Toast.LENGTH_SHORT).show();
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial wasn't loaded yet.");

                    String standardName;
                    int standardId;
                    Intent intent;

                    if (boardId == -1 && (homeItemId == 7 || homeItemId == 3 || homeItemId == 9)) {
                        standardId = practicePapersList.get(position).getId();
                        standardName = practicePapersList.get(position).getName();

                    } else {
                        standardId = standardsList.get(position).getId();
                        standardName = standardsList.get(position).getName();
                    }

                    if (standardId == 6 || standardId == 7) {                // 6 for 11th class and 7 for 12th class
                        intent = new Intent(StandardActivity.this, StreamActivity.class);

                    } else {
                        intent = new Intent(StandardActivity.this, NonBoardActivity.class);
                    }

                    intent.putExtra("standard_id", standardId);
                    intent.putExtra("home_item_name", homeItemName);
                    intent.putExtra("standard_name", standardName);
                    intent.putExtra("board_id", boardId);
                    intent.putExtra("home_item_id", homeItemId);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }


    /**
     *
     * @param apiInterface
     */
    private void getStandard(ApiInterface apiInterface) {
        progressBar.setVisibility(View.VISIBLE);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("658CE1DF8EB039135583BF17C48E41D8").build();
        Call<StandardResponse> call = apiInterface.getStandards();
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.message());

                if (response.isSuccessful()) {
                    assert response.body() != null : "Server Response is Empty";
                    if (boardId == -1 && homeItemId == 9) {
                        for (Standard standard : response.body().getStandardsList()) {
                            if (standard.getId() == 5 || standard.getId() == 7) {
                                practicePapersList.add(standard);
                            }
                        }

                        StandardAdapter standardAdapter = new StandardAdapter(StandardActivity.this, practicePapersList);
                        rvStandard.setLayoutManager(new GridLayoutManager(StandardActivity.this, 3));
                        rvStandard.setAdapter(standardAdapter);

                    } else if (boardId == -1 && (homeItemId == 7 || homeItemId == 3)) {
                        for (Standard standard : response.body().getStandardsList()) {
                            if (standard.getId() == 5 || standard.getId() == 6 || standard.getId() == 7) {
                                practicePapersList.add(standard);
                            }
                        }

                        StandardAdapter standardAdapter = new StandardAdapter(StandardActivity.this, practicePapersList);
                        rvStandard.setLayoutManager(new GridLayoutManager(StandardActivity.this, 3));
                        rvStandard.setAdapter(standardAdapter);

                    } else {
                        standardsList = response.body().getStandardsList();
                        StandardAdapter standardAdapter = new StandardAdapter(StandardActivity.this, standardsList);
                        rvStandard.setLayoutManager(new GridLayoutManager(StandardActivity.this, 3));
                        rvStandard.setAdapter(standardAdapter);
                    }

                    mAdView.loadAd(adRequest);
                    mInterstitialAd.loadAd(adRequest);

                } else {
                    Toast.makeText(StandardActivity.this, "Error Communicating with Server", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Error Communicating with Server");
                } 
            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(StandardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                mAdView.loadAd(adRequest);
                mInterstitialAd.loadAd(adRequest);
            }
        });
    }
}
