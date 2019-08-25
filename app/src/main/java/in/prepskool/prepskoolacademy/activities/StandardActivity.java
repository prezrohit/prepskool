package in.prepskool.prepskoolacademy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;

import in.prepskool.prepskoolacademy.AppController;
import in.prepskool.prepskoolacademy.CheckNetworkService;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.StandardAdapter;
import in.prepskool.prepskoolacademy.model.Standard;
import in.prepskool.prepskoolacademy.utils.ConnectivityReceiver;

public class StandardActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    private GridView gvStandard;
    private ArrayList<Standard> list;
    private StandardAdapter standardAdapter;
    private HashMap<String, String> stdRomans;
    private String CATEGORY_HOME;
    private String SUBCATEGORY_HOME;
    private String type;
    private String STANDARD;
    private String BOARD;
    private int sourceId;
    private String TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);

        CATEGORY_HOME = getIntent().getStringExtra("CATEGORY_HOME");
        SUBCATEGORY_HOME = getIntent().getStringExtra("SUBCATEGORY_HOME");
        type = getIntent().getStringExtra("type");
        BOARD = getIntent().getStringExtra("BOARD");
        sourceId = getIntent().getIntExtra("source", 0);
        TYPE = getIntent().getStringExtra("TYPE");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_standard);

        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.breadCrumbStandard);
        // loads html from string and displays cat_pic.png from the app's drawable folder
        htmlTextView.setHtml("<small><font color=\"#29b6f6\">" + SUBCATEGORY_HOME.replace(" BOARD", "") + "</font></small>",
                new HtmlResImageGetter(htmlTextView));

        gvStandard = (GridView) findViewById(R.id.grid_view_class);
        stdRomans = new HashMap<>();
        list = new ArrayList<>();

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

        populateClasses();

        standardAdapter = new StandardAdapter(list, this);

        gvStandard.setAdapter(standardAdapter);

        gvStandard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                STANDARD = list.get(i).getName();

                switch (CATEGORY_HOME) {
                    case "NCERT":

                        if (STANDARD.equals("Class 11") || STANDARD.equals("Class 12")) {

                            Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                            intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                            intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                            intent.putExtra("STANDARD", STANDARD);
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(getApplicationContext(), NonBoardActivity.class);
                            intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                            intent.putExtra("STANDARD", STANDARD);
                            intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                            startActivity(intent);
                        }

                        break;

                    case "SCHOOL BOARDS":

                        if (SUBCATEGORY_HOME.equals("ICSE Board")) {

                            if (STANDARD.equals("Class 11") || STANDARD.equals("Class 12") || STANDARD.equals("Class 9")
                                    || STANDARD.equals("Class 10")) {

                                Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                                intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                                intent.putExtra("STANDARD", STANDARD);
                                intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                                intent.putExtra("TYPE", TYPE);
                                intent.putExtra("source", sourceId);
                                startActivity(intent);

                            } else {

                                Intent intent = new Intent(getApplicationContext(), NonBoardActivity.class);
                                intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                                intent.putExtra("STANDARD", STANDARD);
                                intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                                startActivity(intent);
                            }

                        } else {

                            if (STANDARD.equals("Class 11") || STANDARD.equals("Class 12")) {

                                Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                                intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                                intent.putExtra("STANDARD", STANDARD);
                                intent.putExtra("TYPE", TYPE);
                                intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                                intent.putExtra("source", sourceId);
                                startActivity(intent);

                            } else {

                                Intent intent = new Intent(getApplicationContext(), NonBoardActivity.class);
                                intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                                intent.putExtra("STANDARD", STANDARD);
                                intent.putExtra("TYPE", TYPE);
                                intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                                intent.putExtra("source", sourceId);
                                startActivity(intent);
                            }
                        }
                        break;

                    case "CBSE PRACTICE PAPERS":

                        Intent intent;

                        if(STANDARD.equals("Class 12")) {
                            intent  = new Intent(getApplicationContext(), StreamActivity.class);
                            intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                            intent.putExtra("STANDARD", STANDARD);
                            intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                            intent.putExtra("BOARD", BOARD);
                            startActivity(intent);
                        }

                        else if (STANDARD.equals("Class 10")) {
                            intent = new Intent(getApplicationContext(), NonBoardActivity.class);
                            intent.putExtra("SUBCATEGORY_HOME", SUBCATEGORY_HOME);
                            intent.putExtra("STANDARD", STANDARD);
                            intent.putExtra("CATEGORY_HOME", CATEGORY_HOME);
                            intent.putExtra("BOARD", BOARD);
                            startActivity(intent);
                        }

                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        list.clear();
        populateClasses();
        standardAdapter = new StandardAdapter(list, this);
        gvStandard.setAdapter(standardAdapter);

        AppController.getInstance().setConnectivityListener(StandardActivity.this);
    }

    public void populateClasses() {

        if (type.equals("0")) {
            String s[] = {"XII", "XI", "X", "IX", "VIII", "VII", "VI"};
            String s2[] = {"Class 12", "Class 11", "Class 10", "Class 9", "Class 8", "Class 7", "Class 6"};
            int ind = 0;
            for (int i = 12; i >= 6; i--) {
                Standard c = new Standard();
                c.setRom(s[ind]);
                c.setName(s2[ind]);
                ind++;
                list.add(c);
            }
        } else if (type.equals("1")) {
            String s[] = {"XII", "X"};
            String s2[] = {"Class 12", "Class 10"};

            Standard s1 = new Standard();
            s1.setRom(s[0]);
            s1.setName(s2[0]);
            list.add(s1);

            Standard s3 = new Standard();
            s3.setRom(s[1]);
            s3.setName(s2[1]);
            list.add(s3);
        }
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.grid_view_class), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
