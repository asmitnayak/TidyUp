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
    //private static final DocumentReference docRef = fFirestore.collection("Rewards").document(fAuth.getUid());
    private static Map<String, Map<String, List<String>>> grDB;

    public static int addReward(String userID, String groupID, String rewardDescription, String rewardName, int rewardVal){
        if(grDB.containsKey(groupID)){
            Map<String, List<String>> groupRewardMap = new HashMap<>(grDB.get(groupID));
            if(groupRewardMap.containsKey(rewardName)){
                return 0; //Reward already added
            }else{
                groupRewardMap.put(rewardName, Arrays.asList(new String[]{rewardDescription, String.valueOf(rewardVal), null}));
                grDB.put(groupID, groupRewardMap);
                return 1;
            }

        }else{
            Map<String, List<String>> groupRewardMap = new HashMap<>();
            groupRewardMap.put(rewardName, Arrays.asList(new String[]{rewardDescription, String.valueOf(rewardVal), null}));
            grDB.put(groupID, groupRewardMap);
            return 1;
        }
    }

    public static void readGroupRewardsDB(){
        DocumentReference docRef = fFirestore.collection("Rewards-Penalties").document("Rewards");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Rewards rewardDocument = documentSnapshot.toObject(Rewards.class);
                grDB = rewardDocument.rewardsMap;
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Log.w("RewardsManagementFirebase", "Error reading document", e);
            }
        });

    }
    public void automateRewardAssignment(String grpID){

    }

    public void AssignReward(String userID){

    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private static class Rewards{
        public Map<String, Map<String, List<String>>> rewardsMap = new HashMap<>();
        Rewards(){}
        Rewards(Map<String, Map<String, List<String>>> customMap){
            this.rewardsMap = customMap;
        }
        public Map<String, Map<String, List<String>>> getRewardsMap() { return this.rewardsMap;}
    }
}
