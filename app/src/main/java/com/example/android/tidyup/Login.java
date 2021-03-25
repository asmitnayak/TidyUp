package com.example.android.tidyup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity {

    private String Role = "";
    private EditText mEmail, mPassword;
    private Button mLoginBtn;
    private FirebaseAuth fauth;
    public static HashMap<String, Object> userMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//    }
//
//    // redirect user to task page if user credentials are correct
//    public void login(View view) {
        mEmail = findViewById(R.id.emailInput);
        mPassword = findViewById(R.id.cPasswordInput);
        mLoginBtn = findViewById(R.id.loginButton);
        fauth = FirebaseAuth.getInstance();

        GroupManagement gm = new GroupManagement();
        gm.execute();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password must be greater than or equal to 6 characters");
                    return;
                }


                //check credentials of user through firebase
                fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                            // check if admin move to Account.java

                            userMap = UserManagement.getUserDetails();
                            if(userMap.get("Group") != "")
                                startActivity(new Intent(getApplicationContext(), TaskPage.class));
                            else
                                startActivity(new Intent(getApplicationContext(), Account.class));
                        } else{
                            Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public void login_success (){

    }

    // Redirect the user to Create Account screen
    public void goToCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }
    //Redirect the user to Task Page
    public void goToTaskPage() {
        Intent intent = new Intent(this, TaskPage.class);
        startActivity(intent);
    }

    public void goToAccountPage(View view) {
        Intent intent = new Intent(this, Account.class);
        startActivity(intent);
    }
}