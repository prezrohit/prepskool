package in.prepskool.prepskoolacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
