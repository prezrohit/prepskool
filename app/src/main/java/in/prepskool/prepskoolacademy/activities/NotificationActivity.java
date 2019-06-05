package in.prepskool.prepskoolacademy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.AppController;
import in.prepskool.prepskoolacademy.Endpoints;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.adapter.NotificationAdapter;
import in.prepskool.prepskoolacademy.model.Notification;
import in.prepskool.prepskoolacademy.model.Subject;

public class NotificationActivity extends AppCompatActivity {

    NotificationAdapter adapter;
    RecyclerView recyclerView;
    StringRequest stringRequest;
    private ProgressDialog mProgressDialog;
    private Toolbar toolbar;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Notification> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Notifications");
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

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.rvNotifications);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView,
                new RecyclerTouchListener.ClickListener() {

                    @Override
                    public void onClick(View view, int position) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(position).getLink()));
                        startActivity(browserIntent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        mProgressDialog.show();

        stringRequest = new StringRequest(Request.Method.GET,
                Endpoints.NOTIFICATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Notification notification = new Notification();
                        notification.setName(jsonArray.getJSONObject(i).getString("title"));
                        notification.setLink(jsonArray.getJSONObject(i).getString("link"));
                        arrayList.add(notification);
                    }

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter = new NotificationAdapter(NotificationActivity.this, arrayList);
                    recyclerView.setAdapter(adapter);

                    mProgressDialog.dismiss();

                } catch (JSONException e) {
                    Log.v("#####", "error occured");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.v("VolleyError", "not working");
                Log.v("VolleyError", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
