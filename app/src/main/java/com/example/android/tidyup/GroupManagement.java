package com.example.android.tidyup;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;

public class GroupManagement extends AsyncTask<Void, Void, Void> {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "GroupManagementFirebase";
    private static final String GROUP_CODE_DB = "GroupCodeDB";
    private static final String GROUP_CODE_DB_DOCUMENT = "test";
    private static final String GROUP_DB = "Groupsz";
    private static final String GROUP_DB_DOCUMENT = "Groups";
    private static Map<String, List<String>> gcDB;
    private static Map<String, List<String>> grpDB;

    protected static String getCode(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static void addUserToGroup(String groupID, String userID){
        if(grpDB.containsKey(groupID)){
            ArrayList<String> lst = (ArrayList<String>) gcDB.get(groupID);
            if (lst.contains(userID))
                return;
            lst.add(userID);
            gcDB.put(groupID, lst);
        } else
            grpDB.put(groupID, Collections.singletonList(userID));

        Group grp = new Group();
        db.collection(GROUP_DB).document(GROUP_CODE_DB).set(grp);
    }

    public static void addGroupCodes(String groupID, String groupCode){
        if(gcDB.containsKey(groupID)){
            ArrayList<String> lst = (ArrayList<String>) gcDB.get(groupID);
            if (lst.contains(groupCode))
                return;
            lst.add(groupCode);
            gcDB.put(groupID, lst);
        } else {
            gcDB.put(groupID, Collections.singletonList(groupCode));
        }
        Group_Code gc = new Group_Code(gcDB);
        db.collection(GROUP_CODE_DB).document(GROUP_CODE_DB_DOCUMENT).set(gc);
    }

    public static void readGroupCodeDB(){
        DocumentReference docRef = db.collection(GROUP_CODE_DB).document(GROUP_CODE_DB_DOCUMENT);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Group_Code gcc = documentSnapshot.toObject(Group_Code.class);
                gcDB = gcc.grpCodeMap;
                Log.d(TAG, gcc.grpCodeMap.toString());
                Log.d(TAG, gcDB.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error reading document", e);
            }
        });
    }

    public static void readGroupDB(){
        DocumentReference docRef = db.collection(GROUP_DB).document(GROUP_DB_DOCUMENT);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Group gcc = documentSnapshot.toObject(Group.class);
                if(gcc == null)
                    return;
                grpDB = gcc.grpMap;
                Log.d(TAG, gcc.grpMap.toString());
                Log.d(TAG, grpDB.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error reading document", e);
            }
        });
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        readGroupCodeDB();
        readGroupDB();
        super.onPreExecute();
    }

    private static class Group_Code{
        public Map<String, List<String>> grpCodeMap = new HashMap<>();

        Group_Code(){}
        Group_Code(Map<String, List<String>> customMap){
            this.grpCodeMap = new HashMap<>();
            this.grpCodeMap = customMap;
        }

        public Map<String, List<String>> getGrpCodeMap(){
            return this.grpCodeMap;
        }
    }
    private static class Group{
        // Group ID: ==> [user list]
        public Map<String, List<String>> grpMap = new HashMap<>();

        Group(){}
        Group(Map<String, List<String>> customMap){
            this.grpMap = new HashMap<>();
            this.grpMap = customMap;
        }

        public Map<String, List<String>> getGrpCodeMap(){
            return this.grpMap;
        }
    }
}