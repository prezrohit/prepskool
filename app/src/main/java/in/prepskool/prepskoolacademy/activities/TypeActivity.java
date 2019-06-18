package in.prepskool.prepskoolacademy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;

import in.prepskool.prepskoolacademy.AppController;
import in.prepskool.prepskoolacademy.utils.Endpoints;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.adapter.TypeAdapter;

public class TypeActivity extends AppCompatActivity {

    //region Variable Declaration
    private TypeAdapter typeAdapter;
    private ArrayList<String> list;
    private ArrayList<String> listRemove;
    private LinearLayoutManager manager;
    private RecyclerView recyclerView;
    private String TYPE;
    private String SUBJECT;
    private String STANDARD;
    private String SUBCATEGORY_HOME;
    private String CATEGORY_HOME;
    private String url;
    private TextView tvNoData;
    private ProgressDialog mProgressDialog;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        HashMap<String, String> standards = new HashMap<>();
        standards.put("Class 12", "12th");
        standards.put("Class 11", "11th");
        standards.put("Class 10", "10th");
        standards.put("Class 9", "9th");
        standards.put("Class 8", "8th");
        standards.put("Class 7", "7th");
        standards.put("Class 6", "6th");

        SUBJECT = getIntent().getStringExtra("SUBJECT");
        STANDARD = getIntent().getStringExtra("STANDARD");
        SUBCATEGORY_HOME = getIntent().getStringExtra("SUBCATEGORY_HOME");
        CATEGORY_HOME = getIntent().getStringExtra("CATEGORY_HOME");

        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.tvBreadCrumbType);
        // loads html from string and displays cat_pic.png from the app's drawable folder
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + SUBCATEGORY_HOME.replace(" BOARD", "")
                + "</font></small> >> <small><font color=\"#12c48b\">" + standards.get(STANDARD) + "</font></small> >> <small><font color='red'>"
                + SUBJECT + "</font></small>", new HtmlResImageGetter(htmlTextView));

        tvNoData = findViewById(R.id.tv_no_data_type);
        tvNoData.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(STANDARD);
        setSupportActionBar(toolbar);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list = new ArrayList<>();
        listRemove = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_paper);

        typeAdapter = new TypeAdapter(TypeActivity.this, list);
        manager = new LinearLayoutManager(TypeActivity.this);

        //region Generating Url

        url = Endpoints.TYPE + SUBCATEGORY_HOME
                .replace(" ", "%20") + "/" + STANDARD
                .replace(" ", "%20") + "/" + SUBJECT
                .replace(" ", "%20");

        //endregion

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
                new RecyclerTouchListener.ClickListener() {

                    @Override
                    public void onClick(View view, int position) {

                        TYPE = list.get(position);

                        Intent intent = new Intent(getApplicationContext(), PdfListActivity.class);
                        intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                        intent.putExtra("SUBJECT", SUBJECT);
                        intent.putExtra("STANDARD", STANDARD);
                        intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                        intent.putExtra("TYPE", TYPE);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));

        getData();
    }

    public void getData() {

        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mProgressDialog.dismiss();

                        if (response.equals("{\"status\":\"no data\"}") || response.equals("[]")) {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                        else {

                            try {

                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    list.add(jsonArray.getJSONObject(i).getString("name"));
                                }

                                if (CATEGORY_HOME.equals("SCHOOL BOARDS"))
                                    for (String name : list)
                                        if (name.contains("NCERT"))
                                            listRemove.add(name);

                                list.removeAll(listRemove);
                                recyclerView.setLayoutManager(manager);
                                if (!(list.size() < 1))
                                    recyclerView.setAdapter(typeAdapter);
                                else
                                    tvNoData.setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.e("Volley", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
