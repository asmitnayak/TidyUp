package com.example.android.tidyup;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

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

    public static void setUpEmulator(){
        // [START fs_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        //FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
        db.useEmulator("10.0.2.2", 8080);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        // [END fs_emulator_connect]
    }
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

    public static String getGroupIDFromUserID(String uID){
        if(grpDB == null)
            return null;
        for (Map.Entry<String, List<String>> entry : grpDB.entrySet()) {
            if (entry.getValue().contains(uID)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // TODO: Implement the int return type to avoid adding members to different grp, or same ones.
    public static int addUserToGroup(String groupID, String userID, String code, String groupName){

        if(getGroupIDFromUserID(userID) != null) // already in a grp
            return -1;
        if(grpDB.containsKey(groupID)){
            ArrayList<String> lst = (ArrayList<String>) grpDB.get(groupID);
            if(getGroupTask(groupID) == null)
                lst.add(0, "false");
            if(getGroupName(groupID) == null){
                if(groupName != null)
                    lst.add(1, "GroupName:"+groupName);
            }
            if(lst.contains(userID))
                return 0;
            lst.add(userID);
            grpDB.put(groupID, lst);
        } else
            grpDB.put(groupID, Arrays.asList("false", "GroupName:"+groupName, userID));

        Group grp = new Group(grpDB);
        db.collection(GROUP_DB).document(GROUP_DB_DOCUMENT).set(grp);
        if(code != null)
            addGroupCodes(groupID, code);
        return 1;
    }

    public static ArrayList<String> getGroupMemberList(String groupID){
        if(grpDB == null){
            readGroupDB();
            if(grpDB == null)
                return null;
        }
        if(!grpDB.containsKey(groupID)) {
            return null;
        }
        else {
            ArrayList<String> returnList = new ArrayList<>(grpDB.get(groupID));
            returnList.remove(0);
            returnList.remove(1);
            return returnList;
        }
    }

    public static String getGroupName(String groupID){
        String name = grpDB.get(groupID).get(1);
        if(name.startsWith("GroupName:")){
            String[] arrStr = name.split(":");
            return arrStr[1];
        } else
            return null;
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

            // TODO if length of group is 2 then delete group
            if(userList.size() == 2) {
                grpDB.remove(groupID);
                AutomateRewardsService.automateRewardsService.stopSelf();
            }
            else
                grpDB.put(groupID,userList);
            Group gc = new Group(grpDB);
            db.collection(GROUP_DB).document(GROUP_DB_DOCUMENT).set(gc);

        }
    }

    public static void addGroupCodes(String groupID, String groupCode){
        if(gcDB.containsKey(groupID)){
            ArrayList<String> lst = new ArrayList<>(gcDB.get(groupID));
            if (lst.contains(groupCode))
                return;
            lst.add(groupCode);
            gcDB.put(groupID, lst);
        } else {
            gcDB.put(groupID, Arrays.asList(groupCode));
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

    public static String getGroupID(String groupName){

        Date dt = new Date();
        groupName += dt.getTime();
        byte[] cipherText = null;
        try {

            byte[] plaintext = groupName.getBytes();
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(256);
            SecretKey key = keygen.generateKey();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(plaintext);
            byte[] iv = cipher.getIV();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(cipherText, Base64.DEFAULT).replaceAll("\\n", "");
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