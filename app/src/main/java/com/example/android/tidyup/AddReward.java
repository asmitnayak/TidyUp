package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    ArrayList<Object> rewardsValue;
    int rewardPointVal;
    int listItem;
    private Map<String, List<Object>> rewardsMap;
    ArrayList<String>rewardsKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);
        RewardsManagement rm = new RewardsManagement();
        rm.execute();
        this.rewardNameEDT = findViewById(R.id.rewardName);
        this.rewardDescriptionEDT = findViewById(R.id.rewardDescription);
        this.rewardPointValEDT = findViewById(R.id.rewardPointVal);

    }

    public void onAddReward(View view) {
        String rewardPointValHolder = this.rewardPointValEDT.getText().toString().toLowerCase();
        this.rewardDescription = this.rewardDescriptionEDT.getText().toString().toLowerCase();
        this.rewardName = this.rewardNameEDT.getText().toString().toLowerCase();
        if (TextUtils.isEmpty(rewardName)){
            rewardNameEDT.setError("Please Enter a Name");
            return;
        }
        if (TextUtils.isEmpty(rewardDescription)){
            rewardDescriptionEDT.setError("Please Enter a Description");
            return;
        }
        if (TextUtils.isEmpty(rewardPointValHolder)){
            rewardPointValEDT.setError("Please Enter Point Value");
            return;
        }
        try{
            rewardPointVal = Integer.parseInt(this.rewardPointValEDT.getText().toString());
        }catch(NumberFormatException e){
            rewardPointValEDT.setError("Only Enter Numbers, No Other Characters Allowed");
            return;
        }
        RewardsManagement.addReward(GroupManagement.getGroupIDFromUserID(fAuth.getCurrentUser().getUid()),
                this.rewardDescription, this.rewardName, this.rewardPointVal);
        Intent intent = new Intent(this, RewardAndPenalty.class);
        finish();
        startActivity(intent);
    }
}