package in.prepskool.prepskoolacademy.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.pvryan.easycrypt.ECResultListener;
import com.pvryan.easycrypt.symmetric.ECSymmetric;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import in.prepskool.prepskoolacademy.PaymentActivity;
import in.prepskool.prepskoolacademy.R;

public class PdfLoaderActivity extends AppCompatActivity {

    private String decResourceFilePath;
    private String encResourceFilePath;
    private static final String RESOURCE_DIRECTORY = ".Prepskool";
    private static final String EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().toString();

    private static final String TAG = "PdfLoaderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_loader);

        String pdfSlug = getIntent().getStringExtra("slug");
        String pdfName = getIntent().getStringExtra("name");

        Log.d(TAG, "onCreate: " + pdfSlug);

        Context context = PdfLoaderActivity.this;

        decResourceFilePath = EXTERNAL_STORAGE_PATH + File.separator + "Android" + File.separator + "data" + File.separator + RESOURCE_DIRECTORY + File.separator + pdfSlug + ".pdf";
        encResourceFilePath = EXTERNAL_STORAGE_PATH + File.separator + "Android" + File.separator + "data" + File.separator + RESOURCE_DIRECTORY + File.separator + pdfSlug + ".pdf.ecrypt";

        final File file = new File(encResourceFilePath);

        if (file.exists()) {

            Log.d(TAG, "onCreate: file exists");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ECSymmetric ecSymmetric = new ECSymmetric();
                    ecSymmetric.decrypt(file, getString(R.string.string_resource_encryption_password), new ECResultListener() {
                        @Override
                        public void onProgress(int i, long l, long l1) {
                        }

                        @Override
                        public <T> void onSuccess(T t) {
                            Log.d(TAG, "result: " + t.toString());
                            Log.d(TAG, "onSuccess: file decrypted");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WebView webView = (WebView) findViewById(R.id.webView);
                                    webView.setWebChromeClient(new WebChromeClient());
                                    webView.getSettings().setBuiltInZoomControls(true);
                                    webView.getSettings().setJavaScriptEnabled(true);
                                    webView.getSettings().setAllowFileAccessFromFileURLs(true);
                                    webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                                    webView.getSettings().setSupportZoom(true);
                                    webView.getSettings().setDisplayZoomControls(true);

                                    Uri path = Uri.parse(decResourceFilePath);

                                    webView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=file://" + path);

                                    Window window = getWindow();

                                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                                    window.setStatusBarColor(ContextCompat.getColor(PdfLoaderActivity.this, R.color.black));
                                }
                            });
                        }

                        @Override
                        public void onFailure(@NotNull String s, @NotNull Exception e) {
                            Log.d(TAG, "onFailure: " + s);
                            Log.d(TAG, "message: " + e.getLocalizedMessage());
                        }
                    });
                }
            });

        } else {

            String price = getIntent().getStringExtra("price");
            String pdfLink = getIntent().getStringExtra("link");

            Intent intent;
            if (price == null || Integer.valueOf(price) == 0) {
                intent = new Intent(context, DownloadActivity.class);
                intent.putExtra("link", pdfLink);
                intent.putExtra("slug", pdfSlug);
                intent.putExtra("name", pdfName);

            } else {
                intent = new Intent(context, PaymentActivity.class);
                intent.putExtra("price", price);
                intent.putExtra("link", pdfLink);
                intent.putExtra("slug", pdfSlug);
                intent.putExtra("name", pdfName);
            }

            context.startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        File file = new File(decResourceFilePath);
        boolean isDeleted = file.delete();
        if (isDeleted) {
            Log.d(TAG, "onDestroy: file deleted");

        } else {
            Log.d(TAG, "onDestroy: file not deleted");
        }
    }
}


//TODO: delete file onDestroy