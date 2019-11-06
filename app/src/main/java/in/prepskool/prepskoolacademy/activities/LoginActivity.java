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
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.app.AppSharedPreferences;
import in.prepskool.prepskoolacademy.app.PrepskoolApplication;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.Login;
import in.prepskool.prepskoolacademy.retrofit_model.LoginResponse;
import in.prepskool.prepskoolacademy.retrofit_model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private EditText edtEmail;
    private EditText edtPassword;

    private ProgressBar progressBar;

    private static final String SUCCESS = "success";
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.string_login);
        setSupportActionBar(toolbar);

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
                        LoginResponse loginResponse = response.body();
                        assert loginResponse != null : "Login Response is Empty";
                        User user = loginResponse.getUser();
                        AppSharedPreferences appSharedPreferences = new AppSharedPreferences(LoginActivity.this);
                        appSharedPreferences.setId(user.getId());
                        appSharedPreferences.setEmail(email);
                        appSharedPreferences.setName(user.getName());
                        appSharedPreferences.setPhone(user.getPhone());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();

                    } else {
                        try {
                            assert response.errorBody() != null : "Error is Empty";
                            JSONObject errorBody = new JSONObject(response.errorBody().string());
                            String errorMessage = errorBody.getString("message");
                            Log.d(TAG, "onResponseError: " + errorMessage);
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
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

    public void onClickSkip(View view) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
