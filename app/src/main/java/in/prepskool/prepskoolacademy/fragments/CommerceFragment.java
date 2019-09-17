package in.prepskool.prepskoolacademy.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.SubjectAdapter;
import in.prepskool.prepskoolacademy.retrofit_model.Subject;

public class CommerceFragment extends Fragment {

    private ArrayList<Subject> subjectList;

    private static final String TAG = "CommerceFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_commerce, container, false);

        if (getArguments() != null) {
            subjectList = (ArrayList<Subject>) getArguments().getSerializable("list");
        }

        RecyclerView rvCommerce = view.findViewById(R.id.rv_commerce);
        TextView lblNoData = (TextView) view.findViewById(R.id.tv_no_data_commerce);
        lblNoData.setVisibility(View.GONE);

        if (subjectList == null || subjectList.isEmpty()) {
            lblNoData.setVisibility(View.VISIBLE);
            rvCommerce.setVisibility(View.GONE);
        }

        rvCommerce.setLayoutManager(new LinearLayoutManager(getActivity()));
        SubjectAdapter subjectAdapter = new SubjectAdapter(getActivity(), subjectList);
        rvCommerce.setAdapter(subjectAdapter);

        return view;
    }
}



/*rvCommerce.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                rvCommerce, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                SUBJECT = arrayList.get(position).getName();

                Intent intent = new Intent(getActivity(), ResourceActivity.class);
                intent.putExtra("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME);
                intent.putExtra("SUBJECT", SUBJECT);
                intent.putExtra("STANDARD", IntentData.STANDARD);
                intent.putExtra("CATEGORY_HOME", IntentData.CATEGORY_HOME);

                if (IntentData.CATEGORY_HOME.equals("SCHOOL BOARDS")) {

                    if (sourceId == 1) {
                        intent.putExtra("TYPE", TYPE);
                        intent.putExtra("source", 1);
                    }

                    else {
                        intent = new Intent(getActivity(), ResourceTypeActivity.class);
                        intent.putExtra("SUBCATEGORY_HOME", IntentData.SUBCATEGORY_HOME);
                        intent.putExtra("SUBJECT", SUBJECT);
                        intent.putExtra("STANDARD", IntentData.STANDARD);
                        intent.putExtra("CATEGORY_HOME", IntentData.CATEGORY_HOME);
                    }

                } else if (IntentData.CATEGORY_HOME.equals("CBSE PRACTICE PAPERS"))
                    intent.putExtra("BOARD", BOARD);

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/