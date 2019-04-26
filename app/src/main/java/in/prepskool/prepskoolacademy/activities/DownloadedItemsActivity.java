package in.prepskool.prepskoolacademy.activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.adapter.DownloadedItemsAdapter;
import in.prepskool.prepskoolacademy.model.Home;

public class DownloadedItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private ArrayList<Home> arrayList;
    private DownloadedItemsAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_items);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.rv_downloaded_pdf);

        arrayList = new ArrayList<>();

        final String path = Environment.getExternalStorageDirectory().toString() + "/Prepskool";

        File directory = new File(path);

        final File[] files = directory.listFiles();

        //read all files inside Prepskool Directory
        for (int i=0; i<files.length; i++) {

            arrayList.add(new Home(i + 1 + ". " + files[i].getName(), R.mipmap.ic_pdf));
        }

        manager = new LinearLayoutManager(this);
        adapter = new DownloadedItemsAdapter(arrayList);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
                new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                String fullName = files[position].getName();
                String slug = fullName.replace(".pdf", "");

                Intent intent = new Intent(DownloadedItemsActivity.this, PdfLoaderActivity.class);
                intent.putExtra("slug", slug);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
