package in.prepskool.prepskoolacademy.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.ViewPagerAdapter;
import in.prepskool.prepskoolacademy.fragments.ArtFragment;
import in.prepskool.prepskoolacademy.fragments.CommerceFragment;
import in.prepskool.prepskoolacademy.fragments.ScienceFragment;

public class StreamActivity extends AppCompatActivity {

    private static final String TAG = "StreamActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        MobileAds.initialize(this, getString(R.string.app_id));

        /*//region breadcrumbs setup
        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.breadCrumbStream);
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + IntentData.SUBCATEGORY_HOME.replace(" BOARD", "")
                        + "</font></small> >> <small><font color=\"#12c48b\">" + IntentData.STANDARD +"</font></small>", new HtmlResImageGetter(htmlTextView));
        //endregion*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_stream);
        toolbar.setTitle(R.string.title_subject);
        setSupportActionBar(toolbar);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("658CE1DF8EB039135583BF17C48E41D8").build();
        mAdView.loadAd(adRequest);

        ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tablayout);

        ScienceFragment scienceFragment = new ScienceFragment();
        CommerceFragment commerceFragment = new CommerceFragment();
        ArtFragment artFragment = new ArtFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(scienceFragment, "Science");
        viewPagerAdapter.addFragment(commerceFragment, "Commerce");
        viewPagerAdapter.addFragment(artFragment, "Humanities");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

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
}
