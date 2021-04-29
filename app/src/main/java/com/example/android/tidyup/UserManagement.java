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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MetadataChanges;
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
import androidx.annotation.Nullable;

import static com.example.android.tidyup.Login.done;

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
    private static  DocumentReference docRef;
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
    public static void updateUserGroup(String newGroupID, String role, Context cntxt) {
        docRef.update("GroupID", newGroupID,
                      "Group", GroupManagement.getGroupName(newGroupID), "Role", role)
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
//        docRef.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        map = document.getData();
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
//        try {
//            done.await(); //it will wait till the response is received from firebase.
//        } catch(InterruptedException e) {
//            e.printStackTrace();
//        }
        return (HashMap<String, Object>) map;
    }


    //private static Map<String, String> otherUserMap = new HashMap<>();
    private static Map<String, List<String>> otherUserMap = new HashMap<>();
    public static String getUserNameFromUID (String uid){
        return ((List<String>)otherUserMap.get(uid)).get(0);
    }

    public static String getEmailFromUID (String uid){
        return ((List<String>)otherUserMap.get(uid)).get(2);
    }

    /*
    public static String getUserPointsFromUID (String uid){
        return ((List<String>)otherUserMap.get(uid)).get(1);
    } */

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
    protected static void setToken(String token) {docRef.update("Token", token);}
    protected static String getUserTokenFromUID(String uid){return otherUserMap.get(uid).get(3);}
    protected static void setUserGroupID(String groupID){
        docRef.update(KEY_GroupID, groupID);
    }

    protected static void setUserGroup(String groupName){
        docRef.update(KEY_Group, groupName);
    }


    private List userInfo = new ArrayList<String>();
    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        docRef = fFirestore.collection(COLLECTIONPATH_USERS).document(fAuth.getCurrentUser().getUid());
        map = new HashMap<>();
        CollectionReference colRefUID = fFirestore.collection(COLLECTIONPATH_USERS);
        colRefUID.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        list.add(document.getId());
                        if(document.getData().get("GroupID").equals(map.get("GroupID"))) {
                            userInfo = new ArrayList<String>();
                            userInfo.add((String) document.getData().get("Username"));
                            userInfo.add((String) document.getData().get("UserPoints"));
                            userInfo.add((String) document.getData().get("Email"));
                            userInfo.add((String) document.getData().get("Token"));
                            otherUserMap.put(document.getId(), userInfo);
                            //otherUserMap.put(document.getId(), (String) document.getData().get("Username"));
                        }
                    }
                    Log.d(TAG, "asdjlk  "+ otherUserMap.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", error);
                }
            }
        });
        colRefUID.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                        if(document.getData().get("GroupID").equals(map.get("GroupID"))) {
                            userInfo = new ArrayList<String>();
                            userInfo.add((String) document.getData().get("Username"));
                            userInfo.add((String) document.getData().get("UserPoints"));
                            userInfo.add((String) document.getData().get("Email"));
                            userInfo.add((String) document.getData().get("Token"));
                            otherUserMap.put(document.getId(), userInfo);
                            //otherUserMap.put(document.getId(), (String) document.getData().get("Username"));
                        }
                    }
                    done.countDown();
                    Log.d(TAG, "asdjlk  "+ otherUserMap.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        addStringField("Token");
        deleteField("Password");

        docRef.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String grpID = "";
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        map = document.getData();
                        grpID = (String) map.get("GroupID");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                    if (grpID != null && !grpID.isEmpty()) {
                        TaskManagment tm = new TaskManagment();
                        tm.execute();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        docRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.w(TAG, "listen:error", error);
                    return;
                }
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    map = document.getData();
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        });

        addStringField("Token");
        super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
