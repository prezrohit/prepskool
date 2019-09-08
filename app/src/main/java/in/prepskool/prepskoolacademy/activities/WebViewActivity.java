package in.prepskool.prepskoolacademy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.prepskool.prepskoolacademy.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = (WebView) findViewById(R.id.wvCriteria);

        int key = getIntent().getIntExtra("sender", 0);
        String url = getIntent().getStringExtra("url");

        if (key == 1) {
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            webView.loadUrl(url);
        }
    }
}