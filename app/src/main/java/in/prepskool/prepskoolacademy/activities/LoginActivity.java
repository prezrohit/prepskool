package in.prepskool.prepskoolacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.app.AppSharedPreferences;
import in.prepskool.prepskoolacademy.app.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.Login;
import in.prepskool.prepskoolacademy.retrofit_model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Inject Retrofit retrofit;

    private EditText edtEmail;
    private EditText edtPassword;

    private ProgressBar progressBar;

    private static final String SUCCESS = "success";
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        progressBar = findViewById(R.id.progress_bar);
    }

    public void onClickLogin(View view) {
        boolean isInvalid = false;

        final String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            isInvalid = true;
            edtEmail.setError("this cannot be empty");

        } else if (!email.contains("@")) {
            isInvalid = true;
            edtEmail.setError("this doesn't look like an email address");
        }

        if (TextUtils.isEmpty(password)) {
            isInvalid = true;
            edtPassword.setError("this cannot be empty");

        } else if (password.length() < 6) {
            isInvalid = true;
            edtPassword.setError("minimum password length is 6");
        }

        if (!isInvalid) {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Logging In....", Toast.LENGTH_SHORT).show();
            ((PrepskoolApplication) getApplication()).getLoginComponent().inject(this);
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<LoginResponse> call = apiInterface.login(new Login(email, password));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: " + response.code());
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(SUCCESS)) {
                            AppSharedPreferences appSharedPreferences = new AppSharedPreferences(LoginActivity.this);
                            appSharedPreferences.setEmail(email);
                            appSharedPreferences.setToken(response.body().getToken());
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();

                        } else {
                            Log.d(TAG, "onResponse: status error");
                            Toast.makeText(LoginActivity.this, "status error", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d(TAG, "onResponse: response not successful");
                        Toast.makeText(LoginActivity.this, "response not successful", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onClickCreateAccount(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}
