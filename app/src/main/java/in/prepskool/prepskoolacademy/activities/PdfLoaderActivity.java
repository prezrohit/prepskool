package in.prepskool.prepskoolacademy.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

import in.prepskool.prepskoolacademy.R;

public class PdfLoaderActivity extends AppCompatActivity {

    private static final String RESOURCE_DIRECTORY = "Prepskool";
    private static final String EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().toString();

    private static final String TAG = "PdfLoaderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_loader);

        String pdfSlug = getIntent().getStringExtra("slug");
        String pdfName = getIntent().getStringExtra("name");

        WebView webView = (WebView) findViewById(R.id.webView);
        Context context = PdfLoaderActivity.this;

        File file = new File(EXTERNAL_STORAGE_PATH + File.separator + RESOURCE_DIRECTORY + File.separator + pdfSlug + ".pdf");

        if (file.exists()) {

            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setDisplayZoomControls(true);

            Uri path = Uri.parse(EXTERNAL_STORAGE_PATH + File.separator + RESOURCE_DIRECTORY + File.separator + pdfSlug + ".pdf");

            webView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=file://" + path);

            Window window = getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(PdfLoaderActivity.this, R.color.black));

        } else {

            String pdfLink = getIntent().getStringExtra("link");
            Intent intent = new Intent(context, DownloadActivity.class);
            intent.putExtra("link", pdfLink);
            intent.putExtra("slug", pdfSlug);
            intent.putExtra("name", pdfName);
            context.startActivity(intent);
            finish();
        }
    }
}
