package com.example.android.tidyup;

import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class RewardsManagement extends AsyncTask<Void, Void, Void> {
    private static final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    //private static final DocumentReference docRef = fFirestore.collection("Rewards").document(fAuth.getUid());
    private static Map<String, List<String>> grDB;
    public static int addReward(String userID, String groupID, String rewardDescription, String rewardName, int rewardVal){
        if(userID == null || groupID == null || rewardDescription == null || rewardName == null || rewardVal <= 0){
            return -1; // Missing information
        }
        return -1;
    }
    public static String getGroupRewards(String groupID){
        if(grDB == null)
            return null;
        for (Map.Entry<String, List<String>> entry : grDB.entrySet()) {
            if (entry.getValue().contains(groupID)) {
                return entry.getKey();
            }
        }
        return null;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
