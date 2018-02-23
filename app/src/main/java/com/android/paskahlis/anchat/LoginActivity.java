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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "ANCHAT";
    private static final String GOOGLE_SERVER_CLIENT_ID = "93431922925-oegckhe4hvqb600lnk03ho4ti1i9iuk4.apps.googleusercontent.com";
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 12;

    private EditText editTextEmailAddress;
    private EditText editTextPassword;

    private Button buttonLoginEmailPassword;
    private Button buttonLoginGoogle;

    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference().child(EntityUser.USER_ROOT);

    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_SERVER_CLIENT_ID).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        editTextEmailAddress = (EditText) findViewById(R.id.edit_text_email_address);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);

        buttonLoginEmailPassword = (Button) findViewById(R.id.button_login_email_password);
        buttonLoginGoogle = (Button) findViewById(R.id.button_login_google);

        buttonLoginEmailPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }

            private void login() {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                if (!isFormCompleted()) return;
                loginWithEmailAndPassword(
                        editTextEmailAddress.getText().toString(),
                        editTextPassword.getText().toString()
                );
            }
        });
        buttonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }

            private void login() {
                loginWithGoogle();
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

    private void loginWithEmailAndPassword(String email, String password) {
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

    private void loginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    private void googleAuthentication(final GoogleSignInAccount account) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("authenticating...");
        dialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser fu = firebaseAuth.getCurrentUser();
                            databaseReference.child(fu.getUid()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dialog.cancel();
                                            if (!dataSnapshot.exists()) {
                                                EntityUser user = new EntityUser(account.getDisplayName(),
                                                        null, 0d, 0d,
                                                        account.getPhotoUrl().toString());
                                                databaseReference.child(fu.getUid()).setValue(user);
                                                Intent intent = new Intent(activity, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        } else {
                            FirebaseException exception = (FirebaseException) task.getException();
                            Log.d(TAG, "Login with google failed. " + exception.getMessage());
                            Toast.makeText(activity, "Login failed, please try again", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GOOGLE_SIGN_IN_REQUEST_CODE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount account = null;
                if (result.isSuccess()) {
                    account = result.getSignInAccount();
                    googleAuthentication(account);
                } else {
                    Log.d(TAG, "Login with google failed.");
                }
                break;
        }
    }
}