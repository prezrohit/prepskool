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
import org.json.JSONObject;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.AppController;
import in.prepskool.prepskoolacademy.Endpoints;
import in.prepskool.prepskoolacademy.IntentData;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.RecyclerTouchListener;
import in.prepskool.prepskoolacademy.activities.PdfListActivity;
import in.prepskool.prepskoolacademy.activities.StreamActivity;
import in.prepskool.prepskoolacademy.activities.TypeActivity;
import in.prepskool.prepskoolacademy.adapter.SubjectAdapter;
import in.prepskool.prepskoolacademy.model.Subject;

public class CommerceFragment extends Fragment {

    View vi;
    SubjectAdapter adapter;
    RecyclerView rvCommerce;
    StringRequest stringRequest;
    private ProgressDialog mProgressDialog;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Subject> arrayList = new ArrayList<>();
    String url;
    private String SUBJECT;
    private String BOARD;
    private TextView tvNoData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vi = inflater.inflate(R.layout.fragment_commerce, container, false);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");

        tvNoData = (TextView) vi.findViewById(R.id.tv_no_data_commerce);
        tvNoData.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        arrayList = new ArrayList<>();
        rvCommerce = vi.findViewById(R.id.rvComm);

        IntentData.SUBCATEGORY_HOME = getArguments().getString("SUBCATEGORY_HOME");
        IntentData.CATEGORY_HOME = getArguments().getString("CATEGORY_HOME");
        IntentData.STANDARD = getArguments().getString("STANDARD");
        BOARD = getArguments().getString("BOARD");

        if (IntentData.CATEGORY_HOME.equals("PRACTICE PAPERS"))

            url = Endpoints.PAPERS + IntentData.STANDARD
                    .replace(" ", "%20") + "/" + IntentData.SUBCATEGORY_HOME
                    .replace(" ", "%20") + "/Commerce";
        else
            url = Endpoints.SUBJECTS + "/" + IntentData.STANDARD
                    .replace(" ", "%20") + "/" + IntentData.SUBCATEGORY_HOME
                    .replace(" ", "%20") + "/Commerce";

        rvCommerce.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                rvCommerce, new RecyclerTouchListener.ClickListener() {

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

                Log.v(CommerceFragment.class.getSimpleName(), response);

                if (response.equals("{\"status\":\"no data\"}")) {
                    tvNoData.setVisibility(View.VISIBLE);
                    rvCommerce.setVisibility(View.GONE);
                } else {

                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        Log.v(CommerceFragment.class.getSimpleName(), response + "       " + jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Subject subject = new Subject();
                            subject.setName(jsonArray.getJSONObject(i).getString("name"));
                            arrayList.add(subject);
                            Log.v(CommerceFragment.class.getSimpleName(), arrayList.get(i).getName());
                        }

                        rvCommerce.setHasFixedSize(true);
                        rvCommerce.setLayoutManager(linearLayoutManager);
                        adapter = new SubjectAdapter(getActivity(), arrayList);
                        rvCommerce.setAdapter(adapter);

                        mProgressDialog.dismiss();

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