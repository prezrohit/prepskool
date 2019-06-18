package in.prepskool.prepskoolacademy.activities;

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.ExpandableListAdapter;
import in.prepskool.prepskoolacademy.adapter.HomeSectionRecyclerViewAdapter;
import in.prepskool.prepskoolacademy.model.Home;
import in.prepskool.prepskoolacademy.model.NavigationMenu;
import in.prepskool.prepskoolacademy.model.SectionHome;
import in.prepskool.prepskoolacademy.utils.RecyclerViewType;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //region VARIABLE DECLARATION
    private static final int PERMISSION_REQUEST_CODE = 200;
    private FloatingActionButton fab;
    private ExpandableListView expandableListView;
    private String TAG = HomeActivity.class.getSimpleName();
    private ArrayList<NavigationMenu> headerList = new ArrayList<>();
    private DrawerLayout drawer;
    private LinkedHashMap<String, ArrayList<NavigationMenu>> childList = new LinkedHashMap<>();
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //region Toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.bringToFront();
        myToolbar.setTitle(R.string.study_material);
        setSupportActionBar(myToolbar);
        //endregion

        //region Floating Action Button => Notification Activity
        fab = findViewById(R.id.fab_home);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificationIntent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(notificationIntent);
            }
        });
        //endregion

        RecyclerViewType recyclerViewType = RecyclerViewType.GRID;

        requestStoragePermission();

        expandableListView = findViewById(R.id.expandableListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView rvCategories = findViewById(R.id.home_sectioned_recycler_view);
        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(linearLayoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //region Generating Arraylist
        ArrayList<SectionHome> sectionHomeArrayList = new ArrayList<>();
        ArrayList<Home> arrayListNcert = new ArrayList<>();
        ArrayList<Home> arrayListPracticePaper = new ArrayList<>();
        ArrayList<Home> arrayListBoards = new ArrayList<>();
        ArrayList<String> arrayListSections = new ArrayList<>();

        arrayListSections.add("NCERT");
        arrayListSections.add("CBSE PRACTICE PAPERS");
        arrayListSections.add("SCHOOL BOARDS");

        arrayListNcert.add(new Home("NCERT BOOKS", R.mipmap.ic_ncert_books));
        arrayListNcert.add(new Home("NCERT NOTES", R.mipmap.ic_ncert_notes));
        arrayListNcert.add(new Home("NCERT SOLUTIONS", R.mipmap.ic_ncert_solutions));
        arrayListNcert.add(new Home("EXEMPLAR BOOKS", R.mipmap.ic_exemplar_books));
        arrayListNcert.add(new Home("EXEMPLAR SOLUTIONS", R.mipmap.ic_exemplar_books));

        sectionHomeArrayList.add(new SectionHome(arrayListSections.get(0), arrayListNcert));

        arrayListPracticePaper.add(new Home("SAMPLE PAPERS", R.drawable.ic_cbse));
        arrayListPracticePaper.add(new Home("PAST YEAR PAPERS", R.drawable.ic_cbse));
        arrayListPracticePaper.add(new Home("GUESS PAPERS", R.drawable.ic_cbse));

        sectionHomeArrayList.add(new SectionHome(arrayListSections.get(1), arrayListPracticePaper));

        arrayListBoards.add(new Home("CBSE", R.mipmap.ic_cbse));
        arrayListBoards.add(new Home("ICSE", R.mipmap.ic_cicse));
        arrayListBoards.add(new Home("UP", R.mipmap.ic_up));
        arrayListBoards.add(new Home("DELHI", R.mipmap.ic_delhi));
        arrayListBoards.add(new Home("TAMIL", R.mipmap.ic_tamil));

        sectionHomeArrayList.add(new SectionHome(arrayListSections.get(2), arrayListBoards));
        //endregion

        HomeSectionRecyclerViewAdapter adapter = new HomeSectionRecyclerViewAdapter(this,
                recyclerViewType, sectionHomeArrayList);

        rvCategories.setAdapter(adapter);
        rvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        prepareMenuData();
        populateExpandableList();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //region Requesting Storage Permission
    private void requestStoragePermission() {

        if (checkPermission()) {

            boolean success = true;
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Prepskool");
            if (!folder.exists()) {
                success = folder.mkdirs();
            }

            if (success) {
                Log.v("HomeActivity", "Folder Created");
            } else
                Log.v("HomeActivity", "Folder Creating Error");
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]
                {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeAccepted && readAccepted)
                    Log.v(TAG, "Permission Granted");
                else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,
                                                    READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                                        }
                                    });
                        }
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeActivity.this).setMessage(message).setPositiveButton
                ("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }
    //endregion

    //region OptionsMenu and NavigationView Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SavedFilesActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion

    private void prepareMenuData() {

        headerList.add(new NavigationMenu("NCERT Book", R.drawable.ic_ncert_book));
        headerList.add(new NavigationMenu("Saved Files", R.drawable.ic_saved_files_black));
        headerList.add(new NavigationMenu("CBSE", R.mipmap.ic_cbse));
        headerList.add(new NavigationMenu("ICSE", R.mipmap.ic_cicse));
        headerList.add(new NavigationMenu("DELHI", R.mipmap.ic_delhi));
        headerList.add(new NavigationMenu("Online Test", R.drawable.ic_online_test));
        headerList.add(new NavigationMenu("Rate App", R.drawable.ic_rate));
        headerList.add(new NavigationMenu("Share", R.drawable.ic_menu_share));
        headerList.add(new NavigationMenu("About Us", R.drawable.ic_about_us));
        headerList.add(new NavigationMenu("Privacy Policy", R.drawable.ic_privacy_policy));

        childList.put("NCERT Book", null);

        childList.put(headerList.get(1).getTitle(), null);

        ArrayList<NavigationMenu> arrayList = new ArrayList<>();
        arrayList.add(new NavigationMenu("NCERT Notes", R.mipmap.ic_pdf));
        arrayList.add(new NavigationMenu("Topper Answer Sheet", R.mipmap.ic_pdf));
        arrayList.add(new NavigationMenu("Chapter Wise Questions", R.mipmap.ic_pdf));

        childList.put(headerList.get(2).getTitle(), arrayList);

        ArrayList<NavigationMenu> arrayList1 = new ArrayList<>();
        arrayList1.add(new NavigationMenu("Past Year Papers", R.mipmap.ic_pdf));
        arrayList1.add(new NavigationMenu("Marking Scheme", R.mipmap.ic_pdf));
        arrayList1.add(new NavigationMenu("Sample Papers", R.mipmap.ic_pdf));

        childList.put(headerList.get(3).getTitle(), arrayList1);
        childList.put(headerList.get(4).getTitle(), arrayList1);
        childList.put(headerList.get(5).getTitle(), null);
        childList.put(headerList.get(6).getTitle(), null);
        childList.put(headerList.get(7).getTitle(), null);
        childList.put(headerList.get(8).getTitle(), null);
        childList.put(headerList.get(9).getTitle(), null);
    }

    private void populateExpandableList() {

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (childList.get(headerList.get(groupPosition).getTitle()) == null) {
                    drawer.closeDrawers();
                }

                String list = headerList.get(groupPosition).getTitle();

                switch (list) {

                    case "NCERT Book":
                        Intent intent = new Intent(HomeActivity.this, StandardActivity.class);
                        intent.putExtra("CATEGORY_HOME", "NCERT");
                        intent.putExtra("SUBCATEGORY_HOME", "BOOKS");
                        intent.putExtra("type", "0");
                        startActivity(intent);
                        break;

                    case "Saved Files":
                        startActivity(new Intent(HomeActivity.this, SavedFilesActivity.class));
                        break;

                    case "Rate App":
                        Uri uri = Uri.parse("market://details?id=com.ext.ui");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=com.ext.ui")));
                        }
                        break;

                    case "Online Test":
                        Intent intent1 = new Intent(HomeActivity.this, WebViewActivity.class);
                        intent1.putExtra("sender", 1);
                        intent1.putExtra("url", "https://logicalpaper.co/practice-online-test/");
                        startActivity(intent1);
                        break;

                    case "Share":
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Download PrepskoolAcademy Android App");
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;

                    case "About Us":
                        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
                        break;

                    case "Privacy Policy":
                        startActivity(new Intent(HomeActivity.this, PrivacyPolicyActivity.class));
                        break;
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                drawer.closeDrawers();

                String childName = childList.get(headerList.get(groupPosition).getTitle()).get(childPosition).getTitle();
                Intent intent = new Intent(getApplicationContext(), StandardActivity.class);

                switch (childName) {

                    case "NCERT Notes":
                        intent.putExtra("CATEGORY_HOME", "NCERT");
                        intent.putExtra("SUBCATEGORY_HOME", "NOTES");
                        intent.putExtra("type", "1");
                        startActivity(intent);
                        break;

                    case "Topper Answer Sheet":
                        intent.putExtra("CATEGORY_HOME", "SCHOOL BOARDS");
                        intent.putExtra("SUBCATEGORY_HOME", "CBSE BOARD");
                        intent.putExtra("TYPE", "Topper Answer Sheet");
                        intent.putExtra("source", 1);
                        intent.putExtra("type", "1");
                        startActivity(intent);
                        break;

                    case "Chapter Wise Questions":
                        intent.putExtra("CATEGORY_HOME", "SCHOOL BOARDS");
                        intent.putExtra("SUBCATEGORY_HOME", "CBSE BOARD");
                        intent.putExtra("TYPE", "NCERT Chapterwise Important Questions");
                        intent.putExtra("source", 1);
                        intent.putExtra("type", "1");
                        startActivity(intent);
                        break;
                }

                String header = headerList.get(groupPosition).getTitle();
                Log.v(TAG, "header: " + header + " and Child: " + childName);

                switch (childName) {

                    case "Past Year Papers":

                        switch (header) {
                            case "ICSE":
                                intent.putExtra("CATEGORY_HOME", "PRACTICE PAPERS");
                                intent.putExtra("SUBCATEGORY_HOME", "Past Year Papers");
                                intent.putExtra("BOARD", "ICSE Board");
                                intent.putExtra("type", "1");
                                startActivity(intent);
                                break;

                            case "DELHI":
                                intent.putExtra("CATEGORY_HOME", "PRACTICE PAPERS");
                                intent.putExtra("SUBCATEGORY_HOME", "Past Year Papers");
                                intent.putExtra("BOARD", "DELHI BOARD");
                                intent.putExtra("type", "1");
                                startActivity(intent);
                                break;
                        }
                        break;

                    case "Marking Scheme":

                        switch (header) {
                            case "ICSE":
                                intent.putExtra("CATEGORY_HOME", "SCHOOL BOARDS");
                                intent.putExtra("SUBCATEGORY_HOME", "ICSE Board");
                                intent.putExtra("TYPE", "Marking Scheme");
                                intent.putExtra("source", 1);
                                intent.putExtra("type", "1");
                                startActivity(intent);
                                break;

                            case "DELHI":
                                intent.putExtra("CATEGORY_HOME", "SCHOOL BOARDS");
                                intent.putExtra("SUBCATEGORY_HOME", "DELHI BOARD");
                                intent.putExtra("TYPE", "Marking Scheme");
                                intent.putExtra("source", 1);
                                intent.putExtra("type", "1");
                                startActivity(intent);
                                break;
                        }
                        break;

                    case "Sample Papers":

                        switch (header) {
                            case "ICSE":
                                intent.putExtra("CATEGORY_HOME", "PRACTICE PAPERS");
                                intent.putExtra("SUBCATEGORY_HOME", "Sample Papers");
                                intent.putExtra("BOARD", "ICSE Board");
                                intent.putExtra("type", "1");
                                startActivity(intent);
                                break;

                            case "DELHI":
                                intent.putExtra("CATEGORY_HOME", "PRACTICE PAPERS");
                                intent.putExtra("SUBCATEGORY_HOME", "Sample Papers");
                                intent.putExtra("BOARD", "DELHI BOARD");
                                intent.putExtra("type", "1");
                                startActivity(intent);
                                break;
                        }
                        break;
                }
                return false;
            }
        });
    }
}
