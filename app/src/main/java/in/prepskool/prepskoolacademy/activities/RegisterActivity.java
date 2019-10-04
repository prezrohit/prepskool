package in.prepskool.prepskoolacademy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import in.prepskool.prepskoolacademy.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
    }

    public void onClickRegister(View view) {
        boolean isInvalid = false;

        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
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
            Toast.makeText(this, "Registering....", Toast.LENGTH_SHORT).show();
        }
    }
}
