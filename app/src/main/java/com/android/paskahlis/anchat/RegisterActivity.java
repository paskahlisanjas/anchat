package com.android.paskahlis.anchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    Button buttonRegister;
    EditText editTextEmailAddress;
    EditText editTextPassword;
    EditText editTextConfirmPassword;

    FirebaseAuth firebaseAuth;

    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister = (Button) findViewById(R.id.button_register);
        editTextEmailAddress = (EditText) findViewById(R.id.edit_text_email_address);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.edit_text_confirm_password);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                if (!isFormCompleted()) return;
                registerUser(
                        editTextEmailAddress.getText().toString(),
                        editTextPassword.getText().toString()
                );
            }
        });
    }

    public void getToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean isFormCompleted() {
        boolean temp = isFieldFilled(editTextEmailAddress)
                && isFieldFilled(editTextPassword)
                && isFieldFilled(editTextConfirmPassword);
        temp = temp ? editTextPassword.getText().toString().equals(editTextConfirmPassword
                .getText().toString()) : temp;
        return temp;
    }

    private boolean isFieldFilled(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("You can\'t leave the field empty");
            return false;
        }
        return true;
    }

    private void registerUser(String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("processing...");
        dialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.cancel();
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "Registration success, check your " +
                                    "emaiil for confirmation.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "Something went wrong, please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
