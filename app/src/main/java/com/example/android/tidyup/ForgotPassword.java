package com.example.android.tidyup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText mEmail;
    private Button mResetPassword;
    private ProgressBar mProgressBar;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEmail = findViewById(R.id.fpEmail);
        mResetPassword = findViewById(R.id.fpResetPasswordButton);
        mProgressBar = findViewById(R.id.fpProgressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();
        
        mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = mEmail.getText().toString().trim();
        if (email.isEmpty()){
            mEmail.setError("Email Required!");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please provide valid email!");
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your Password!", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(ForgotPassword.this, "Error! Reset Link is Not Sent " + task.getException(), Toast.LENGTH_LONG).show();
                    mEmail.setError("Please provide valid email!");
                    mProgressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                mProgressBar.setVisibility(View.INVISIBLE);
                finish();
            }
        });
    }


}