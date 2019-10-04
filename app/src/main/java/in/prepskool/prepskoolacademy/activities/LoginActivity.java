package in.prepskool.prepskoolacademy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.prepskool.prepskoolacademy.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
    }

    public void onClickLogin(View view) {
        boolean isInvalid = false;

        String email = edtEmail.getText().toString().trim();
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
            Toast.makeText(this, "Logging In....", Toast.LENGTH_SHORT).show();
        }
    }
}
