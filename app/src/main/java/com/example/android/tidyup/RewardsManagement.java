package com.example.android.tidyup;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardsManagement extends AsyncTask<Void, Void, Void> {
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static final String COLLECTIONPATH_REWARDS_PENALTIES = "Rewards_Penalties";
    private static final String DOCUMENTPATH_REWARDS = "Rewards";
    private static final String TAG = "UserManagement";
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS);
    private static Map<String, Map<String, List<Object>>> grDB;

    //Adds a reward to a specific group
    public static int addReward(String groupID, String rewardDescription, String rewardName, int rewardVal){
        if(grDB == null){
            readGroupRewardsDB();
            if(grDB == null){
                return -1;
            }
        }
        if(grDB.containsKey(groupID)){
            Map<String, List<Object>> groupRewardMap = new HashMap<>(grDB.get(groupID));
            if(groupRewardMap.containsKey(rewardName)){
                return 0; //Reward already added
            }else{
                groupRewardMap.put(rewardName, Arrays.asList(new String[]{rewardDescription, String.valueOf(rewardVal), null}));
                grDB.put(groupID, groupRewardMap);
            }

        }else{
            Map<String, List<Object>> groupRewardMap = new HashMap<>();
            groupRewardMap.put(rewardName, Arrays.asList(new String[]{rewardDescription, String.valueOf(rewardVal), null}));
            grDB.put(groupID, groupRewardMap);

        }
        Rewards rewards = new Rewards(grDB);
        fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS).set(rewards);
        return 1;
    }
    //Gets the reward map for the specific group
    public static Map<String, List<Object>> getGroupRewardsMap(String groupID){
        Map<String, List<Object>> groupRewardMap;
        if(grDB == null){
            readGroupRewardsDB();
            if(grDB == null){
                return null;
            }
        }
        if(grDB.containsKey(groupID)){
            groupRewardMap = new HashMap<>(grDB.get(groupID));
        }else{
            groupRewardMap = new HashMap<>();
        }
        return groupRewardMap;
    }
    //Gets the specific info for the reward. Specifically description and value
    public static ArrayList<Object> getRewardInfo(String groupID, String rewardName){
        Map<String, List<Object>> groupRewardMap = getGroupRewardsMap(groupID);
        List<Object> rewardInfo = new ArrayList<Object>();
        if (groupRewardMap != null) {
            if (groupRewardMap.containsKey(rewardName)) {
                rewardInfo = groupRewardMap.get(rewardName);
                return (ArrayList<Object>) rewardInfo;
            } else {
                return null;
            }
        }else{
            return null;
        }
    }
    public static ArrayList<String> getRewardNameList(Map<String, List<Object>> groupRewardMap) {
        ArrayList<String> rewardNames = new ArrayList<String>();
        if (groupRewardMap != null){
            rewardNames = new ArrayList<String>(groupRewardMap.keySet());
        }
        return rewardNames;

    }




    //Initialized grDB by reading the document from the database
    public static void readGroupRewardsDB(){
        DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Rewards rewardDocument = documentSnapshot.toObject(Rewards.class);
                grDB = rewardDocument.getRewardsMap();
                System.out.println(grDB);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w("RewardsManagementFirebase", "Error reading document", e);
            }
        });

    }



    public void automateRewardAssignment(String grpID){

    }



    public static int AssignReward(String userID, String grpID, int userPoints){
        if(grDB == null) {
            readGroupRewardsDB();
            if (grDB == null) {
                return -1;
            }
        }
        else {
            Map<String, List<Object>> rewardMap = RewardsManagement.getGroupRewardsMap(grpID);
            for (Map.Entry<String, List<Object>> entry : rewardMap.entrySet()) {
                int rewardVal;
                try {
                    rewardVal = Integer.parseInt(((String) entry.getValue().get(1)).trim());
                }catch (Exception e){
                    Log.d(TAG, "Error converting rewardVal to int");
                    return -1;
                }
                if (userPoints >= rewardVal){
                    if(((String) entry.getValue().get(2)).equals(null)){
                         entry.getValue().set(2, userID);
                         Rewards rewards = new Rewards(grDB);
                         fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS).set(rewards);
                         return 1;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        readGroupRewardsDB();
        super.onPreExecute();
    }

    private static class Rewards{
        public Map<String, Map<String, List<Object>>> rewardsMap = new HashMap<>();
        Rewards(){}
        Rewards(Map<String, Map<String, List<Object>>> customMap){
            rewardsMap = new HashMap<>();
            this.rewardsMap = customMap;
        }
        public Map<String, Map<String, List<Object>>> getRewardsMap() {
            return this.rewardsMap;}
    }
}
