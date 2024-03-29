package com.example.android.tidyup;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RewardPopUp extends AppCompatActivity {

    private static final String EXTRA_REWARD_NAME = "EXTRA_REWARD_NAME";
    private static final String EXTRA_ASSIGNED_USER = "EXTRA_ASSIGNED_USER";
    private static final String EXTRA_REWARD_VAL = "EXTRA_REWARD_VAL";
    private static final String EXTRA_REWARD_DESCRIPT = "EXTRA_REWARD_DESCRIPT";

    private TextView mRewardName, mAssignedUser, mRewardVal, mRewardDescript;

    private String rewardName;
    private String assignedUser;
    private String rewardVal;
    private String rewardDescript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_pop_up_window);

        mRewardName = findViewById(R.id.rpuRewardName);
        mAssignedUser = findViewById(R.id.rpuUserName);
        mRewardVal = findViewById(R.id.rpuRewardVal);
        mRewardDescript = findViewById(R.id.rpuRewardDescription);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            rewardName = null;
            assignedUser = null;
            rewardVal = null;
            rewardDescript = null;
        } else {
            rewardName = extras.getString(EXTRA_REWARD_NAME);
            assignedUser = extras.getString(EXTRA_ASSIGNED_USER);
            rewardVal = extras.getString(EXTRA_REWARD_VAL);
            rewardDescript = extras.getString(EXTRA_REWARD_DESCRIPT);
        }

        try {
            mRewardName.setText(rewardName);
            mAssignedUser.setText("Assigned User: " + assignedUser);
            mRewardVal.setText("Reward Value: " + rewardVal);
            mRewardDescript.setText("Reward Description:\n"+ rewardDescript);

        } catch (Exception e){
            Toast.makeText(RewardPopUp.this, "Error! " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.9),(int) (height*.3));

    }

    public void deleteReward(View view){
        RewardsManagement.removeReward(GroupManagement.getGroupIDFromUserID(FirebaseAuth.getInstance().getUid()), rewardName);
        finish();
        Intent intent = RewardAndPenalty.rp.getIntent();
        RewardAndPenalty.rp.finish();
        startActivity(intent);
    }



}
