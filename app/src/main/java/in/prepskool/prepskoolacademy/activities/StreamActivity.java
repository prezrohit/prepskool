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

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.Serializable;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.app.AppSharedPreferences;
import in.prepskool.prepskoolacademy.app.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.ViewPagerAdapter;
import in.prepskool.prepskoolacademy.fragments.ArtFragment;
import in.prepskool.prepskoolacademy.fragments.CommerceFragment;
import in.prepskool.prepskoolacademy.fragments.ScienceFragment;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.Stream;
import in.prepskool.prepskoolacademy.retrofit_model.StreamResponse;
import in.prepskool.prepskoolacademy.utils.ApiHeaders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StreamActivity extends AppCompatActivity {
    @Inject
    Retrofit retrofit;

    private String homeItemName;
    private String standardName;

    private ProgressBar progressBar;
    private static final String TAG = "StreamActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        int homeItemId = getIntent().getIntExtra("home_item_id", -1);
        homeItemName = getIntent().getStringExtra("home_item_name");
        standardName = getIntent().getStringExtra("standard_name");
        int boardId = getIntent().getIntExtra("board_id", -1);
        int standardId = getIntent().getIntExtra("standard_id", -1);

        Log.d(TAG, "onCreate: " + homeItemId);

        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.bread_crumb_stream);
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + homeItemName + "</font></small> >> <small><font color=\"#12c48b\">"
                + standardName +"</font></small>", new HtmlResImageGetter(htmlTextView));

        MobileAds.initialize(this, getString(R.string.app_id));

        ((PrepskoolApplication) getApplication()).getStreamComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        progressBar = findViewById(R.id.progress_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_stream);
        toolbar.setTitle(R.string.title_subject);
        setSupportActionBar(toolbar);

        getStreamsAndSubjects(apiInterface, homeItemId, boardId, standardId);

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


    /**
     *
     * @param apiInterface
     * @param boardId
     * @param standardId
     */
    private void getStreamsAndSubjects(ApiInterface apiInterface, final int homeItemId, final int boardId, final int standardId) {
        AppSharedPreferences appSharedPreferences = new AppSharedPreferences(this);
        Call<StreamResponse> call = apiInterface.getStreams(ApiHeaders.ACCEPT_VALUE, ApiHeaders.BEARER + appSharedPreferences.getToken(), standardId);
        call.enqueue(new Callback<StreamResponse>() {
            @Override
            public void onResponse(@NonNull Call<StreamResponse> call, @NonNull Response<StreamResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {

                    Stream scienceStream = response.body().getStreamList().get(0);
                    Stream commerceStream = response.body().getStreamList().get(1);
                    Stream artStream = response.body().getStreamList().get(2);

                    ViewPager viewPager = findViewById(R.id.pager);
                    TabLayout tabLayout = findViewById(R.id.tablayout);

                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    viewPagerAdapter.addFragment(getScienceFragmentWithBundle((Serializable) scienceStream.getSubjectsList(), homeItemId, boardId, standardId),
                            scienceStream.getName());

                    viewPagerAdapter.addFragment(getCommerceFragmentWithBundle((Serializable) commerceStream.getSubjectsList(), homeItemId, boardId, standardId),
                            commerceStream.getName());

                    viewPagerAdapter.addFragment(getArtFragmentWithBundle((Serializable) artStream.getSubjectsList(), boardId, homeItemId, standardId),
                            artStream.getName());

                    viewPager.setAdapter(viewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                }
                else
                    Toast.makeText(StreamActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                AdView mAdView = findViewById(R.id.ad_banner_stream);
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("658CE1DF8EB039135583BF17C48E41D8").build();
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onFailure(@NonNull Call<StreamResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(StreamActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                AdView mAdView = findViewById(R.id.ad_banner_stream);
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("658CE1DF8EB039135583BF17C48E41D8").build();
                mAdView.loadAd(adRequest);
            }
        });
    }


    /**
     *
     * @param subjectList
     * @return
     */
    private ScienceFragment getScienceFragmentWithBundle(Serializable subjectList, int homeItemId, int boardId, int standardId) {
        ScienceFragment scienceFragment = new ScienceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", subjectList);
        bundle.putInt("home_item_id", homeItemId);
        bundle.putString("home_item_name", homeItemName);
        bundle.putString("standard_name", standardName);
        bundle.putInt("board_id", boardId);
        bundle.putInt("standard_id", standardId);
        scienceFragment.setArguments(bundle);
        return scienceFragment;
    }


    /**
     *
     * @param subjectList
     * @return
     */
    private CommerceFragment getCommerceFragmentWithBundle(Serializable subjectList, int homeItemId, int boardId, int standardId) {
        CommerceFragment commerceFragment = new CommerceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", subjectList);
        bundle.putInt("home_item_id", homeItemId);
        bundle.putString("home_item_name", homeItemName);
        bundle.putString("standard_name", standardName);
        bundle.putInt("board_id", boardId);
        bundle.putInt("standard_id", standardId);
        commerceFragment.setArguments(bundle);
        return commerceFragment;
    }


    /**
     *
     * @param subjectList
     * @return
     */
    private ArtFragment getArtFragmentWithBundle(Serializable subjectList, int homeItemId, int boardId, int standardId) {
        ArtFragment artFragment = new ArtFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", subjectList);
        bundle.putInt("home_item_id", homeItemId);
        bundle.putString("home_item_name", homeItemName);
        bundle.putString("standard_name", standardName);
        bundle.putInt("board_id", boardId);
        bundle.putInt("standard_id", standardId);
        artFragment.setArguments(bundle);
        return artFragment;
    }
 }
