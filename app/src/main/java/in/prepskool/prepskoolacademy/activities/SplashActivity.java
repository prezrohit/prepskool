package in.prepskool.prepskoolacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.app.AppSharedPreferences;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_SplashActivity);
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppSharedPreferences appSharedPreferences = new AppSharedPreferences(SplashActivity.this);
                if (appSharedPreferences.getEmail() != null) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();

                } else {
                    startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                    finish();
                }
            }
        }, 1000);
    }
}
