package com.example.android.tidyup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Account extends AppCompatActivity {
    private static final String COLLECTIONPATH_GROUP = "Users";
    private static final String TAG = "Account";
    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_GroupID = "GroupID";
    private static final String KEY_Group = "Group";



    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private final DocumentReference docRef  = fFirestore.collection(COLLECTIONPATH_GROUP).document(fAuth.getUid());

    private TextView mName, mEmail, mPassword, mGroup;
    private EditText mNewUserEmail;
    private Button mAddMembers;
    private String addedUserID;
    private String grpID;
    private String grpName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        mName = findViewById(R.id.acName);
        mEmail = findViewById(R.id.acEmail);
        mPassword = findViewById(R.id.acPassword);

        mGroup = findViewById(R.id.acGroup);
        mNewUserEmail = findViewById(R.id.acNewUserEmail);
        mAddMembers = findViewById(R.id.acAddMembersButton);

        // load and display user info on Account Page
        loadUserData();

        // add members button functionality
        mAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserEmail = mNewUserEmail.getText().toString().trim();
                if (TextUtils.isEmpty(newUserEmail)){
                    mNewUserEmail.setError("New User Email is required");
                    return;
                }
                fFirestore.collection(COLLECTIONPATH_GROUP)
                        .whereEqualTo(KEY_EMAIL, newUserEmail)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                             if (task.getResult().getDocuments().size() != 1){
                                 Toast.makeText(Account.this, "Error! more than one user found with email " + newUserEmail, Toast.LENGTH_LONG).show();
                                 Log.d(TAG, "Error! more than one user found with email " + newUserEmail);
                                 return;
                             }

                             addedUserID = task.getResult().getDocuments().get(0).getId();
                             DocumentReference addedUserDoc = fFirestore.collection(COLLECTIONPATH_GROUP).document(addedUserID);
                             GroupManagement.addUserToGroup(grpID, addedUserID, null);
                             addedUserDoc.update(KEY_GroupID, grpID);
                             addedUserDoc.update(KEY_Group, grpName);

                        } else {
                            Toast.makeText(Account.this, "Error! " + newUserEmail + "Does not have a Tidy Up " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Error! " + task.getException().getMessage() + newUserEmail);
                        }
                    }
                });
            }
        });

    }

    private void loadUserData() {

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String name = documentSnapshot.getString(KEY_NAME);
                    String email = documentSnapshot.getString(KEY_EMAIL);
                    String password = documentSnapshot.getString(KEY_PASSWORD);
                    grpID = documentSnapshot.getString(KEY_GroupID);
                    grpName = documentSnapshot.getString(KEY_Group);
                    mName.setText("Name: " + name);
                    mEmail.setText("Email: "+ email);
                    mPassword.setText("Password: " + password);
                    if (!grpName.equals("")){
                        mGroup.setText("Group: " + grpName);
                    } else {
                        mGroup.setText("Group: No Group Yet");
                    }
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

    public void updateUserInfo() {

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
    public void gotToGroupSettings(View view){
        Intent intent = new Intent(this, GroupSettings.class);
        startActivity(intent);
    }

    public void addMembers (){

    }
}
