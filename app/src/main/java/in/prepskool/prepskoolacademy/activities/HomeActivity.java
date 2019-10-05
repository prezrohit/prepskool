package in.prepskool.prepskoolacademy.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.adapter.ExpandableListAdapter;
import in.prepskool.prepskoolacademy.adapter.HomeSectionRecyclerViewAdapter;
import in.prepskool.prepskoolacademy.model.NavigationMenu;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.Board;
import in.prepskool.prepskoolacademy.retrofit_model.HomeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.Ncert;
import in.prepskool.prepskoolacademy.retrofit_model.PracticePaper;
import in.prepskool.prepskoolacademy.retrofit_model.SectionedHome;
import in.prepskool.prepskoolacademy.services.CheckNetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.ABOUT_US;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.CBSE;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.CHAPTER_WISE_QUES;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.DELHI;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.ICSE;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.MARKING_SCHEME;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.NCERT_BOOK;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.NCERT_NOTES;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.ONLINE_TEST;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.PAST_YEAR_PAPER;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.PRIVACY;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.RATE_APP;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.SAMPLE_PAPER;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.SAVED_FILES;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.SHARE;
import static in.prepskool.prepskoolacademy.model.NavigationMenu.Menu.TOPPER_ANSWERS;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Inject
    Retrofit retrofit;

    private DrawerLayout drawer;
    private RecyclerView rvCategories;
    private ProgressBar progressBar;
    private ExpandableListView expandableListView;

    private ArrayList<NavigationMenu> headerList;
    private LinkedHashMap<NavigationMenu.Menu, ArrayList<NavigationMenu>> childList;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((PrepskoolApplication) getApplication()).getHomeComponent().inject(this);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        startService(new Intent(this, CheckNetworkService.class));

        setToolbar();

        FirebaseMessaging.getInstance().subscribeToTopic("prepskool")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Log.d(TAG, msg);
                    }
                });


        final FloatingActionButton fab = findViewById(R.id.fab_notifications);
        drawer = findViewById(R.id.drawer_layout);
        progressBar = findViewById(R.id.progress_bar);
        expandableListView = findViewById(R.id.expandable_list_view);
        rvCategories = findViewById(R.id.rv_sectioned_home);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        requestStoragePermission();

        childList = new LinkedHashMap<>();
        headerList = new ArrayList<>();

        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

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


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(notificationIntent);
            }
        });

        getHomeResponse(apiInterface);

        prepareMenuData();
        populateExpandableList();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                setToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private Toolbar setToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.bringToFront();
        myToolbar.setTitle(R.string.study_material);
        setSupportActionBar(myToolbar);
        return myToolbar;
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

    //region Preparing Navigation Menu Items
    private void prepareMenuData() {

        headerList.add(new NavigationMenu(NCERT_BOOK, "NCERT Book", R.drawable.ic_ncert_book));
        headerList.add(new NavigationMenu(SAVED_FILES,"Saved Files", R.drawable.ic_saved_files_black));
        headerList.add(new NavigationMenu(CBSE,"CBSE", R.mipmap.ic_cbse));
        headerList.add(new NavigationMenu(ICSE,"ICSE", R.mipmap.ic_cicse));
        headerList.add(new NavigationMenu(DELHI,"DELHI", R.mipmap.ic_delhi));
        headerList.add(new NavigationMenu(ONLINE_TEST,"Online Test", R.drawable.ic_online_test));
        headerList.add(new NavigationMenu(RATE_APP,"Rate App", R.drawable.ic_rate));
        headerList.add(new NavigationMenu(SHARE,"Share", R.drawable.ic_menu_share));
        headerList.add(new NavigationMenu(ABOUT_US,"About Us", R.drawable.ic_about_us));
        headerList.add(new NavigationMenu(PRIVACY,"Privacy Policy", R.drawable.ic_privacy_policy));

        childList.put(headerList.get(0).getMenuId(), null);

        childList.put(headerList.get(1).getMenuId(), null);

        ArrayList<NavigationMenu> arrayList = new ArrayList<>();
        arrayList.add(new NavigationMenu(NCERT_NOTES, "NCERT Notes", R.mipmap.ic_pdf));
        arrayList.add(new NavigationMenu(TOPPER_ANSWERS, "Topper Answer Sheet", R.mipmap.ic_pdf));
        arrayList.add(new NavigationMenu(CHAPTER_WISE_QUES, "Chapter Wise Questions", R.mipmap.ic_pdf));

        childList.put(headerList.get(2).getMenuId(), arrayList);

        ArrayList<NavigationMenu> arrayList1 = new ArrayList<>();
        arrayList1.add(new NavigationMenu(PAST_YEAR_PAPER, "Past Year Papers", R.mipmap.ic_pdf));
        arrayList1.add(new NavigationMenu(MARKING_SCHEME, "Marking Scheme", R.mipmap.ic_pdf));
        arrayList1.add(new NavigationMenu(SAMPLE_PAPER, "Sample Papers", R.mipmap.ic_pdf));

        childList.put(headerList.get(3).getMenuId(), arrayList1);
        childList.put(headerList.get(4).getMenuId(), arrayList1);
        childList.put(headerList.get(5).getMenuId(), null);
        childList.put(headerList.get(6).getMenuId(), null);
        childList.put(headerList.get(7).getMenuId(), null);
        childList.put(headerList.get(8).getMenuId(), null);
        childList.put(headerList.get(9).getMenuId(), null);
    }

    private void populateExpandableList() {

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (childList.get(headerList.get(groupPosition).getMenuId()) == null)
                    drawer.closeDrawers();

                NavigationMenu.Menu menu = headerList.get(groupPosition).getMenuId();

                switch (menu) {

                    case NCERT_BOOK:
                        Intent intent = new Intent(HomeActivity.this, StandardActivity.class);
                        intent.putExtra("board_id", -1);
                        intent.putExtra("home_item_name", "NCERT Book");
                        startActivity(intent);
                        break;

                    case SAVED_FILES:
                        startActivity(new Intent(HomeActivity.this, SavedFilesActivity.class));
                        break;

                    case RATE_APP:
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

                    case ONLINE_TEST:
                        Intent intent1 = new Intent(HomeActivity.this, WebViewActivity.class);
                        intent1.putExtra("sender", 1);
                        intent1.putExtra("url", "https://logicalpaper.co/practice-online-test/");
                        startActivity(intent1);
                        break;

                    case SHARE:
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Download PrepskoolAcademy Android App");
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;

                    case ABOUT_US:
                        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
                        break;

                    case PRIVACY:
                        startActivity(new Intent(HomeActivity.this, PrivacyPolicyActivity.class));
                        break;
                }

                return false;
            }
        });

        //TODO: NavigationView intent passing

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, 
                int childPosition, long id) {

                drawer.closeDrawers();

                NavigationMenu.Menu menuId = childList.get(headerList.get(groupPosition).getMenuId()).get(childPosition).getMenuId();
                Intent intent = new Intent(getApplicationContext(), StandardActivity.class);

                switch (menuId) {

                    case NCERT_NOTES:
                        intent.putExtra("board_id", 2);
                        intent.putExtra("home_item_id", 5);
                        intent.putExtra("home_item_name", "NCERT Notes");
                        startActivity(intent);
                        break;

                    case TOPPER_ANSWERS:
                        intent.putExtra("board_id", 2);
                        intent.putExtra("home_item_id", 11);
                        intent.putExtra("home_item_name", "Topper Answer Sheet");
                        startActivity(intent);
                        break;

                    case CHAPTER_WISE_QUES:
                        intent.putExtra("board_id", 2);
                        intent.putExtra("home_item_id", 12);
                        intent.putExtra("home_item_name", "Chapter Wise Questions");
                        startActivity(intent);
                        break;
                }

                NavigationMenu.Menu headerMenuId = headerList.get(groupPosition).getMenuId();
                Log.v(TAG, "header: " + headerMenuId + " and Child: " + menuId);

                switch (menuId) {

                    case PAST_YEAR_PAPER:

                        switch (headerMenuId) {
                            case ICSE:

                            case DELHI:
                                intent.putExtra("board_id", 2);
                                intent.putExtra("home_item_id", 7);
                                intent.putExtra("home_item_name", "Past Year Papers");
                                startActivity(intent);
                                break;
                        }
                        break;

                    case MARKING_SCHEME:

                        switch (headerMenuId) {
                            case ICSE:

                            case DELHI:
                                intent.putExtra("board_id", 2);
                                intent.putExtra("home_item_id", 14);
                                intent.putExtra("home_item_name", "Marking Scheme");
                                startActivity(intent);
                                break;
                        }
                        break;

                    case SAMPLE_PAPER:

                        switch (headerMenuId) {
                            case ICSE:

                            case DELHI:
                                intent.putExtra("board_id", 2);
                                intent.putExtra("home_item_id", 9);
                                intent.putExtra("home_item_name", "Sample Paper");
                                startActivity(intent);
                                break;
                        }
                        break;
                }
                return false;
            }
        });
    }
    //endregion

    private void getHomeResponse(ApiInterface apiInterface) {
        progressBar.setVisibility(View.VISIBLE);
        Call<HomeResponse> call = apiInterface.getHomeResponse();
        call.enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(@NonNull Call<HomeResponse> call, @NonNull Response<HomeResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.message());
                Log.d(TAG, "onResponse: " + response.isSuccessful());

                if (response.isSuccessful()) {
                    HomeResponse homeResponse = response.body();
                    Ncert ncert = homeResponse.getNcert();
                    Board board = homeResponse.getBoard();
                    PracticePaper practicePaper = homeResponse.getPracticePaper();
                    ArrayList<SectionedHome> homeArrayList = new ArrayList<>();

                    SectionedHome sectionedHome = new SectionedHome();
                    sectionedHome.generateListByNcert(0, ncert.getLabel(), ncert.getNcertDataList());
                    homeArrayList.add(sectionedHome);

                    SectionedHome sectionedHome1 = new SectionedHome();
                    sectionedHome1.generateListByPracticePaper(1, practicePaper.getLabel(), practicePaper.getPracticePaperDataList());
                    homeArrayList.add(sectionedHome1);

                    SectionedHome sectionedHome2 = new SectionedHome();
                    sectionedHome2.generateListByBoard(2, board.getLabel(), board.getBoardDataList());
                    homeArrayList.add(sectionedHome2);

                    Log.d(TAG, "board data list size: " + board.getBoardDataList().size());

                    HomeSectionRecyclerViewAdapter homeSectionRecyclerViewAdapter = new HomeSectionRecyclerViewAdapter(HomeActivity.this,
                            homeArrayList);
                    rvCategories.setAdapter(homeSectionRecyclerViewAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}




















/*switch (menuId) {

        case PAST_YEAR_PAPER:

        switch (headerMenuId) {
        case ICSE:
        intent.putExtra("board_id", 2);
        intent.putExtra("home_item_name", "Past Year Papers");
        startActivity(intent);
        break;

        case DELHI:
        intent.putExtra("board_id", 2);
        intent.putExtra("section_name", "Past Year Papers");
        startActivity(intent);
        break;
        }
        break;

        case MARKING_SCHEME:

        switch (headerMenuId) {
        case ICSE:
        intent.putExtra("board_id", 2);
        intent.putExtra("section_name", "Marking Scheme");
        startActivity(intent);
        break;

        case DELHI:
        intent.putExtra("board_id", 2);
        intent.putExtra("section_name", "Marking Scheme");
        startActivity(intent);
        break;
        }
        break;

        case SAMPLE_PAPER:

        switch (headerMenuId) {
        case ICSE:
        intent.putExtra("board_id", 2);
        intent.putExtra("section_name", "Sample Paper");
        startActivity(intent);
        break;

        case DELHI:
        intent.putExtra("board_id", 2);
        intent.putExtra("section_name", "Sample Paper");
        startActivity(intent);
        break;
        }
        break;
        }*/
