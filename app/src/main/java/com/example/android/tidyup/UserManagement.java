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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class UserManagement extends AsyncTask<Void, Void, Void> {
    private static final String COLLECTIONPATH_USERS = "Users";
    private static final String TAG = "UserManagement";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_ROLE= "Role";
    private static final String KEY_GroupID = "GroupID";
    private static final String KEY_Group = "Group";
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_USERS).document(fAuth.getUid());
    private static final CollectionReference collRef = fFirestore.collection(COLLECTIONPATH_USERS);

    public static void addStringField(String fieldName) {
        Map<String, String> data = new HashMap<>();
        data.put(fieldName, "");

        docRef.set(data, SetOptions.merge());
    }

    public static void addIntField(String fieldName) {
        Map<String, String> data = new HashMap<>();
        data.put(fieldName, "0");

        docRef.set(data, SetOptions.merge());
    }

    public static void deleteField(String fieldName) {
        Map<String, String> data = new HashMap<>();
        data.put(fieldName, null);

        docRef.update(fieldName, FieldValue.delete());
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

    private static Map<String, String> otherUserMap = new HashMap<>();
    public static String getUserNameFromUID (String uid){
        //        DocumentReference docRefUID = fFirestore.collection(COLLECTIONPATH_USERS).document(uid);
//        docRefUID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        otherUserMap = document.getData();
//
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });

        return otherUserMap.get(uid);
    }

    protected static void resetAllUserPoints(String grpID){
        ArrayList<String> membersList = GroupManagement.getGroupMemberList(grpID);
        if(membersList == null){
            Log.d(TAG, "Error getting Members List");
            return;
        }
        for (String uid: membersList){
            DocumentReference docRefUID = fFirestore.collection(COLLECTIONPATH_USERS).document(uid);
            Map<String, String> data = new HashMap<>();
            data.put("UserPoints", "0");
            docRefUID.set(data, SetOptions.merge());
        }
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

    protected static void setUserRole(String role){
        docRef.update(KEY_ROLE, role);
    }

    protected static void setUserGroupID(String groupID){
        docRef.update(KEY_GroupID, groupID);
    }

    protected static void setUserGroup(String groupName){
        docRef.update(KEY_Group, groupName);
    }


    @Override
    protected Void doInBackground(Void... params) {
        CollectionReference colRefUID = fFirestore.collection(COLLECTIONPATH_USERS);
        colRefUID.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                        if(document.getData().get("GroupID").equals(map.get("GroupID")))
                            otherUserMap.put(document.getId(), (String) document.getData().get("Username"));
                    }
                    Log.d(TAG, "asdjlk  "+ otherUserMap.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return null;
    }

    @Override
    protected void onPreExecute() {
        addIntField("UserPoints");
        deleteField("Password");
        super.onPreExecute();
    }


}
