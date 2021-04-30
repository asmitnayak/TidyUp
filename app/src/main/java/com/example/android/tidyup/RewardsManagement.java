package com.example.android.tidyup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardsManagement extends AsyncTask<Void, Void, Void> {
    private static FirebaseAuth fAuth;// = FirebaseAuth.getInstance();
    private static FirebaseFirestore fFirestore;// = FirebaseFirestore.getInstance();
    private static final String COLLECTIONPATH_REWARDS_PENALTIES = "Rewards_Penalties";
    private static final String DOCUMENTPATH_REWARDS = "Rewards";
    private static final String TAG = "RewardsManagement";
    private static final String EXTRA_REWARD_NAME = "EXTRA_REWARD_NAME";
    private static final String EXTRA_REWARD_DESCRIPT = "EXTRA_REWARD_DESCRIPT";
    private static DocumentReference docRef;
    private static Map<String, Map<String, List<Object>>> grDB;
    private static Calendar cal;


    public RewardsManagement( FirebaseAuth fireAuth, FirebaseFirestore fireStore){
        fAuth = fireAuth;
        fFirestore = fireStore;
        docRef = fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS);
        grDB = new HashMap<>();
    }

    public RewardsManagement(){
        fAuth = FirebaseAuth.getInstance();
        fFirestore = FirebaseFirestore.getInstance();
        docRef = fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS);
    }

    //Adds a reward to a specific group
    public static int addReward(Context context, String groupID, String rewardDescription, String rewardName, int rewardVal){
        if(grDB == null){
//            readGroupRewardsDB();
//            if(grDB == null){
                return -1;
            //}
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

        if( context != null) {
            ArrayList<String> membersList = GroupManagement.getGroupMemberList(groupID);
            for (int i = 0; i < membersList.size(); i++) {
                assignReward(context, membersList.get(i), groupID, Integer.parseInt((String) UserManagement.getUserPointsFromUID(membersList.get(i))));
            }
        }
        return 1;
    }


    public static int removeReward(String groupID, String rewardName){
       if(grDB != null) {
           if (grDB.containsKey(groupID)) {
               Map<String, List<Object>> groupRewardMap = new HashMap<>(grDB.get(groupID));
               if (groupRewardMap.containsKey(rewardName)) {
                   groupRewardMap.remove(rewardName);
                   grDB.replace(groupID, groupRewardMap);
                   //remove reward
                   Rewards rewards = new Rewards(grDB);
                   fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS).set(rewards);
                   return 1;
               }
           }
       }

        return 0;
    }


    public static int removeRewardsMap(String groupID){
        if(grDB == null){
           return 0;
       }
        if(grDB.containsKey(groupID)) {
            grDB.remove(groupID);
            Rewards rewards = new Rewards(grDB);
            fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS).set(rewards);
            return 1;
        }

        return 0;
    }




    //Gets the reward map for the specific group
    public static Map<String, List<Object>> getGroupRewardsMap(String groupID){
        Map<String, List<Object>> groupRewardMap;
        if(grDB == null){
//            readGroupRewardsDB();
//            if(grDB == null){
                return null;
//            }
        }
        if(grDB.containsKey(groupID)){
            groupRewardMap = new HashMap<>(grDB.get(groupID));
        }else{
            groupRewardMap = new HashMap<>();
        }
        return groupRewardMap;
    }



    //Gets the specific info for the reward. Specifically description and value
    public static List<Object> getRewardInfo(String groupID, String rewardName){
        Map<String, List<Object>> groupRewardMap = getGroupRewardsMap(groupID);
        List<Object> rewardInfo = new ArrayList<Object>();
        if (groupRewardMap != null) {
            if (groupRewardMap.containsKey(rewardName)) {
                rewardInfo = groupRewardMap.get(rewardName);
                return (List<Object>) rewardInfo;
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
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w(TAG, "listen:error", error);
                    return;
                }

                if(documentSnapshot==null || !documentSnapshot.exists())
                    return;

                Rewards rewardDocument = documentSnapshot.toObject(Rewards.class);
                grDB = rewardDocument.getRewardsMap();
                System.out.println(grDB);
            }
        });
    }



    public static int assignReward(Context context, String userID, String grpID, int userPoints){
        int test = 0;
        if(grDB == null) {
            readGroupRewardsDB();
            if (grDB == null) {
                return -1;
            }
        }
        else {
            Map<String, List<Object>> rewardMap = RewardsManagement.getGroupRewardsMap(grpID);
            APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
            for (Map.Entry<String, List<Object>> entry : rewardMap.entrySet()) {
                int rewardVal;
                try {
                    rewardVal = Integer.parseInt(((String) entry.getValue().get(1)).trim());
                }catch (Exception e){
                    Log.d(TAG, "Error converting rewardVal to int");
                    return -1;
                }
                if (userPoints >= rewardVal){
                    if(((String) entry.getValue().get(2)) == null){
                        grDB.get(grpID).get(entry.getKey()).set(2, userID);
                        Rewards rewards = new Rewards(grDB);
                        fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS).set(rewards);
                        test = 1;
                        if (context != null) {
                            String userToken = UserManagement.getUserTokenFromUID(fAuth.getUid());
                            NotificationManager.sendNotifications(userToken, "Tidy Up", "Congrats because of your Points you got reward " + entry.getKey(), context, apiService);

                        }
                    }
                }
            }
        }
        return test;
    }

    public static int resetRewardAssignments(String grpID){
        Map<String, List<Object>> rewardMap = getGroupRewardsMap(grpID);
        if(rewardMap.size() == 0){
            Log.d(TAG, "Error getting Reward Map");
            return -1;
        }
        for (Map.Entry<String, List<Object>> entry: rewardMap.entrySet()){
            entry.getValue().set(2, null);
        }
        Rewards rewards = new Rewards(grDB);
        fFirestore.collection(COLLECTIONPATH_REWARDS_PENALTIES).document(DOCUMENTPATH_REWARDS).set(rewards);
        return 1;
    }

    public static void resetUserRewards(String grpID){
        cal = Calendar.getInstance();
        if (grpID != null) {
            int listedWeek =  Integer.parseInt(GroupManagement.getWeekOfYear(grpID));
            int currWeek = cal.get(cal.WEEK_OF_YEAR);
            if (listedWeek != currWeek){
                String sCurrWeek = String.valueOf(currWeek);
                GroupManagement.setWeekofYear(grpID, sCurrWeek);
                UserManagement.resetAllUserPoints(grpID);
                resetRewardAssignments(grpID);
                Log.d(TAG, "updated Weekly DB");
            }
        }
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


    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
