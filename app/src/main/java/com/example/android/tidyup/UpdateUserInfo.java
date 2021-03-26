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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateUserInfo extends AppCompatActivity {
    private static final String TAG = "UpdateUserInfo";
    private static final String COLLECTIONPATH_USERS = "Users";
    private static final String KEY_PASSWORD = "Password";

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private final DocumentReference docRef  = fFirestore.collection(COLLECTIONPATH_USERS).document(fAuth.getUid());

    private EditText mNewUsername, mNewEmail, mNewPassword;
    private Button mReturn, mUpdateUsername, mUpdateEmail, mUpdatePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info_page);

        mNewUsername = findViewById(R.id.uaNewUsername);
        mNewEmail = findViewById(R.id.uaNewEmail);
        mNewPassword = findViewById(R.id.uaNewPassword);
        mReturn = findViewById(R.id.uaReturnButton);
        mUpdateUsername = findViewById(R.id.uaUpdateUsernameButton);
        mUpdateEmail = findViewById(R.id.uaUpdateEmailButton);
        mUpdatePassword = findViewById(R.id.uaUpdatePasswordButton);

    }

    public void updateUsername(View view){
        String newUsername = mNewUsername.getText().toString().trim();
        if (newUsername.equals("")){
            mNewUsername.setError("New Username is required");
            return;
        } else{
            UserManagement.setUsername(newUsername);
            Toast.makeText(UpdateUserInfo.this, "Username Updated", Toast.LENGTH_LONG ).show();
            mNewUsername.setText("");
        }
    }

    public void updateEmail(View view){
        String newEmail = mNewEmail.getText().toString().trim();

        if (newEmail.equals("")){
            mNewEmail.setError("New Email is required");
            return;
        } else {
            fAuth.fetchSignInMethodsForEmail(newEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    boolean noEmail = task.getResult().getSignInMethods().isEmpty();
                    if(noEmail){
                        Intent intent = new Intent(getApplication(), AuthenticationPassword.class);
                        intent.putExtra("EXTRA_EMAIL", newEmail);
                        intent.putExtra("EXTRA_SUBJECT", "Email");
                        startActivity(intent);
                    } else{
                        mNewEmail.setError("Error! Email already in use");
                        return;
                    }
                }
            });
        }
    }


    public void updatePassword(View view){ ;
        String newPassword = mNewPassword.getText().toString().trim();

        if (newPassword.equals("")){
            mNewPassword.setError("New Password is required");
            return;
        } else if(newPassword.length() < 6){
            mNewPassword.setError("Password must be greater than or equal to 6 characters");
            return;
        }else{
            Intent intent = new Intent(getApplication(), AuthenticationPassword.class);
            intent.putExtra("EXTRA_PASSWORD", newPassword);
            intent.putExtra("EXTRA_SUBJECT", "Password");
            startActivity(intent);
        }
    }

    public void goToAccountPage(View view){
        Intent intent = new Intent(this, Account.class);
        finish();
        startActivity(intent);
    }

}
