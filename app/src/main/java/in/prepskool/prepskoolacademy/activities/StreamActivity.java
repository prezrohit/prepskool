package in.prepskool.prepskoolacademy.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.HashMap;

import in.prepskool.prepskoolacademy.utils.IntentData;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.ViewPagerAdapter;
import in.prepskool.prepskoolacademy.fragments.ArtFragment;
import in.prepskool.prepskoolacademy.fragments.CommerceFragment;
import in.prepskool.prepskoolacademy.fragments.ScienceFragment;

public class StreamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        HashMap<String, String> standards = new HashMap<>();
        standards.put("Class 12", "12th");
        standards.put("Class 11", "11th");

        IntentData.SUBCATEGORY_HOME = getIntent().getStringExtra("SUBCATEGORY_HOME");
        IntentData.STANDARD = getIntent().getStringExtra("STANDARD");
        IntentData.CATEGORY_HOME = getIntent().getStringExtra("CATEGORY_HOME");
        String BOARD = getIntent().getStringExtra("BOARD");
        int sourceId = getIntent().getIntExtra("source", 0);
        String TYPE = getIntent().getStringExtra("TYPE");

        Bundle data = new Bundle();
        data.putString("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME);
        data.putString("CATEGORY_HOME", IntentData.CATEGORY_HOME);
        data.putString("STANDARD", IntentData.STANDARD);
        data.putString("BOARD", BOARD);
        if (sourceId == 1) {
            data.putInt("source", sourceId);
            data.putString("TYPE", TYPE);
        }

        //region breadcrumbs setup
        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.breadCrumbStream);
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + IntentData.SUBCATEGORY_HOME.replace(" BOARD", "")
                        + "</font></small> >> <small><font color=\"#12c48b\">" + IntentData.STANDARD +"</font></small>", new HtmlResImageGetter(htmlTextView));
        //endregion

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_stream);
        toolbar.setTitle(R.string.title_subject);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tablayout);

        ScienceFragment scienceFragment = new ScienceFragment();
        scienceFragment.setArguments(data);
        CommerceFragment commerceFragment = new CommerceFragment();
        commerceFragment.setArguments(data);
        ArtFragment artFragment = new ArtFragment();
        artFragment.setArguments(data);

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
