package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardAndPenatly extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    RewardsAdaptor customAdp;
    private ListView listView;
    private Map<String, List<Object>> rewardsMap;
    RewardsAdaptor rewardsAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_and_penatly);

        listView = findViewById(R.id.rewardsList);
        rewardsMap = new HashMap<>();
        List reward1 = new ArrayList();
        List reward2 = new ArrayList();
        List reward3 = new ArrayList();
        rewardsMap.put("Reward 1", reward1);
        rewardsMap.put("Reward 2", reward2);
        rewardsMap.put("Reward 3", reward3);

        Map<String, List<String>> rewardsMap = RewardsManagement.getGroupRewardsMap(
                GroupManagement.getGroupIDFromUserID(fAuth.getUid()));
        customAdp = new RewardsAdaptor(getApplicationContext(), rewardsMap);
        listView.setAdapter(customAdp);
        //rewardsAdaptor
    }

    public void OnAddReward(View view) {
        Intent intent = new Intent(this, AddReward.class);
        startActivity(intent);
    }

    public void OnAddPenalty(View view) {
    }

    public void OnAssignPenalty(View view) {
    }
}