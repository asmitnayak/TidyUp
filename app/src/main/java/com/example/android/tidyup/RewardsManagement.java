package com.example.android.tidyup;

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

public class RewardsManagement extends AsyncTask<Void, Void, Void> {
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private static final String COLLECTIONPATH_REWARDS_PENALTIES = "Rewards_Penalties";
    private static final String DOCUMENTPATH_REWARDS = "Rewards";
    private static final String DOCUMENTPATH_REWARD_UPDATER = "RewardUpdater";
    private static final String TAG = "RewardsManagement";
    private static final String EXTRA_REWARD_NAME = "EXTRA_REWARD_NAME";
    private static final String EXTRA_REWARD_DESCRIPT = "EXTRA_REWARD_DESCRIPT";
    private static final DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS);
    private static Map<String, Map<String, List<Object>>> grDB;
    private static Map<String, Integer> ruDB;

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


    public static int setRewardUpdater(String groupID, Integer currweek){
        if(ruDB == null){
            readGroupRewardsDB();
            if(ruDB == null){
                return -1;
            }
        }
        if(ruDB.containsKey(groupID)){
            ruDB.replace(groupID, currweek);
        }
        else {
            ruDB.put(groupID,currweek);
        }
        RewardUpdater RewardUpdater = new RewardUpdater(ruDB);
        fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARD_UPDATER).set(RewardUpdater);
        return 1;
    }

    //Gets the specific info for the reward. Specifically description and value
    public static Integer getRewardUpdaterInfo(String groupID){
        if(ruDB == null){
            readGroupRewardsDB();
            if(ruDB == null){
                return null;
            }
        }
        if(ruDB.containsKey(groupID)){
            return  ruDB.get(groupID);
        } else {
            ruDB.put(groupID,cal.get(cal.WEEK_OF_YEAR));
            return ruDB.get(groupID);
        }
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
    public static void readGroupRewardsDB() {
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

    //Initialized grDB by reading the document from the database
    public static void readRewardUpdaterDB(){
        DocumentReference docRef = fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARD_UPDATER);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                RewardUpdater rewardUpdaterDocument = documentSnapshot.toObject(RewardUpdater.class);
                ruDB = rewardUpdaterDocument.getRewardUpdaterMap();
                System.out.println(ruDB);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w("RewardsManagementFirebase", "Error reading document", e);
            }
        });
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

    public static int resetRewardAssignments(){
        String grpID = (String) UserManagement.getUserDetails().get("GroupID");
        Map<String, List<Object>> rewardMap = getGroupRewardsMap(grpID);
        if(rewardMap.equals(null)){
            Log.d(TAG, "Error getting Members List");
            return -1;
        }
        for (Map.Entry<String, List<Object>> entry: rewardMap.entrySet()){
            entry.getValue().set(2, null);
        }
        return 1;
    }


    private static Calendar cal = Calendar.getInstance();
    private String grpID = (String) UserManagement.getUserDetails().get("GroupID");
    private Integer listedWeek;
    private int currWeek;
    @Override
    protected Void doInBackground(Void... voids) {

/*
        if (grpID != null) {
            listedWeek = getRewardUpdaterInfo(grpID);
            currWeek = cal.get(cal.WEEK_OF_YEAR);
            if (listedWeek.intValue() != currWeek){
                setRewardUpdater(grpID, Integer.valueOf(currWeek));
                UserManagement.resetAllUserPoints(grpID);
                resetRewardAssignments();

            }
            //Log.d(TAG, "updated Reward Updater DB");

        }
*/


        return null;
    }

    @Override
    protected void onPreExecute() {
        readGroupRewardsDB();
        readRewardUpdaterDB();
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

    private static class RewardUpdater{
        public Map<String, Integer> rewardUpdaterMap = new HashMap<>();
        RewardUpdater(){}
        RewardUpdater(Map<String, Integer> customMap){
            this.rewardUpdaterMap = new HashMap<>();
            this.rewardUpdaterMap = customMap;
        }
        public Map<String, Integer> getRewardUpdaterMap() {
            return this.rewardUpdaterMap;}
    }
}
