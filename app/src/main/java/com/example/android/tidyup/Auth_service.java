package com.example.android.tidyup;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Auth_service {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fFirestore;
    private FirebaseUser currentUser;

    protected void Auth_service() {


    }

    public void authenticate_login(Login login,String email, String password) {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(login, "Login Successful", Toast.LENGTH_LONG).show();
                    currentUser = fAuth.getCurrentUser();
                    login.startActivity(new Intent(login.getApplicationContext(), Account.class));
                } else {
                    Log.d("authentication", "Toast failure");
                    Toast.makeText(login, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void create_user(CreateAccount createAccount, String email,String password, String name,String address,String phoneNumber, String role) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("Name", name);
                    userMap.put("Email", email);
                    userMap.put("Password", password);
                    userMap.put("Address", address);
                    userMap.put("Phone Number", phoneNumber);
                    userMap.put("Role", role);

                    // Store user information into Firestore
                    fFirestore.collection("Users").add(userMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(createAccount, "Account created", Toast.LENGTH_LONG).show();
                                currentUser = fAuth.getCurrentUser();
                                createAccount.startActivity(new Intent(createAccount.getApplicationContext(), Account.class));
                            } else {
                                Toast.makeText(createAccount, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(createAccount, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected FirebaseUser get_curr_UID(){
        return currentUser;
    }

}
