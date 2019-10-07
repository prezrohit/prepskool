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

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.app.AppSharedPreferences;
import in.prepskool.prepskoolacademy.app.PrepskoolApplication;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.LoginResponse;
import in.prepskool.prepskoolacademy.retrofit_model.Register;
import in.prepskool.prepskoolacademy.retrofit_model.RegisterResponse;
import in.prepskool.prepskoolacademy.retrofit_model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

    private ProgressBar progressBar;

    private static final String SUCCESS = "success";
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);

        progressBar = findViewById(R.id.progress_bar);
    }

    public void onClickRegister(View view) {
        boolean isInvalid = false;

        final String name = edtName.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            isInvalid = true;
            edtName.setError("this cannot be empty");

        } else if (!name.matches("^[a-zA-Z]*$")) {
            isInvalid = true;
            edtName.setError("this doesn't look like a name");
        }


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

        } else if (TextUtils.isEmpty(confirmPassword)) {
            isInvalid = true;
            edtConfirmPassword.setError("this cannot be empty");

        } else if (!password.equals(confirmPassword)) {
            isInvalid = true;
            edtConfirmPassword.setError("passwords do not match");
        }


        if (!isInvalid) {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Registering....", Toast.LENGTH_SHORT).show();

            ((PrepskoolApplication) getApplication()).getRegisterComponent().inject(this);
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<RegisterResponse> call = apiInterface.register(new Register(name, email, password));
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: " + response.code());
                    Log.d(TAG, "onResponse: " + response.isSuccessful());
                    RegisterResponse registerResponse = response.body();
                    if (response.isSuccessful()) {
                        if (registerResponse.getStatus().equals(SUCCESS)) {
                            AppSharedPreferences appSharedPreferences = new AppSharedPreferences(RegisterActivity.this);
                            appSharedPreferences.setToken(registerResponse.getToken());
                            appSharedPreferences.setEmail(email);
                            appSharedPreferences.setName(registerResponse.getUser().getName());
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finish();

                        } else {
                            Log.d(TAG, "onResponse: status error");
                            Toast.makeText(RegisterActivity.this, "status error", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d(TAG, "onResponse: response not successful");
                        Toast.makeText(RegisterActivity.this, "response not successful", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onClickLoginInstead(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
