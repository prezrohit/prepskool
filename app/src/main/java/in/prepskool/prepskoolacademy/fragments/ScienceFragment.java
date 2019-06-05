package in.prepskool.prepskoolacademy.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import in.prepskool.prepskoolacademy.activities.PdfListActivity;
import in.prepskool.prepskoolacademy.activities.TypeActivity;
import in.prepskool.prepskoolacademy.adapter.SubjectAdapter;
import in.prepskool.prepskoolacademy.model.Subject;

public class ScienceFragment extends Fragment {

    View vi;
    SubjectAdapter adapter;
    RecyclerView rvScience;
    StringRequest stringRequest;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Subject> arrayList = new ArrayList<>();
    private String SUBJECT;
    private String BOARD;
    private String url;
    private TextView tvNoData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vi = inflater.inflate(R.layout.fragment_science, container, false);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        arrayList = new ArrayList<>();
        rvScience = vi.findViewById(R.id.rvSci);

        tvNoData = (TextView) vi.findViewById(R.id.tv_no_data_science);
        tvNoData.setVisibility(View.GONE);

        IntentData.SUBCATEGORY_HOME = getArguments().getString("SUBCATEGORY_HOME");
        IntentData.CATEGORY_HOME = getArguments().getString("CATEGORY_HOME");
        IntentData.STANDARD = getArguments().getString("STANDARD");
        BOARD = getArguments().getString("BOARD");

        switch (IntentData.CATEGORY_HOME) {
            case "SCHOOL BOARDS":
                url = Endpoints.B_SUBJECTS + "/" + IntentData.STANDARD
                        .replace(" ", "%20") + "/" + IntentData.SUBCATEGORY_HOME
                        .replace(" ", "%20") + "/Science";
                break;
            case "PRACTICE PAPERS":
                url = Endpoints.PAPERS + IntentData.STANDARD
                        .replace(" ", "%20") + "/" + IntentData.SUBCATEGORY_HOME
                        .replace(" ", "%20") + "/Science";
                break;
            default:
                url = Endpoints.SUBJECTS + "/" + IntentData.STANDARD
                        .replace(" ", "%20") + "/" + IntentData.SUBCATEGORY_HOME
                        .replace(" ", "%20") + "/Science";
                break;
        }

        rvScience.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvScience,
                new RecyclerTouchListener.ClickListener() {

                    @Override
                    public void onClick(View view, int position) {

                        SUBJECT = arrayList.get(position).getName();

                        Intent intent = new Intent(getActivity(), PdfListActivity.class);
                        intent.putExtra("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME);
                        intent.putExtra("SUBJECT", SUBJECT);
                        intent.putExtra("STANDARD", IntentData.STANDARD);
                        intent.putExtra("CATEGORY_HOME", IntentData.CATEGORY_HOME);

                        if (IntentData.CATEGORY_HOME.equals("SCHOOL BOARDS")) {

                            intent = new Intent(getActivity(), TypeActivity.class);
                            intent.putExtra("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME);
                            intent.putExtra("SUBJECT", SUBJECT);
                            intent.putExtra("STANDARD", IntentData.STANDARD);
                            intent.putExtra("CATEGORY_HOME", IntentData.CATEGORY_HOME);
                        } else if (IntentData.CATEGORY_HOME.equals("PRACTICE PAPERS"))
                            intent.putExtra("BOARD", BOARD);

                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.v(ScienceFragment.class.getSimpleName(), response);

                if (response.equals("{\"status\":\"no data\"}")) {
                    tvNoData.setVisibility(View.VISIBLE);
                    rvScience.setVisibility(View.GONE);
                } else {
                    try {

                        JSONArray jsonArray = new JSONArray(response);

                        Log.v(ScienceFragment.class.getSimpleName(), response + "       " + jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Subject news = new Subject();
                            news.setName(jsonArray.getJSONObject(i).getString("name"));
                            arrayList.add(news);
                            Log.v(ScienceFragment.class.getSimpleName(), arrayList.get(i).getName());
                        }

                        rvScience.setHasFixedSize(true);
                        rvScience.setLayoutManager(linearLayoutManager);
                        adapter = new SubjectAdapter(getActivity(), arrayList);
                        rvScience.setAdapter(adapter);

                    } catch (JSONException e) {
                        Log.v("#####", "error occured");
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VolleyError", "not working");
                Log.v("VolleyError", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);

        return vi;
    }
}
