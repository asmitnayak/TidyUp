package com.example.android.tidyup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddReward extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    EditText rewardNameEDT;
    String rewardName;
    EditText rewardDescriptionEDT;
    String rewardDescription;
    EditText rewardPointValEDT;
    int rewardPointVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);
        this.rewardNameEDT = findViewById(R.id.rewardName);
        this.rewardDescriptionEDT = findViewById(R.id.rewardDescription);
        this.rewardPointValEDT = findViewById(R.id.rewardPointVal);

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