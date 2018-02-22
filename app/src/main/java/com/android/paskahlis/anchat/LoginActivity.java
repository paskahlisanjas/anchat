package com.android.paskahlis.anchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "[ANCHAT]";
    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private Button buttonLogin;

    private FirebaseAuth firebaseAuth;

    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmailAddress = (EditText) findViewById(R.id.edit_text_email_address);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        buttonLogin = (Button) findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                if (!isFormCompleted()) return;
                loginUser(
                        editTextEmailAddress.getText().toString(),
                        editTextPassword.getText().toString()
                );
            }
        });
    }

    private boolean isFormCompleted() {
        return isFieldFilled(editTextEmailAddress) && isFieldFilled(editTextPassword);
    }

    private boolean isFieldFilled(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("You can\'t leave the field empty");
            return false;
        }
        return true;
    }

    private void loginUser(String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("authenticating...");
        dialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.cancel();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(activity, "Login failed, please try again.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseException exception = (FirebaseException) task.getException();
                            Log.d(TAG, "Failed login. " + exception.getMessage());
                        }
                    }
                });
    }

    public void getToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void resetUserPassword(View view) {

    }
}