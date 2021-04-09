package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddReward extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    EditText rewardNameEDT;
    String rewardName;
    EditText rewardDescriptionEDT;
    String rewardDescription;
    EditText rewardPointValEDT;
    List<Object> rewardsValue;
    int rewardPointVal;
    int listItem;
    private Map<String, List<String>> rewardsMap;
    ArrayList<String>rewardsKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);
        this.rewardNameEDT = findViewById(R.id.rewardName);
        this.rewardDescriptionEDT = findViewById(R.id.rewardDescription);
        this.rewardPointValEDT = findViewById(R.id.rewardPointVal);
        //When a reward is clicked load reward info into addReward window
        //This will allow the user to edit the reward.
        Intent intent = getIntent();
        listItem = intent.getIntExtra("listItem", -1);
        if(listItem != -1){
            //Load in reward data
            rewardsMap = RewardsManagement.getGroupRewardsMap(GroupManagement.getGroupIDFromUserID(fAuth.getUid()));
            rewardsKey = new ArrayList<String>(rewardsMap.keySet());
            rewardNameEDT.setText(rewardsKey.get(listItem));
            rewardsValue = new ArrayList<Object>(rewardsMap.values());
            ArrayList<String> rewardInfo = (ArrayList<String>) rewardsValue.get(listItem);
            rewardDescriptionEDT.setText(rewardInfo.get(0));
            rewardPointValEDT.setText(rewardInfo.get(1));
        }

    }

    public void onAddReward(View view) {
        this.rewardPointVal = Integer.parseInt(this.rewardPointValEDT.getText().toString());
        this.rewardDescription = this.rewardDescriptionEDT.getText().toString();
        this.rewardName = this.rewardNameEDT.getText().toString();
        RewardsManagement.addReward(GroupManagement.getGroupIDFromUserID(fAuth.getUid()),
                this.rewardDescription, this.rewardName, this.rewardPointVal);
        Intent intent = new Intent(this, RewardAndPenatly.class);
        startActivity(intent);
    }
}