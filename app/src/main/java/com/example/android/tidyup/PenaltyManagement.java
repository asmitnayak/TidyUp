package com.example.android.tidyup;

import android.os.AsyncTask;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PenaltyManagement extends AsyncTask<Void, Void, Void> {
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static final String COLLECTIONPATH_REWARDS_PENALTIES = "Rewards_Penalties";
    private static final String DOCUMENTPATH_PENALTY = "Penalty";
    private static final String DOCUMENTPATH_REWARD_UPDATER = "PenaltyUpdater";
    private static final String TAG = "PenaltyManagement";
    private static final String EXTRA_PENALTY_NAME = "EXTRA_PENALTY_NAME";
    private static final String EXTRA_PENALTY_DESCRIPT = "EXTRA_PENALTY_DESCRIPT";
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_PENALTY);
    private static Map<String, Map<String, List<Object>>> grDB;
    //Initialized grDB by reading the document from the database
    public static void readGroupPenaltyDB() {
        DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_PENALTY);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Penalty penaltyDocument = documentSnapshot.toObject(PenaltyManagement.Penalty.class);
                grDB = penaltyDocument.getPenaltyMap();
                System.out.println(grDB);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w("PenaltyManagementFirebase", "Error reading document", e);
            }
        });
    }
    //Adds a penalty to a specific group
    public static int addPenalty(String groupID, String penaltyDescription, String penaltyName){
        if(grDB == null){
            readGroupPenaltyDB();
            if(grDB == null){
                return -1;
            }
        }
        if(grDB.containsKey(groupID)){
            Map<String, List<Object>> groupPenaltyMap = new HashMap<>(grDB.get(groupID));
            if(groupPenaltyMap.containsKey(penaltyName)){
                return 0; //Penalty already added
            }else{
                groupPenaltyMap.put(penaltyName, Arrays.asList(new String[]{penaltyDescription, null}));
                grDB.put(groupID, groupPenaltyMap);
            }

        }else{
            Map<String, List<Object>> groupPenaltyMap = new HashMap<>();
            groupPenaltyMap.put(penaltyName, Arrays.asList(new String[]{penaltyDescription, null}));
            grDB.put(groupID, groupPenaltyMap);

        }
        PenaltyManagement.Penalty penalties = new PenaltyManagement.Penalty(grDB);
        fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_PENALTY).set(penalties);
        return 1;
    }

    public static int removePenalty(String groupID, String penaltyName){
        if(grDB == null){
            readGroupPenaltyDB();
            if(grDB == null){
                return -1;
            }
        }
        if(grDB.containsKey(groupID)) {
            Map<String, List<Object>> groupRewardMap = new HashMap<>(grDB.get(groupID));
            if (groupRewardMap.containsKey(penaltyName)) {
                groupRewardMap.remove(penaltyName);
                grDB.replace(groupID, groupRewardMap);
                //remove penalty
                PenaltyManagement.Penalty penalty = new PenaltyManagement.Penalty(grDB);
                fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_PENALTY).set(penalty);
                return 1;
            }
        }

        return 0;
    }

    public static int removePenaltyMap(String groupID){
        if(grDB == null){
            readGroupPenaltyDB();
            if(grDB == null){
                return -1;
            }
        }
        if(grDB.containsKey(groupID)) {
            grDB.remove(groupID);
            PenaltyManagement.Penalty penalty = new PenaltyManagement.Penalty(grDB);
            fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_PENALTY).set(penalty);
            return 1;
        }
        return 0;
    }

    //Gets the penalty map for the specific group
    public static Map<String, List<Object>> getGroupPenaltyMap(String groupID){
        Map<String, List<Object>> groupPenaltyMap;
        if(grDB == null){
            readGroupPenaltyDB();
            if(grDB == null){
                return null;
            }
        }
        if(grDB.containsKey(groupID)){
            groupPenaltyMap = new HashMap<>(grDB.get(groupID));
        }else{
            groupPenaltyMap = new HashMap<>();
        }
        return groupPenaltyMap;
    }


    //Gets the specific info for the penalty. Specifically description and value
    public static ArrayList<Object> getPenaltyInfo(String groupID, String penaltyName){
        Map<String, List<Object>> groupPenaltyMap = getGroupPenaltyMap(groupID);
        List<Object> penaltyInfo = new ArrayList<Object>();
        if (groupPenaltyMap != null) {
            if (groupPenaltyMap.containsKey(penaltyName)) {
                penaltyInfo = groupPenaltyMap.get(penaltyName);
                return (ArrayList<Object>) penaltyInfo;
            } else {
                return null;
            }
        }else{
            return null;
        }
    }
    public static ArrayList<String> getPenaltyNameList(Map<String, List<Object>> groupPenaltyMap) {
        ArrayList<String> penaltyNames = new ArrayList<String>();
        if (groupPenaltyMap != null){
            penaltyNames = new ArrayList<String>(groupPenaltyMap.keySet());
        }
        return penaltyNames;

    }


    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    private static class Penalty{
        public Map<String, Map<String, List<Object>>> penaltyMap = new HashMap<>();
        Penalty(){}
        Penalty(Map<String, Map<String, List<Object>>> customMap){
            penaltyMap = new HashMap<>();
            this.penaltyMap = customMap;
        }
        public Map<String, Map<String, List<Object>>> getPenaltyMap() {
            return this.penaltyMap;}
    }


    @Override
    protected void onPreExecute() {
        readGroupPenaltyDB();
        super.onPreExecute();
    }

    private static class PenaltyUpdater{
        public Map<String, Integer> penaltyUpdaterMap = new HashMap<>();
        PenaltyUpdater(){}
        PenaltyUpdater(Map<String, Integer> customMap){
            this.penaltyUpdaterMap = new HashMap<>();
            this.penaltyUpdaterMap = customMap;
        }
        public Map<String, Integer> getPenaltyUpdaterMap() {
            return this.penaltyUpdaterMap;}
    }

}
