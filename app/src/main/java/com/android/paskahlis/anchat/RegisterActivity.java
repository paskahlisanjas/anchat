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

import com.android.paskahlis.anchat.entity.EntityUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private final String USER_ROOT = "users";

    private Button buttonRegister;
    private EditText editTextDisplayName;
    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference().child(EntityUser.USER_ROOT);

    private Activity activity = this;

    private final String TAG = "ANCHAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister = (Button) findViewById(R.id.button_register);
        editTextDisplayName = (EditText) findViewById(R.id.edit_text_display_name);
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
                        editTextPassword.getText().toString(),
                        editTextDisplayName.getText().toString()
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
        boolean temp = isFieldFilled(editTextDisplayName)
                && isFieldFilled(editTextEmailAddress)
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

    private void registerUser(final String email, String password, final String displayName) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("processing...");
        dialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.cancel();
                        if (task.isSuccessful()) {
                            EntityUser user = new EntityUser(displayName, null,
                                    0d, 0d, null);
                            FirebaseUser fu = firebaseAuth.getCurrentUser();
                            databaseReference.child(fu.getUid()).setValue(user).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(activity, "Registration success.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(activity, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(activity, "Failed to store to database.",
                                                        Toast.LENGTH_SHORT).show();
                                                FirebaseException exception = (FirebaseException) task.getException();
                                                Log.d(TAG, "Failed to store to database. " + exception.getMessage());
                                            }
                                        }
                                    }
                            );
                        } else {
                            Toast.makeText(activity, "Registration failed, please try again.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseException exception = (FirebaseException) task.getException();
                            Log.d(TAG, "Failed registration. " + exception.getMessage());
                        }
                    }
                });
    }
}
