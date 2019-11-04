package in.prepskool.prepskoolacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pvryan.easycrypt.ECResultListener;
import com.pvryan.easycrypt.symmetric.ECSymmetric;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.SavedFilesAdapter;
import in.prepskool.prepskoolacademy.model.Home;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;

public class SavedFilesActivity extends AppCompatActivity {

    private static final String TAG = "SavedFilesActivity";
    private static final String RESOURCE_DIRECTORY_NAME = ".Prepskool";
    private static final File EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_files);

        TextView tvNoFiles = findViewById(R.id.tv_no_data_downloads);
        tvNoFiles.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.string_saved_resources);
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

        final String path = EXTERNAL_STORAGE_PATH + File.separator + "Android" + File.separator + "data" + File.separator + RESOURCE_DIRECTORY_NAME;

        File directory = new File(path);
        final File[] files = directory.listFiles();

        if (files == null || files.length < 1)
            tvNoFiles.setVisibility(View.VISIBLE);

        else {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_downloaded_pdf);
            ArrayList<Home> arrayList = new ArrayList<>();

            //read all files inside Prepskool Directory
            for (int i = 0; i < files.length; i++) {
                arrayList.add(new Home(i + 1 + ". " + files[i].getName().replace(".pdf.ecrypt", ""), R.mipmap.ic_pdf));
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
                            String slug = fullName.replace(".pdf.ecrypt", "");

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

    public void decrypt(View view) {
        final File file = new File(EXTERNAL_STORAGE_PATH + File.separator + "Android" + File.separator + "data" + File.separator + RESOURCE_DIRECTORY_NAME + File.separator + "file.pdf.ecrypt");
        ECSymmetric ecSymmetric = new ECSymmetric();
        ecSymmetric.decrypt(file, "qwerty", new ECResultListener() {
            @Override
            public void onProgress(int i, long l, long l1) {
            }

            @Override
            public <T> void onSuccess(T t) {
                Log.d(TAG, "result: " + t.toString());
            }

            @Override
            public void onFailure(@NotNull String s, @NotNull Exception e) {
                Log.d(TAG, "onFailure: " + s);
                Log.d(TAG, "message: " + e.getLocalizedMessage());
            }
        });
    }
}
