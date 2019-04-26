package in.prepskool.prepskoolacademy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.json.JSONObject;
import java.util.ArrayList;
import in.prepskool.prepskoolacademy.AppController;
import in.prepskool.prepskoolacademy.Endpoints;
import in.prepskool.prepskoolacademy.IntentData;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.adapter.NonBoardAdapter;

public class NonBoardActivity extends AppCompatActivity {

    //region Variable Declaration
    private NonBoardAdapter adapter;
    private RecyclerView recyclerView;
    private StringRequest stringRequest;
    private ProgressDialog mProgressDialog;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> arrayListSubject = new ArrayList<>();
    private ArrayList<String> arrayListRemove;
    private Toolbar toolbar;
    private TextView tvCvHeader;
    private String CATEGORY_HOME;
    private String SUBCATEGORY_HOME;
    private String STANDARD;
    private String SUBJECT;
    private TextView tvBreadCrumbNonBoard;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_board);

        SUBCATEGORY_HOME = getIntent().getStringExtra("SUBCATEGORY_HOME");
        STANDARD = getIntent().getStringExtra("STANDARD");
        CATEGORY_HOME = getIntent().getStringExtra("CATEGORY_HOME");

        //region Setting Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        tvBreadCrumbNonBoard = (TextView) findViewById(R.id.breadCrumbNonBoard);
        tvBreadCrumbNonBoard.setText(" /" + SUBCATEGORY_HOME + "/" + STANDARD);

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
        recyclerView = findViewById(R.id.rvSci);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
                new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                SUBJECT = arrayListSubject.get(position);

                Intent intent = new Intent(NonBoardActivity.this, PdfListActivity.class);
                intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                intent.putExtra("SUBJECT", SUBJECT);
                intent.putExtra("STANDARD", STANDARD);
                intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);

                if (IntentData.CATEGORY_HOME.equals("SCHOOL BOARDS")) {

                    intent = new Intent(NonBoardActivity.this, TypeActivity.class);
                    intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                    intent.putExtra("SUBJECT", SUBJECT);
                    intent.putExtra("STANDARD", STANDARD);
                    intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                }

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        mProgressDialog.show();

        stringRequest = new StringRequest(Request.Method.GET, Endpoints.SUBJECTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.v("####sci", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    Log.v("#### arrayss", jsonArray.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {

                        arrayListSubject.add(jsonArray.getJSONObject(i).getString("name"));
                    }

                    if (SUBCATEGORY_HOME.equals("ICSE")) {
                        if (!(STANDARD.equals("Class 12") || STANDARD.equals("Class 11") ||
                                STANDARD.equals("Class 10") || STANDARD.equals("Class 9")))
                            arrayListSubject.removeAll(arrayListRemove);
                    }
                    else
                        if (!(STANDARD.equals("Class 12") || STANDARD.equals("Class 11")))
                            arrayListSubject.removeAll(arrayListRemove);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter = new NonBoardAdapter(NonBoardActivity.this, arrayListSubject);
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
