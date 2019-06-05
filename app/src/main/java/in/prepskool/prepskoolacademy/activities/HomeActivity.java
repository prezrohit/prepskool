package in.prepskool.prepskoolacademy.activities;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.RecyclerViewType;
import in.prepskool.prepskoolacademy.adapter.HomeSectionRecyclerViewAdapter;
import in.prepskool.prepskoolacademy.model.Home;
import in.prepskool.prepskoolacademy.model.SectionHome;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //region VARIABLE DECLARATION
    private static final int PERMISSION_REQUEST_CODE = 200;
    private FloatingActionButton fab;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //region Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.bringToFront();
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

        RecyclerView rvCategories = (RecyclerView) findViewById(R.id.home_sectioned_recycler_view);
        rvCategories.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCategories.setLayoutManager(linearLayoutManager);

        //region Generating Arraylist
        ArrayList<SectionHome> sectionHomeArrayList = new ArrayList<>();
        ArrayList<Home> arrayListNcert = new ArrayList<>();
        ArrayList<Home> arrayListPracticePaper = new ArrayList<>();
        ArrayList<Home> arrayListBoards = new ArrayList<>();
        ArrayList<String> arrayListSections = new ArrayList<>();

        arrayListSections.add("NCERT");
        arrayListSections.add("PRACTICE PAPERS");
        arrayListSections.add("SCHOOL BOARDS");

        arrayListNcert.add(new Home("NCERT BOOKS", R.mipmap.ic_ncert_books));
        arrayListNcert.add(new Home("NCERT NOTES", R.mipmap.ic_ncert_notes));
        arrayListNcert.add(new Home("NCERT SOLUTIONS", R.mipmap.ic_ncert_solutions));
        arrayListNcert.add(new Home("EXEMPLAR BOOKS", R.mipmap.ic_exemplar_books));
        arrayListNcert.add(new Home("EXEMPLAR SOLUTIONS", R.mipmap.ic_exemplar_books));

        sectionHomeArrayList.add(new SectionHome(arrayListSections.get(0), arrayListNcert));

        arrayListPracticePaper.add(new Home("SAMPLE PAPERS", R.mipmap.ic_pdf));
        arrayListPracticePaper.add(new Home("PAST YEAR PAPERS", R.mipmap.ic_pdf));
        arrayListPracticePaper.add(new Home("GUESS PAPERS", R.mipmap.ic_pdf));

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //region Requesting Storage Permission
    private void requestStoragePermission() {

        if (checkPermission()) {

            Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writeAccepted && readAccepted)
                        Toast.makeText(HomeActivity.this, "Permission Granted",
                                Toast.LENGTH_SHORT).show();
                    else {

                        Toast.makeText(HomeActivity.this, "Permission Denied",
                                Toast.LENGTH_SHORT).show();

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
                                return;
                            }
                        }
                    }
                }
                break;
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
        startActivity(new Intent(this, DownloadedItemsActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.nav_ncert_books:
                Intent intent = new Intent(this, StandardActivity.class);
                intent.putExtra("CATEGORY_HOME", "NCERT");
                intent.putExtra("SUBCATEGORY_HOME", "BOOKS");
                intent.putExtra("type", "0");
                startActivity(intent);
                break;

            case R.id.nav_downloads:
                startActivity(new Intent(this, DownloadedItemsActivity.class));
                break;

            case R.id.nav_rate:
                Toast.makeText(this, "Rating", Toast.LENGTH_SHORT).show();
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

            case R.id.nav_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;

            case R.id.nav_privacy_policy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share Link", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion
}
