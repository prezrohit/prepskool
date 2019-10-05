package in.prepskool.prepskoolacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.adapter.SavedFilesAdapter;
import in.prepskool.prepskoolacademy.model.Home;

public class SavedFilesActivity extends AppCompatActivity {

    private static final String TAG = "SavedFilesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_items);

        TextView tvNoFiles = findViewById(R.id.tv_no_data_downloads);
        tvNoFiles.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.saved_files);
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

        final String path = Environment.getExternalStorageDirectory().toString() + "/Prepskool";

        File directory = new File(path);
        final File[] files = directory.listFiles();

        if (files.length < 1)
            tvNoFiles.setVisibility(View.VISIBLE);

        else {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_downloaded_pdf);
            ArrayList<Home> arrayList = new ArrayList<>();

            //read all files inside Prepskool Directory
            for (int i = 0; i < files.length; i++) {

                arrayList.add(new Home(i + 1 + ". " + files[i].getName(), R.mipmap.ic_pdf));
            }

            RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
            SavedFilesAdapter adapter = new SavedFilesAdapter(arrayList);

            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
                    new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {

                            String fullName = files[position].getName();
                            String slug = fullName.replace(".pdf", "");

                            Intent intent = new Intent(SavedFilesActivity.this, PdfLoaderActivity.class);
                            intent.putExtra("slug", slug);
                            startActivity(intent);
                        }

                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));
        }
    }
}
