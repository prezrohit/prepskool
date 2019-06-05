package in.prepskool.prepskoolacademy.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.prepskool.prepskoolacademy.Endpoints;
import in.prepskool.prepskoolacademy.R;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DownloadActivity extends AppCompatActivity {

    private Button btnDownload;
    private String folderPath;
    private String pdfUrl;
    private String pdfSlug;
    private String pdfName;
    private ProgressDialog mProgressDialog;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String TAG = DownloadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        pdfUrl = getIntent().getStringExtra("link");
        pdfSlug = getIntent().getStringExtra("slug");
        pdfName = getIntent().getStringExtra("name");

        Log.v(TAG, "Name: " + pdfName);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(pdfName);
        setSupportActionBar(toolbar);

        btnDownload = findViewById(R.id.btnDownload);

        requestStoragePermission();

        mProgressDialog = new ProgressDialog(DownloadActivity.this);
        mProgressDialog.setMessage("Downloading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadActivity.DownloadTask downloadTask = new DownloadActivity.DownloadTask(DownloadActivity.this);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadTask.execute(Endpoints.GOOGLEDRIVE + getPdfId(pdfUrl));
            }
        });
    }

    private void requestStoragePermission() {

        if (checkPermission()) {

            Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
            boolean folderExists = createFolder();

            if (folderExists) {
                folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + "Prepskool").getAbsolutePath();
            } else {
                Log.v(TAG, "Error Creating Prepskool Directory");
            }
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

        boolean folderExists = createFolder();

        if (folderExists) {
            folderPath = new File(Environment.getExternalStorageDirectory() + File.separator + "Prepskool").getAbsolutePath();
        } else {
            Log.v(TAG, "Error Creating Prepskool Directory");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeAccpeted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writeAccpeted && readAccepted)
                        Toast.makeText(DownloadActivity.this, "Permission Granted",
                                Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(DownloadActivity.this, "Permission Denied",
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
        new AlertDialog.Builder(DownloadActivity.this).setMessage(message).setPositiveButton
                ("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }

    private boolean createFolder() {

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Prepskool");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        return success;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/Prepskool/" + pdfSlug + ".pdf");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null) output.close();
                    if (input != null) input.close();
                } catch (IOException ignored) {
                }

                if (connection != null) connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)

                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else {

                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DownloadActivity.this, PdfLoaderActivity.class);
                intent.putExtra("slug", pdfSlug);
                intent.putExtra("link", pdfUrl);
                startActivity(intent);
                finish();
            }
        }
    }

    private String getPdfId(String pdfLink) {

        return pdfLink.substring(pdfLink.lastIndexOf("=") + 1);
    }
}




/*mProgressDialog = new ProgressDialog(DownloadActivity.this);
        mProgressDialog.setMessage("Downloading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadActivity.DownloadTask downloadTask = new DownloadActivity.DownloadTask(DownloadActivity.this);

        url = getIntent().getStringExtra("link");
        pdfSlug = getIntent().getStringExtra("slug");

        requestStoragePermission();

        file = new File(folderpath, pdfSlug + ".pdf");

        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
                    if (file.exists()) {

                        url = file.getAbsolutePath();
                        Intent intent = new Intent(DownloadActivity.this, PdfLoaderActivity.class);
                        intent.putExtra("filePath", url);
                        intent.putExtra("slug", pdfSlug);
                        intent.putExtra("sender", "0");
                        startActivity(intent);
                        finish();

                    } else {
                        downloadTask.execute("https://drive.google.com/uc?export=download&id=" + getPdfId(url));
                    }
                }
                else {
                    requestPermission();
                    Toast.makeText(DownloadActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            }
        });*/











/*private void requestStoragePermission() {

        if(checkPermission()) {

            Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
            boolean success = true;
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Prepskool");
            if (!folder.exists()) {
                success = folder.mkdirs();
            }

            if (success) {
                Log.v("DownloadActivity", "Folder Created");
                folderpath = folder.getAbsolutePath();
            }
            else
                Log.v("DownloadActivity", "Folder Creating Error");
        }
        else {
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
        folderpath = createFolder();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(DownloadActivity.this, "Permission Granted",
                                Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(DownloadActivity.this, "Permission Denied",
                                Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,
                                                            READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                                                }
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
        new AlertDialog.Builder(DownloadActivity.this).setMessage(message).setPositiveButton
                ("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }

    private String createFolder() {

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Prepskool");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            Log.v("###", "folder_created1");

        } else {
            Log.v("###", "folder_created2");    // Do something else on failure
        }
        return folder.getAbsolutePath();
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/Prepskool/" + pdfSlug + ".pdf");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null) output.close();
                    if (input != null) input.close();
                } catch (IOException ignored) {
                }

                if (connection != null) connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
                File file = new File(folderpath, pdfSlug + ".pdf");
                url = file.getAbsolutePath();
                Intent intent = new Intent(DownloadActivity.this, PdfLoaderActivity.class);
                intent.putExtra("filePath", url);
                intent.putExtra("slug", pdfSlug);
                startActivity(intent);
                finish();
            }
        }
    }

    private String getPdfId(String pdfLink) {

        return pdfLink.substring(pdfLink.lastIndexOf("=") + 1);
    }

}*/