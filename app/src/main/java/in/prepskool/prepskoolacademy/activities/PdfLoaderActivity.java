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

    private File file;
    private String pdfLink;
    private String pdfSlug;
    private WebView webView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_loader);

        pdfSlug = getIntent().getStringExtra("slug");

        webView = (WebView) findViewById(R.id.webView);
        context = PdfLoaderActivity.this;

        file = new File(new File(Environment.getExternalStorageDirectory() + File.separator + "Prepskool").getAbsolutePath(),
                pdfSlug + ".pdf");

        if (file.exists()) {

            WebSettings settings = webView.getSettings();

            settings.setJavaScriptEnabled(true);
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setBuiltInZoomControls(true);
            webView.setWebChromeClient(new WebChromeClient());

            Uri path = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/Prepskool/" + pdfSlug + ".pdf");

            webView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=file://" + path);

            Window window = getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(PdfLoaderActivity.this, R.color.black));

        } else {

            pdfLink = getIntent().getStringExtra("link");
            Intent intent = new Intent(context, DownloadActivity.class);
            intent.putExtra("link", pdfLink);
            intent.putExtra("slug", pdfSlug);
            context.startActivity(intent);
            finish();
        }
    }
}
