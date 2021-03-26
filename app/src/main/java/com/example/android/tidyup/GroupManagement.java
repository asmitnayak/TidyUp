package com.example.android.tidyup;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

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
    private static final String GROUP_DB = "Groups";
    private static final String GROUP_DB_DOCUMENT = "Groups";
    private static Map<String, List<String>> gcDB;
    private static Map<String, List<String>> grpDB;
    private static boolean grpRand;

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

    public static String getGroup(String code){
        if(gcDB == null)
            return null;
        for (Map.Entry<String, List<String>> entry : gcDB.entrySet()) {
            if (entry.getValue().contains(code)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void addUserToGroup(String groupID, String userID, String code){
        if(grpDB.containsKey(groupID)){
            ArrayList<String> lst = (ArrayList<String>) grpDB.get(groupID);
            if(getGroupTask(groupID) == null)
                lst.add(0, "false");
            if (lst.contains(userID))
                return;
            lst.add(userID);
            grpDB.put(groupID, lst);
        } else
            grpDB.put(groupID, Arrays.asList("false", userID));

        Group grp = new Group(grpDB);
        db.collection(GROUP_DB).document(GROUP_DB_DOCUMENT).set(grp);
        if(code != null)
            addGroupCodes(groupID, code);
    }

    public static ArrayList<String> getGroupMemberList(String groupID){
        if(grpDB == null){
            readGroupDB();
            if(grpDB == null)
                return null;
        }
        if(!grpDB.containsKey(groupID))
            return null;
        else
            return (ArrayList<String>) grpDB.get(groupID);

    }

    public static Boolean getGroupTask(String groupID){
        if(grpDB.get(groupID).get(0).equalsIgnoreCase("true"))
            return true;
        else if(grpDB.get(groupID).get(0).equalsIgnoreCase("false"))
            return false;
        return null;
    }

    public static void setGroupTask(String groupID, boolean value){
        ArrayList<String> lst = (ArrayList<String>) grpDB.get(groupID);
        if(getGroupTask(groupID) == null)
            lst.add(0, "false");
        else
            lst.set(0, Boolean.toString(value));
    }

    public static void removeUserFromGroup(String groupID, String userID){
        if(grpDB.containsKey(groupID)){
            ArrayList<String> userList = (ArrayList<String>) gcDB.get(groupID);
            if(userList.contains(userID))
                userList.remove(userID);
            gcDB.put(groupID,userList);
        }
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

        public Map<String, List<String>> getGrpMap(){
            return this.grpMap;
        }

    }
}