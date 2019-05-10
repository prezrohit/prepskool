package in.prepskool.prepskoolacademy.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.AppController;
import in.prepskool.prepskoolacademy.Endpoints;
import in.prepskool.prepskoolacademy.IntentData;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.RecyclerViewType;
import in.prepskool.prepskoolacademy.adapter.SectionRecyclerViewAdapter;
import in.prepskool.prepskoolacademy.model.Pdf;
import in.prepskool.prepskoolacademy.model.SectionModel;

public class PdfListActivity extends AppCompatActivity {

    //region Variable Declaration
    protected static final String RECYCLER_VIEW_TYPE = "recycler_view_type";
    private RecyclerViewType recyclerViewType;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private String CATEGORY_HOME;
    private String SUBJECT;
    private String SUBCATEGORY_HOME;
    private String STANDARD;
    private String SUBCATEGORY_DEFENCE;
    private String RESOURCE;
    private String BOARD;
    private String TYPE;
    private String url;
    private TextView tvNoData;
    private TextView tvBreadCrumbPdfList;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);

        STANDARD = getIntent().getStringExtra("STANDARD");
        SUBCATEGORY_HOME = getIntent().getStringExtra("SUBCATEGORY_HOME");
        CATEGORY_HOME = getIntent().getStringExtra("CATEGORY_HOME");
        SUBJECT = getIntent().getStringExtra("SUBJECT");
        BOARD = getIntent().getStringExtra("BOARD");

        if(CATEGORY_HOME.equals("SCHOOL BOARDS"))
            TYPE = getIntent().getStringExtra("TYPE");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvNoData = (TextView) findViewById(R.id.tvNoData);

        tvNoData.setVisibility(View.GONE);

        if (!CATEGORY_HOME.equals("DEFENCE")) toolbar.setTitle(SUBJECT);
        else toolbar.setTitle(SUBCATEGORY_DEFENCE);

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

        tvBreadCrumbPdfList = (TextView) findViewById(R.id.tvBreadCrumbPdfList);
        switch (CATEGORY_HOME) {
            case "SCHOOL BOARDS":
                tvBreadCrumbPdfList.setText(" /" + SUBCATEGORY_HOME + " /" + STANDARD + " /" + SUBJECT + " /" + TYPE);
                break;
            default:
                tvBreadCrumbPdfList.setText(" /" + SUBCATEGORY_HOME + " /" + STANDARD + " /" + SUBJECT);
                break;
        }

        // region Selecting URL
        switch (CATEGORY_HOME) {

            case "SCHOOL BOARDS":

                url = Endpoints.PDF + "/" + SUBCATEGORY_HOME
                        .replace(" ", "%20") + "/" + STANDARD
                        .replace(" ", "%20") + "/" + SUBJECT
                        .replace(" ", "%20") + "/" + TYPE
                        .replace(" ", "%20");
                break;

            case "PRACTICE PAPERS":

                url = Endpoints.PDF + "/" + BOARD
                        .replace(" ", "%20") + "/" + STANDARD
                        .replace(" ", "%20") + "/" + SUBJECT
                        .replace(" ", "%20") + "/" + SUBCATEGORY_HOME
                        .replace(" ", "%20");
                break;

            case "NCERT":
                url = Endpoints.PDF + "/" + SUBCATEGORY_HOME
                        .replace(" ", "%20") + "/" + STANDARD
                        .replace(" ", "%20") + "/" + SUBJECT
                        .replace(" ", "%20");
        }
        //endregion

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Fetching PDFs...");

        //get enum type passed from MainActivity
        recyclerViewType = (RecyclerViewType) getIntent().getSerializableExtra(RECYCLER_VIEW_TYPE);

        setUpRecyclerView();
        populateRecyclerView();
    }

    //setup recycler view
    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.sectioned_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    ArrayList<SectionModel> sectionModelArrayList = new ArrayList<>();
    SectionRecyclerViewAdapter adapter;

    //populate recycler view
    private void populateRecyclerView() {

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                try {

                    if (response.equals("[]")) {
                        recyclerView.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            ArrayList<Pdf> itemArrayList = new ArrayList<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String year = jsonObject.getString("year");

                            String data = jsonObject.getString("data");
                            JSONArray jsonArray1 = new JSONArray(data);

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                Pdf pdf = new Pdf();
                                pdf.setName(jsonObject1.getString("name"));
                                pdf.setLink(jsonObject1.getString("link"));
                                pdf.setSlug(jsonObject1.getString("slug"));
                                itemArrayList.add(pdf);
                            }
                            sectionModelArrayList.add(new SectionModel(year, itemArrayList));
                        }
                    }

                    adapter = new SectionRecyclerViewAdapter(PdfListActivity.this, recyclerViewType, sectionModelArrayList);
                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
