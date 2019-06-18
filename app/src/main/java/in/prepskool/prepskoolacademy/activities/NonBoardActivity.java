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
import in.prepskool.prepskoolacademy.utils.IntentData;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.adapter.NonBoardAdapter;

public class NonBoardActivity extends AppCompatActivity {

    //region Variable Declaration
    private NonBoardAdapter adapter;
    private RecyclerView rvNonBoard;
    private ProgressDialog mProgressDialog;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> arrayListSubject = new ArrayList<>();
    private ArrayList<String> arrayListRemove;
    private String CATEGORY_HOME;
    private String SUBCATEGORY_HOME;
    private String STANDARD;
    private String BOARD;
    private String SUBJECT;
    private int sourceId;
    private String TYPE;
    private TextView tvNoData;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_board);

        HashMap<String, String> standards = new HashMap<String, String>();
        standards.put("Class 10", "10th");
        standards.put("Class 9", "9th");
        standards.put("Class 8", "8th");
        standards.put("Class 7", "7th");
        standards.put("Class 6", "6th");
        SUBCATEGORY_HOME = getIntent().getStringExtra("SUBCATEGORY_HOME");
        STANDARD = getIntent().getStringExtra("STANDARD");
        CATEGORY_HOME = getIntent().getStringExtra("CATEGORY_HOME");
        BOARD = getIntent().getStringExtra("BOARD");
        sourceId = getIntent().getIntExtra("source", 0);
        TYPE = getIntent().getStringExtra("TYPE");

        String url;
        switch (CATEGORY_HOME) {

            case "SCHOOL BOARDS":
                url = Endpoints.NB_SUBJECTS + "?id=" + STANDARD
                        .replace(" ", "%20") + "&category=" + SUBCATEGORY_HOME
                        .replace(" ", "%20");
                break;

            case "CBSE PRACTICE PAPERS":
                url = Endpoints.PAPERS + STANDARD
                        .replace(" ", "%20") + "/" + SUBCATEGORY_HOME
                        .replace(" ", "%20");
                break;

            default:
                url = Endpoints.SUBJECTS + "?id=" + STANDARD
                        .replace(" ", "%20") + "&category=" + SUBCATEGORY_HOME
                        .replace(" ", "%20");
                break;
        }

        tvNoData = (TextView) findViewById(R.id.txt_no_data_non_board);
        tvNoData.setVisibility(View.GONE);

        //region Setting Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_subject);
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
        //endregion

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");

        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.breadCrumbNonBoard);
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + SUBCATEGORY_HOME.replace(" BOARD", "")
                        + "</font></small> >> <small><font color=\"#12c48b\">" + standards.get(STANDARD)
                + "</font></small>", new HtmlResImageGetter(htmlTextView));

        arrayListRemove = new ArrayList<>();
        arrayListRemove.add("Physics");
        arrayListRemove.add("Chemistry");
        arrayListRemove.add("Biology");
        arrayListRemove.add("Business Studies");
        arrayListRemove.add("Accountancy");
        arrayListRemove.add("Economics");
        arrayListRemove.add("Geography");
        arrayListRemove.add("History");
        arrayListRemove.add("Information Practice");
        arrayListRemove.add("Computer Science");

        arrayListSubject = new ArrayList<>();
        rvNonBoard = findViewById(R.id.rvSci);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvNonBoard.addOnItemTouchListener(new RecyclerTouchListener(this, rvNonBoard,
                new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                SUBJECT = arrayListSubject.get(position);

                Intent intent = new Intent(NonBoardActivity.this, PdfListActivity.class);
                intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                intent.putExtra("SUBJECT", SUBJECT);
                intent.putExtra("STANDARD", STANDARD);
                intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);

                if (CATEGORY_HOME.equals("SCHOOL BOARDS")) {

                    if (sourceId == 1) {
                        intent.putExtra("TYPE", TYPE);
                        intent.putExtra("source", 1);
                    }

                    else {

                        intent = new Intent(NonBoardActivity.this, TypeActivity.class);
                        intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                        intent.putExtra("SUBJECT", SUBJECT);
                        intent.putExtra("STANDARD", STANDARD);
                        intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                    }
                }

                else if (CATEGORY_HOME.equals("CBSE PRACTICE PAPERS"))
                    intent.putExtra("BOARD", BOARD);

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        Log.v( "NonBoard", "url: " + url );

        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        mProgressDialog.dismiss();

                        if (response.equals("{\"status\":\"no data\"}")) {
                            rvNonBoard.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        } else {
                            try {

                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    arrayListSubject.add(jsonArray.getJSONObject(i).getString("name"));
                                }

                                if (SUBCATEGORY_HOME.equals("ICSE")) {
                                    if (!(STANDARD.equals("Class 12") || STANDARD.equals("Class 11") ||
                                            STANDARD.equals("Class 10") || STANDARD.equals("Class 9")))
                                        arrayListSubject.removeAll(arrayListRemove);
                                } else if (!(STANDARD.equals("Class 12") || STANDARD.equals("Class 11")))
                                    arrayListSubject.removeAll(arrayListRemove);

                                rvNonBoard.setHasFixedSize(true);
                                rvNonBoard.setLayoutManager(linearLayoutManager);
                                adapter = new NonBoardAdapter(NonBoardActivity.this, arrayListSubject);
                                rvNonBoard.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Log.v("VolleyError", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
