package com.example.android.tidyup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationPassword extends AppCompatActivity {
    private static final String TAG = "UpdateUserInfo";
    private static final String COLLECTIONPATH_USERS = "Users";
    private static final String KEY_PASSWORD = "Password";
    private static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    private static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";
    private static final String EXTRA_SUBJECT = "EXTRA_SUBJECT";

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();

    private EditText mAuthPassword;
    private Button mSubmit;

    private String newEmail = null;
    private String subject = null;
    private String newPassword = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_password);
        mAuthPassword = findViewById(R.id.apPasswordAuthentication);
        mSubmit = findViewById(R.id.apPasswordSubmit);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newEmail = null;
            subject = null;
            newPassword = null;
        } else {
            subject = extras.getString(EXTRA_SUBJECT);
            if(subject.equals("Email")){
                newEmail = extras.getString(EXTRA_EMAIL);
                mAuthPassword.setHint("Password");
            } else {
                newPassword = extras.getString(EXTRA_PASSWORD);
                mAuthPassword.setHint("Old Password");
            }
        }


    }

    public void submit(View view) {
        String authPassword = mAuthPassword.getText().toString().trim();
        if(authPassword.equals("")){
            mAuthPassword.setError("Password Required");
            return;
        } else{
            String userPassword = (String) UserManagement.getUserDetails().get(KEY_PASSWORD);
            if(authPassword.equals(userPassword)){
                //update user email
                if (subject.equals("Email")) {
                    if (newEmail != null) {
                        fAuth.getCurrentUser().updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            UserManagement.setUserEmail(newEmail);
                                            Toast.makeText(AuthenticationPassword.this, "Email Updated", Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "User email updated");
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), UpdateUserInfo.class));
                                        } else {
                                            Toast.makeText(AuthenticationPassword.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "Error! " + task.getException().getMessage());
                                        }
                                    }
                                });
                    } else {
                        Log.d(TAG, "Error! new email is null");
                    }
                } else if (subject.equals("Password")){
                    if(newPassword != null) {
                        fAuth.getCurrentUser().updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            UserManagement.setUserPassword(newPassword);
                                            Toast.makeText(AuthenticationPassword.this, "Password Updated", Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "User password updated");
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), UpdateUserInfo.class));
                                        } else {
                                            Toast.makeText(AuthenticationPassword.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "Error! " + task.getException().getMessage());
                                        }
                                    }
                                });
                    } else {
                        Log.d(TAG, "Error! new password is null");
                    }
                }
            } else {
                mAuthPassword.setError("Password Does not Match");
                return;
            }
        }
    }

    public void goToUpdateUserInfoPage (View view){
        Intent intent = new Intent(this, UpdateUserInfo.class);
        finish();
        startActivity(intent);
    }

}
