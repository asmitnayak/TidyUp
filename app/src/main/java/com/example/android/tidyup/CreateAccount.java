package com.example.android.tidyup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {


    protected static final ArrayList<String> CREDENTIALS = new ArrayList<>();

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfView;
    private EditText mAddressView;
    private EditText mPhoneNumberView;
    private Spinner mRoleSpinner;
    private Button mCreateButton;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_page);
        Spinner spinner = findViewById(R.id.role);
        ArrayList<String> roleList = new ArrayList<>();
        roleList.add("Admin");
        roleList.add("User");
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roleList);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(listAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String tutorial = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

        mNameView = findViewById(R.id.cName);
        mEmailView = findViewById(R.id.cEmailInput);
        mPasswordView = findViewById(R.id.cPasswordInput);
        mPasswordConfView = findViewById(R.id.passwordInputConfirm);
        mAddressView = findViewById(R.id.address);
        mPhoneNumberView = findViewById(R.id.phoneNumber);
        mRoleSpinner = spinner;
        mCreateButton = findViewById(R.id.registerLink);
        fAuth = FirebaseAuth.getInstance();
        fFirestore = FirebaseFirestore.getInstance();

        // if user account is already there with same information just log them in
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Account.class));
            finish();
        }
        Button mRegisterButton = (Button) findViewById(R.id.registerLink);
        registerUser(mRegisterButton);
    }

    private void registerUser(Button mRegisterButton) {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameView.getText().toString().trim();
                String email = mEmailView.getText().toString().trim();
                String password = "";
                String address = mAddressView.getText().toString().trim();
                String phoneNumber = mPhoneNumberView.getText().toString().trim();
                String role = mRoleSpinner.getSelectedItem().toString();
                if(mPasswordView.getText().toString().trim().equals(mPasswordConfView.getText().toString().trim())){
                    password = mPasswordView.getText().toString().trim();
                } else{
                    mPasswordConfView.setError("Password and Confirm password do no match");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    mEmailView.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPasswordView.setError("Password is required");
                    return;
                }
                if(password.length() < 6){
                    mPasswordView.setError("Password must be greater than or equal to 6 characters");
                    return;
                }
                String finalPassword = password;
                //create user and store in Firestore
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("Name", name);
                            userMap.put("Email", email);
                            userMap.put("Password", finalPassword);
                            userMap.put("Address", address);
                            userMap.put("Phone Number", phoneNumber);
                            userMap.put("Role", role);
                            userMap.put("Group", "");

                            // Store user information into Firestore
                            fFirestore.collection("Users").document(fAuth.getUid()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CreateAccount.this, "Account created", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), Account.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateAccount.this, "Error! " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(CreateAccount.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }



    public void goToAccountPage(){
        Intent intent = new Intent(this, Account.class);
        startActivity(intent);
    }

}
