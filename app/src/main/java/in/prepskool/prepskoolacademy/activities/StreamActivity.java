package in.prepskool.prepskoolacademy.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.Serializable;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.ViewPagerAdapter;
import in.prepskool.prepskoolacademy.fragments.ArtFragment;
import in.prepskool.prepskoolacademy.fragments.CommerceFragment;
import in.prepskool.prepskoolacademy.fragments.ScienceFragment;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.Stream;
import in.prepskool.prepskoolacademy.retrofit_model.StreamResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StreamActivity extends AppCompatActivity {
    @Inject
    Retrofit retrofit;

    private ProgressBar progressBar;

    private static final String TAG = "StreamActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        MobileAds.initialize(this, getString(R.string.app_id));

        ((PrepskoolApplication) getApplication()).getStreamComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        progressBar = findViewById(R.id.progress_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_stream);
        toolbar.setTitle(R.string.title_subject);
        setSupportActionBar(toolbar);

        AdView mAdView = findViewById(R.id.ad_banner_stream);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("658CE1DF8EB039135583BF17C48E41D8").build();
        mAdView.loadAd(adRequest);

        getStreamsAndSubjects(apiInterface);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getStreamsAndSubjects(ApiInterface apiInterface) {
        Call<StreamResponse> call = apiInterface.getStreams();
        call.enqueue(new Callback<StreamResponse>() {
            @Override
            public void onResponse(@NonNull Call<StreamResponse> call, @NonNull Response<StreamResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.isSuccessful());

                Stream scienceStream = response.body().getStreamList().get(0);
                Stream commerceStream = response.body().getStreamList().get(1);
                Stream artStream = response.body().getStreamList().get(2);

                ViewPager viewPager = findViewById(R.id.pager);
                TabLayout tabLayout = findViewById(R.id.tablayout);

                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                viewPagerAdapter.addFragment(getScienceFragmentWithBundle((Serializable) scienceStream.getSubjectsList()), scienceStream.getName());
                viewPagerAdapter.addFragment(getCommerceFragmentWithBundle((Serializable) commerceStream.getSubjectsList()), commerceStream.getName());
                viewPagerAdapter.addFragment(getArtFragmentWithBundle((Serializable) artStream.getSubjectsList()), artStream.getName());

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onFailure(@NonNull Call<StreamResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(StreamActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ScienceFragment getScienceFragmentWithBundle(Serializable subjectList) {
        ScienceFragment scienceFragment = new ScienceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", subjectList);
        scienceFragment.setArguments(bundle);
        return scienceFragment;
    }

    private CommerceFragment getCommerceFragmentWithBundle(Serializable subjectList) {
        CommerceFragment commerceFragment = new CommerceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", subjectList);
        commerceFragment.setArguments(bundle);
        return commerceFragment;
    }

    private ArtFragment getArtFragmentWithBundle(Serializable subjectList) {
        ArtFragment artFragment = new ArtFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", subjectList);
        artFragment.setArguments(bundle);
        return artFragment;
    }
 }
