package com.example.android.tidyup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class UserManagement extends AsyncTask<Void, Void, Void> {
    private static final String COLLECTIONPATH_USERS = "Users";
    private static final String TAG = "UserManagement";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_GroupID = "GroupID";
    private static final String KEY_Group = "Group";
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_USERS).document(fAuth.getUid());

    public static void addGroupField() {
        Map<String, String> data = new HashMap<>();
        data.put("Group", "");

        docRef.set(data, SetOptions.merge());
    }

    public static void updateUserGroup(String newGroupID, Context cntxt) {
        docRef.update("GroupID", newGroupID,
                      "Group", GroupManagement.getGroupName(newGroupID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(cntxt, "Group Added Successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }


    private static Map<String, Object> map = new HashMap<>();
    public static HashMap<String, Object> getUserDetails(){
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        map = document.getData();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return (HashMap<String, Object>) map;
    }

    protected static void setUsername(String username){
        docRef.update(KEY_USERNAME, username);
    }

    protected static void setUserEmail(String email){
        docRef.update(KEY_EMAIL, email);
    }
    protected static void setUserPassword(String password){
        docRef.update(KEY_PASSWORD, password);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
