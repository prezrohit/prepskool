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
import in.prepskool.prepskoolacademy.utils.Endpoints;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.utils.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.activities.PdfListActivity;
import in.prepskool.prepskoolacademy.activities.TypeActivity;
import in.prepskool.prepskoolacademy.adapter.SubjectAdapter;
import in.prepskool.prepskoolacademy.model.Subject;

public class ArtFragment extends Fragment {

    View vi;
    SubjectAdapter adapter;
    RecyclerView rvArts;
    StringRequest stringRequest;
    private ProgressDialog mProgressDialog;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Subject> arrayList = new ArrayList<>();
    private String SUBJECT;
    private String BOARD;
    private String STANDARD;
    private String SUBCATEGORY_HOME;
    private String CATEGORY_HOME;
    private String url;
    private int sourceId;
    private String TYPE;
    private TextView tvNoData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vi = inflater.inflate(R.layout.fragment_arts, container, false);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");

        tvNoData = (TextView) vi.findViewById(R.id.tv_no_data_arts);
        tvNoData.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        arrayList = new ArrayList<>();
        rvArts = vi.findViewById(R.id.rvArt);

        SUBCATEGORY_HOME = getArguments().getString("SUBCATEGORY_HOME");
        CATEGORY_HOME = getArguments().getString("CATEGORY_HOME");
        STANDARD = getArguments().getString("STANDARD");
        BOARD = getArguments().getString("BOARD");
        sourceId = getArguments().getInt("source", 0);
        TYPE = getArguments().getString("TYPE");

        switch (CATEGORY_HOME) {
            case "SCHOOL BOARDS":
                url = Endpoints.B_SUBJECTS +  "/" + STANDARD
                        .replace(" ", "%20") + "/" + SUBCATEGORY_HOME
                        .replace(" ", "%20") + "/Arts";
                break;
            case "CBSE PRACTICE PAPERS":
                url = Endpoints.PAPERS + STANDARD
                        .replace(" ", "%20") + "/" + SUBCATEGORY_HOME
                        .replace(" ", "%20") + "/Arts";
                break;
            default:
                url = Endpoints.SUBJECTS + "/" + STANDARD
                        .replace(" ", "%20") + "/" + SUBCATEGORY_HOME
                        .replace(" ", "%20") + "/Arts";
                break;
        }

        rvArts.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvArts,
                new RecyclerTouchListener.ClickListener() {

                    @Override
                    public void onClick(View view, int position) {

                        SUBJECT = arrayList.get(position).getName();

                        Intent intent = new Intent(getActivity(), PdfListActivity.class);
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
                                intent = new Intent(getActivity(), TypeActivity.class);
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

        Log.v( "Arts", "url: " + url );

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.v(ArtFragment.class.getSimpleName(), response);

                if (response.equals("{\"status\":\"no data\"}")) {
                    tvNoData.setVisibility(View.VISIBLE);
                    rvArts.setVisibility(View.GONE);
                } else {

                    try {

                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Subject news = new Subject();
                            news.setName(jsonArray.getJSONObject(i).getString("name"));
                            arrayList.add(news);
                        }

                        rvArts.setHasFixedSize(true);
                        rvArts.setLayoutManager(linearLayoutManager);
                        adapter = new SubjectAdapter(getActivity(), arrayList);
                        rvArts.setAdapter(adapter);
                        mProgressDialog.dismiss();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(ArtFragment.class.getSimpleName(), "VolleyError: " + error.getLocalizedMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
        return vi;
    }
}
