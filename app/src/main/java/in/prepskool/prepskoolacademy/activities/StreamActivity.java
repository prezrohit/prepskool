package in.prepskool.prepskoolacademy.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.prepskool.prepskoolacademy.IntentData;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.ViewPagerAdapter;
import in.prepskool.prepskoolacademy.fragments.ArtFragment;
import in.prepskool.prepskoolacademy.fragments.CommerceFragment;
import in.prepskool.prepskoolacademy.fragments.ScienceFragment;

public class StreamActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private TextView tvCvHeader;
    private String BOARD;
    private TextView tvBreadCrumbStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        IntentData.SUBCATEGORY_HOME = getIntent().getStringExtra("SUBCATEGORY_HOME");
        IntentData.STANDARD = getIntent().getStringExtra("STANDARD");
        IntentData.CATEGORY_HOME = getIntent().getStringExtra("CATEGORY_HOME");
        BOARD = getIntent().getStringExtra("BOARD");

        Bundle data = new Bundle();
        data.putString("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME);
        data.putString("CATEGORY_HOME", IntentData.CATEGORY_HOME);
        data.putString("STANDARD", IntentData.STANDARD);
        data.putString("BOARD", BOARD);

        tvBreadCrumbStream = findViewById(R.id.breadCrumbStream);
        tvBreadCrumbStream.setText(" /" + IntentData.SUBCATEGORY_HOME + " /" + IntentData.STANDARD);

        toolbar = (Toolbar) findViewById(R.id.toolbar_stream);
        toolbar.setTitle(R.string.title_subject);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tablayout);

        ScienceFragment scienceFragment = new ScienceFragment();
        scienceFragment.setArguments(data);
        CommerceFragment commerceFragment = new CommerceFragment();
        commerceFragment.setArguments(data);
        ArtFragment artFragment = new ArtFragment();
        artFragment.setArguments(data);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(scienceFragment, "Science");
        viewPagerAdapter.addFragment(commerceFragment, "Commerce");
        viewPagerAdapter.addFragment(artFragment, "Humanities");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        //back to previous activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
