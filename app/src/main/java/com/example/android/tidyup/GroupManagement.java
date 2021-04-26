package com.example.android.tidyup;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MetadataChanges;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GroupManagement extends AsyncTask<Void, Void, Void> {
    private static final String COLLECTIONPATH_USERS = "Users";
    private static FirebaseFirestore db;
    private static final String TAG = "GroupManagementFirebase";
    private static final String GROUP_CODE_DB = "GroupCodeDB";
    private static final String GROUP_CODE_DB_DOCUMENT = "Codes";
    private static final String GROUP_DB = "Groups";
    private static final String GROUP_DB_DOCUMENT = "Groups";
    private static Map<String, List<String>> gcDB;
    private static Map<String, List<String>> grpDB;
    private static boolean grpRand;
    private static String currGroup;
    private static FirebaseAuth fAuth;
    private static Calendar cal;

    // Testing only
    public GroupManagement(FirebaseFirestore firestore, FirebaseAuth fireAuth, String groupID, String groupName, String userID){
        db = firestore;
        cal = Calendar.getInstance();
        fAuth = fireAuth;
        grpDB = new HashMap<>();
        gcDB = new HashMap<>();

        grpDB.put(groupID, new ArrayList<>(Arrays.asList("false", "GroupName:"+groupName, "WeekofYear:" + String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)), userID)));
    }

    public GroupManagement(){
        db = FirebaseFirestore.getInstance();
        cal = Calendar.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    public boolean isAvailable(){
        return false;
    }

    public void setUpEmulator(){
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

    public static String getCurrentGroup(){
        String groupID = "";
        if(fAuth.getCurrentUser() != null)
            groupID = GroupManagement.getGroupIDFromUserID(fAuth.getCurrentUser().getUid());
        return groupID;
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

        if(grpDB == null) {
            grpDB = new HashMap<>();
        }

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
            if(getWeekOfYear(groupID) == null){
                lst.add(2, "WeekofYear:" + String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)));
            }
            if(lst.contains(userID))
                return 0;
            lst.add(userID);
            grpDB.put(groupID, lst);
        } else
            grpDB.put(groupID, new ArrayList<>(Arrays.asList("false", "GroupName:"+groupName, "WeekofYear:" + String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)), userID)));

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
            returnList.remove(0);
            returnList.remove(0);
            return returnList;
        }
    }

    public static String getGroupName(String groupID){
        String name = "";
        try {
            name = grpDB.get(groupID).get(1);
        } catch (Exception e){
            return null;
        }
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

    public static String getWeekOfYear(String groupID){
        String week = grpDB.get(groupID).get(2);
        if(week.startsWith("WeekofYear:")){
            String[] arrStr = week.split(":");
            return arrStr[1];
        } else
            return null;
    }

    public static void setGroupTask(String groupID, boolean value){
        if(getGroupTask(groupID) == null)
            grpDB.get(groupID).add(0, "false");
        else
            grpDB.get(groupID).set(0, Boolean.toString(value));

        Group grp = new Group(grpDB);
        db.collection(GROUP_DB).document(GROUP_DB_DOCUMENT).set(grp);
    }

    public static void setWeekofYear(String groupID,  String currweek){
        ArrayList<String> lst = (ArrayList<String>) grpDB.get(groupID);
        if(getGroupTask(groupID) == null) {
            lst.add(2, "WeekofYear:"+String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)));
        }
        else
            lst.set(2, "WeekofYear:"+currweek);
        Group grp = new Group(grpDB);
        db.collection(GROUP_DB).document(GROUP_DB_DOCUMENT).set(grp);
    }

    public static void removeUserFromGroup(String groupID, String userID){
        if(grpDB.containsKey(groupID)){
            ArrayList<String> userList = (ArrayList<String>) grpDB.get(groupID);
            userList.remove(userID);
            if(userList.size() == 3) {
                grpDB.remove(groupID);
            }
            else
                grpDB.put(groupID,userList);
            Group gc = new Group(grpDB);
            db.collection(GROUP_DB).document(GROUP_DB_DOCUMENT).set(gc);

            DocumentReference docRef  = db.collection(COLLECTIONPATH_USERS).document(userID);
            docRef.update("Group", "",
                    "GroupID", "");
        }
    }
    public static String getGroupCode(String groupID){
        readGroupCodeDB();
        List<String> groupCodeList = new ArrayList<String>();
        String groupCode = "";
        if(gcDB != null){
            if(groupID != null) {
                if (gcDB.containsKey(groupID)) {
                    groupCodeList = gcDB.get(groupID);
                    groupCode = groupCodeList.get(0);
                }
            }
        }
        return groupCode;
    }

    public static void addGroupCodes(String groupID, String groupCode){

        if(gcDB == null) {
            gcDB = new HashMap<>();
        }

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

    private static DocumentReference docRef_gc = null;
    private static DocumentReference docRef_g = null;

    public static void readGroupCodeDB(){
        docRef_gc = db.collection(GROUP_CODE_DB).document(GROUP_CODE_DB_DOCUMENT);
        docRef_gc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Group_Code gcc = documentSnapshot.toObject(Group_Code.class);
                if(gcc == null) {
                    gcDB = new HashMap<>();
                    return;
                }
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

        docRef_gc.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }
                Group_Code gcc = documentSnapshot.toObject(Group_Code.class);
                if(gcc == null) {
                    gcDB = new HashMap<>();
                    return;
                }
                gcDB = gcc.grpCodeMap;
                Log.d(TAG, gcc.grpCodeMap.toString());
                Log.d(TAG, gcDB.toString());
            }
        });
    }

    public static void readGroupDB(){
        docRef_g = db.collection(GROUP_DB).document(GROUP_DB_DOCUMENT);
        docRef_g.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Group gcc = documentSnapshot.toObject(Group.class);
                if(gcc == null) {
                    grpDB = new HashMap<>();
                    return;
                }
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

        docRef_g.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }

                Group gcc = documentSnapshot.toObject(Group.class);
                if(gcc == null) {
                    grpDB = new HashMap<>();
                    return;
                }
                grpDB = gcc.grpMap;
                Log.d(TAG, gcc.grpMap.toString());
                Log.d(TAG, grpDB.toString());
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