package com.example.android.tidyup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;

public class Account extends AppCompatActivity {
    private static final String TAG = "Account";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_PHONENUMBER = "Phone Number";



    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private final DocumentReference docRef  = fFirestore.collection("Users").document(fAuth.getUid());

    private TextView mName, mEmail, mPassword, mAddress, mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        mName = findViewById(R.id.acName);
        mEmail = findViewById(R.id.acEmail);
        mPassword = findViewById(R.id.acPassword);
        mAddress = findViewById(R.id.acAddress);
        mPhoneNumber = findViewById(R.id.acPhoneNumber);

        // Display user info on Account Page
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name = documentSnapshot.getString(KEY_NAME);
                    String email = documentSnapshot.getString(KEY_EMAIL);
                    String password = documentSnapshot.getString(KEY_PASSWORD);
                    String address = documentSnapshot.getString(KEY_ADDRESS);
                    String phonenumber = documentSnapshot.getString(KEY_PHONENUMBER);
                    mName.setText("Name: " + name);
                    mEmail.setText("Email: "+ email);
                    mPassword.setText("Password: " + password);
                    mAddress.setText("Address: " + address);
                    mPhoneNumber.setText("Phone Number: " + phonenumber);
                }else {
                    Toast.makeText(Account.this, "Document does not Exist", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Account.this, "Error! "+ e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, e.toString());
            }
        });

    }


    public void loguot(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void goToCreateGroupPage(View view){
        Intent intent = new Intent(this, CreateGroup.class);
        finish();
        startActivity(intent);
    }
    public void goToJoinGroupPage(View view){
        Intent intent = new Intent(this, JoinGroup.class);
        startActivity(intent);
    }
    public void gotToAddMemberPage(View view){
        Intent intent = new Intent(this, AddMembers.class);
        startActivity(intent);
    }
}
