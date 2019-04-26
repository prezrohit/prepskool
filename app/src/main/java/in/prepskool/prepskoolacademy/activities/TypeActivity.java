package in.prepskool.prepskoolacademy.activities;

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
import java.util.ArrayList;
import in.prepskool.prepskoolacademy.AppController;
import in.prepskool.prepskoolacademy.Endpoints;
import in.prepskool.prepskoolacademy.IntentData;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.adapter.TypeAdapter;

public class TypeActivity extends AppCompatActivity {

    //region Variable Declaration
    private TypeAdapter typeAdapter;
    private ArrayList<String> list;
    private ArrayList<String> listRemove;
    private LinearLayoutManager manager;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView tvBreadCrumbType;
    private String TYPE;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        IntentData.SUBJECT = getIntent().getStringExtra("SUBJECT");
        IntentData.STANDARD = getIntent().getStringExtra("STANDARD");
        IntentData.SUBCATEGORY_HOME = getIntent().getStringExtra("SUBCATEGORY_HOME");
        IntentData.CATEGORY_HOME = getIntent().getStringExtra("CATEGORY_HOME");

        tvBreadCrumbType = (TextView) findViewById(R.id.tvBreadCrumbType);
        tvBreadCrumbType.setText(" /" + IntentData.SUBCATEGORY_HOME + " /" + IntentData.STANDARD + " /" + IntentData.SUBJECT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(IntentData.STANDARD);
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

        list = new ArrayList<>();
        listRemove = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_paper);

        typeAdapter = new TypeAdapter(TypeActivity.this, list);
        manager = new LinearLayoutManager(TypeActivity.this);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
                new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                TYPE = list.get(position);

                Intent intent = new Intent(getApplicationContext(), PdfListActivity.class);
                intent.putExtra("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME);
                intent.putExtra("SUBJECT", IntentData.SUBJECT);
                intent.putExtra("STANDARD", IntentData.STANDARD);
                intent.putExtra("CATEGORY_HOME", IntentData.CATEGORY_HOME);
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

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Endpoints.SCHOOLBOARDS,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        list.add(jsonArray.getJSONObject(i).getString("name"));
                    }

                    if (IntentData.CATEGORY_HOME.equals("SCHOOL BOARDS"))
                        for (String name : list) {
                            if (name.contains("NCERT"))
                                listRemove.add(name);
                        }

                    list.removeAll(listRemove);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(typeAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
