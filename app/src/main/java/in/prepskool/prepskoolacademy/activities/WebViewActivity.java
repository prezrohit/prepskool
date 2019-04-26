package in.prepskool.prepskoolacademy.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.StringReader;

import in.prepskool.prepskoolacademy.AppController;
import in.prepskool.prepskoolacademy.Endpoints;
import in.prepskool.prepskoolacademy.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url;
    private String CRITERIA;
    private String SUBCATEGORY_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.wvCriteria);

        CRITERIA = getIntent().getStringExtra("CRITERIA");
        SUBCATEGORY_HOME = getIntent().getStringExtra("SUBCATEGORY_HOME");
        url = Endpoints.CRITERIA + SUBCATEGORY_HOME + "/" + CRITERIA.replace(" ", "%20");
        Log.v("WEBVIEWACTIVITY", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("response", response);

                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
                webView.loadUrl(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v("Volley Error", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}