package com.example.android.tidyup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class Login extends AppCompatActivity {

    private static String TAG_LOGIN = "Login";

    private String Role = "";
    private EditText mEmail, mPassword;
    private Button mLoginBtn;
    private FirebaseAuth fauth;
    private static HashMap<String, Object> userMap = new HashMap<>();
    private ProgressBar mProgressBar;
    private TextView mForgotPassView;

    public static CountDownLatch done = new CountDownLatch(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseAuth.getInstance().signOut();
        try {
            mEmail = findViewById(R.id.emailInput);
            mPassword = findViewById(R.id.cPasswordInput);
            mLoginBtn = findViewById(R.id.loginButton);

            fauth = FirebaseAuth.getInstance();
            mForgotPassView = findViewById(R.id.forgotPassView);
            mProgressBar = findViewById(R.id.lProgressBar);
            mProgressBar.setVisibility(View.INVISIBLE);
            GroupManagement gm = new GroupManagement();
            gm.execute();
        }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error! " + e.getMessage(), Toast.LENGTH_LONG ).show();
            }

    }

    public void login(View view) {

        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            mEmail.setError("Email is required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Invalid Email!");
            return;
        }

        if(!isValid(email)){
            mEmail.setError("Please enter a valid Email!");
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


        mProgressBar.setVisibility(View.VISIBLE);
        //check credentials of user through firebase
        fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                    // check if admin move to Account.java

                    UserManagement um = new UserManagement();
                    um.execute();
                    RewardsManagement rm = new RewardsManagement();
                    rm.execute();
                    PenaltyManagement pm = new PenaltyManagement();
                    pm.execute();
                    userMap = UserManagement.getUserDetails();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    if(userMap.get("Group") != "")
                        // change back from TaskPage to Account
                       // startActivity(new Intent(getApplicationContext(), TaskPage.class));
                        startActivity(new Intent(getApplicationContext(), Account.class));
                    else
                       // startActivity(new Intent(getApplicationContext(), TaskPage.class));
                        startActivity(new Intent(getApplicationContext(), Account.class));
                } else{
                    mPassword.setError("Invalid Username or Password");
                    mEmail.setError("Invalid Username or Password");
                    Toast.makeText(Login.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }


    public void forgotPassword(View view){
        startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
    }


    protected void sendEmail() {
        String email = mEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email is required to send password");
            return;
        }
        //String password =
        //fauth.fetchSignInMethodsForEmail(email).
        String[] TO = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tidy Up Password");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Here is your password: ");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i(TAG_LOGIN, "Finished sending email");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Login.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    // Redirect the user to Create Account screen
    public void goToCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccount.class);
        finish();
        startActivity(intent);
    }

}